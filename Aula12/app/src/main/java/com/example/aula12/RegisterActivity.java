package com.example.aula12;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmailCadastro;
    private EditText editSenhaCadastro;
    private Button btnCadastrar;
    private Button btnVoltarLogin;

    private static final String URL_CADASTRO =
            "http://10.0.2.2/mobile_app/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNome = findViewById(R.id.editNome);
        editEmailCadastro = findViewById(R.id.editEmailCadastro);
        editSenhaCadastro = findViewById(R.id.editSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);

        btnCadastrar.setOnClickListener(view -> cadastrarUsuario());

        btnVoltarLogin.setOnClickListener(view -> finish());
    }

    private void cadastrarUsuario() {
        String nome = editNome.getText().toString().trim();
        String email = editEmailCadastro.getText().toString().trim();
        String senha = editSenhaCadastro.getText().toString();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        btnCadastrar.setEnabled(false);
        btnCadastrar.setText("Cadastrando...");

        new Thread(() -> {
            HttpURLConnection conexao = null;

            try {
                URL url = new URL(URL_CADASTRO);
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
                        "nome=" + URLEncoder.encode(
                                nome,
                                StandardCharsets.UTF_8.toString()
                        )
                                + "&email=" + URLEncoder.encode(
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

                runOnUiThread(() -> {
                    restaurarBotao();

                    Toast.makeText(
                            RegisterActivity.this,
                            mensagem,
                            Toast.LENGTH_LONG
                    ).show();

                    if (sucesso) {
                        finish();
                    }
                });

            } catch (Exception erro) {
                runOnUiThread(() -> {
                    restaurarBotao();

                    Toast.makeText(
                            RegisterActivity.this,
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
        btnCadastrar.setEnabled(true);
        btnCadastrar.setText("Cadastrar");
    }
}