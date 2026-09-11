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

    Pet cao1, cao2, cao3;
    Pet gato1, gato2, gato3;

    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /* Instância dawgs*/
        cao1 = new Pet("ThorWhite", "branco com manchas pretas", "Bull Terrier", 3, "10/09/2026" );
        gato1 = new Pet("Daminha", "branco", "Persa", 2, "11/09/2026" );




        Button cao1 = findViewById(R.id.detalhe1);
        Button detalhec1 = findViewById(R.id.detalhec1);

        cao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String info = textInfo.getText().toString();

                Intent segundaTela = new Intent(MainActivity.this, DetalhesPetActivity.class);
                segundaTela.putExtra(detalhes, detalhesRecebido);
                startActivity(segundaTela);
            }
        });

    }
}