package com.example.aula5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText edtPeso, edtAltura;
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

        edtPeso = findViewById(R.id.edtPeso);
        edtAltura = findViewById(R.id.edtAltura);
        btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtPeso.getText().toString().isEmpty() ||
                        edtAltura.getText().toString().isEmpty()) {

                    Toast.makeText(MainActivity.this,
                            "Preencha peso e altura",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                double peso = Double.parseDouble(
                        edtPeso.getText().toString());

                double altura = Double.parseDouble(
                        edtAltura.getText().toString());

                double imc = peso / (altura * altura);

                Intent intent;

                if (imc < 18.5) {
                    intent = new Intent(MainActivity.this,
                            ResultadoAbaixoPeso.class);
                } else if (imc < 25) {
                    intent = new Intent(MainActivity.this,
                            ResultadoNormal.class);
                } else if (imc < 30) {
                    intent = new Intent(MainActivity.this,
                            ResultadoSobrepeso.class);
                } else if (imc < 35) {
                    intent = new Intent(MainActivity.this,
                            ResultadoObesidade1.class);
                } else {
                    intent = new Intent(MainActivity.this,
                            ResultadoObesidade2.class);
                }

                startActivity(intent);
            }
        });
    }
}