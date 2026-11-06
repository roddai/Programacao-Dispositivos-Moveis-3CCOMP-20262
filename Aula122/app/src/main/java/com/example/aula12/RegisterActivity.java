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

public class RegisterActivity extends AppCompatActivity {

    EditText edtNome, edtEmail, edtSenha;

    Button btnCadastrar;
    Button btnVoltar;

    String URL_REGISTER =
            "http://10.0.2.2/mobile_app/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

        btnCadastrar =
                findViewById(R.id.btnCadastrar);

        btnVoltar =
                findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> finish());

        btnCadastrar.setOnClickListener(
                v -> cadastrarUsuario()
        );
    }

    private void cadastrarUsuario(){

        String nome =
                edtNome.getText().toString().trim();

        String email =
                edtEmail.getText().toString().trim();

        String senha =
                edtSenha.getText().toString().trim();

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        URL_REGISTER,

                        response -> {

                            try {

                                JSONObject json =
                                        new JSONObject(response);

                                boolean success =
                                        json.getBoolean("success");

                                if(success){

                                    Toast.makeText(
                                            this,
                                            "Cadastro realizado com sucesso",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    Intent intent =
                                            new Intent(
                                                    RegisterActivity.this,
                                                    LoginActivity.class
                                            );

                                    startActivity(intent);

                                    finish();

                                }else{

                                    Toast.makeText(
                                            this,
                                            json.getString("message"),
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
                    protected Map<String,String> getParams(){

                        Map<String,String> params =
                                new HashMap<>();

                        params.put(
                                "usu_nome",
                                nome
                        );

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