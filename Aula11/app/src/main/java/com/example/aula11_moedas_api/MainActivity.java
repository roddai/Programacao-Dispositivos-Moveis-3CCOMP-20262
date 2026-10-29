package com.example.aula11_moedas_api;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerBase;
    private EditText etAmount;
    private Button btnConvert;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização de Componentes da UI
        spinnerBase = findViewById(R.id.spinnerBase);
        etAmount = findViewById(R.id.etAmount);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);

        // Configuração do Spinner com as moedas exigidas
        String[] currencies = {"BRL", "USD", "EUR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencies);
        spinnerBase.setAdapter(adapter);

        // Ação do Botão
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchExchangeRates();
            }
        });
    }

    private void fetchExchangeRates() {
        String baseCurrency = spinnerBase.getSelectedItem().toString();
        String amountStr = etAmount.getText().toString().trim();

        if (amountStr.isEmpty()) {
            etAmount.setError("Por favor, insira um valor válido.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            etAmount.setError("Valor numérico inválido.");
            return;
        }

        tvResult.setText("Buscando cotações atualizadas...");

        // Executando a requisição HTTP em background com ExecutorService
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Monta dinamicamente a URL da API conforme a moeda base selecionada
            String urlString = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
            StringBuilder response = new StringBuilder();

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000); // Aumentado para 8 segundos para redes lentas
                connection.setReadTimeout(8000);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leitura de Sucesso
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Retorna o resultado para a Thread Principal para atualizar a UI
                    runOnUiThread(() -> parseAndDisplayResult(response.toString(), baseCurrency, amount));
                } else {
                    // Captura o corpo do erro retornado pelo servidor (caso exista)
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    errorReader.close();

                    String finalErrorMsg = "Erro HTTP: " + responseCode + "\nDetalhes: " + errorResponse.toString();
                    runOnUiThread(() -> tvResult.setText(finalErrorMsg));
                }
            } catch (Exception e) {
                // Exibe o tipo da exceção e a mensagem exata do erro no Android
                String errorDetails = e.getClass().getName() + "\nMensagem: " + e.getMessage();
                runOnUiThread(() -> tvResult.setText("⚠️ Falha na Conexão:\n" + errorDetails));
            }
        });
    }

    private void parseAndDisplayResult(String jsonStr, String baseCurrency, double amount) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject rates = jsonObject.getJSONObject("rates");

            // Extraindo cotações para BRL, USD e EUR
            double rateBrl = rates.optDouble("BRL", 0.0);
            double rateUsd = rates.optDouble("USD", 0.0);
            double rateEur = rates.optDouble("EUR", 0.0);

            // Calculando conversões para o valor digitado
            double convertedBrl = amount * rateBrl;
            double convertedUsd = amount * rateUsd;
            double convertedEur = amount * rateEur;

            // Construindo o relatório formatado
            StringBuilder sb = new StringBuilder();
            sb.append("📌 Moeda Base Selecionada: ").append(baseCurrency).append("\n\n");

            sb.append("📈 Cotações Atuais (Base 1 ").append(baseCurrency).append("):\n");
            sb.append("• Real (BRL): R$ ").append(String.format("%.4f", rateBrl)).append("\n");
            sb.append("• Dólar (USD): $ ").append(String.format("%.4f", rateUsd)).append("\n");
            sb.append("• Euro (EUR): € ").append(String.format("%.4f", rateEur)).append("\n\n");

            sb.append("💱 Conversão para ").append(String.format("%.2f", amount)).append(" ").append(baseCurrency).append(":\n");
            if (!baseCurrency.equals("BRL")) sb.append("• Real (BRL): R$ ").append(String.format("%.2f", convertedBrl)).append("\n");
            if (!baseCurrency.equals("USD")) sb.append("• Dólar (USD): $ ").append(String.format("%.2f", convertedUsd)).append("\n");
            if (!baseCurrency.equals("EUR")) sb.append("• Euro (EUR): € ").append(String.format("%.2f", convertedEur)).append("\n");

            tvResult.setText(sb.toString().trim());

        } catch (JSONException e) {
            tvResult.setText("Erro ao interpretar dados JSON:\n" + e.getMessage());
        }
    }
}