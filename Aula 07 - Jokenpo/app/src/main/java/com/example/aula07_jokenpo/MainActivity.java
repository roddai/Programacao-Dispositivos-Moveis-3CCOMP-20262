package com.example.aula07_jokenpo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;
    ImageView imgMaquina, imgPedra, imgPapel, imgTesoura, imgHomem, imgMulher;

    String personagemEscolhido = "";

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
        imgMulher = findViewById(R.id.imgMulher);
        imgHomem = findViewById(R.id.imgHomem);

        imgHomem.setOnClickListener(v -> {
            personagemEscolhido = "Homem";
        });

        imgMulher.setOnClickListener(v -> {
            personagemEscolhido = "Mulher";
        });

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));
    }

    public void jogar(String escolhaUsuario){
        String[] opcoes = {"pedra", "papel", "tesoura"};
        int numero = new Random().nextInt(3);
        String escolhaMaquina = "";
        escolhaMaquina = opcoes[numero];

        switch (escolhaMaquina) {
            case "pedra":
                imgMaquina.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgMaquina.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgMaquina.setImageResource(R.drawable.tesoura);
                break;
        }

        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText(personagemEscolhido + " escolheu " + escolhaUsuario + "\nEmpate");
        } else if ((escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))) {
            txtResultado.setText(personagemEscolhido + " escolheu " + escolhaUsuario + "\nVocê ganhou!");
        } else {
            txtResultado.setText(personagemEscolhido + " escolheu " + escolhaUsuario + "\nVocê perdeu!");
        }
    }

}