package com.example.aula04_exemplo1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Livro livro1, livro2, livro3;
    TextView textinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /*instanciando os livros*/
        livro1 = new Livro("Desenvovendo seu Primeiro Aplicativo Android", "Luiz Carlos",2013);
        livro2 = new Livro("Programação de Jogos Andoid","Edgard B. Damiani",2014);
        livro3 = new Livro("Desenvolvimento de Aplicativos. Um guia Prático Para Criar Aplicativos","Erik Leger",2018);

        textinfo = findViewById(R.id.textInfo);

        Button button1 =findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button1.setOnClickListener(v -> textinfo.setText(livro1.getinfo()));
        button2.setOnClickListener(v -> textinfo.setText(livro2.getinfo()));
        button3.setOnClickListener(v -> textinfo.setText(livro3.getinfo()));

    }
}