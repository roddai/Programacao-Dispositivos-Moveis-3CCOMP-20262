package com.example.projetoaula05;

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

    EditText editPeso, editAltura;
    Button btnCalcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editPeso = findViewById(R.id.editPeso);
        editAltura = findViewById(R.id.editAltura);
        btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(v -> {
            double peso = Double.parseDouble(editPeso.getText().toString());
            double altura = Double.parseDouble(editAltura.getText().toString());
            double imc = peso / (altura * altura);

            Intent intent;

            if (imc < 18.5) {
                intent = new Intent(MainActivity.this, ResultadoAbaixoPeso.class);
            } else if (imc < 24.9) {
                intent = new Intent(MainActivity.this, ResultadoNormal.class);
            } else if (imc < 29.9) {
                intent = new Intent(MainActivity.this, ResultadoSobrepeso.class);
            } else if (imc < 34.9) {
                intent = new Intent(MainActivity.this, ResultadoObesidade1.class);
            } else {
                intent = new Intent(MainActivity.this, ResultadoObesidade2.class);
            }

           /* Baixo peso: IMC < 18,5
            Normal: IMC entre 18,5 e 24,9
            Sobrepeso: IMC entre 25 e 29,9
            Obesidade I: IMC entre 30 e 34,9
            Obesidade II: IMC entre 35 e 39,9
            Obesidade III: IMC ≥ 40/*/

        });


    }
}