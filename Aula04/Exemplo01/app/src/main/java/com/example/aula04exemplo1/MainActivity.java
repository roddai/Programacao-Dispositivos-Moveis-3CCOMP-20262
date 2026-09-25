package com.example.aula04exemplo1;

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
    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /*Instanciando os livros*/
        livro1 = new Livro("Título 1", "Autor 1", 2020 );
        livro2 = new Livro("Título 2", "Autor 2", 2020);
        livro3 = new Livro("Título 3", "Autor 3", 2020);

        /*Aqui estamos referenciando que deve procurar o metodo pelo id*/
        textInfo = findViewById(R.id.textInfo);

        /*(IMPORTANTE) Aqui estamos setando que para achar o botão, deve-se procurar pelo id*/
        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        /*Adicionando o metodo que irá acontecer quando o botão for clicado*/
        button1.setOnClickListener(v -> textInfo.setText(livro1.getInfo()));
        button2.setOnClickListener(v -> textInfo.setText(livro2.getInfo()));
        button3.setOnClickListener(v -> textInfo.setText(livro3.getInfo()));
    }
}