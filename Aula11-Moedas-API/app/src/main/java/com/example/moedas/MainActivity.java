package com.example.moedas;

import android.os.Bundle;
import android.telecom.Call;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;

import javax.security.auth.callback.Callback;

public class MainActivity extends AppCompatActivity {

    EditText Moeda1, Moeda2;
    TextView txtResultado;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        Moeda1 = findViewById(R.id.Moeda1);
        Moeda2 = findViewById(R.id.Moeda2);
        txtResultado = findViewById(R.id.txtResultado);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.btn1).setOnClickListener(v -> buscar());

    }

    void buscar() {
        String moeda = Moeda1.getText().toString().trim().toUpperCase();
        String v = Moeda2.getText().toString().replace(",", ".");
        if (moeda.isEmpty() || v.isEmpty()) return;
        double valor = Double.parseDouble(v);

        String url = "https://api.exchangerate-api.com/v4/latest/" + moeda;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONObject rates = response.getJSONObject("rates");

                        String resultado = String.format(
                                "BRL: %.2f\nUSD: %.2f\nEUR: %.2f",
                                valor * rates.getDouble("BRL"),
                                valor * rates.getDouble("USD"),
                                valor * rates.getDouble("EUR")
                        );

                        txtResultado.setText(resultado);

                    } catch (Exception e) {
                        txtResultado.setText("Erro ao converter");
                    }
                },
                error -> txtResultado.setText("Erro de conexão")
        );

        queue.add(request);

    }
}