package com.example.aula12;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnLogin, btnNovoCadastro;

    String URL_LOGIN = "http://10.0.2.2/mobile_app/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

        btnLogin = findViewById(R.id.btnLogin);
        btnNovoCadastro = findViewById(R.id.btnNovoCadastro);

        btnNovoCadastro.setOnClickListener(v -> {

            Intent intent =
                    new Intent(LoginActivity.this,
                            RegisterActivity.class);

            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> realizarLogin());
    }

    private void realizarLogin() {

        String email =
                edtEmail.getText().toString().trim();

        String senha =
                edtSenha.getText().toString().trim();

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        URL_LOGIN,

                        response -> {

                            try {

                                JSONObject json =
                                        new JSONObject(response);

                                boolean success =
                                        json.getBoolean("success");

                                if(success){

                                    Intent intent =
                                            new Intent(
                                                    LoginActivity.this,
                                                    SuccessActivity.class
                                            );

                                    startActivity(intent);

                                }else{

                                    Toast.makeText(
                                            this,
                                            "Login inválido",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            }catch(Exception e){

                                Toast.makeText(
                                        this,
                                        e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        },

                        error -> Toast.makeText(
                                this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()
                ){

                    @Override
                    protected Map<String, String> getParams(){

                        Map<String,String> params =
                                new HashMap<>();

                        params.put(
                                "usu_email",
                                email
                        );

                        params.put(
                                "usu_password",
                                senha
                        );

                        return params;
                    }
                };

        RequestQueue queue =
                Volley.newRequestQueue(this);

        queue.add(request);
    }
}