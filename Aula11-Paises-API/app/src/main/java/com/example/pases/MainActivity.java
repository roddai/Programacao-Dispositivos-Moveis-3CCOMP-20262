package com.example.pases;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.*;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText edtPais;
    Button btnBuscar;
    TextView tvResultado;
    ImageView imgBandeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPais = findViewById(R.id.edPais);
        btnBuscar = findViewById(R.id.btBuscar);
        tvResultado = findViewById(R.id.tResultado);
        imgBandeira = findViewById(R.id.imBandeira);

        btnBuscar.setOnClickListener(v -> {

            String pais = edtPais.getText().toString().trim();

            if (pais.isEmpty()) {
                tvResultado.setText("Digite um país!");
                return;
            }

            tvResultado.setText("Buscando...");
            imgBandeira.setImageDrawable(null);

            new Thread(() -> {
                try {

                    // Busca a lista de países do IBGE
                    URL url = new URL(
                            "https://servicodados.ibge.gov.br/api/v1/paises"
                    );

                    Scanner leitor = new Scanner(url.openStream());
                    String resposta = leitor.useDelimiter("\\A").next();
                    leitor.close();

                    JSONArray lista = new JSONArray(resposta);

                    JSONObject encontrado = null;

                    // Procura o país digitado
                    for (int i = 0; i < lista.length(); i++) {

                        JSONObject item = lista.getJSONObject(i);
                        String nome = item.getJSONObject("nome")
                                .getString("abreviado");

                        if (nome.equalsIgnoreCase(pais)) {
                            encontrado = item;
                            break;
                        }
                    }

                    if (encontrado == null) {
                        runOnUiThread(() ->
                                tvResultado.setText("País não encontrado!")
                        );
                        return;
                    }

                    // Informações do país
                    String nomePais = encontrado.getJSONObject("nome")
                            .getString("abreviado");

                    String capital = encontrado.getJSONObject("governo")
                            .getJSONObject("capital")
                            .getString("nome");

                    String regiao = encontrado.getJSONObject("localizacao")
                            .getJSONObject("regiao")
                            .getString("nome");

                    String codigo = encontrado.getJSONObject("id")
                            .getString("ISO-3166-1-ALPHA-2");

                    // Busca o indicador de população
                    String populacao = "Não disponível";

                    try {
                        URL urlPop = new URL(
                                "https://servicodados.ibge.gov.br/api/v1/paises/"
                                        + codigo + "/indicadores/77849"
                        );

                        Scanner leitorPop = new Scanner(urlPop.openStream());
                        String respostaPop = leitorPop.useDelimiter("\\A").next();
                        leitorPop.close();

                        JSONArray indicadores = new JSONArray(respostaPop);
                        JSONArray series = indicadores.getJSONObject(0)
                                .getJSONArray("series");

                        JSONArray valores = series.getJSONObject(0)
                                .getJSONArray("serie");

                        // Procura o último ano com população informada
                        for (int i = valores.length() - 1; i >= 0; i--) {

                            JSONObject ano = valores.getJSONObject(i);
                            String chave = ano.keys().next();
                            String numero = ano.optString(chave);

                            if (!numero.isEmpty() &&
                                    !numero.equals("null") &&
                                    !numero.equals("-")) {
                                populacao = numero + " (" + chave + ")";
                                break;
                            }
                        }

                    } catch (Exception e) {
                        populacao = "Não disponível";
                    }

                    String resultado =
                            "País: " + nomePais +
                                    "\nCapital: " + capital +
                                    "\nRegião: " + regiao +
                                    "\nPopulação: " + populacao;

                    runOnUiThread(() ->
                            tvResultado.setText(resultado)
                    );

                } catch (Exception e) {

                    runOnUiThread(() ->
                            tvResultado.setText(
                                    "Erro na consulta: " + e.getMessage()
                            )
                    );

                }
            }).start();
        });
    }
}

