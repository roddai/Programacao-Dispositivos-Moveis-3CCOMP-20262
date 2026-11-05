package com.example.aula12;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aula12.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnLogin.setOnClickListener(v -> handleLogin());

        binding.btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError(getString(R.string.error_empty_fields));
            return;
        } else {
            binding.tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError(getString(R.string.error_empty_fields));
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        realizarLoginApi(email, password);
    }

    private void realizarLoginApi(String email, String password) {
        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText(R.string.loading_login);

        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(ApiConfig.URL_LOGIN);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", email);
                jsonParam.put("password", password);
                jsonParam.put("senha", password);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();

                InputStream is = (responseCode >= 200 && responseCode < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                StringBuilder responseBuilder = new StringBuilder();
                if (is != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                    }
                }

                String responseBody = responseBuilder.toString();
                JSONObject jsonResposta = new JSONObject(responseBody.isEmpty() ? "{}" : responseBody);

                boolean sucesso = jsonResposta.optBoolean("sucesso",
                        jsonResposta.optBoolean("success", responseCode == HttpURLConnection.HTTP_OK));
                String mensagem = jsonResposta.optString("mensagem",
                        jsonResposta.optString("message", sucesso ? getString(R.string.login_success) : "Falha ao autenticar."));

                runOnUiThread(() -> {
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText(R.string.btn_login);

                    if (sucesso) {
                        Toast.makeText(LoginActivity.this, mensagem, Toast.LENGTH_SHORT).show();

                        String nomeUsuario = "";
                        JSONObject usuarioObj = jsonResposta.optJSONObject("usuario");
                        if (usuarioObj != null) {
                            nomeUsuario = usuarioObj.optString("nome", "");
                        }

                        Intent intent = new Intent(LoginActivity.this, AcessActivity.class);
                        intent.putExtra("USER_NAME", nomeUsuario);
                        intent.putExtra("USER_EMAIL", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, mensagem, Toast.LENGTH_LONG).show();
                        binding.tilPassword.setError(mensagem);
                    }
                });

            } catch (Exception e) {
                android.util.Log.e("API_DEBUG", "Erro Login", e);
                runOnUiThread(() -> {
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText(R.string.btn_login);
                    Toast.makeText(LoginActivity.this, "Erro ao conectar com servidor: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
