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
    ImageView imgPlayer, imgNeo, imgDeo, imgOpcaoMaquina, imgOpcaoPlayer,imgPedra, imgPapel, imgTesoura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);
        imgPlayer = findViewById(R.id.imgPlayer);
        imgNeo = findViewById(R.id.imgNeo);
        imgDeo = findViewById(R.id.imgDeo);
        imgOpcaoPlayer = findViewById(R.id.imgOpcaoPlayer);
        imgOpcaoMaquina = findViewById(R.id.imgOpcaoMaquina);
        imgPedra = findViewById(R.id.imgPedra);
        imgPapel = findViewById(R.id.imgPapel);
        imgTesoura = findViewById(R.id.imgTesoura);

        imgNeo.setOnClickListener(v -> personagem("Neo"));
        imgDeo.setOnClickListener(v -> personagem("Deo"));

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));
    }

    public void personagem(String escolhaPersonagem) {
        String[] opcoes = {"Neo", "Deo"};
        switch (escolhaPersonagem) {
            case "Neo":
                imgPlayer.setImageResource(R.drawable.neo);
                break;
            case "Deo":
                imgPlayer.setImageResource(R.drawable.deo);
                break;
        }
    }
    public void jogar(String escolhaUsuario) {
        String[] opcoes = {"pedra", "papel", "tesoura"};

        switch (escolhaUsuario){
            case "pedra":
                imgOpcaoPlayer.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgOpcaoPlayer.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgOpcaoPlayer.setImageResource(R.drawable.tesoura);
                break;
        }
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoes[numero];


        switch (escolhaMaquina) {
            case "pedra":
                imgOpcaoMaquina.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgOpcaoMaquina.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgOpcaoMaquina.setImageResource(R.drawable.tesoura);
                break;
        }

        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText("Empate!");
        } else if ((escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))) {
            txtResultado.setText("Você Venceu!!!");
        } else {
            txtResultado.setText("Você perdeu!");
        }
    }
}
