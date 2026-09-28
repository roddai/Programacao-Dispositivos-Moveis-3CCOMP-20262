package com.example.aula07_jokenpo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;

    ImageView imgMaquina;
    ImageView imgEscolhaUsuario;
    ImageView imgResultadoUsuario;
    ImageView imgPersonagemSelecionado;

    ImageView imgPedra;
    ImageView imgPapel;
    ImageView imgTesoura;

    ImageView imgUsuarioFem;
    ImageView imgUsuarioMasc;

    int personagemSelecionado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);

        imgMaquina = findViewById(R.id.imgMaquina);
        imgEscolhaUsuario = findViewById(R.id.imgEscolhaUsuario);
        imgResultadoUsuario = findViewById(R.id.imgResultadoUsuario);
        imgPersonagemSelecionado = findViewById(
                R.id.imgPersonagemSelecionado
        );

        imgPedra = findViewById(R.id.imgPedra);
        imgPapel = findViewById(R.id.imgPapel);
        imgTesoura = findViewById(R.id.imgTesoura);

        imgUsuarioFem = findViewById(R.id.imgUsuarioFem);
        imgUsuarioMasc = findViewById(R.id.imgUsuarioMasc);

        imgUsuarioFem.setOnClickListener(v -> {
            personagemSelecionado = 1;

            imgPersonagemSelecionado.setImageResource(
                    R.drawable.usuario_fem
            );
        });

        // Recebe a escolha do Usuário

        imgUsuarioMasc.setOnClickListener(v -> {
            personagemSelecionado = 2;

            imgPersonagemSelecionado.setImageResource(
                    R.drawable.usuario_masc
            );
        });

        imgPedra.setOnClickListener(v -> jogar("pedra"));

        imgPapel.setOnClickListener(v -> jogar("papel"));

        imgTesoura.setOnClickListener(v -> jogar("tesoura"));
    }

    public void jogar(String escolhaUsuario) {

        if (personagemSelecionado == 0) {
            Toast.makeText(
                    MainActivity.this,
                    "Escolha um personagem primeiro",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (escolhaUsuario.equals("pedra")) {
            imgEscolhaUsuario.setImageResource(R.drawable.pedra);
            imgResultadoUsuario.setImageResource(R.drawable.pedra);
        }

        if (escolhaUsuario.equals("papel")) {
            imgEscolhaUsuario.setImageResource(R.drawable.papel);
            imgResultadoUsuario.setImageResource(R.drawable.papel);
        }

        if (escolhaUsuario.equals("tesoura")) {
            imgEscolhaUsuario.setImageResource(R.drawable.tesoura);
            imgResultadoUsuario.setImageResource(R.drawable.tesoura);
        }

        String[] opcoes = {
                "pedra",
                "papel",
                "tesoura"
        };

        // Maquina sorteia qual jogar

        int numero = new Random().nextInt(3);

        String escolhaMaquina = opcoes[numero];

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
        // Define quando o jogo empata:

        if (escolhaUsuario.equals(escolhaMaquina)) {

            txtResultado.setText("Empate!");

        } else if (
                escolhaUsuario.equals("pedra")
                        && escolhaMaquina.equals("tesoura")
        )

        // Define quando o Usuário vence
        {

            txtResultado.setText("Você venceu!");

        } else if (
                escolhaUsuario.equals("papel")
                        && escolhaMaquina.equals("pedra")
        ) {

            txtResultado.setText("Você venceu!");

        } else if (
                escolhaUsuario.equals("tesoura")
                        && escolhaMaquina.equals("papel")
        ) {

            txtResultado.setText("Você venceu!");

        }
        // Define quando a Máquina vence
        else {

            txtResultado.setText("Você perdeu!");
        }
    }
}