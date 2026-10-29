package com.example.aula07jokenpo;

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
    ImageView imgResulMaq, imgResulJog;
    ImageView imgJogadorFem, imgJogadorMasc, imgJogador;

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

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));

        imgResulMaq = findViewById(R.id.imgResulMaq);
        imgResulJog = findViewById(R.id.imgResulJog);

        imgJogadorFem = findViewById(R.id.imgJogadorFem);
        imgJogadorMasc = findViewById(R.id.imgJogadorMasc);
        imgJogador = findViewById(R.id.imgJogador);

        imgJogadorFem.setOnClickListener(v -> escolher("feminino"));
        imgJogadorMasc.setOnClickListener(v -> escolher("masculino"));

    }

    public void jogar(String escolhaUsuario){
        String[] opcoes = {"pedra", "papel", "tesoura"};
        //escolha da maquina ->
        int numero = new Random().nextInt(3);//variavel tipo int, nextInt = quantidade de obj (nesse caso 0-2, ou seja 3)
        String escolhaMaquina = "";
                escolhaMaquina = opcoes[numero]; //pegar a apartir do indice 0 e conta 3 valores

        switch (escolhaMaquina) {
            case "pedra":
                imgResulMaq.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgResulMaq.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgResulMaq.setImageResource(R.drawable.tesoura);
                break;

        }

        switch (escolhaUsuario) {
            case "pedra":
                imgResulJog.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgResulJog.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgResulJog.setImageResource(R.drawable.tesoura);
                break;

        }

        if(escolhaUsuario.equals(escolhaMaquina)){
            txtResultado.setText("Empate");
        } else if ((escolhaUsuario.equals("pedra")&& escolhaMaquina.equals("tesoura")) ||
                    (escolhaUsuario.equals("papel")&& escolhaMaquina.equals("pedra")) ||
                    (escolhaUsuario.equals("tesoura")&& escolhaMaquina.equals("papel"))) {
            txtResultado.setText("Você venceu!!!!!!!!!!!!");
        } else {
            txtResultado.setText("Você perdeu :(");
        }

    };

    public void escolher(String escolhaPersonagem){
        switch (escolhaPersonagem) {
            case "feminino":
                imgJogador.setImageResource(R.drawable.usuario_fem);
                break;
            case "masculino":
                imgJogador.setImageResource(R.drawable.usuario_masc);
                break;
        }
    }

}