package com.example.appsdepets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Pet cao1, cao2, cao3, gato1, gato2, gato3;
    private Button detalhe1, detalhe2, detalhe3, detalhec1, detalhec2, detalhec3;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /*Instância dawgs*/
        cao1 = new Pet("ThorWhite", "branco com manchas pretas", "Bull Terrier", 3, "10/09/2026" );
        cao2 = new Pet("Pugode", "bege", "Pug", 3, "10/09/2026" );
        cao3 = new Pet("Sploft", "marrom", "Spitz Alemão", 3, "10/09/2026" );
        gato1 = new Pet("Daminha", "branco", "Persa", 2, "11/09/2026" );
        gato2 = new Pet("Caro", "marrom?", "Siamês", 2, "11/09/2026" );
        gato3 = new Pet("Anubis", "sei não", "Sphynx", 2, "11/09/2026" );


        detalhe1 = findViewById(R.id.detalhe1);
        detalhe2 = findViewById(R.id.detalhe2);
        detalhe3 = findViewById(R.id.detalhe3);
        detalhec1 = findViewById(R.id.detalhec1);
        detalhec2 = findViewById(R.id.detalhec2);
        detalhec3 = findViewById(R.id.detalhec3);

        detalhe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent segundaTela = new Intent(MainActivity.this, DetalhesPetActivity.class);
                segundaTela.putExtra("info", cao1.getInfo());
                startActivity(segundaTela);
            }
        });

        detalhe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent segundaTela = new Intent(MainActivity.this, DetalhesPetActivity.class);
                segundaTela.putExtra("info", cao2.getInfo());
                startActivity(segundaTela);
            }
        });

        detalhe3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent segundaTela = new Intent(MainActivity.this, DetalhesPetActivity.class);
                segundaTela.putExtra("info", cao3.getInfo());
                startActivity(segundaTela);
            }
        });

        detalhec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent segundaTela = new Intent(MainActivity.this, DetalhesPetActivity.class);
                segundaTela.putExtra("info", gato1.getInfo());
                startActivity(segundaTela);
            }
        });

        detalhec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent segundaTela = new Intent(MainActivity.this, DetalhesPetActivity.class);
                segundaTela.putExtra("info", gato2.getInfo());
                startActivity(segundaTela);
            }
        });

        detalhec3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent segundaTela = new Intent(MainActivity.this, DetalhesPetActivity.class);
                segundaTela.putExtra("info", gato3.getInfo());
                startActivity(segundaTela);
            }
        });

    }
}