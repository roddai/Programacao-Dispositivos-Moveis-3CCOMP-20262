package com.example.aula06_pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Pet rex;
    Pet thor;
    Pet luna;
    Pet mimi;
    Pet nina;
    Pet tom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rex = new Pet("Rex", "Caramelo", "Border-collier",
                "4 anos", "20 de julho", R.drawable.rex);

        thor = new Pet("Thor", "Cinza", "Persa",
                "3 anos", "15 de março", R.drawable.thor);

        luna = new Pet("Luna", "Branca e cinza", "Persa",
                "2 anos", "7 de setembro", R.drawable.luna);

        mimi = new Pet("Mimi", "Branca e marrom", "Siamês",
                "5 anos", "11 de janeiro", R.drawable.mimi);

        nina = new Pet("Nina", "Branca e preta", "Border-collier",
                "3 anos", "25 de maio", R.drawable.nina);

        tom = new Pet("Tom", "Preto", "Pantera",
                "1 ano", "3 de novembro", R.drawable.tom);

        Button btnRex = findViewById(R.id.btnRex);
        Button btnThor = findViewById(R.id.btnThor);
        Button btnLuna = findViewById(R.id.btnLuna);
        Button btnMimi = findViewById(R.id.btnMimi);
        Button btnNina = findViewById(R.id.btnNina);
        Button btnTom = findViewById(R.id.btnTom);

        btnRex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(rex);
            }
        });

        btnThor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(thor);
            }
        });

        btnLuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(luna);
            }
        });

        btnMimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(mimi);
            }
        });

        btnNina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(nina);
            }
        });

        btnTom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(tom);
            }
        });
    }

    private void abrirDetalhes(Pet pet) {
        Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

        intent.putExtra("nome", pet.getNome());
        intent.putExtra("cor", pet.getCor());
        intent.putExtra("raca", pet.getRaca());
        intent.putExtra("idade", pet.getIdade());
        intent.putExtra("aniversario", pet.getAniversario());
        intent.putExtra("imagem", pet.getImagem());

        startActivity(intent);
    }
}