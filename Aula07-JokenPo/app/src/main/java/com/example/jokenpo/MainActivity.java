package com.example.aula7;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;

    ImageView imgMaquina, imgInterrogacao, imgPedra, imgPapel, imgTesoura;
    ImageView imgUsuario, imgUsuarioEscolha;
    ImageView imgUsuarioFem, imgUsuarioMasc;

    String personagemEscolhido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);

        imgMaquina = findViewById(R.id.imgMaquina);
        imgInterrogacao = findViewById(R.id.imgInterrogacao);

        imgPedra = findViewById(R.id.imgPedra);
        imgPapel = findViewById(R.id.imgPapel);
        imgTesoura = findViewById(R.id.imgTesoura);

        imgUsuario = findViewById(R.id.imgUsuario);
        imgUsuarioEscolha = findViewById(R.id.imgUsuarioEscolha);

        imgUsuarioFem = findViewById(R.id.imgUsuarioFem);
        imgUsuarioMasc = findViewById(R.id.imgUsuarioMasc);

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));

        imgUsuarioFem.setOnClickListener(v -> {
            personagemEscolhido = "fem";
            imgUsuario.setImageResource(R.drawable.usuario_fem);
        });

        imgUsuarioMasc.setOnClickListener(v -> {
            personagemEscolhido = "masc";
            imgUsuario.setImageResource(R.drawable.usuario_masc);
        });
    }

    public void jogar(String escolhaUsuario) {

        if (personagemEscolhido.isEmpty()) {
            txtResultado.setText("Escolha um personagem primeiro!");
            return;
        }

        switch (escolhaUsuario) {
            case "pedra":
                imgUsuarioEscolha.setImageResource(R.drawable.pedra);
                break;

            case "papel":
                imgUsuarioEscolha.setImageResource(R.drawable.papel);
                break;

            case "tesoura":
                imgUsuarioEscolha.setImageResource(R.drawable.tesoura);
                break;
        }

        String[] opcoes = {"pedra", "papel", "tesoura"};
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoes[numero];

        switch (escolhaMaquina) {
            case "pedra":
                imgInterrogacao.setImageResource(R.drawable.pedra);
                break;

            case "papel":
                imgInterrogacao.setImageResource(R.drawable.papel);
                break;

            case "tesoura":
                imgInterrogacao.setImageResource(R.drawable.tesoura);
                break;
        }

        if (escolhaUsuario.equals(escolhaMaquina)) {

            txtResultado.setText("Empate");

        } else if (
                (escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                        (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                        (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))
        ) {

            txtResultado.setText("Você venceu!!!");

        } else {

            txtResultado.setText("Você perdeu");

        }
    }
}