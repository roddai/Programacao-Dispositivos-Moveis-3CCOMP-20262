package com.example.aula11_paises_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etPais;
    private Button btnBuscar;
    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPais = findViewById(R.id.etPais);
        btnBuscar = findViewById(R.id.btnBuscar);
        txtInfo = findViewById(R.id.txtInfo);

        btnBuscar.setOnClickListener(v -> buscarPais());
    }

    private void buscarPais() {

        String pais = etPais.getText()
                .toString()
                .trim()
                .toUpperCase();

        if (pais.isEmpty()) {

            Toast.makeText(
                    this,
                    "Digite a sigla do país (BR, US, AR)",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String url =
                "https://servicodados.ibge.gov.br/api/v1/paises/"
                        + pais;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(() ->
                        txtInfo.setText(
                                "Erro de conexão: "
                                        + e.getMessage()
                        )
                );
            }

            @Override
            public void onResponse(Call call,
                                   Response response)
                    throws IOException {

                try {

                    String json =
                            response.body().string();

                    JSONArray array =
                            new JSONArray(json);

                    JSONObject paisObj =
                            array.getJSONObject(0);

                    String nome =
                            paisObj.getJSONObject("nome")
                                    .getString("abreviado");

                    String capital =
                            paisObj.getJSONObject("governo")
                                    .getJSONObject("capital")
                                    .getString("nome");

                    String regiao =
                            paisObj.getJSONObject("localizacao")
                                    .getJSONObject("regiao")
                                    .getString("nome");

                    String subRegiao =
                            paisObj.getJSONObject("localizacao")
                                    .getJSONObject("sub-regiao")
                                    .getString("nome");

                    String moeda =
                            paisObj.getJSONArray("unidades-monetarias")
                                    .getJSONObject(0)
                                    .getString("nome");

                    String idioma =
                            paisObj.getJSONArray("linguas")
                                    .getJSONObject(0)
                                    .getString("nome");

                    runOnUiThread(() -> {

                        txtInfo.setText(
                                "🌎 País: " + nome +
                                        "\n\n🏛 Capital: " + capital +
                                        "\n\n📍 Região: " + regiao +
                                        "\n\n🗺 Sub-Região: " + subRegiao +
                                        "\n\n🗣 Idioma: " + idioma +
                                        "\n\n💰 Moeda: " + moeda
                        );

                    });

                } catch (Exception e) {

                    runOnUiThread(() ->
                            txtInfo.setText(
                                    "País não encontrado."
                            )
                    );
                }
            }
        });
    }
}