package com.example.aula12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

public class RegisterActivity extends AppCompatActivity {

    private static final String URL_REGISTER = "http://10.0.2.2/mobile_app/register.php";

    private FrameLayout layoutFoto;
    private ImageView imgFoto;
    private TextView tvEscolherFoto;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private Button btnCadastrar;
    private Button btnVoltarLogin;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    private Uri fotoSelecionadaUri = null;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    fotoSelecionadaUri = uri;
                    try {
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    } catch (Exception ignored) {
                    }
                    imgFoto.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layoutFoto = findViewById(R.id.layoutFoto);
        imgFoto = findViewById(R.id.imgFoto);
        tvEscolherFoto = findViewById(R.id.tvEscolherFoto);
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);
        progressBar = findViewById(R.id.progressBar);

        requestQueue = Volley.newRequestQueue(this);

        View.OnClickListener selecionarFotoListener = v -> pickImageLauncher.launch("image/*");
        layoutFoto.setOnClickListener(selecionarFotoListener);
        imgFoto.setOnClickListener(selecionarFotoListener);
        tvEscolherFoto.setOnClickListener(selecionarFotoListener);

        btnCadastrar.setOnClickListener(v -> validarECadastrar());

        btnVoltarLogin.setOnClickListener(v -> finish());
    }

    private void validarECadastrar() {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            edtNome.setError("Informe o nome");
            edtNome.requestFocus();
            return;
        }

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

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmPassword.setError("Confirme a senha");
            edtConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("As senhas não coincidem");
            edtConfirmPassword.requestFocus();
            return;
        }

        realizarCadastro(nome, email, password);
    }

    private void realizarCadastro(String nome, String email, String password) {
        btnCadastrar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());
                            boolean success = jsonObject.optBoolean("success", false)
                                    || "true".equalsIgnoreCase(jsonObject.optString("success"))
                                    || jsonObject.optInt("success", 0) == 1;
                            String message = jsonObject.optString("message", "Sem resposta");

                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                            if (success) {
                                // Salvar a URI da foto em SharedPreferences após cadastro com sucesso
                                if (fotoSelecionadaUri != null) {
                                    SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                                    prefs.edit().putString("foto_uri", fotoSelecionadaUri.toString()).apply();
                                }

                                // Fechar RegisterActivity e retornar automaticamente para LoginActivity
                                finish();
                            } else {
                                btnCadastrar.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            btnCadastrar.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Erro ao interpretar JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnCadastrar.setEnabled(true);
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

                        Toast.makeText(RegisterActivity.this, detalheErro.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usu_nome", nome);
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
