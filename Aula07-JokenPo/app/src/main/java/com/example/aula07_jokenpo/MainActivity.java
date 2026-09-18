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
    ImageView imgPedra,imgPapel,imgTesoura,imgUsuarioFem1,imgUsuarioFem2,imgUsuarioMasc
    ,imgInterrogacao1,imgInterrogacao2;
    String personagemEscolhido = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado = findViewById(R.id.txtResultado);
        imgPedra = findViewById(R.id.imgPedra);
        imgPapel =findViewById(R.id.imgPapel);
        imgTesoura =findViewById(R.id.imgTesoura);
        imgUsuarioFem1 = findViewById(R.id.imgUsuarioFem1);
        imgUsuarioMasc = findViewById(R.id.imgUsuarioMasc);
        imgInterrogacao1 = findViewById((R.id.imgInterrogacao1);
        imgInterrogacao2 = findViewById(R.id.imgInterrogacao2);




        imgPedra.setOnClickListener(v -> jogar("pedra"));
        imgPapel.setOnClickListener(v -> jogar("papel"));
        imgTesoura.setOnClickListener(v -> jogar("tesoura"));

        imgUsuarioFem1.setOnClickListener(v -> {
                personagemEscolhido = "Feminino";
                imgUsuarioFem1.setImageResource(R.drawable.usuario_fem);
                });

        imgUsuarioMasc.setOnClickListener(v -> {
            personagemEscolhido = "Masculino";
            imgUsuarioFem1.setImageResource(R.drawable.usuario_masc);
        });




    }
    public void jogar(String escolhaUsuario){
        String[] opcoes = {"pedra","papel","tesoura"};
        int numero = new Random().nextInt(3);
        String escolhaMaquina = opcoes[numero];

        switch(escolhaMaquina){
            case "pedra":
                imgPedra.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imgPapel.setImageResource((R.drawable.papel));
                break;
            case "tesoura":
                imgTesoura.setImageResource(R.drawable.tesoura);
                break;

        }

        if (escolhaUsuario.equals(escolhaMaquina)) {
            txtResultado.setText("Empate!");
        }else if ((escolhaUsuario.equals("pedra") && escolhaMaquina.equals("tesoura")) ||
                (escolhaUsuario.equals("papel") && escolhaMaquina.equals("pedra")) ||
                (escolhaUsuario.equals("tesoura") && escolhaMaquina.equals("papel"))) {
            txtResultado.setText("Você Venceu!!!");
        }else{
            txtResultado.setText("Você Perdeu!!!");
        }
    }


}