package com.example.pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       cao1 = new Pet("Rex","Caramelo","Golden",5,"10/09/2021");
       cao2 = new Pet("Thor","Branco","Husky",3,"10/09/2022");
       cao3 = new Pet("Luna","Bege","Shitsu",5,"10/09/2023");

       gato1 = new Pet("Steph","Branco", "Vira Lata", 4, "10/09/2021");
       gato2 = new Pet("Marg","Preto", "Vira Lata", 4, "10/09/2021");
       gato3 = new Pet("Fran","Rosa", "Vira Lata", 4, "10/09/2021");

        Button caoA = findViewById(R.id.cao1);
        Button caoB = findViewById(R.id.cao2);
        Button caoC = findViewById(R.id.cao3);

        Button gatoA = findViewById(R.id.gato1);
        Button gatoB = findViewById(R.id.gato2);
        Button gatoC = findViewById(R.id.gato3);


        caoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", cao1.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        caoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", cao2.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        caoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", cao3.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        gatoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", gato1.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        gatoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", gato2.getInfo());
                startActivity(irParaSegundaTela);
            }
        });
        gatoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("info", gato3.getInfo());
                startActivity(irParaSegundaTela);
            }
        });



    }
}