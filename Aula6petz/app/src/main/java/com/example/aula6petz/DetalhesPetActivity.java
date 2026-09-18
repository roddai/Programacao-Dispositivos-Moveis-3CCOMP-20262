package com.example.aula6petz;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetalhesPetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalhes);

        TextView txtNome = findViewById(R.id.txtNome);
        TextView txtCor = findViewById(R.id.txtCor);
        TextView txtRaca = findViewById(R.id.txtRaca);
        TextView txtIdade = findViewById(R.id.txtIdade);
        TextView txtAniversario = findViewById(R.id.txtAniversario);

        ImageView imgPet = findViewById(R.id.imgPet);

        String nome = getIntent().getStringExtra("nome");
        String cor = getIntent().getStringExtra("cor");
        String raca = getIntent().getStringExtra("raca");
        int idade = getIntent().getIntExtra("idade", 0);
        String aniversario = getIntent().getStringExtra("aniversario");

        int imagem = getIntent().getIntExtra("imagem", 0);

        txtNome.setText("Nome: " + nome);
        txtCor.setText("Cor: " + cor);
        txtRaca.setText("Raça: " + raca);
        txtIdade.setText("Idade: " + idade + " anos");
        txtAniversario.setText("Aniversário: " + aniversario);

        imgPet.setImageResource(imagem);
    }
}