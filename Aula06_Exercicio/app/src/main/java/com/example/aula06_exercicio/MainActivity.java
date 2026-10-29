package com.example.aula06_exercicio;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cao1 = new Pet("Bob", "Caramelo", "Vira-lata", 2, "20/02");
        cao2 = new Pet("Lobinho", "Cinza", "Husky", 4, "10/09");
        cao3 = new Pet("Jorgin", "Marrom", "Salsicha", 5, "20/03");
        gato1 = new Pet("Floquinho", "Cinza", "Rajado", 5, "20/06");
        gato2 = new Pet("Chamusco", "Bege", "Siamês", 8, "19/10");
        gato3 = new Pet("Foguinho", "Laranja", "Laranja", 5, "15/06");

        Button button1 = findViewById(R.id.btnCao1);
        Button button2 = findViewById(R.id.btnCao2);
        Button button3 = findViewById(R.id.btnCao3);
        Button button4 = findViewById(R.id.btnGato1);
        Button button5 = findViewById(R.id.btnGato2);
        Button button6 = findViewById(R.id.btnGato3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("detalhePet", cao1.getinfo());
                startActivity(irParaSegundaTela);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("detalhePet", cao2.getinfo());
                startActivity(irParaSegundaTela);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("detalhePet", cao3.getinfo());
                startActivity(irParaSegundaTela);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("detalhePet", gato1.getinfo());
                startActivity(irParaSegundaTela);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("detalhePet", gato2.getinfo());
                startActivity(irParaSegundaTela);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("detalhePet", gato3.getinfo());
                startActivity(irParaSegundaTela);
            }
        });




    }
}