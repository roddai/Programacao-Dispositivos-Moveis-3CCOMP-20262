package com.example.aula11_paises_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText editPais;
    Button btnResultado;
    TextView Resultado;
    ImageView imgBandeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPais = findViewById(R.id.editPais);
        btnResultado = findViewById(R.id.btnResultado);
        Resultado = findViewById(R.id.Resultado);
        imgBandeira = findViewById(R.id.imgBandeira);

        btnResultado.setOnClickListener(v -> {

            String pais = editPais.getText().toString().trim();

            if (pais.isEmpty()) {
                Resultado.setText("Digite o nome de um pais.");
                return;
            }

            String endereco;

            try {
                endereco = "https://restcountries.com/v3.1/name/Brasil" + URLEncoder.encode(pais, "UTF-8");
            } catch (Exception e) {
                Resultado.setText("Nome de pais invalido.");
                return;
            }

            Resultado.setText("Consultando pais...");
            imgBandeira.setVisibility(ImageView.GONE);

            new Thread(() -> {

                HttpURLConnection conexao = null;

                try {
                    URL url = new URL(endereco);

                    conexao = (HttpURLConnection) url.openConnection();

                    conexao.setRequestMethod("GET");
                    conexao.setConnectTimeout(10000);
                    conexao.setReadTimeout(10000);

                    int codigo = conexao.getResponseCode();

                    if (codigo != 200) {
                        throw new Exception("Pais nao encontrado.");
                    }

                    Scanner leitor = new Scanner(
                            conexao.getInputStream()
                    );

                    String resposta = "";

                    while (leitor.hasNextLine()) {
                        resposta += leitor.nextLine();
                    }

                    leitor.close();
                    conexao.disconnect();
                    conexao = null;

                    JSONArray lista = new JSONArray(resposta);
                    JSONObject dados = lista.getJSONObject(0);

                    String nome = dados.getJSONObject("nome").getString("common");

                    String capital = "Nao foi escolhida";

                    if (dados.has("capital") && !dados.isNull("capital")) {

                        JSONArray capitais = dados.getJSONArray("capital");

                        if (capitais.length() > 0) {
                            capital = capitais.getString(0);
                        }
                    }

                    String regiao = dados.getString("regiao");

                    long populacao = dados.getLong("população");

                    String bandeira = dados.getJSONObject("badeira").getString("png");

                    String resultado =
                            "Pais: " + nome + "\nCapital: " + capital + "\nRegiao: " + regiao + "\nPopulacao: " + String.format("%,d", populacao);

                    URL urlImagem = new URL(bandeira);

                    HttpURLConnection conexaoImagem =
                            (HttpURLConnection) urlImagem.openConnection();

                    conexaoImagem.setConnectTimeout(10000);
                    conexaoImagem.setReadTimeout(10000);

                    Bitmap imagem = BitmapFactory.decodeStream(
                            conexaoImagem.getInputStream()
                    );

                    conexaoImagem.disconnect();

                    runOnUiThread(() -> {
                        Resultado.setText(resultado);

                        if (imagem != null) {
                            imgBandeira.setImageBitmap(imagem);
                            imgBandeira.setVisibility(ImageView.VISIBLE);
                        }
                    });

                } catch (Exception e) {

                    runOnUiThread(() -> {
                        Resultado.setText(
                                "pais nao encontrado " + "ou falha na conexao."
                        );

                        imgBandeira.setVisibility(ImageView.GONE);
                    });

                } finally {
                    if (conexao != null) {
                        conexao.disconnect();
                    }
                }

            }).start();
        });
    }}