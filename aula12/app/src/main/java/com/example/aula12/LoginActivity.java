package com.example.aula12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String URL_LOGIN = "http://10.0.2.2/mobile_app/login.php";

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnEntrar;
    private TextView tvCriarConta;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se já estiver logado, redireciona para a SuccessActivity
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (prefs.getBoolean("logged_in", false)) {
            Intent intent = new Intent(LoginActivity.this, SuccessActivity.class);
            intent.putExtra("usu_nome", prefs.getString("usu_nome", ""));
            intent.putExtra("usu_email", prefs.getString("usu_email", ""));
            startActivity(intent);
            finish();
            return;
        }

        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvCriarConta = findViewById(R.id.tvCriarConta);
        progressBar = findViewById(R.id.progressBar);

        requestQueue = Volley.newRequestQueue(this);

        btnEntrar.setOnClickListener(v -> validarELogar());

        tvCriarConta.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void validarELogar() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Informe o e-mail");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Formato de e-mail inválido");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Informe a senha");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("A senha deve ter pelo menos 6 caracteres");
            edtPassword.requestFocus();
            return;
        }

        realizarLogin(email, password);
    }

    private void realizarLogin(String email, String password) {
        btnEntrar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btnEntrar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());
                            boolean success = jsonObject.optBoolean("success", false)
                                    || "true".equalsIgnoreCase(jsonObject.optString("success"))
                                    || jsonObject.optInt("success", 0) == 1;
                            String message = jsonObject.optString("message", "Sem resposta");

                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                            if (success) {
                                String usuNome = jsonObject.optString("usu_nome", "");
                                String usuEmail = jsonObject.optString("usu_email", email);

                                // Salvar dados da sessão em SharedPreferences
                                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                                prefs.edit()
                                        .putBoolean("logged_in", true)
                                        .putString("usu_nome", usuNome)
                                        .putString("usu_email", usuEmail)
                                        .apply();

                                // Abrir SuccessActivity enviando usu_nome e usu_email pelo Intent
                                Intent intent = new Intent(LoginActivity.this, SuccessActivity.class);
                                intent.putExtra("usu_nome", usuNome);
                                intent.putExtra("usu_email", usuEmail);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Erro ao processar resposta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnEntrar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);

                        StringBuilder detalheErro = new StringBuilder();
                        String mensagemErro = "Erro desconhecido";

                        if (error != null) {
                            if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                                mensagemErro = error.getMessage();
                            } else {
                                mensagemErro = error.getClass().getSimpleName();
                            }
                            detalheErro.append("Erro Volley: ").append(mensagemErro);

                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                detalheErro.append("\nCódigo HTTP: ").append(statusCode);

                                if (error.networkResponse.data != null) {
                                    String corpoResposta = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                    detalheErro.append("\nCorpo: ").append(corpoResposta);

                                    try {
                                        JSONObject json = new JSONObject(corpoResposta.trim());
                                        if (json.has("message")) {
                                            detalheErro.append("\nMensagem: ").append(json.getString("message"));
                                        }
                                    } catch (JSONException ignored) {
                                    }
                                }
                            }
                        } else {
                            detalheErro.append("Erro Volley: ").append(mensagemErro);
                        }

                        Toast.makeText(LoginActivity.this, detalheErro.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usu_email", email);
                params.put("usu_password", password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
