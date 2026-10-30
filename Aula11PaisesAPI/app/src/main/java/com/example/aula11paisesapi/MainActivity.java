package com.example.aula11paisesapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etPais;
    private Button btnBuscar;
    private TextView tvResultado;
    private ImageView imgBandeira;
    private ProgressBar progressBar;

    private JSONArray listaPaises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPais = findViewById(R.id.etPais);
        btnBuscar = findViewById(R.id.btnBuscar);
        tvResultado = findViewById(R.id.tvResultado);
        imgBandeira = findViewById(R.id.imgBandeira);
        progressBar = findViewById(R.id.progressBar);

        // O IBGE não envia uma imagem da bandeira.
        imgBandeira.setVisibility(View.GONE);

        btnBuscar.setOnClickListener(view -> validarBusca());

        carregarPaises();
    }

    private void carregarPaises() {
        String url =
                "https://servicodados.ibge.gov.br/api/v1/"
                        + "localidades/paises?view=nivelado";

        iniciarCarregamento("Carregando dados do IBGE...");

        JsonArrayRequest requisicao = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                resposta -> {
                    listaPaises = resposta;
                    finalizarCarregamento();

                    tvResultado.setText(
                            "Dados do IBGE carregados.\n\n"
                                    + "Digite o nome de um país e toque em buscar."
                    );
                },

                erro -> {
                    finalizarCarregamento();

                    if (erro.networkResponse == null) {
                        tvResultado.setText(
                                "Não foi possível acessar o IBGE.\n\n"
                                        + "Verifique a conexão com a internet."
                        );
                    } else {
                        tvResultado.setText(
                                "Erro ao acessar o IBGE.\n\n"
                                        + "Código: "
                                        + erro.networkResponse.statusCode
                        );
                    }
                }
        );

        Volley.newRequestQueue(this).add(requisicao);
    }

    private void validarBusca() {
        String nomeDigitado =
                etPais.getText().toString().trim();

        if (nomeDigitado.isEmpty()) {
            etPais.setError("Digite o nome de um país");
            etPais.requestFocus();
            return;
        }

        if (listaPaises == null) {
            tvResultado.setText(
                    "Os dados ainda não foram carregados.\n"
                            + "Aguarde alguns instantes."
            );

            carregarPaises();
            return;
        }

        buscarPais(nomeDigitado);
    }

    private void buscarPais(String nomeDigitado) {
        String pesquisaNormalizada =
                removerAcentos(nomeDigitado);

        JSONObject paisEncontrado = null;

        try {
            for (int i = 0; i < listaPaises.length(); i++) {
                JSONObject pais =
                        listaPaises.getJSONObject(i);

                String nome = pais.optString(
                        "pais-nome",
                        ""
                );

                String nomeNormalizado =
                        removerAcentos(nome);

                if (nomeNormalizado.equals(pesquisaNormalizada)) {
                    paisEncontrado = pais;
                    break;
                }
            }

            if (paisEncontrado == null) {
                tvResultado.setText(
                        "País não encontrado.\n\n"
                                + "Tente escrever o nome em português.\n"
                                + "Exemplos: Brasil, Japão ou Argentina."
                );

                return;
            }

            mostrarResultado(paisEncontrado);

        } catch (Exception erro) {
            tvResultado.setText(
                    "Não foi possível pesquisar o país."
            );
        }
    }

    private void mostrarResultado(JSONObject pais) {
        String nome = pais.optString(
                "pais-nome",
                "Não informado"
        );

        String codigo2 = pais.optString(
                "pais-ISO-ALPHA-2",
                ""
        );

        String codigo3 = pais.optString(
                "pais-ISO-ALPHA-3",
                "Não informado"
        );

        String regiao = pais.optString(
                "regiao-nome",
                "Não informada"
        );

        String subRegiao = pais.optString(
                "sub-regiao-nome",
                "Não informada"
        );

        String regiaoIntermediaria = pais.optString(
                "regiao-intermediaria-nome",
                ""
        );

        String bandeira = criarEmojiBandeira(codigo2);

        String resultado =
                bandeira
                        + "\n\nPAÍS ENCONTRADO"

                        + "\n\nNome"
                        + "\n" + nome

                        + "\n\nCódigo ISO de 2 letras"
                        + "\n" + codigo2

                        + "\n\nCódigo ISO de 3 letras"
                        + "\n" + codigo3

                        + "\n\nRegião"
                        + "\n" + regiao

                        + "\n\nSub-região"
                        + "\n" + subRegiao;

        if (!regiaoIntermediaria.isEmpty()
                && !regiaoIntermediaria.equals("null")) {

            resultado +=
                    "\n\nRegião intermediária"
                            + "\n" + regiaoIntermediaria;
        }

        resultado += "\n\nFonte: IBGE";

        tvResultado.setText(resultado);
    }

    private String removerAcentos(String texto) {
        String normalizado =
                Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                );

        return normalizado
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }

    private String criarEmojiBandeira(String codigo) {
        if (codigo == null || codigo.length() != 2) {
            return "🌎";
        }

        codigo = codigo.toUpperCase(Locale.ROOT);

        int primeiraLetra =
                Character.codePointAt(codigo, 0)
                        - 'A'
                        + 0x1F1E6;

        int segundaLetra =
                Character.codePointAt(codigo, 1)
                        - 'A'
                        + 0x1F1E6;

        return new String(
                Character.toChars(primeiraLetra)
        ) + new String(
                Character.toChars(segundaLetra)
        );
    }

    private void iniciarCarregamento(String mensagem) {
        progressBar.setVisibility(View.VISIBLE);

        btnBuscar.setEnabled(false);
        btnBuscar.setText("Carregando...");

        tvResultado.setText(mensagem);
    }

    private void finalizarCarregamento() {
        progressBar.setVisibility(View.GONE);

        btnBuscar.setEnabled(true);
        btnBuscar.setText("Buscar país");
    }
}