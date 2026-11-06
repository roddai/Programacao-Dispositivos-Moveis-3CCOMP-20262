package com.example.aula11moedasapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etMoedaBase;
    private EditText etValor;
    private Button btnConverter;
    private TextView tvResultado;
    private ProgressBar progressBar;
    private RequestQueue filaRequisicoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMoedaBase = findViewById(R.id.etMoedaBase);
        etValor = findViewById(R.id.etValor);
        btnConverter = findViewById(R.id.btnConverter);
        tvResultado = findViewById(R.id.tvResultado);
        progressBar = findViewById(R.id.progressBar);

        filaRequisicoes = Volley.newRequestQueue(this);

        btnConverter.setOnClickListener(view -> validarDados());
    }

    private void validarDados() {

        etMoedaBase.setError(null);
        etValor.setError(null);

        String moeda = etMoedaBase
                .getText()
                .toString()
                .trim()
                .toUpperCase(Locale.ROOT);

        String textoValor = etValor
                .getText()
                .toString()
                .trim()
                .replace(",", ".");

        if (moeda.isEmpty()) {
            etMoedaBase.setError("Digite a moeda");
            etMoedaBase.requestFocus();

            tvResultado.setText(
                    "Informe uma moeda para realizar a conversão."
            );

            return;
        }

        if (!moeda.equals("BRL")
                && !moeda.equals("USD")
                && !moeda.equals("EUR")) {

            etMoedaBase.setError("Use BRL, USD ou EUR");
            etMoedaBase.requestFocus();

            tvResultado.setText(
                    "Moeda inválida.\n\n" +
                            "Use somente:\n" +
                            "BRL para Real\n" +
                            "USD para Dólar\n" +
                            "EUR para Euro"
            );

            return;
        }

        if (textoValor.isEmpty()) {
            etValor.setError("Digite o valor");
            etValor.requestFocus();

            tvResultado.setText(
                    "Informe o valor que deseja converter."
            );

            return;
        }

        double valor;

        try {
            valor = Double.parseDouble(textoValor);

        } catch (NumberFormatException erro) {
            etValor.setError("Digite um número válido");
            etValor.requestFocus();

            tvResultado.setText(
                    "O valor informado não é válido."
            );

            return;
        }

        if (valor <= 0) {
            etValor.setError("O valor deve ser maior que zero");
            etValor.requestFocus();

            tvResultado.setText(
                    "Digite um valor maior que zero."
            );

            return;
        }

        buscarCotacao(moeda, valor);
    }

    private void buscarCotacao(String moeda, double valor) {

        String url =
                "https://api.exchangerate-api.com/v4/latest/" + moeda;

        progressBar.setVisibility(View.VISIBLE);
        btnConverter.setEnabled(false);
        btnConverter.setText("Consultando...");

        tvResultado.setText(
                "Buscando a cotação atual.\nAguarde..."
        );

        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                resposta -> mostrarResultado(
                        resposta,
                        moeda,
                        valor
                ),

                erro -> {
                    finalizarCarregamento();

                    if (erro.networkResponse == null) {

                        tvResultado.setText(
                                "Não foi possível acessar a cotação.\n\n" +
                                        "Verifique se o dispositivo está conectado " +
                                        "à internet e tente novamente."
                        );

                    } else {

                        int codigoErro =
                                erro.networkResponse.statusCode;

                        if (codigoErro == 404) {

                            tvResultado.setText(
                                    "A moeda não foi encontrada."
                            );

                        } else if (codigoErro == 429) {

                            tvResultado.setText(
                                    "Muitas consultas foram realizadas.\n\n" +
                                            "Aguarde alguns instantes e tente novamente."
                            );

                        } else {

                            tvResultado.setText(
                                    "Erro ao consultar a cotação.\n\n" +
                                            "Código do erro: " + codigoErro
                            );
                        }
                    }
                }
        );

        requisicao.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        1,
                        1.0f
                )
        );

        filaRequisicoes.add(requisicao);
    }

    private void mostrarResultado(
            JSONObject resposta,
            String moeda,
            double valor
    ) {
        finalizarCarregamento();

        try {
            JSONObject taxas = resposta.getJSONObject("rates");

            double taxaBRL = taxas.getDouble("BRL");
            double taxaUSD = taxas.getDouble("USD");
            double taxaEUR = taxas.getDouble("EUR");

            double valorBRL = valor * taxaBRL;
            double valorUSD = valor * taxaUSD;
            double valorEUR = valor * taxaEUR;

            String resultado =
                    "VALOR INFORMADO"
                            + "\n"
                            + formatar(valor) + " " + moeda

                            + "\n\nRESULTADOS DA CONVERSÃO"

                            + "\n\nReal brasileiro"
                            + "\nR$ " + formatar(valorBRL)

                            + "\n\nDólar americano"
                            + "\nUS$ " + formatar(valorUSD)

                            + "\n\nEuro"
                            + "\n€ " + formatar(valorEUR);

            tvResultado.setText(resultado);

        } catch (Exception erro) {

            tvResultado.setText(
                    "Não foi possível ler as cotações recebidas."
            );
        }
    }

    private void finalizarCarregamento() {
        progressBar.setVisibility(View.GONE);
        btnConverter.setEnabled(true);
        btnConverter.setText("Converter agora");
    }

    private String formatar(double numero) {
        return String.format(
                new Locale("pt", "BR"),
                "%,.2f",
                numero
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (filaRequisicoes != null) {
            filaRequisicoes.cancelAll(this);
        }
    }
}