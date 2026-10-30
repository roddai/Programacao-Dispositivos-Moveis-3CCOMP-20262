package com.example.aula11_moedas_api;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerOrigem, spinnerDestino;
    private TextView tvLabelOrigem, tvLabelDestino, tvResultadoGeral;
    private EditText etValorOrigem, etValorDestino;
    private Button btnConverter;

    private JSONObject taxasCambio = null;

    private final String[] moedas = {"BRL", "USD", "EUR"};
    private final String[] nomesPaises = {
            "Real Brasileiro (BRL)",
            "Dólar Americano (USD)",
            "Euro (EUR)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando Componentes
        spinnerOrigem = findViewById(R.id.spinnerOrigem);
        spinnerDestino = findViewById(R.id.spinnerDestino);
        tvLabelOrigem = findViewById(R.id.tvLabelOrigem);
        tvLabelDestino = findViewById(R.id.tvLabelDestino);
        tvResultadoGeral = findViewById(R.id.tvResultadoGeral);
        etValorOrigem = findViewById(R.id.etValorOrigem);
        etValorDestino = findViewById(R.id.etValorDestino);
        btnConverter = findViewById(R.id.btnConverter);

        // Configurando Adaptadores dos Spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nomesPaises);
        spinnerOrigem.setAdapter(adapter);
        spinnerDestino.setAdapter(adapter);

        // Ouvintes para atualizar os TextViews descritivos acima dos Spinners em tempo real
        spinnerOrigem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvLabelOrigem.setText(nomesPaises[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvLabelDestino.setText(nomesPaises[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Ação do Botão Converter
        btnConverter.setOnClickListener(v -> realizarConversao());

        // Buscar dados da API ao iniciar
        buscarDadosApi();
    }

    private void buscarDadosApi() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String resultadoJson = "";
            try {
                URL url = new URL("https://api.exchangerate-api.com/v4/latest/brl");
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setConnectTimeout(5000);
                conexao.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    stringBuilder.append(linha);
                }
                reader.close();
                resultadoJson = stringBuilder.toString();

                JSONObject jsonObject = new JSONObject(resultadoJson);
                taxasCambio = jsonObject.getJSONObject("rates");

            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                if (taxasCambio == null) {
                    Toast.makeText(MainActivity.this, "Erro ao carregar cotações (Verifique a rede)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Cotações atualizadas com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void realizarConversao() {
        if (taxasCambio == null) {
            Toast.makeText(this, "Aguarde, as taxas ainda não foram carregadas.", Toast.LENGTH_SHORT).show();
            return;
        }

        String valorStr = etValorOrigem.getText().toString().trim();
        if (valorStr.isEmpty()) {
            etValorOrigem.setError("Insira um valor!");
            return;
        }

        try {
            double valorDigitado = Double.parseDouble(valorStr);
            String moedaOrigem = moedas[spinnerOrigem.getSelectedItemPosition()];
            String moedaDestino = moedas[spinnerDestino.getSelectedItemPosition()];

            // Conversão intermediária para BRL como base, já que a API usa base BRL
            // Valor em BRL = valorDigitado / taxaDaMoedaOrigemEmRelacaoAoBrl
            double valorEmBrl;
            if (moedaOrigem.equals("BRL")) {
                valorEmBrl = valorDigitado;
            } else {
                double taxaOrigem = taxasCambio.getDouble(moedaOrigem);
                valorEmBrl = valorDigitado / taxaOrigem;
            }

            // Converter de BRL para a Moeda de Destino
            double valorFinal;
            if (moedaDestino.equals("BRL")) {
                valorFinal = valorEmBrl;
            } else {
                double taxaDestino = taxasCambio.getDouble(moedaDestino);
                valorFinal = valorEmBrl * taxaDestino;
            }

            // Exibir no EditText de destino
            etValorDestino.setText(String.format("%.2f", valorFinal));

            // Calcular o valor nas outras duas moedas para exibir no Card informativo abaixo
            double brlVal = valorEmBrl;
            double usdVal = brlVal * (moedaDestino.equals("USD") ? taxasCambio.getDouble("USD") : (moedaOrigem.equals("USD") ? 1.0 : taxasCambio.getDouble("USD")));
            // Simplificação para exibição geral no Card:
            double valUsd = brlVal * taxasCambio.getDouble("USD");
            double valEur = brlVal * taxasCambio.getDouble("EUR");

            String resumo = String.format("Base em Reais (BRL): R$ %.2f\nEquivalent em Dólar (USD): $ %.2f\nEquivalente em Euro (EUR): € %.2f",
                    brlVal, valUsd, valEur);

            tvResultadoGeral.setText(resumo);

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao processar conversão.", Toast.LENGTH_SHORT).show();
        }
    }
}