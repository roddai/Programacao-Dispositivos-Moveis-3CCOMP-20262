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
    ImageView imgMaquina, imgPedra, imgPapel, imgTesoura;
    ImageView imgUsuarioMasc, imgUsuarioFem;
    ImageView imgUsuarioSelecionado, imgUsuarioJogada,  imgInterrogacaoMaquina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado= findViewById(R.id.txtResultado);
        imgMaquina = findViewById(R.id.imgMaquina);
        imgPedra= findViewById(R.id.imgPedra);
        imgPapel = findViewById(R.id.imgPapel);
        imgTesoura = findViewById(R.id.imgTesoura);
        imgInterrogacaoMaquina = findViewById(R.id.imgInterrogacaoMaquina);
        imgUsuarioSelecionado = findViewById(R.id.imgUsuarioSelecionado);
        imgUsuarioJogada = findViewById(R.id.imgUsuarioJogada);
        imgUsuarioFem = findViewById(R.id.imgUsuarioFem);
        imgUsuarioMasc = findViewById(R.id.imgUsuarioMasc);

        imgUsuarioFem.setOnClickListener(v -> {
            imgUsuarioSelecionado.setImageResource(R.drawable.usuario_fem);
        });
        imgUsuarioMasc.setOnClickListener(v -> {
            imgUsuarioSelecionado.setImageResource(R.drawable.usuario_masc);
        });

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));


    }
    public void jogar(String escolhaUsuario){
        switch (escolhaUsuario) {
            case "pedra" :
                imgUsuarioJogada.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgUsuarioJogada.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgUsuarioJogada.setImageResource(R.drawable.tesoura);
                break;
        }
        String[] opcoes = {"pedra" , "papel" , "tesoura" };
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoes[numero];

        switch (escolhaMaquina) {
            case "pedra":
                imgInterrogacaoMaquina.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgInterrogacaoMaquina.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgInterrogacaoMaquina.setImageResource(R.drawable.tesoura);
                break;
        }

        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText("Empate!");
        } else if ((escolhaUsuario.equals("pedra")&& escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel")&& escolhaMaquina.equals(("pedra")) ||
                        (escolhaUsuario.equals("tesoura")) && escolhaMaquina.equals("papel"))) {
            txtResultado.setText("Voce venceu!!!");
        } else {
            txtResultado.setText("Voce perdeu!");
        }
    }

}