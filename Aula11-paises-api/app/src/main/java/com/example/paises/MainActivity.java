package com.example.paises;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText etPais;
    private Button btnBuscar;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPais = findViewById(R.id.etPais);
        btnBuscar = findViewById(R.id.btnBuscar);
        tvResultado = findViewById(R.id.tvResultado);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarDadosIbge();
            }
        });
    }

    private void buscarDadosIbge() {
        String entradaPais = etPais.getText().toString().trim();


        if (entradaPais.isEmpty()) {
            Toast.makeText(this, "Por favor, digite o nome ou sigla do país.", Toast.LENGTH_SHORT).show();
            return;
        }

        tvResultado.setText("A carregar informações...");


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String paisCodificado = URLEncoder.encode(entradaPais, "UTF-8");
                    String urlString = "https://servicodados.ibge.gov.br/api/v1/paises/" + paisCodificado;

                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(6000);
                    connection.setReadTimeout(6000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder resposta = new StringBuilder();
                        String linha;

                        while ((linha = reader.readLine()) != null) {
                            resposta.append(linha);
                        }
                        reader.close();

                        JSONArray jsonArray = new JSONArray(resposta.toString());
                        if (jsonArray.length() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvResultado.setText("País não encontrado na base do IBGE.");
                                }
                            });
                            return;
                        }

                        JSONObject primeiroItem = jsonArray.getJSONObject(0);


                        String nomePais = primeiroItem.getJSONObject("nome").getString("abreviado");


                        String siglaIso = "";
                        if (primeiroItem.has("id") && primeiroItem.getJSONObject("id").has("ISO-3166-1-ALPHA-2")) {
                            siglaIso = primeiroItem.getJSONObject("id").getString("ISO-3166-1-ALPHA-2");
                        }

                        JSONObject governo = primeiroItem.optJSONObject("governo");
                        String capital = "Não informada";
                        if (governo != null && governo.has("capital")) {
                            capital = governo.getJSONObject("capital").optString("nome", "Não informada");
                        }

                        // Região
                        JSONObject localizacao = primeiroItem.optJSONObject("localizacao");
                        String regiao = "Não informada";
                        if (localizacao != null && localizacao.has("regiao")) {
                            regiao = localizacao.getJSONObject("regiao").optString("nome", "Não informada");
                        }

                        final String resultadoFormatado = "País: " + nomePais + "\n" +
                                "Capital: " + capital + "\n" +
                                "Região: " + regiao + "\n" +
                                "Sigla: " + siglaIso;

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
                                tvResultado.setText("Erro de busca na API (Código: " + responseCode + ")");
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