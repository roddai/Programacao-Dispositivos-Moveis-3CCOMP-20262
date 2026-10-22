package com.example.aula4exemplo1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Livro livro1, livro2, livro3;
    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /* Instanciando os livros */
        livro1 = new Livro("Desenvolvendo seu Primeiro Aplicativo Android", "Luiz Carlos", 2015);
        livro2 = new Livro("Titulo2", "Autor2", 2020);
        livro3 = new Livro("Titulo3", "Autor3", 2018);

        textInfo = findViewById(R.id.textInfo);

        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button1.setOnClickListener(v -> textInfo.setText(livro1.getInfo()));
        button1.setOnClickListener(v -> textInfo.setText(livro2.getInfo()));
        button1.setOnClickListener(v -> textInfo.setText(livro3.getInfo()));
         }
}