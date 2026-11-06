
package com.example.aula12;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import java.net.URLEncoder;

public class LoginActivity extends Activity {

    EditText edtEmailLogin, edtSenhaLogin;
    Button btnEntrar, btnIrCadastro;

    String url = "http://10.0.2.2/mobile_app/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtSenhaLogin = findViewById(R.id.edtSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnIrCadastro = findViewById(R.id.btnIrCadastro);

        // Botão para abrir a tela de cadastro
        btnIrCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Botão para fazer login
        btnEntrar.setOnClickListener(v -> fazerLogin());
    }

    private void fazerLogin() {

        String email = edtEmailLogin.getText().toString().trim();
        String senha = edtSenhaLogin.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.POST, url,

                response -> {
                    try {
                        JSONObject json = new JSONObject(response);

                        if (json.optBoolean("success")) {

                            Intent intent = new Intent(
                                    this, AccessActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(this,
                                    json.optString("message"),
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Erro na resposta",
                                Toast.LENGTH_SHORT).show();
                    }
                },

                error -> Toast.makeText(this, "Erro de conexão",
                        Toast.LENGTH_SHORT).show()

        ) {
            @Override
            public byte[] getBody() {
                try {
                    String data = "usu_email=" +
                            URLEncoder.encode(email, "UTF-8") +
                            "&usu_password=" +
                            URLEncoder.encode(senha, "UTF-8");

                    return data.getBytes("UTF-8");

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
