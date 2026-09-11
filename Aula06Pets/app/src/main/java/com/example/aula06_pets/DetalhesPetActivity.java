package com.example.aula06_pets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetalhesPetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pet);

        ImageView imgPet = findViewById(R.id.imgPet);
        TextView txtNome = findViewById(R.id.txtNome);
        TextView txtCor = findViewById(R.id.txtCor);
        TextView txtRaca = findViewById(R.id.txtRaca);
        TextView txtIdade = findViewById(R.id.txtIdade);
        TextView txtAniversario = findViewById(R.id.txtAniversario);

        Intent intent = getIntent();

        String nome = intent.getStringExtra("nome");
        String cor = intent.getStringExtra("cor");
        String raca = intent.getStringExtra("raca");
        String idade = intent.getStringExtra("idade");
        String aniversario = intent.getStringExtra("aniversario");
        int imagem = intent.getIntExtra("imagem", 0);

        txtNome.setText("Nome: " + nome);
        txtCor.setText("Cor: " + cor);
        txtRaca.setText("Raça: " + raca);
        txtIdade.setText("Idade: " + idade);
        txtAniversario.setText("Aniversário: " + aniversario);

        imgPet.setImageResource(imagem);
    }
}
