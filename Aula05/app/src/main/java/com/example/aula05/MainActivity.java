package com.example.aula05;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editPeso = findViewById(R.id.editPeso);
        editAltura = findViewById(R.id.editAltura);
        btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                double peso = Double.parseDouble(editPeso.getText().toString());
                double altura = Double.parseDouble(editAltura.getText().toString());
                double imc = peso / (altura * altura);

                Intent intent;

                if (imc < 18.5){
                    intent = new Intent(MainActivity.this, ResultadoAbaixoPeso.class); //leva até a classe que controla a tela
                } else if (imc < 24.9){
                    intent = new Intent(MainActivity.this, ResultadoNormal.class);
                } else if (imc < 29.9){
                    intent = new Intent(MainActivity.this, ResultadoSobrepeso.class);
                }else if (imc < 34.9){
                    intent = new Intent(MainActivity.this, ResultadoObesidade1.class);
                } else{
                    intent = new Intent(MainActivity.this, ResultadoObesidade2.class);
                }

                startActivity(intent);

                /* menor que 18 = abaixo do peso;
                entre 18,5 e 24,9 = peso normal
                entre 25 e 29,9 = sobrepeso
                entre 30 e 39,9 = obesidade 1
                maior que 40 = obesidade 2

                 */
            }
        });

    }
}