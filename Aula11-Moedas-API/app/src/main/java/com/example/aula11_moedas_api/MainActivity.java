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

    private Spinner spinnerFrom, spinnerTo;
    private TextView tvCountryFrom, tvCountryTo, tvResultSummary;
    private EditText etAmountFrom, etAmountTo;
    private Button btnConvert;

    private JSONObject ratesObject;
    private final Map<String, String> currencyNames = new HashMap<>();
    private final Map<String, String> currencyFlags = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes visuais
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        tvCountryFrom = findViewById(R.id.tvCountryFrom);
        tvCountryTo = findViewById(R.id.tvCountryTo);
        etAmountFrom = findViewById(R.id.etAmountFrom);
        etAmountTo = findViewById(R.id.etAmountTo);
        btnConvert = findViewById(R.id.btnConvert);
        tvResultSummary = findViewById(R.id.tvResultSummary);

        setupMetadata();
        fetchExchangeRates();

        btnConvert.setOnClickListener(v -> performConversion());
    }

    private void setupMetadata() {
        // Mapeamento de nomes descritivos e bandeiras (emojis)
        currencyNames.put("BRL", "Real Brasileiro");
        currencyNames.put("USD", "Dólar Americano");
        currencyNames.put("EUR", "Euro");
        currencyNames.put("GBP", "Libra Esterlina");
        currencyNames.put("ARS", "Peso Argentino");
        currencyNames.put("CAD", "Dólar Canadense");
        currencyNames.put("JPY", "Iene Japonês");
        currencyNames.put("AUD", "Dólar Australiano");
        currencyNames.put("CHF", "Franco Suíço");

        currencyFlags.put("BRL", "🇧🇷");
        currencyFlags.put("USD", "🇺🇸");
        currencyFlags.put("EUR", "🇪🇺");
        currencyFlags.put("GBP", "🇬🇧");
        currencyFlags.put("ARS", "🇦🇷");
        currencyFlags.put("CAD", "🇨🇦");
        currencyFlags.put("JPY", "🇯🇵");
        currencyFlags.put("AUD", "🇦🇺");
        currencyFlags.put("CHF", "🇨🇭");
    }

    private void fetchExchangeRates() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL("https://api.exchangerate-api.com/v4/latest/brl");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
                ratesObject = jsonResponse.getJSONObject("rates");

                runOnUiThread(this.
                        populateSpinners());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Erro ao conectar com a API (Verifique a rede da faculdade)",
                        Toast.LENGTH_LONG).show());
            }
        });
    }

    private Runnable populateSpinners() {
        return () -> {
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
            spinnerTo.setAdapter(adapter);

            // Selecionar BRL como origem e USD como destino padrão, se existirem
            if (currencyList.contains("BRL")) spinnerFrom.setSelection(currencyList.indexOf("BRL"));
            if (currencyList.contains("USD")) spinnerTo.setSelection(currencyList.indexOf("USD"));

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

            spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = parent.getItemAtPosition(position).toString();
                    String flag = currencyFlags.containsKey(selected) ? currencyFlags.get(selected) + " " : "🏳️ ";
                    String name = currencyNames.containsKey(selected) ? currencyNames.get(selected) : selected;
                    tvCountryTo.setText(flag + name + " (" + selected + ")");
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        };
    }

    private void performConversion() {
        if (ratesObject == null) {
            Toast.makeText(this, "Aguarde o carregamento das cotações...", Toast.LENGTH_SHORT).show();
            return;
        }

        String inputStr = etAmountFrom.getText().toString().trim();
        if (inputStr.isEmpty()) {
            etAmountFrom.setError("Informe um valor");
            return;
        }

        try {
            double amount = Double.parseDouble(inputStr);
            String fromCurr = spinnerFrom.getSelectedItem().toString();
            String toCurr = spinnerTo.getSelectedItem().toString();

            // A API base é BRL, então convertemos: Valor -> BRL -> Destino
            double rateFrom = ratesObject.getDouble(fromCurr);
            double rateTo = ratesObject.getDouble(toCurr);

            // Conversão cruzada baseada no BRL
            double amountInBrl = amount / rateFrom;
            double convertedAmount = amountInBrl * rateTo;

            etAmountTo.setText(String.format("%.2f", convertedAmount));
            tvResultSummary.setText(String.format("%.2f %s = %.2f %s", amount, fromCurr, convertedAmount, toCurr));

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao calcular conversão.", Toast.LENGTH_SHORT).show();
        }
    }
}