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
    ImageView imgMaquina, imgPedra, imgPapel, imgTesoura, personagemUsuario, imgMasculino, imgFeminino, resultadoMaquina, resultadoUsuario;

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
        resultadoMaquina = findViewById(R.id.resultadoMaquina);
        personagemUsuario = findViewById(R.id.personagemUsuario);
        resultadoUsuario = findViewById(R.id.resultadoUsuario);
        imgMasculino = findViewById(R.id.masculino);
        imgFeminino = findViewById(R.id.feminino);

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));
        imgMasculino.setOnClickListener(v -> personagemUsuario.setImageResource(R.drawable.usuario_masc));
        imgFeminino.setOnClickListener(v -> personagemUsuario.setImageResource(R.drawable.usuario_fem));
    }

    public void jogar(String escolhaUsuario) {
        String[] opcoes = {"pedra", "papel", "tesoura"};
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoes[numero];

        switch (escolhaMaquina) {
            case "pedra":
                resultadoMaquina.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                resultadoMaquina.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                resultadoMaquina.setImageResource(R.drawable.tesoura);
        }

        switch (escolhaUsuario) {
            case "pedra":
                resultadoUsuario.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                resultadoUsuario.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                resultadoUsuario.setImageResource(R.drawable.tesoura);
        }

        if (escolhaUsuario.equals(escolhaMaquina)){
            txtResultado.setText("Empate!");
        } else if ((escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))){
            txtResultado.setText("Você venceu!!!");
        } else {
            txtResultado.setText("Você perdeu!");
        }
    }
}
