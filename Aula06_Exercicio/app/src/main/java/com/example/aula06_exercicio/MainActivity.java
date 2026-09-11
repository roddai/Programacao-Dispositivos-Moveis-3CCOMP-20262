package com.example.aula06_exercicio;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Pet cao1, cao2, cao3, gato1, gato2, gato3;

    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cao1 = new Pet("Bob", "Caramelo", "Vira-lata", 2, "20/02");
        cao2 = new Pet("Lobinho", "Cinza", "Husky", 4, "10/09");
        cao3 = new Pet("Jorgin", "Marrom", "Salsicha", 5, "20/03");
        gato1 = new Pet("Floquinho", "Cinza", "Rajado", 5, "20/06");
        gato2 = new Pet("Chamusco", "Bege", "Siamês", 8, "19/10");
        gato3 = new Pet("Foguinho", "Laranja", "Laranja", 5, "15/06");

        textInfo = findViewById(R.id.textInfo);

        Button button1 = findViewById(R.id.btnCao1);
        Button button2 = findViewById(R.id.btnCao2);
        Button button3 = findViewById(R.id.btnCao3);
        Button button4 = findViewById(R.id.btnGato1);
        Button button5 = findViewById(R.id.btnGato2);
        Button button6 = findViewById(R.id.btnGato3);

        button1.setOnClickListener(v -> textInfo.setText(cao1.getinfo()));
        button1.setOnClickListener(v -> textInfo.setText(cao2.getinfo()));
        button1.setOnClickListener(v -> textInfo.setText(cao3.getinfo()));
        button1.setOnClickListener(v -> textInfo.setText(gato1.getinfo()));
        button1.setOnClickListener(v -> textInfo.setText(gato2.getinfo()));
        button1.setOnClickListener(v -> textInfo.setText(gato3.getinfo()));



    }
}