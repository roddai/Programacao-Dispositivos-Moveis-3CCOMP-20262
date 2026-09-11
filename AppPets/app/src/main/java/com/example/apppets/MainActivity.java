package com.example.apppets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    DadosPet pet1, pet2, pet3, pet4, pet5, pet6;
    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        pet1 = new DadosPet("Marley", "Cocker","Amarelo",8,"8 de maio");
        pet2 = new DadosPet("Bob", "Golden","Amarelo",12,"4 de abril");
        pet3 = new DadosPet("Guilherme", "Doberman","Preto",9,"9 de junho");
        pet4 = new DadosPet("Lua", "Mainecoon","Preto",8,"15 de Dezembro");
        pet5 = new DadosPet("Arthur", "Vira-lata","Branco",8,"19 de maio");
        pet1 = new DadosPet("Julia", "Persa","Mista",8,"18 de Janeiro");


        Button button1 = findViewById(R.id.btnCachorro1);
        Button button2 = findViewById(R.id.btnCachorro2);
        Button button3 = findViewById(R.id.btnCachorro3);
        Button button4 = findViewById(R.id.btnGato1);
        Button button5 = findViewById(R.id.btnGato2);
        Button button6 = findViewById(R.id.btnGato3);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent irParaSegundaTela = new Intent(MainActivity.this, DetalhesActivity.class);
                irParaSegundaTela.putExtra("Info", pet1.getInfo());
                startActivity(irParaSegundaTela);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent irParaSegundaTela = new Intent(MainActivity.this, DetalhesActivity.class);
                irParaSegundaTela.putExtra("Info", pet2.getInfo());
                startActivity(irParaSegundaTela);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent irParaSegundaTela = new Intent(MainActivity.this, DetalhesActivity.class);
                irParaSegundaTela.putExtra("Info", pet3.getInfo());
                startActivity(irParaSegundaTela);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent irParaSegundaTela = new Intent(MainActivity.this, DetalhesActivity.class);
                irParaSegundaTela.putExtra("Info", pet4.getInfo());
                startActivity(irParaSegundaTela);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent irParaSegundaTela = new Intent(MainActivity.this, DetalhesActivity.class);
                irParaSegundaTela.putExtra("Info", pet5.getInfo());
                startActivity(irParaSegundaTela);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent irParaSegundaTela = new Intent(MainActivity.this, DetalhesActivity.class);
                irParaSegundaTela.putExtra("Info", pet6.getInfo());
                startActivity(irParaSegundaTela);
            }
        });





    }
}