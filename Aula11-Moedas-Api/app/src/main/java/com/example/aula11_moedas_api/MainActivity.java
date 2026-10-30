package com.example.aula11_moedas_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText editMoeda;
    EditText editValor;
    Button btnConsultar;
    TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMoeda = findViewById(R.id.editMoeda);
        editValor = findViewById(R.id.editValor);
        btnConsultar = findViewById(R.id.btnConsultar);
        txtResultado = findViewById(R.id.Resultado);

        btnConsultar.setOnClickListener(v -> {

            String moeda = editMoeda.getText().toString()
                    .trim().toUpperCase();

            String valorTexto = editValor.getText().toString()
                    .trim().replace(",", ".");

            if (moeda.isEmpty() || valorTexto.isEmpty()) {
                txtResultado.setText("Preencha todos os campos.");
                return;
            }
            if (!moeda.equals("BRL")
                    && !moeda.equals("USD")
                    && !moeda.equals("EUR")) {

                txtResultado.setText("Digite BRL, USD ou EUR.");
                return;
            }

            double valor;

            try {
                valor = Double.parseDouble(valorTexto);

                if (valor < 0 || Double.isInfinite(valor)
                        || Double.isNaN(valor)) {
                    txtResultado.setText("Digite um valor valido.");
                    return;
                }

            } catch (NumberFormatException e) {
                txtResultado.setText("Valor invalido.");
                return;
            }

            String endereco = "https://api.exchangerate-api.com/v4/latest/brl" + moeda;

            txtResultado.setText("Verficando cotacao");

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
                        throw new Exception("Erro ao acessar a API.");
                    }
                    Scanner leitor = new Scanner(
                            conexao.getInputStream()
                    );

                    String resposta = "";

                    while (leitor.hasNextLine()) {
                        resposta += leitor.nextLine();
                    }

                    leitor.close();

                    JSONObject dados = new JSONObject(resposta);
                    JSONObject taxas = dados.getJSONObject("rates");

                    double real = taxas.getDouble("BRL");
                    double dolar = taxas.getDouble("USD");
                    double euro = taxas.getDouble("EUR");

                    double valorReal = valor * real;
                    double valorDolar = valor * dolar;
                    double valorEuro = valor * euro;

                    String resultado =
                            "Moeda base: " + moeda + "\nValor informado: " + valor + "\n\nCOTACOES:" + "\n1 " + moeda + " = " + real + " BRL" + "\n1 " + moeda + " = " + dolar + " USD" + "\n1 " + moeda + " = " + euro + " EUR"
                                    + "\n\nCONVERSOES:"
                                    + "\nReal: " + valorReal + " BRL"
                                    + "\nDolar: " + valorDolar + " USD"
                                    + "\nEuro: " + valorEuro + " EUR";

                    runOnUiThread(() -> {
                        txtResultado.setText(resultado);
                    });

                } catch (Exception e) {

                    runOnUiThread(() -> {
                        txtResultado.setText(
                                "Erro na conexao ou na consulta."
                        );
                    });

                } finally {
                    if (conexao != null) {
                        conexao.disconnect();
                    }
                }

            }).start();
        });
    }
}