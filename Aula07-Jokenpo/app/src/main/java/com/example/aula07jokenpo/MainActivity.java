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

    }

    public void jogar(String escolhaUsuario){
        String[] opcoes = {"pedra", "papel", "tesoura"};
        //escolha da maquina ->
        int numero = new Random().nextInt(3);//variavel tipo int, nextInt = quantidade de obj (nesse caso 0-2, ou seja 3)
        String escolhaMaquina = opcoes[numero]; //pegar a apartir do indice 0 e conta 3 valores

        switch (escolhaMaquina) {
            case "pedra":
                imgMaquina.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgMaquina.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgTesoura.setImageResource(R.drawable.tesoura);
                break;

        }

        if(escolhaUsuario.equals(escolhaMaquina)){
            txtResultado.setText("Empate");
        } else if ((escolhaUsuario.equals("Pedra")&& escolhaMaquina.equals("tesoura")) ||
                    (escolhaUsuario.equals("papel")&& escolhaMaquina.equals("pedra")) ||
                    (escolhaUsuario.equals("tesoura")&& escolhaMaquina.equals("papel"))) {
            txtResultado.setText("Você venceu!!!!!!!!!!!!");
        } else {
            txtResultado.setText("Você perdeu :(");
        }

    };

}