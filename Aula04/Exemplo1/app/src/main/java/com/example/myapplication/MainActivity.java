package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declara três objetos do tipo Livro[cite: 1]
    Livro livro1, livro2, livro3;
    // Declara um TextView que exibirá as informações[cite: 1]
    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciando os livros (Criando os objetos a partir da classe)[cite: 1]
        livro1 = new Livro("Desenvolvendo seu Primeiro Aplicativo Android", "Luiz Carlos", 2017);
        livro2 = new Livro("Programação de Jogos Android", "Edgard B. Damiani", 2016);
        livro3 = new Livro("Desenvolvimento de Aplicativos", "Erik", 2018);

        // Ligando os elementos da interface (XML) ao código Java[cite: 1]
        textInfo = findViewById(R.id.textInfo);
        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        // Ações dos botões (Listeners): quando clicados, chamam o método getInfo() do livro[cite: 1]
        button1.setOnClickListener(v -> textInfo.setText(livro1.getInfo()));
        button2.setOnClickListener(v -> textInfo.setText(livro2.getInfo()));
        button3.setOnClickListener(v -> textInfo.setText(livro3.getInfo()));
    }
}