package com.example.aula06;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private EditText editNome;
    public Pet Thor = new Pet ("Thor", "Dourado", "Golden Retriever", "22/05/2024", 3);
    public Pet Rex = new Pet("Rex", "Preto", "Pinsher", "12/08/2025", 1);
    public Pet Luna = new Pet("Luna", "Dourado", "raça3", "02/03/2025",1);
    public Pet Olaf = new Pet("Olaf", "Brando", "raça4", "07/09/2025", 1);
    public Pet Simba = new Pet("Simba", "Laranja", "raça5", "22/03/2023", 3);
    public Pet Chico = new Pet("Chico", "Branco e Preto", "raça6", "10/01/2025", 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent detalhes = new Intent(MainActivity.this, DetalhesPetActivity.class);
             //   detalhes.putExtra("Thor", Thor.getInfo());
                startActivity(detalhes);
            }
        });
    }
}