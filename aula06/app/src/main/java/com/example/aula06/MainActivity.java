package com.example.aula06;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView  txtResultado;

    ImageView imgMaquina, imgPedra, imgPapel, imgTesoura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);
        imgMaquina = findViewById(R.id.imgMaquina);
        imgPedra = findViewById(R.id.imgPedra);
        imgPapel = findViewById(R.id.imgPapel);
        imgTesoura = findViewById(R.id.imgTesoura);

        imgPedra.setOnClickListener(v-> jogar("pedra") );
        imgPapel.setOnClickListener(v-> jogar("papel") );
        imgTesoura.setOnClickListener(v-> jogar("tesoura"));

    }
    public void jogar(String escolhaUsuario){
        String[] opcoes = {"pedra", "papel", "tesoura"};
        int number = new Random().nextInt(3);
        String escolhaMaquina = opcoes[number];

        switch (escolhaMaquina){
            case "pedra":
                imgMaquina.setImageResource(R.drawable.pedra);
            case "papel":
                imgPapel.setImageResource(R.drawable.papel);
            case "tesoura":
                imgTesoura.setImageResource((R.drawable.tesoura));

        }
    }
}