package com.example.aula11_paises;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etCountryName;
    private Button btnSearch;
    private TextView tvResult;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização dos componentes
        etCountryName = findViewById(R.id.etCountryName);
        btnSearch = findViewById(R.id.btnSearch);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);

        // Fila de requisições do Volley
        requestQueue = Volley.newRequestQueue(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCountry();
            }
        });
    }

    private void searchCountry() {
        String query = etCountryName.getText().toString().trim();

        // 1. Validação de campo vazio
        if (query.isEmpty()) {
            etCountryName.setError("Informe o nome de um país");
            etCountryName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        tvResult.setText("");

        // 2. URL correta da API de Países do IBGE
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/paises";

        // 3. Requisição HTTP GET (JsonArray)
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        boolean found = false;

                        try {
                            // Percorre a lista de países retornada pela API
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject paisObj = response.getJSONObject(i);
                                String nomePais = paisObj.getString("nome");

                                // Comparação insensível a maiúsculas/minúsculas
                                if (nomePais.equalsIgnoreCase(query)) {
                                    found = true;

                                    // Extração segura dos códigos ISO a partir do objeto "id"
                                    JSONObject idObj = paisObj.optJSONObject("id");
                                    String isoAlpha2 = "N/D";
                                    String isoAlpha3 = "N/D";
                                    if (idObj != null) {
                                        isoAlpha2 = idObj.optString("ISO-ALFA-2", "N/D");
                                        isoAlpha3 = idObj.optString("ISO-ALFA-3", "N/D");
                                    }

                                    // Extração da região intermediária (se disponível)
                                    String regiao = "N/D";
                                    JSONObject regiaoInterm = paisObj.optJSONObject("regiao-intermediaria");
                                    if (regiaoInterm != null) {
                                        regiao = regiaoInterm.optString("nome", "N/D");
                                    }

                                    // Montagem do resultado formatado no ecrã
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("🌍 **País:** ").append(nomePais).append("\n");
                                    sb.append("🏷️ **ISO Alpha-2:** ").append(isoAlpha2).append("\n");
                                    sb.append("🏷️ **ISO Alpha-3:** ").append(isoAlpha3).append("\n");
                                    sb.append("🗺️ **Região:** ").append(regiao).append("\n");

                                    tvResult.setText(sb.toString());
                                    break;
                                }
                            }

                            if (!found) {
                                tvResult.setText("⚠️ País não encontrado na base de dados do IBGE.");
                            }

                        } catch (JSONException e) {
                            tvResult.setText("❌ Erro ao processar os dados recebidos da API.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        // 4. Tratamento de erros de ligação
                        tvResult.setText("❌ Falha na ligação. Verifique a sua internet e tente novamente.");
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}