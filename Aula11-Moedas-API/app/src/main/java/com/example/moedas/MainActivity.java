package com.example.moedas;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText edtMoeda, edtValor;
    Button btnConsultar;
    TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtMoeda = findViewById(R.id.edtMoeda);
        edtValor = findViewById(R.id.edtValor);
        btnConsultar = findViewById(R.id.btnConsultar);
        tvResultado = findViewById(R.id.tvResultado);

        btnConsultar.setOnClickListener(v -> {

            String moeda = edtMoeda.getText().toString().trim().toUpperCase();
            String texto = edtValor.getText().toString().trim();

            if (moeda.isEmpty() || texto.isEmpty()) {
                tvResultado.setText("Preencha os campos!");
                return;
            }

            if (!moeda.equals("BRL") && !moeda.equals("USD")
                    && !moeda.equals("EUR")) {
                tvResultado.setText("Moeda inválida!");
                return;
            }

            double valor;

            try {
                valor = Double.parseDouble(texto.replace(",", "."));
            } catch (Exception e) {
                tvResultado.setText("Valor inválido!");
                return;
            }

            tvResultado.setText("Consultando...");

            new Thread(() -> {
                try {
                    URL url = new URL(
                            "https://api.exchangerate-api.com/v4/latest/" + moeda
                    );

                    Scanner leitor = new Scanner(url.openStream());
                    String resposta = leitor.useDelimiter("\\A").next();
                    leitor.close();

                    JSONObject json = new JSONObject(resposta);
                    JSONObject taxas = json.getJSONObject("rates");

                    double real = taxas.getDouble("BRL");
                    double dolar = taxas.getDouble("USD");
                    double euro = taxas.getDouble("EUR");

                    String resultado =
                            "Moeda base: " + moeda +
                                    "\n\nCotação BRL: " + real +
                                    "\nCotação USD: " + dolar +
                                    "\nCotação EUR: " + euro +
                                    "\n\nValor convertido:" +
                                    "\nBRL: " + (valor * real) +
                                    "\nUSD: " + (valor * dolar) +
                                    "\nEUR: " + (valor * euro);

                    runOnUiThread(() ->
                            tvResultado.setText(resultado)
                    );

                } catch (Exception e) {
                    runOnUiThread(() ->
                            tvResultado.setText("Erro na consulta!")
                    );
                }
            }).start();
        });
    }
}