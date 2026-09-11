package com.example.aula06_exercicio;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Detalhes extends AppCompatActivity {

    public TextView detalhePet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_pet); // Layout da segunda tela

        detalhePet = findViewById(R.id.detalhePet);

        // Pegando o nome enviado da MainActivity
        String detalheRecebido = getIntent().getStringExtra("detalhePet");

        detalhePet.setText(detalheRecebido);
    }
}
