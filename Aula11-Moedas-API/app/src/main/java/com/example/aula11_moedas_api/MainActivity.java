package com.example.aula11_moedas_api;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerFrom, spinnerCustom;
    private TextView tvCountryFrom, tvCountryFrom2;
    private EditText etAmountInput;
    private Button btnConvert;

    private TextView tvResultValue1, tvResultValue2, tvResultValue3, tvResultValueCustom;

    private JSONObject ratesObject;
    private final Map<String, String> currencyNames = new HashMap<>();
    private final Map<String, String> currencyFlags = new HashMap<>();
    private final Map<String, String> currencySymbols = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerCustom = findViewById(R.id.spinnerCustom);
        tvCountryFrom = findViewById(R.id.tvCountryFrom);
        tvCountryFrom2 = findViewById(R.id.tvCountryFrom2);
        etAmountInput = findViewById(R.id.etAmountInput);
        btnConvert = findViewById(R.id.btnConvert);

        tvResultValue1 = findViewById(R.id.tvResultValue1);
        tvResultValue2 = findViewById(R.id.tvResultValue2);
        tvResultValue3 = findViewById(R.id.tvResultValue3);
        tvResultValueCustom = findViewById(R.id.tvResultValueCustom);

        setupMetadata();
        fetchExchangeRates();

        btnConvert.setOnClickListener(v -> performMultiConversion());
    }

    private void setupMetadata() {
        currencyNames.put("BRL", "Real Brasileiro");
        currencyNames.put("USD", "Dólar Americano");
        currencyNames.put("EUR", "Euro");
        currencyNames.put("GBP", "Libra Esterlina");
        currencyNames.put("ARS", "Peso Argentino");
        currencyNames.put("CAD", "Dólar Canadense");
        currencyNames.put("JPY", "Iene Japonês");
        currencyNames.put("AUD", "Dólar Australiano");

        currencyFlags.put("BRL", "🇧🇷");
        currencyFlags.put("USD", "🇺🇸");
        currencyFlags.put("EUR", "🇪🇺");
        currencyFlags.put("GBP", "🇬🇧");
        currencyFlags.put("ARS", "🇦🇷");
        currencyFlags.put("CAD", "🇨🇦");
        currencyFlags.put("JPY", "🇯🇵");
        currencyFlags.put("AUD", "🇦🇺");

        currencySymbols.put("BRL", "R$");
        currencySymbols.put("USD", "$");
        currencySymbols.put("EUR", "€");
        currencySymbols.put("GBP", "£");
        currencySymbols.put("ARS", "$");
        currencySymbols.put("CAD", "$");
        currencySymbols.put("JPY", "¥");
        currencySymbols.put("AUD", "$");
    }

    private void fetchExchangeRates() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL("https://api.exchangerate-api.com/v4/latest/brl");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
                ratesObject = jsonResponse.getJSONObject("rates");

                runOnUiThread(this::populateSpinners);

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Erro ao conectar com a API na rede restrita", Toast.LENGTH_LONG).show());
            }
        });
    }

    private void populateSpinners() {
        if (ratesObject == null) return;

        ArrayList<String> currencyList = new ArrayList<>();
        Iterator<String> keys = ratesObject.keys();
        while (keys.hasNext()) {
            currencyList.add(keys.next());
        }
        Collections.sort(currencyList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, currencyList);

        spinnerFrom.setAdapter(adapter);
        spinnerCustom.setAdapter(adapter);

        if (currencyList.contains("BRL")) spinnerFrom.setSelection(currencyList.indexOf("BRL"));
        if (currencyList.contains("GBP")) spinnerCustom.setSelection(currencyList.indexOf("GBP"));

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                String flag = currencyFlags.containsKey(selected) ? currencyFlags.get(selected) + " " : "🏳️ ";
                String name = currencyNames.containsKey(selected) ? currencyNames.get(selected) : selected;
                tvCountryFrom.setText(flag + name + " (" + selected + ")");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                String flag = currencyFlags.containsKey(selected) ? currencyFlags.get(selected) + " " : "🏳️ ";
                String name = currencyNames.containsKey(selected) ? currencyNames.get(selected) : selected;
                tvCountryFrom2.setText(flag + name + " (" + selected + ")");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void performMultiConversion() {
        if (ratesObject == null) {
            Toast.makeText(this, "Aguarde o carregamento das cotações...", Toast.LENGTH_SHORT).show();
            return;
        }

        String inputStr = etAmountInput.getText().toString().trim();
        if (inputStr.isEmpty()) {
            etAmountInput.setError("Digite um valor");
            return;
        }

        try {
            double amount = Double.parseDouble(inputStr);
            String fromCurr = spinnerFrom.getSelectedItem().toString();
            String customCurr = spinnerCustom.getSelectedItem().toString();

            // Conversão cruzada baseada na API (base BRL)
            double rateFrom = ratesObject.getDouble(fromCurr);
            double amountInBrl = amount / rateFrom;

            // 3 Cards principais fixos
            double resBrl = amountInBrl * ratesObject.getDouble("BRL");
            double resUsd = amountInBrl * ratesObject.getDouble("USD");
            double resEur = amountInBrl * ratesObject.getDouble("EUR");

            tvResultValue1.setText(String.format("R$ %.2f", resBrl));
            tvResultValue2.setText(String.format("$ %.2f", resUsd));
            tvResultValue3.setText(String.format("€ %.2f", resEur));

            // 4º Card personalizado pelo usuário
            double resCustom = amountInBrl * ratesObject.getDouble(customCurr);
            String symbol = currencySymbols.containsKey(customCurr) ? currencySymbols.get(customCurr) + " " : customCurr + " ";
            tvResultValueCustom.setText(String.format("%s%.2f (%s)", symbol, resCustom, customCurr));

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao calcular conversões.", Toast.LENGTH_SHORT).show();
        }
    }
}