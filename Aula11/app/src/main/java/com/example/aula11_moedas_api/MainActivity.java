package com.example.aula11_moedas_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aula11_moedas_api.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etMoedaBase;
    private EditText etValor;
    private Button btnConsultar;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMoedaBase = findViewById(R.id.etMoedaBase);
        etValor = findViewById(R.id.etValor);
        btnConsultar = findViewById(R.id.btnConsultar);
        tvResultado = findViewById(R.id.tvResultado);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executarConsulta();
            }
        });
    }

    private void executarConsulta() {
        String moedaBase = etMoedaBase.getText().toString().trim().toUpperCase(Locale.ROOT);
        String valorTexto = etValor.getText().toString().trim();


        if (moedaBase.isEmpty() || valorTexto.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        final double valorInformado;
        try {
            valorInformado = Double.parseDouble(valorTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Insira um valor numérico válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        tvResultado.setText("A carregar dados...");


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String urlString = "https://api.exchangerate-api.com/v4/latest/" + moedaBase;
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder resposta = new StringBuilder();
                        String linha;

                        while ((linha = reader.readLine()) != null) {
                            resposta.append(linha);
                        }
                        reader.close();


                        JSONObject jsonObject = new JSONObject(resposta.toString());
                        String base = jsonObject.getString("base");
                        JSONObject rates = jsonObject.getJSONObject("rates");

                        double taxaBRL = rates.optDouble("BRL", 0.0);
                        double taxaUSD = rates.optDouble("USD", 0.0);
                        double taxaEUR = rates.optDouble("EUR", 0.0);


                        double convertidoBRL = valorInformado * taxaBRL;
                        double convertidoUSD = valorInformado * taxaUSD;
                        double convertidoEUR = valorInformado * taxaEUR;

                        final String resultadoFormatado = "Moeda Base: " + base + "\n\n" +
                                "--- Cotação ---\n" +
                                "Real (BRL): " + taxaBRL + "\n" +
                                "Dólar (USD): " + taxaUSD + "\n" +
                                "Euro (EUR): " + taxaEUR + "\n\n" +
                                "--- Conversão de " + valorInformado + " " + base + " ---\n" +
                                String.format(Locale.US, "Total em BRL: %.2f\n", convertidoBRL) +
                                String.format(Locale.US, "Total em USD: %.2f\n", convertidoUSD) +
                                String.format(Locale.US, "Total em EUR: %.2f", convertidoEUR);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResultado.setText(resultadoFormatado);
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResultado.setText("Erro: Moeda inválida ou não encontrada na API.");
                            }
                        });
                    }

                    connection.disconnect();

                } catch (Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResultado.setText("Falha de conexão com a API: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }
}