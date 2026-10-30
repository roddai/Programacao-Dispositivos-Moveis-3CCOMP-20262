package com.example.aula11_paisesapi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerPais;
    private TextView tvIndicadorPais;
    private Button btnPesquisar;
    private TextView tvEmojiBandeira, tvNomePais, tvCapital, tvRegiao, tvLinguas;

    private final Map<String, String> paisesIsoMap = new HashMap<String, String>() {{
        put("Brasil", "BR");
        put("Argentina", "AR");
        put("Estados Unidos", "US");
        put("Japão", "JP");
        put("Alemanha", "DE");
        put("França", "FR");
        put("Portugal", "PT");
    }};

    private final Map<String, String> paisesEmojiMap = new HashMap<String, String>() {{
        put("Brasil", "🇧🇷");
        put("Argentina", "🇦🇷");
        put("Estados Unidos", "🇺🇸");
        put("Japão", "🇯🇵");
        put("Alemanha", "🇩🇪");
        put("França", "🇫🇷");
        put("Portugal", "🇵🇹");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerPais = findViewById(R.id.spinnerPais);
        tvIndicadorPais = findViewById(R.id.tvIndicadorPais);
        btnPesquisar = findViewById(R.id.btnPesquisar);
        tvEmojiBandeira = findViewById(R.id.tvEmojiBandeira);
        tvNomePais = findViewById(R.id.tvNomePais);
        tvCapital = findViewById(R.id.tvCapital);
        tvRegiao = findViewById(R.id.tvRegiao);
        tvLinguas = findViewById(R.id.tvLingua);

        configurarSpinner();

        btnPesquisar.setOnClickListener(v -> {
            String paisSelecionado = spinnerPais.getSelectedItem().toString();
            String codigoIso = paisesIsoMap.get(paisSelecionado);
            if (codigoIso != null) {
                buscarDadosPais(codigoIso, paisSelecionado);
            }
        });
    }

    private void configurarSpinner() {
        String[] listaPaises = paisesIsoMap.keySet().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaPaises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPais.setAdapter(adapter);

        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String paisSelecionado = listaPaises[position];
                tvIndicadorPais.setText("País Selecionado: " + paisSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void buscarDadosPais(String codigoIso, String nomePaisSelecionado) {
        String url = "https://servicodados.ibge.gov.br/api/v1/paises/" + codigoIso;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .header("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Erro de Conexão. Verifique a rede.", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        if (jsonArray.length() > 0) {
                            JSONObject paisObj = jsonArray.getJSONObject(0);

                            String nome = paisObj.getJSONObject("nome").getString("abreviado");
                            String capital = paisObj.getJSONObject("governo").getJSONObject("capital").getString("nome");
                            String regiao = paisObj.getJSONObject("localizacao").getJSONObject("regiao").getString("nome");

                            // Extração das línguas do JSON da API do IBGE
                            JSONArray linguasArray = paisObj.getJSONArray("linguas");
                            StringBuilder sbLinguas = new StringBuilder();
                            for (int i = 0; i < linguasArray.length(); i++) {
                                JSONObject linguaObj = linguasArray.getJSONObject(i);
                                sbLinguas.append(linguaObj.getString("nome"));
                                if (i < linguasArray.length() - 1) {
                                    sbLinguas.append(", ");
                                }
                            }
                            String linguasStr = sbLinguas.length() > 0 ? sbLinguas.toString() : "Não informada";

                            String emoji = paisesEmojiMap.get(nomePaisSelecionado);

                            runOnUiThread(() -> {
                                tvEmojiBandeira.setText(emoji != null ? emoji : "🏳️");
                                tvNomePais.setText(nome);
                                tvCapital.setText("Capital: " + capital);
                                tvRegiao.setText("Região: " + regiao);
                                tvLinguas.setText("Línguas: " + linguasStr);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}