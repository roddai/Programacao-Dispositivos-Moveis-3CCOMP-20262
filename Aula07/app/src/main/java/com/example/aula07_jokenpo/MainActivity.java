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
    ImageView imgInterrogacao, imgInterrogacao2, imgPedra, imgPapel, imgTesoura, imgUsuario, imgUsuarioFem, imgUsuarioMasc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);
        imgInterrogacao = findViewById((R.id.imgInterrogacao));
        imgInterrogacao2 = findViewById((R.id.imgInterrogacao2));
        imgPedra = findViewById((R.id.imgPedra));
        imgPapel = findViewById((R.id.imgPapel));
        imgTesoura = findViewById((R.id.imgTesoura));
        imgUsuario = findViewById((R.id.imgUsuario));
        imgUsuarioFem = findViewById((R.id.imgUsuarioFem));
        imgUsuarioMasc = findViewById((R.id.imgUsuarioMasc));

        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));

        imgUsuarioFem.setOnClickListener(v -> perfil("feminino"));
        imgUsuarioMasc.setOnClickListener(v -> perfil("masculino"));
    }

    public void jogar(String escolhaUsuario){
        String[] opcoesSimbolos = {"pedra", "papel", "tesoura"};
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoesSimbolos[numero];

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

        switch (escolhaUsuario) {
            case "pedra":
                imgInterrogacao2.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgInterrogacao2.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imgInterrogacao2.setImageResource(R.drawable.tesoura);
                break;
        }



        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText("Empate");
        }
        else if ((escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))){
            txtResultado.setText("Vitória");
        }
        else {
            txtResultado.setText("Derrota");
        }

    }
    public void perfil (String escolhaFoto){
        switch (escolhaFoto) {
            case "masculino":
                imgUsuario.setImageResource(R.drawable.usuario_masc);
                break;
            case "feminino":
                imgUsuario.setImageResource(R.drawable.usuario_fem);
                break;
        }
    }

}