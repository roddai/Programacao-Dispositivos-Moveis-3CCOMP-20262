package com.example.pets;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Detalhes extends AppCompatActivity{

    private TextView txtDetalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes); // Layout da segunda tela

        txtDetalhes = findViewById(R.id.detalhes);

        // Pegando o nome enviado da MainActivity
        String nomeRecebido = getIntent().getStringExtra("info");

        txtDetalhes.setText("Olá, " + nomeRecebido + "!");
    }


}
