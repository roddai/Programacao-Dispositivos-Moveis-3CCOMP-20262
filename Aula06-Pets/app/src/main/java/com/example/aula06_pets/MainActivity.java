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

    Pet pet1, pet2, pet3, pet4, pet5, pet6;

    TextView textInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /*Instanciando os Pets*/
        pet1 = new Pet("Kleber","Preto", "Doberman",5,15/05/2001 );
        pet2 = new Pet("Arthur","Branco", "Golden",4,16/05/2001);
        pet3 = new Pet("Meg","Marrom e Preto", "Pastor Alemão",8,17/05/2001 );
        pet4 = new Pet("Cat","Amarelo", "Persa",2,18/05/2001 );
        pet5 = new Pet("Peludo","Branco", "Egipcio",5,19/05/2001 );
        pet6 = new Pet("Garfield","Laranja", "Ruivo",8,20/05/2001);


        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);

        button1.setOnClickListener(v -> textInfo.setText(pet1.getInfo()));
        button2.setOnClickListener(v -> textInfo.setText(pet2.getInfo()));
        button3.setOnClickListener(v -> textInfo.setText(pet3.getInfo()));
        button1.setOnClickListener(v -> textInfo.setText(pet4.getInfo()));
        button2.setOnClickListener(v -> textInfo.setText(pet5.getInfo()));
        button3.setOnClickListener(v -> textInfo.setText(pet6.getInfo()));


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("InfoPet",pet1.getInfo());
                startActivity(irParaSegundaTela);

            }

        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("InfoPet",pet2.getInfo());
                startActivity(irParaSegundaTela);

            }

        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("InfoPet",pet3.getInfo());
                startActivity(irParaSegundaTela);

            }

        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("InfoPet",pet4.getInfo());
                startActivity(irParaSegundaTela);

            }

        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("InfoPet",pet5.getInfo());
                startActivity(irParaSegundaTela);

            }

        });
        button6git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent irParaSegundaTela = new Intent(MainActivity.this, Detalhes.class);
                irParaSegundaTela.putExtra("InfoPet",pet6.getInfo());
                startActivity(irParaSegundaTela);

            }

        });

    }
}