package com.example.aula11_paises_api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editPais;
    private Button btnBuscar;
    private TextView txtResultado;
    private ImageView imgBandeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPais = findViewById(R.id.editPais);
        btnBuscar = findViewById(R.id.btnBuscar);
        txtResultado = findViewById(R.id.txtResultado);
        imgBandeira = findViewById(R.id.imgBandeira);
        btnBuscar.setOnClickListener(view -> validarPais());
    }

    private void validarPais() {

        String nomePais = editPais.getText().toString().trim();

        editPais.setError(null);

        if (nomePais.isEmpty()) {
            editPais.setError("Digite o nome de um país");
            editPais.requestFocus();
            return;
        }

        buscarPais(nomePais);
    }

    private void buscarPais(String nomeDigitado) {

        txtResultado.setText("Buscando país...");
        imgBandeira.setVisibility(View.GONE);

        btnBuscar.setEnabled(false);
        btnBuscar.setText("BUSCANDO...");

        new Thread(() -> {

            HttpURLConnection conexao = null;

            try {

                String endereco = "https://servicodados.ibge.gov.br/api/v1/paises/";

                URL url = new URL(endereco);

                conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setConnectTimeout(10000);
                conexao.setReadTimeout(15000);

                int codigoResposta = conexao.getResponseCode();

                if (codigoResposta != 200) {

                    runOnUiThread(() -> mostrarErro("Erro na API do IBGE. Código: " + codigoResposta));

                    return;
                }

                BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }

                leitor.close();

                JSONArray listaPaises = new JSONArray(resposta.toString());

                JSONObject paisEncontrado = null;

                String pesquisa = removerAcentos(nomeDigitado).toLowerCase(Locale.ROOT);

                for (int i = 0; i < listaPaises.length(); i++) {

                    JSONObject pais = listaPaises.getJSONObject(i);

                    JSONObject nomes = pais.getJSONObject("nome");

                    String nomeAbreviado = nomes.getString("abreviado");

                    String nomeComparacao = removerAcentos(nomeAbreviado).toLowerCase(Locale.ROOT);

                    if (nomeComparacao.equals(pesquisa)) {
                        paisEncontrado = pais;
                        break;
                    }
                }

                if (paisEncontrado == null) {

                    runOnUiThread(() -> mostrarErro("País não encontrado."));

                    return;
                }

                JSONObject nomes = paisEncontrado.getJSONObject("nome");

                String nome = nomes.getString("abreviado");

                JSONObject identificadores = paisEncontrado.getJSONObject("id");

                String codigoPais = identificadores.getString("ISO-3166-1-ALPHA-2");

                JSONObject governo = paisEncontrado.getJSONObject("governo");

                String capital = governo.getJSONObject("capital").getString("nome");

                JSONObject localizacao = paisEncontrado.getJSONObject("localizacao");

                String regiao = localizacao.getJSONObject("regiao").getString("nome");

                JSONObject objetoSubRegiao = localizacao.optJSONObject("sub-regiao");
                String subRegiao = "Não informada";

                if (objetoSubRegiao != null) {
                    subRegiao = objetoSubRegiao.optString("nome", "Não informada");
                }

                JSONObject area = paisEncontrado.getJSONObject("area");

                String areaTotal = area.getString("total");

                String simboloArea = area.getJSONObject("unidade").getString("símbolo");

                JSONArray linguas = paisEncontrado.optJSONArray("linguas");

                String idioma = "Não informado";

                if (linguas != null && linguas.length() > 0) {
                    idioma = linguas.getJSONObject(0).getString("nome");
                }

                JSONArray moedas = paisEncontrado.optJSONArray("unidades-monetarias");

                String moeda = "Não informada";

                if (moedas != null && moedas.length() > 0) {
                    moeda = moedas.getJSONObject(0).getString("nome");
                }

                String urlBandeira = "https://flagcdn.com/256x192/" + codigoPais.toLowerCase(Locale.ROOT) + ".png";

                Bitmap bandeira = baixarBandeira(urlBandeira);

                String textoResultado =
                        "País: " + nome
                                + "\n\nCapital: " + capital
                                + "\nRegião: " + regiao
                                + "\nSub-região: " + subRegiao
                                + "\nÁrea: " + areaTotal
                                + " " + simboloArea
                                + "\nIdioma: " + idioma
                                + "\nMoeda: " + moeda;

                String resultadoFinal = textoResultado;
                Bitmap bandeiraFinal = bandeira;

                runOnUiThread(() -> {

                    txtResultado.setText(resultadoFinal);

                    if (bandeiraFinal != null) {
                        imgBandeira.setImageBitmap(bandeiraFinal);
                        imgBandeira.setVisibility(View.VISIBLE);
                    } else {
                        imgBandeira.setVisibility(View.GONE);
                    }

                    restaurarBotao();

                    Toast.makeText(MainActivity.this, "País encontrado", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception erro) {

                erro.printStackTrace();

                runOnUiThread(() -> mostrarErro("Falha ao consultar os dados."));

            } finally {

                if (conexao != null) {
                    conexao.disconnect();
                }
            }

        }).start();
    }

    private Bitmap baixarBandeira(String endereco) {

        HttpURLConnection conexaoImagem = null;

        try {

            URL urlImagem = new URL(endereco);

            conexaoImagem = (HttpURLConnection) urlImagem.openConnection();

            conexaoImagem.setRequestMethod("GET");
            conexaoImagem.setConnectTimeout(10000);
            conexaoImagem.setReadTimeout(10000);

            InputStream entrada = conexaoImagem.getInputStream();

            Bitmap imagem = BitmapFactory.decodeStream(entrada);

            entrada.close();

            return imagem;

        } catch (Exception erro) {

            return null;

        } finally {

            if (conexaoImagem != null) {
                conexaoImagem.disconnect();
            }
        }
    }

    private String removerAcentos(String texto) {

        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);

        return textoNormalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    private void mostrarErro(String mensagem) {

        txtResultado.setText(mensagem);

        imgBandeira.setImageDrawable(null);
        imgBandeira.setVisibility(View.GONE);

        restaurarBotao();

        Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_LONG).show();
    }

    private void restaurarBotao() {

        btnBuscar.setEnabled(true);
        btnBuscar.setText("BUSCAR PAÍS");
    }
}