package com.example.aula11_moedas_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aula11_moedas_api.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {

    EditText etMoeda;
    Button btnConsultar;
    TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMoeda = findViewById(R.id.etMoeda);
        btnConsultar = findViewById(R.id.btnConsultar);
        txtResultado = findViewById(R.id.txtResultado);

        btnConsultar.setOnClickListener(view -> {

            String moeda = etMoeda.getText().toString().trim().toUpperCase();

            // ERRO 1 - Entrada inválida
            if (moeda.isEmpty() || moeda.length() != 3) {

                txtResultado.setText(
                        "Erro: informe uma moeda válida com 3 letras (USD, BRL, EUR...).");

                return;
            }

            new Thread(() -> {

                try {

                    String endereco =
                            "https://api.exchangerate-api.com/v4/latest/" + moeda;

                    URL url = new URL(endereco);

                    HttpURLConnection conexao =
                            (HttpURLConnection) url.openConnection();

                    conexao.setConnectTimeout(5000);
                    conexao.setReadTimeout(5000);

                    // ERRO 2 - API não encontrada
                    int codigoResposta = conexao.getResponseCode();

                    if (codigoResposta != HttpURLConnection.HTTP_OK) {

                        runOnUiThread(() ->
                                txtResultado.setText(
                                        "Erro: API não encontrada ou indisponível."));

                        return;
                    }

                    BufferedReader leitor =
                            new BufferedReader(
                                    new InputStreamReader(
                                            conexao.getInputStream()));

                    StringBuilder resposta = new StringBuilder();
                    String linha;

                    while ((linha = leitor.readLine()) != null) {
                        resposta.append(linha);
                    }

                    leitor.close();

                    JSONObject json = new JSONObject(resposta.toString());

                    if (!json.has("rates")) {

                        runOnUiThread(() ->
                                txtResultado.setText("Moeda inválida."));
                        return;
                    }

                    JSONObject rates = json.getJSONObject("rates");

                    double brl = rates.optDouble("BRL", 0);
                    double usd = rates.optDouble("USD", 0);
                    double eur = rates.optDouble("EUR", 0);

                    String resultado =
                            "Moeda Base: " + moeda +
                                    "\n\nBRL: " + brl +
                                    "\nUSD: " + usd +
                                    "\nEUR: " + eur;

                    runOnUiThread(() ->
                            txtResultado.setText(resultado));

                }

                // ERRO 3 - Falha de conexão
                catch (UnknownHostException | SocketTimeoutException e) {

                    runOnUiThread(() ->
                            txtResultado.setText(
                                    "Erro: falha de conexão. Verifique sua internet."));
                }

                catch (Exception e) {

                    e.printStackTrace();

                    runOnUiThread(() ->
                            txtResultado.setText(
                                    e.getClass().getSimpleName()
                                            + "\n" +
                                            e.getMessage()));
                }

            }).start();
        });
    }
}