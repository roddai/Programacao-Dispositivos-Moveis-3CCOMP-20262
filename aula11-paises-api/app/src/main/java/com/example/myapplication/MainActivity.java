package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Etapa 1: Declaração dos componentes da interface
    private EditText editPais;
    private Button btnBuscar;
    private ProgressBar progressBar;
    private TextView tvMensagem;
    private ImageView ivBandeira;
    private TextView tvNomePais;
    private TextView tvCapital;
    private TextView tvRegiao;
    private TextView tvPopulacao;

    // Fila de requisições do Volley
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Etapa 2: Ligação dos IDs do XML com os atributos Java (findViewById)
        editPais = findViewById(R.id.editPais);
        btnBuscar = findViewById(R.id.btnBuscar);
        progressBar = findViewById(R.id.progressBar);
        tvMensagem = findViewById(R.id.tvMensagem);
        ivBandeira = findViewById(R.id.ivBandeira);
        tvNomePais = findViewById(R.id.tvNomePais);
        tvCapital = findViewById(R.id.tvCapital);
        tvRegiao = findViewById(R.id.tvRegiao);
        tvPopulacao = findViewById(R.id.tvPopulacao);

        // Inicialização da fila de requisições HTTP do Volley
        requestQueue = Volley.newRequestQueue(this);

        // Etapa 3: Evento de clique do botão de busca
        btnBuscar.setOnClickListener(v -> buscarPais());
    }

    private void buscarPais() {
        // Etapa 4: Validação para campo vazio
        String paisDigitado = editPais.getText().toString().trim();

        if (paisDigitado.isEmpty()) {
            tvMensagem.setText("Por favor, digite a sigla ou nome do país.");
            limparResultados();
            return;
        }

        // Etapa 5: Montagem da URL da API
        String url = "https://servicodados.ibge.gov.br/api/v1/paises/" + paisDigitado;

        // Feedback visual: exibe a ProgressBar e mensagem de carregamento
        progressBar.setVisibility(View.VISIBLE);
        tvMensagem.setText("Realizando consulta...");
        limparResultados();

        // Etapa 6: Requisição HTTP GET usando Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    // Esconde a barra de progresso
                    progressBar.setVisibility(View.GONE);

                    // Verifica se o país não foi encontrado (resposta vazia)
                    if (response.length() == 0) {
                        tvMensagem.setText("País não encontrado. Verifique a sigla (ex: BR, US, FR).");
                        return;
                    }

                    try {
                        // Etapa 7: Interpretação do JSON retornado pela API real do IBGE
                        JSONObject paisObj = response.getJSONObject(0);

                        // Nome do país
                        JSONObject nomeObj = paisObj.getJSONObject("nome");
                        String nomePais = nomeObj.getString("abreviado");

                        // Capital
                        JSONObject governoObj = paisObj.optJSONObject("governo");
                        String capital = "Não informada";
                        if (governoObj != null && governoObj.has("capital")) {
                            capital = governoObj.getJSONObject("capital").getString("nome");
                        }

                        // Região
                        JSONObject locObj = paisObj.getJSONObject("localizacao");
                        JSONObject regiaoObj = locObj.getJSONObject("regiao");
                        String regiao = regiaoObj.getString("nome");

                        // População / Área Total
                        JSONObject areaObj = paisObj.getJSONObject("area");
                        String areaTotal = areaObj.getString("total");
                        String unidade = areaObj.getJSONObject("unidade").getString("símbolo");

                        // Código ISO para carregar a bandeira
                        JSONObject idObj = paisObj.getJSONObject("id");
                        String alpha2 = idObj.optString("ISO-3166-1-ALPHA-2", "").toLowerCase();

                        // Etapa 8: Exibição dos resultados nos componentes TextView
                        tvMensagem.setText("");
                        tvNomePais.setText("País: " + nomePais);
                        tvCapital.setText("Capital: " + capital);
                        tvRegiao.setText("Região: " + regiao);
                        tvPopulacao.setText("Área Total: " + areaTotal + " " + unidade);

                        // Etapa 9: Carregamento da imagem da bandeira com Picasso
                        if (!alpha2.isEmpty()) {
                            String urlBandeira = "https://flagcdn.com/w320/" + alpha2 + ".png";
                            Picasso.get().load(urlBandeira).into(ivBandeira);
                        }

                    } catch (JSONException e) {
                        // Tratamento de erro ao interpretar o JSON
                        tvMensagem.setText("Erro ao interpretar a resposta em JSON.");
                    }
                },
                error -> {
                    // Etapa 10: Tratamento de falha de conexão / requisição
                    progressBar.setVisibility(View.GONE);
                    tvMensagem.setText("Falha de conexão com a API. Verifique sua internet.");
                }
        );

        // Adiciona a requisição HTTP na fila do Volley
        requestQueue.add(jsonArrayRequest);
    }

    private void limparResultados() {
        tvNomePais.setText("");
        tvCapital.setText("");
        tvRegiao.setText("");
        tvPopulacao.setText("");
        ivBandeira.setImageDrawable(null);
    }
}
