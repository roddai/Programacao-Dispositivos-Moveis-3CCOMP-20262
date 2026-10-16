package com.example.aula07_jokenpo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView txtResultado;
    private ImageView imgJogadaMaquina, imgJogadaUsuario, imgIconeUsuario;
    private ImageView imgPedra, imgPapel, imgTesoura;
    private ImageView btnUsuarioFem, btnUsuarioMasc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);
        imgJogadaMaquina = findViewById(R.id.JogadaDaMaquina);
        imgJogadaUsuario = findViewById(R.id.JogadaDoUsuario);
        imgIconeUsuario = findViewById(R.id.IconeUsuario);

        imgPedra = findViewById(R.id.imgPedra);
        imgPapel = findViewById(R.id.imgPapel);
        imgTesoura = findViewById(R.id.imgTesoura);

        btnUsuarioFem = findViewById(R.id.UsuarioFem);
        btnUsuarioMasc = findViewById(R.id.usuariomasc);


        btnUsuarioFem.setOnClickListener(v -> imgIconeUsuario.setImageResource(R.drawable.usuario_fem));
        btnUsuarioMasc.setOnClickListener(v -> imgIconeUsuario.setImageResource(R.drawable.usuario_masc));

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));
    }

    public void jogar(String escolhaUsuario) {
        switch (escolhaUsuario) {
            case "pedra":
                imgJogadaUsuario.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgJogadaUsuario.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgJogadaUsuario.setImageResource(R.drawable.tesoura);
                break;
        }

        String[] opcoes = {"pedra", "papel", "tesoura"};
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoes[numero];

        switch (escolhaMaquina) {
            case "pedra":
                imgJogadaMaquina.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgJogadaMaquina.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgJogadaMaquina.setImageResource(R.drawable.tesoura);
                break;
        }

        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText("Empate!");
        } else if ((escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))) {
            txtResultado.setText("Você venceu");
        } else {
            txtResultado.setText("Você perdeu");
        }
    }
}