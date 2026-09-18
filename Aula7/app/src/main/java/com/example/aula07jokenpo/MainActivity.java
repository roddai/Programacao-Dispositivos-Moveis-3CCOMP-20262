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

    ImageView imgMaquina, imgPapel, imgPedra, imgTesoura;

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

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
    }

    public void jogar(String escolhaUsuario) {
        String[] opcoes = {"pedra", "papel", "tesoura"};
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
        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText("Empate!");
        } else if ((escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))) {
            txtResultado.setText("Você venceu!");
        } else {
            txtResultado.setText("Você perdeu!");
        }

    }

}