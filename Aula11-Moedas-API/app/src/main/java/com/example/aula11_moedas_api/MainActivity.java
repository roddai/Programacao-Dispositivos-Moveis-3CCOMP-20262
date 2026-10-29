package com.example.aula11_moedas_api;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.PixelCopy;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.*;

public class MainActivity extends AppCompatActivity {

    EditText etMoeda, etValor;
    Button btnConsultar;
    TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMoeda = findViewById(R.id.etMoeda);
        etValor = findViewById(R.id.etValor);
        btnConsultar = findViewById(R.id.btnConsultar);
        txtResultado = findViewById(R.id.txtResultado);

        btnConsultar.setOnClickListener(v -> buscarMoeda());
    }

    private void buscarMoeda() {

        String moeda = etMoeda.getText().toString().trim().toUpperCase();

        if (moeda.isEmpty()) {
            Toast.makeText(this,
                    "Informe uma moeda",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String url =
                "https://api.exchangerate-api.com/v4/latest/" + moeda;

        OkHttpClient client = new OkHttpClient();

        Request request =
                new Request.Builder()
                        .url(url)
                        .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(() ->
                        txtResultado.setText("Erro de conexão"));
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                try {

                    String json = response.body().string();

                    JSONObject obj =
                            new JSONObject(json);

                    JSONObject rates =
                            obj.getJSONObject("rates");

                    double brl = rates.getDouble("BRL");
                    double usd = rates.getDouble("USD");
                    double eur = rates.getDouble("EUR");

                    double valor =
                            Double.parseDouble(
                                    etValor.getText().toString());

                    double valorBRL = valor * brl;
                    double valorUSD = valor * usd;
                    double valorEUR = valor * eur;

                    runOnUiThread(() -> {

                        txtResultado.setText(
                                "Base: " + moeda +
                                        "\nBRL: " + valorBRL +
                                        "\nUSD: " + valorUSD +
                                        "\nEUR: " + valorEUR
                        );

                    });

                } catch (Exception e) {

                    runOnUiThread(() ->
                            txtResultado.setText("Moeda inválida"));

                }

            }

        });

    }
}