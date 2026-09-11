package com.example.aula06desafio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4, btn5, btn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pet pet1 = new Pet("Pipoca", "Caramelo", "Pug", 3, "24/10");
                Intent mudarTela = new Intent(MainActivity.this, DetalhesActivity.class);
                mudarTela.putExtra("nome", pet1.nome);
                mudarTela.putExtra("cor", pet1.cor);
                mudarTela.putExtra("raca", pet1.raca);
                mudarTela.putExtra("idade", pet1.idade);
                mudarTela.putExtra("aniversario", pet1.aniversario);

                startActivity(mudarTela);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pet pet2 = new Pet("Tyler", "Preto", "Chiuawa", 5, "08/06");
                Intent mudarTela = new Intent(MainActivity.this, DetalhesActivity.class);
                mudarTela.putExtra("nome", pet2.nome);
                mudarTela.putExtra("cor", pet2.cor);
                mudarTela.putExtra("raca", pet2.raca);
                mudarTela.putExtra("idade", pet2.idade);
                mudarTela.putExtra("aniversario", pet2.aniversario);

                startActivity(mudarTela);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pet pet3 = new Pet("Princesa", "Dourado", "Lulu da Pomerania", 2, "12/07");
                Intent mudarTela = new Intent(MainActivity.this, DetalhesActivity.class);
                mudarTela.putExtra("nome", pet3.nome);
                mudarTela.putExtra("cor", pet3.cor);
                mudarTela.putExtra("raca", pet3.raca);
                mudarTela.putExtra("idade", pet3.idade);
                mudarTela.putExtra("aniversario", pet3.aniversario);

                startActivity(mudarTela);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pet pet4 = new Pet("Terror", "Laranja", "SRD", 1, "06/07");
                Intent mudarTela = new Intent(MainActivity.this, DetalhesActivity.class);
                mudarTela.putExtra("nome", pet4.nome);
                mudarTela.putExtra("cor", pet4.cor);
                mudarTela.putExtra("raca", pet4.raca);
                mudarTela.putExtra("idade", pet4.idade);
                mudarTela.putExtra("aniversario", pet4.aniversario);

                startActivity(mudarTela);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pet pet5 = new Pet("Babidi", "Cinza", "SRD", 1, "11/09");
                Intent mudarTela = new Intent(MainActivity.this, DetalhesActivity.class);
                mudarTela.putExtra("nome", pet5.nome);
                mudarTela.putExtra("cor", pet5.cor);
                mudarTela.putExtra("raca", pet5.raca);
                mudarTela.putExtra("idade", pet5.idade);
                mudarTela.putExtra("aniversario", pet5.aniversario);

                startActivity(mudarTela);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pet pet6 = new Pet("Aurora", "Cinza", "Siamês", 4, "25/12");
                Intent mudarTela = new Intent(MainActivity.this, DetalhesActivity.class);
                mudarTela.putExtra("nome", pet6.nome);
                mudarTela.putExtra("cor", pet6.cor);
                mudarTela.putExtra("raca", pet6.raca);
                mudarTela.putExtra("idade", pet6.idade);
                mudarTela.putExtra("aniversario", pet6.aniversario);

                startActivity(mudarTela);
            }
        });
    }
}