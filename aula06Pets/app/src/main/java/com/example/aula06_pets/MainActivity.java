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

        cao1 = new Pet("Bob", "Marrom", "Lulu da Pumerania", 3, "12 de maio");
        cao2 = new Pet("Thor", "Laranja", "Golden", 2, "20 de julho");
        cao3 = new Pet("Mel", "Caramelo", "Labrador", 4, "5 de outubro");

        gato1 = new Pet("Luna", "Branca", "Persa", 1, "15 de março");
        gato2 = new Pet("Simba", "Preta e Cinza", "Siamês", 2, "10 de janeiro");
        gato3 = new Pet("Nina", "Laranja e Branca", "Angorá", 5, "2 de dezembro");


        Button btnCao1 = findViewById(R.id.btnCao1);
        Button btnCao2 = findViewById(R.id.btnCao2);
        Button btnCao3 = findViewById(R.id.btnCao3);

        Button btnGato1 = findViewById(R.id.btnGato1);
        Button btnGato2 = findViewById(R.id.btnGato2);
        Button btnGato3 = findViewById(R.id.btnGato3);
        btnCao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, com.example.myapplication.Detalhes.class);
                irParaSegundaTela.putExtra("info", cao1.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
    }
}