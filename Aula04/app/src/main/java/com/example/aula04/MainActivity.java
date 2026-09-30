package com.example.aula04;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Livro livro1, livro2, livro3;
    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.view.View conteudo = ((android.view.ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        int esquerda=conteudo.getPaddingLeft(), topo=conteudo.getPaddingTop();
        int direita=conteudo.getPaddingRight(), base=conteudo.getPaddingBottom();
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(conteudo, (view, insets) -> {
            androidx.core.graphics.Insets barras=insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            view.setPadding(esquerda+barras.left, topo+barras.top, direita+barras.right, base+barras.bottom);
            return insets;
        });
        androidx.core.view.ViewCompat.requestApplyInsets(conteudo);


        // Instanciando os livros
        livro1 = new Livro(
                "Desenvolvendo seu Primeiro Aplicativo Android",
                "Luiz Carlos",
                2020
        );

        livro2 = new Livro(
                "Programação de Jogos Android",
                "Edgard B. Damiani",
                2018
        );

        livro3 = new Livro(
                "Desenvolvimento de Aplicativos: Um Guia Prático",
                "Autor Exemplo",
                2022
        );

        textInfo = findViewById(R.id.textInfo);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button1.setOnClickListener(v ->
                textInfo.setText(livro1.getInfo())
        );

        button2.setOnClickListener(v ->
                textInfo.setText(livro2.getInfo())
        );

        button3.setOnClickListener(v ->
                textInfo.setText(livro3.getInfo())
        );
    }
}