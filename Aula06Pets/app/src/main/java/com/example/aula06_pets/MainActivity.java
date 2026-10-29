package com.example.aula06_pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Pet rex;
    Pet thor;
    Pet luna;
    Pet mimi;
    Pet nina;
    Pet tom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rex = new Pet("Rex", "Caramelo", "Sem raça definida", "4 anos", "20 de julho", R.drawable.rex);
        nina = new Pet("Nina", "Preta e branca", "Sem raça definida", "3 anos", "25 de maio", R.drawable.nina);
        tom = new Pet("Tom", "Preto", "Sem raça definida", "1 ano", "3 de novembro", R.drawable.tom);
        thor = new Pet("Thor", "Cinza", "Sem raça definida", "3 anos", "15 de março", R.drawable.thor);
        luna = new Pet("Luna", "Cinza e branca", "Sem raça definida", "2 anos", "7 de setembro", R.drawable.luna);
        mimi = new Pet("Mimi", "Caramelo e branco", "Sem raça definida", "5 anos", "11 de janeiro", R.drawable.mimi);

        Button btnRex = findViewById(R.id.btnRex);
        Button btnNina = findViewById(R.id.btnNina);
        Button btnTom = findViewById(R.id.btnTom);
        Button btnThor = findViewById(R.id.btnThor);
        Button btnLuna = findViewById(R.id.btnLuna);
        Button btnMimi = findViewById(R.id.btnMimi);

        btnRex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(rex);
            }
        });

        btnNina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(nina);
            }
        });

        btnTom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(tom);
            }
        });

        btnThor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(thor);
            }
        });

        btnLuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(luna);
            }
        });

        btnMimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalhes(mimi);
            }
        });
    }

    private void abrirDetalhes(Pet pet) {
        Intent intent = new Intent(MainActivity.this, DetalhesPetActivity.class);

        intent.putExtra("nome", pet.getNome());
        intent.putExtra("cor", pet.getCor());
        intent.putExtra("raca", pet.getRaca());
        intent.putExtra("idade", pet.getIdade());
        intent.putExtra("aniversario", pet.getAniversario());
        intent.putExtra("imagem", pet.getImagem());

        startActivity(intent);
    }
}