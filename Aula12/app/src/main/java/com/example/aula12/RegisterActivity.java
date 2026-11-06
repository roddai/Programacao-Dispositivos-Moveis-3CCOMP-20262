package com.example.aula12;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private Button btnVoltarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);

        btnCadastrar.setOnClickListener(view ->
                Toast.makeText(
                        RegisterActivity.this,
                        "Em breve faremos o cadastro",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnVoltarLogin.setOnClickListener(view -> finish());
    }
}