package com.example.aula07jokenpo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;

    ImageView imgMaquina, imgPapel, imgPedra, imgTesoura;
    ImageView imgJogador;
    ImageView imgEscolhaMaquina, imgEscolhaJogador;
    ImageView imgUsuarioFem, imgUsuarioMasc;

    String personagemEscolhido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);

        imgMaquina = findViewById(R.id.imgMaquina);
        imgPapel = findViewById(R.id.imgPapel);
        imgPedra = findViewById(R.id.imgPedra);
        imgTesoura = findViewById(R.id.imgTesoura);

        imgJogador = findViewById(R.id.imgJogador);
        imgEscolhaMaquina = findViewById(R.id.imgEscolhaMaquina);
        imgEscolhaJogador = findViewById(R.id.imgEscolhaJogador);

        imgUsuarioFem = findViewById(R.id.imgUsuarioFem);
        imgUsuarioMasc = findViewById(R.id.imgUsuarioMasc);

        imgUsuarioFem.setOnClickListener(v -> {
            personagemEscolhido = "fem";
            imgJogador.setImageResource(R.drawable.usuario_fem);
        });

        imgUsuarioMasc.setOnClickListener(v -> {
            personagemEscolhido = "masc";
            imgJogador.setImageResource(R.drawable.usuario_masc);
        });

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
    }

    public void jogar(String escolhaUsuario) {

        if (personagemEscolhido.equals("")) {
            txtResultado.setText("Escolha seu personagem!");
            return;
        }

        String[] opcoes = {"pedra", "papel", "tesoura"};
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoes[numero];

        switch (escolhaUsuario) {
            case "pedra":
                imgEscolhaJogador.setImageResource(R.drawable.pedra);
                break;

            case "papel":
                imgEscolhaJogador.setImageResource(R.drawable.papel);
                break;

            case "tesoura":
                imgEscolhaJogador.setImageResource(R.drawable.tesoura);
                break;
        }

        switch (escolhaMaquina) {
            case "pedra":
                imgEscolhaMaquina.setImageResource(R.drawable.pedra);
                break;

            case "papel":
                imgEscolhaMaquina.setImageResource(R.drawable.papel);
                break;

            case "tesoura":
                imgEscolhaMaquina.setImageResource(R.drawable.tesoura);
                break;
        }

        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText("Empate!");

        } else if ((escolhaUsuario.equals("pedra") &&
                escolhaMaquina.equals("tesoura")) ||

                (escolhaUsuario.equals("papel") &&
                        escolhaMaquina.equals("pedra")) ||

                (escolhaUsuario.equals("tesoura") &&
                        escolhaMaquina.equals("papel"))) {

            txtResultado.setText("Você venceu!");

        } else {
            txtResultado.setText("Você perdeu!");
        }
    }
}
