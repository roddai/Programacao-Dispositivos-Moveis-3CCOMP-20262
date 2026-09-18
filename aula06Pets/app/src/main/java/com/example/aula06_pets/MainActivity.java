package com.example.aula06_pets;

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
    Pet cao1, cao2, cao3;
    Pet gato1, gato2, gato3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        cao1 = new Pet("Bob", "Laranja", "Golden", 7, "10 de maio");
        cao2 = new Pet("Thor", "Marrom", "Labrador", 3, "8 de abril");
        cao3 = new Pet("Mel", "Caramelo", "Caramelo", 10, " 20 de setembro");

        gato1 = new Pet("Luna", "Cinza", "Persa", 1, "15 de março");
        gato2 = new Pet("Simba", "Amarelo", "Siamês", 2, "10 de janeiro");
        gato3 = new Pet("Nina", "Marrom", "Angorá", 5, "2 de dezembro");


        Button btnCao1 = findViewById(R.id.btnCao1);
        Button btnCao2 = findViewById(R.id.btnCao2);
        Button btnCao3 = findViewById(R.id.btnCao3);

        Button btnGato1 = findViewById(R.id.btnGato1);
        Button btnGato2 = findViewById(R.id.btnGato2);
        Button btnGato3 = findViewById(R.id.btnGato3);
        btnCao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", cao1.getInfo());
                startActivity(irParaSegundaTela);
            }


        });
        btnCao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", cao2.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        btnCao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", cao3.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        btnGato1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", gato1.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        btnGato2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", gato2.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        btnGato3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", gato3.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
    }
}
