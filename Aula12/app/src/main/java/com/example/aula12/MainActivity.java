package com.example.aula12;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editSenha;
    private Button btnLogin;
    private Button btnCadastro;

    private static final String URL_LOGIN =
            "http://10.0.2.2/mobile_app/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnLogin.setOnClickListener(view -> realizarLogin());

        btnCadastro.setOnClickListener(view -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }

    private void realizarLogin() {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(
                    this,
                    "Preencha e-mail e senha",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Entrando...");

        new Thread(() -> {
            HttpURLConnection conexao = null;

            try {
                URL url = new URL(URL_LOGIN);
                conexao = (HttpURLConnection) url.openConnection();

                conexao.setRequestMethod("POST");
                conexao.setConnectTimeout(10000);
                conexao.setReadTimeout(10000);
                conexao.setDoOutput(true);

                conexao.setRequestProperty(
                        "Content-Type",
                        "application/x-www-form-urlencoded; charset=UTF-8"
                );

                String dados =
                        "email=" + URLEncoder.encode(
                                email,
                                StandardCharsets.UTF_8.toString()
                        )
                                + "&senha=" + URLEncoder.encode(
                                senha,
                                StandardCharsets.UTF_8.toString()
                        );

                try (OutputStream output = conexao.getOutputStream()) {
                    output.write(
                            dados.getBytes(StandardCharsets.UTF_8)
                    );

                    output.flush();
                }

                int codigoResposta = conexao.getResponseCode();

                InputStream input;

                if (codigoResposta >= 200 && codigoResposta < 300) {
                    input = conexao.getInputStream();
                } else {
                    input = conexao.getErrorStream();
                }

                String resposta = lerResposta(input);
                JSONObject json = new JSONObject(resposta);

                boolean sucesso = json.optBoolean("sucesso", false);

                String mensagem = json.optString(
                        "mensagem",
                        "Resposta inválida do servidor"
                );

                if (sucesso) {
                    JSONObject usuario = json.optJSONObject("usuario");

                    if (usuario != null) {
                        String nome = usuario.optString("nome", "");

                        mensagem = mensagem
                                + "\nBem-vindo, "
                                + nome;
                    }
                }

                String mensagemFinal = mensagem;

                runOnUiThread(() -> {
                    restaurarBotao();

                    Toast.makeText(
                            MainActivity.this,
                            mensagemFinal,
                            Toast.LENGTH_LONG
                    ).show();

                    if (sucesso) {
                        editSenha.setText("");
                    }
                });

            } catch (Exception erro) {
                runOnUiThread(() -> {
                    restaurarBotao();

                    Toast.makeText(
                            MainActivity.this,
                            "Erro de conexão: " + erro.getMessage(),
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

    private String lerResposta(InputStream input) throws Exception {
        if (input == null) {
            return "{\"sucesso\":false,"
                    + "\"mensagem\":\"Servidor sem resposta\"}";
        }

        StringBuilder resposta = new StringBuilder();

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(
                        input,
                        StandardCharsets.UTF_8
                ))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }
        }

        return resposta.toString();
    }

    private void restaurarBotao() {
        btnLogin.setEnabled(true);
        btnLogin.setText("Entrar");
    }
}