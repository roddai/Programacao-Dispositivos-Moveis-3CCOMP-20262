package com.example.aula11_moedas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etBaseCurrency, etAmount;
    private Button btnFetchRates;
    private TextView tvResult;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização das views
        etBaseCurrency = findViewById(R.id.etBaseCurrency);
        etAmount = findViewById(R.id.etAmount);
        btnFetchRates = findViewById(R.id.btnFetchRates);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);

        // Inicialização do Volley
        requestQueue = Volley.newRequestQueue(this);

        btnFetchRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCurrencyRates();
            }
        });
    }

    private void fetchCurrencyRates() {
        String baseCurrency = etBaseCurrency.getText().toString().trim().toUpperCase();
        String amountStr = etAmount.getText().toString().trim();

        // 1. Validação de campos vazios
        if (baseCurrency.isEmpty()) {
            etBaseCurrency.setError("Informe a moeda base (ex: BRL)");
            etBaseCurrency.requestFocus();
            return;
        }

        double amount = 1.0;
        if (!amountStr.isEmpty()) {
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                etAmount.setError("Valor inválido");
                return;
            }
        }

        String url = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;

        progressBar.setVisibility(View.VISIBLE);
        tvResult.setText("");

        double finalAmount = amount;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            // Extrair taxas de câmbio
                            JSONObject rates = response.getJSONObject("rates");

                            double rateBRL = rates.optDouble("BRL", -1);
                            double rateUSD = rates.optDouble("USD", -1);
                            double rateEUR = rates.optDouble("EUR", -1);

                            // Montagem do resultado formatado
                            StringBuilder resultText = new StringBuilder();
                            resultText.append("📌 Moeda base: ").append(baseCurrency).append("\n\n");
                            resultText.append("💱 Cotações (1 ").append(baseCurrency).append("):\n");
                            resultText.append(String.format(Locale.getDefault(), "• BRL (Real): %.4f\n", rateBRL));
                            resultText.append(String.format(Locale.getDefault(), "• USD (Dólar): %.4f\n", rateUSD));
                            resultText.append(String.format(Locale.getDefault(), "• EUR (Euro): %.4f\n\n", rateEUR));

                            if (!amountStr.isEmpty()) {
                                resultText.append("💰 Conversão de ").append(amountStr).append(" ").append(baseCurrency).append(":\n");
                                resultText.append(String.format(Locale.getDefault(), "• BRL: R$ %.2f\n", finalAmount * rateBRL));
                                resultText.append(String.format(Locale.getDefault(), "• USD: $ %.2f\n", finalAmount * rateUSD));
                                resultText.append(String.format(Locale.getDefault(), "• EUR: € %.2f\n", finalAmount * rateEUR));
                            }

                            tvResult.setText(resultText.toString());

                        } catch (JSONException e) {
                            tvResult.setText("Erro ao processar dados da API.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);


                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            tvResult.setText("Erro: Moeda informada não foi encontrada ou é inválida.");
                        } else {
                            tvResult.setText("Falha na conexão. Verifique sua internet e tente novamente.");
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}