package com.example.aula05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
                intent = new Intent(MainActivity.this,
                        ResultadoAbaixoDoPesoActivity.class);

            } else if (imc < 25) {
                intent = new Intent(MainActivity.this,
                        ResultadoNormalActivity.class);

            } else if (imc < 30) {
                intent = new Intent(MainActivity.this,
                        ResultadoSobrepesoActivity.class);

            } else if (imc < 35) {
                intent = new Intent(MainActivity.this,
                        ResultadoObesidadeClasse1Activity.class);

            } else {
                intent = new Intent(MainActivity.this,
                        ResultadoObesidadeClasse2Activity.class);
            }

            startActivity(intent);
        });
    }
}