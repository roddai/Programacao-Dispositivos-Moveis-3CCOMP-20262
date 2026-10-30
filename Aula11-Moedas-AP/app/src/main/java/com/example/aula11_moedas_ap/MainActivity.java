package com.example.aula11_moedas_ap;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editMoedaBase;
    private EditText editValor;
    private Spinner spinnerMoeda;
    private Button btnBuscar;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMoedaBase = findViewById(R.id.editMoedaBase);
        editValor = findViewById(R.id.editValor);
        spinnerMoeda = findViewById(R.id.spinnerMoeda);
        btnBuscar = findViewById(R.id.btnBuscar);
        txtResultado = findViewById(R.id.txtResultado);

        btnBuscar.setOnClickListener(view -> validarDados());
    }

    private void validarDados() {

        String moedaBase = editMoedaBase.getText().toString().trim().toUpperCase(Locale.ROOT);

        String valorTexto = editValor.getText().toString().trim()
               .replace(",", ".");

        editMoedaBase.setError(null);
        editValor.setError(null);

        if (moedaBase.isEmpty()) {
            editMoedaBase.setError("Digite a moeda base");
            editMoedaBase.requestFocus();
            return;
        }

        if (moedaBase.length() != 3) {
            editMoedaBase.setError(
                    "Digite 3 letras, como BRL, USD ou EUR"
            );
            editMoedaBase.requestFocus();
            return;
        }

        if (valorTexto.isEmpty()) {
            editValor.setError("Digite um valor");
            editValor.requestFocus();
            return;
        }

        double valor;

        try {
            valor = Double.parseDouble(valorTexto);

            if (valor <= 0) {
                editValor.setError(
                        "Digite um valor maior que zero"
                );
                editValor.requestFocus();
                return;
            }

        } catch (NumberFormatException erro) {
            editValor.setError("Digite um número válido");
            editValor.requestFocus();
            return;
        }

        if (spinnerMoeda.getSelectedItem() == null) {
            txtResultado.setText("Selecione a moeda do valor informado.");
            return;
        }

        String moedaValor = spinnerMoeda.getSelectedItem().toString();

        consultarApi(moedaBase, valor, moedaValor);
    }

    private void consultarApi(String moedaBase, double valor, String moedaValor) {

        txtResultado.setText("Buscando cotações...");
        btnBuscar.setEnabled(false);
        btnBuscar.setText("BUSCANDO...");

        new Thread(() -> {

            HttpURLConnection conexao = null;

            try {

                String endereco =
                        "https://api.exchangerate-api.com/v4/latest/"
                                + moedaBase.toLowerCase(Locale.ROOT);

                URL url = new URL(endereco);

                conexao = (HttpURLConnection) url.openConnection();

                conexao.setRequestMethod("GET");
                conexao.setConnectTimeout(10000);
                conexao.setReadTimeout(10000);

                int codigoResposta = conexao.getResponseCode();

                if (codigoResposta == 404) {

                    runOnUiThread(() ->
                            mostrarErro("Moeda não encontrada. Verifique o código.")
                    );

                    return;
                }

                if (codigoResposta < 200
                        || codigoResposta >= 300) {

                    runOnUiThread(() ->
                            mostrarErro(
                                    "Erro na API. Código: "
                                            + codigoResposta
                            )
                    );

                    return;
                }

                BufferedReader leitor =
                        new BufferedReader(new InputStreamReader(conexao.getInputStream()));

                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }

                leitor.close();

                JSONObject json = new JSONObject(resposta.toString());

                JSONObject taxas = json.getJSONObject("rates");

                double taxaBRL = taxas.getDouble("BRL");

                double taxaUSD = taxas.getDouble("USD");

                double taxaEUR = taxas.getDouble("EUR");

                double taxaOrigem;

                if (moedaValor.equals("BRL")) {

                    taxaOrigem = taxaBRL;

                } else if (moedaValor.equals("USD")) {

                    taxaOrigem = taxaUSD;

                } else {

                    taxaOrigem = taxaEUR;
                }


                double valorNaBase = valor / taxaOrigem;

                double valorEmBRL = valorNaBase * taxaBRL;

                double valorEmUSD = valorNaBase * taxaUSD;

                double valorEmEUR = valorNaBase * taxaEUR;

                String resultado =
                        "Moeda base: " + moedaBase
                                + "\n\nCotações atuais:"
                                + "\n1 " + moedaBase
                                + " = " + formatar(taxaBRL)
                                + " BRL"
                                + "\n1 " + moedaBase
                                + " = " + formatar(taxaUSD)
                                + " USD"
                                + "\n1 " + moedaBase
                                + " = " + formatar(taxaEUR)
                                + " EUR"
                                + "\n\nConversão de "
                                + formatar(valor)
                                + " " + moedaValor
                                + ":\n";


                if (!moedaValor.equals("BRL")) {
                    resultado += formatar(valorEmBRL) + " BRL\n";
                }

                if (!moedaValor.equals("USD")) {
                    resultado += formatar(valorEmUSD) + " USD\n";
                }

                if (!moedaValor.equals("EUR")) {
                    resultado += formatar(valorEmEUR) + " EUR";
                }

                String resultadoFinal = resultado.trim();

                runOnUiThread(() -> {

                    txtResultado.setText(resultadoFinal);

                    restaurarBotao();

                    Toast.makeText(MainActivity.this, "Cotação consultada com sucesso", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception erro) {

                erro.printStackTrace();

                runOnUiThread(() -> {

                    mostrarErro(
                            "Falha ao consultar a API.\n"
                                    + "Verifique a internet e a moeda informada."
                    );

                    Toast.makeText(
                            MainActivity.this,
                            "Não foi possível consultar a API",
                            Toast.LENGTH_LONG
                    ).show();
                });

            } finally {

                if (conexao != null) {
                    conexao.disconnect();
                }
            }

        }).start();
    }

    private String formatar(double valor) {

        return String.format(new Locale("pt", "BR"), "%.2f", valor);
    }

    private void mostrarErro(String mensagem) {

        txtResultado.setText(mensagem);

        restaurarBotao();
    }

    private void restaurarBotao() {

        btnBuscar.setEnabled(true);

        btnBuscar.setText("BUSCAR COTAÇÕES");
    }
}