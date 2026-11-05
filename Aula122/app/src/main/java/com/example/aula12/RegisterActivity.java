package com.example.aula12;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aula12.databinding.ActivityRegisterBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnRegister.setOnClickListener(v -> handleRegister());

        binding.btnGoToLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String name = binding.etName.getText() != null ? binding.etName.getText().toString().trim() : "";
        String email = binding.etRegisterEmail.getText() != null ? binding.etRegisterEmail.getText().toString().trim() : "";
        String password = binding.etRegisterPassword.getText() != null ? binding.etRegisterPassword.getText().toString() : "";
        String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString() : "";

        if (TextUtils.isEmpty(name)) {
            binding.tilName.setError(getString(R.string.error_empty_fields));
            return;
        } else {
            binding.tilName.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            binding.tilRegisterEmail.setError(getString(R.string.error_empty_fields));
            return;
        } else {
            binding.tilRegisterEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilRegisterPassword.setError(getString(R.string.error_empty_fields));
            return;
        } else {
            binding.tilRegisterPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.setError(getString(R.string.error_empty_fields));
            return;
        } else {
            binding.tilConfirmPassword.setError(null);
        }

        if (!Objects.equals(password, confirmPassword)) {
            binding.tilConfirmPassword.setError(getString(R.string.error_password_match));
            return;
        } else {
            binding.tilConfirmPassword.setError(null);
        }

        realizarCadastroApi(name, email, password);
    }

    private void realizarCadastroApi(String name, String email, String password) {
        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText(R.string.loading_register);

        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(ApiConfig.URL_REGISTER);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", name);
                jsonParam.put("nome", name);
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
                        jsonResposta.optBoolean("success", responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED));
                String mensagem = jsonResposta.optString("mensagem",
                        jsonResposta.optString("message", sucesso ? getString(R.string.register_success) : "Falha ao cadastrar."));

                runOnUiThread(() -> {
                    binding.btnRegister.setEnabled(true);
                    binding.btnRegister.setText(R.string.btn_register);

                    if (sucesso) {
                        Toast.makeText(RegisterActivity.this, mensagem, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, mensagem, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                android.util.Log.e("API_DEBUG", "Erro Register", e);
                runOnUiThread(() -> {
                    binding.btnRegister.setEnabled(true);
                    binding.btnRegister.setText(R.string.btn_register);
                    Toast.makeText(RegisterActivity.this, "Erro ao conectar com servidor: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
