package com.example.aula11_moedas_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editMoeda;
    EditText editValor;
    Button btnConsultar;
    TextView Resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editMoeda = findViewById(R.id.editMoeda);
        editValor = findViewById(R.id.editValor);
        btnConsultar = findViewById(R.id.btnConsultar);
        Resultado = findViewById(R.id.Resultado);

        btnConsultar.setOnClickListener(v -> {
            String moeda = editMoeda.getText().toString().trim().toUpperCase();

            String valorTexto = editValor.getText().toString().trim().replace(",",".");

            if (moeda.isEmpty() || valorTexto.isEmpty()) {
                Resultado.setText("Preencha todos os campos");
                return;
            }

            if (!moeda.equals("BRL") && !moeda.equals("USD") && !moeda.equals("EUR")) {
                Resultado.setText("Digite BRL, USD ou EUR");
                return;
            }

            double valor;

            try {
                valor = Double.parseDouble(valorTexto);

                if (valor < 0) {
                    Resultado.setText("Digite um valor positivo");
                    return;
                }
            } catch (Exception e) {
                Resultado.setText("Valor invalido");
                return;
            }


        });
        }
    }