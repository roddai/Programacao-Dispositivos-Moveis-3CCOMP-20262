package com.example.aula06_pets;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Detalhes extends AppCompatActivity {

    private TextView txtNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes); // Layout da segunda tela

        txtNome = findViewById(R.id.txtNome);

        // Pegando o nome enviado da MainActivity
        String nomeRecebido = getIntent().getStringExtra("info");

        txtNome.setText(nomeRecebido);
    }
}