package com.example.aula6petz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pet cao1 = new Pet("Rex", "Caramelo", "Labrador", 3, "15/05/2023");
        Pet cao2 = new Pet("Thor", "Preto", "Pastor Alemão", 5, "20/08/2021");
        Pet cao3 = new Pet("Max", "Branco", "Poodle", 2, "10/01/2024");

        Pet gato1 = new Pet("Luna", "Branca", "Persa", 4, "12/03/2022");
        Pet gato2 = new Pet("Mia", "Cinza", "Siamês", 2, "25/07/2024");
        Pet gato3 = new Pet("Nina", "Preta", "Bombay", 3, "05/11/2023");

        Button btnCao1 = findViewById(R.id.btnCao1);
        Button btnCao2 = findViewById(R.id.btnCao2);
        Button btnCao3 = findViewById(R.id.btnCao3);

        Button btnGato1 = findViewById(R.id.btnGato1);
        Button btnGato2 = findViewById(R.id.btnGato2);
        Button btnGato3 = findViewById(R.id.btnGato3);

        btnCao1.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

            intent.putExtra("nome", cao1.nome);
            intent.putExtra("cor", cao1.cor);
            intent.putExtra("raca", cao1.raca);
            intent.putExtra("idade", cao1.idade);
            intent.putExtra("aniversario", cao1.aniversario);
            intent.putExtra("imagem", R.drawable.cao1);

            startActivity(intent);
        });

        btnCao2.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

            intent.putExtra("nome", cao2.nome);
            intent.putExtra("cor", cao2.cor);
            intent.putExtra("raca", cao2.raca);
            intent.putExtra("idade", cao2.idade);
            intent.putExtra("aniversario", cao2.aniversario);
            intent.putExtra("imagem", R.drawable.cao2);

            startActivity(intent);
        });

        btnCao3.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

            intent.putExtra("nome", cao3.nome);
            intent.putExtra("cor", cao3.cor);
            intent.putExtra("raca", cao3.raca);
            intent.putExtra("idade", cao3.idade);
            intent.putExtra("aniversario", cao3.aniversario);
            intent.putExtra("imagem", R.drawable.cao3);

            startActivity(intent);
        });

        btnGato1.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

            intent.putExtra("nome", gato1.nome);
            intent.putExtra("cor", gato1.cor);
            intent.putExtra("raca", gato1.raca);
            intent.putExtra("idade", gato1.idade);
            intent.putExtra("aniversario", gato1.aniversario);
            intent.putExtra("imagem", R.drawable.gato1);

            startActivity(intent);
        });

        btnGato2.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

            intent.putExtra("nome", gato2.nome);
            intent.putExtra("cor", gato2.cor);
            intent.putExtra("raca", gato2.raca);
            intent.putExtra("idade", gato2.idade);
            intent.putExtra("aniversario", gato2.aniversario);
            intent.putExtra("imagem", R.drawable.gato2);

            startActivity(intent);
        });

        btnGato3.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

            intent.putExtra("nome", gato3.nome);
            intent.putExtra("cor", gato3.cor);
            intent.putExtra("raca", gato3.raca);
            intent.putExtra("idade", gato3.idade);
            intent.putExtra("aniversario", gato3.aniversario);
            intent.putExtra("imagem", R.drawable.gato3);

            startActivity(intent);
        });
    }
}