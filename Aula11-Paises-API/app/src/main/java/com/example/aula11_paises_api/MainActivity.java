package com.example.aula11_paises_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editPais;
    Button btnResultado;
    TextView Resultado;
    ImageView imgBandeira;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        editPais = findViewById(R.id.editPais);
        btnResultado = findViewById(R.id.btnResultado);
        Resultado = findViewById(R.id.Resultado);
        imgBandeira = findViewById(R.id.imgBandeira);

        btnResultado.setOnClickListener(v -> {
            String pais = editPais.getText().toString().trim();

            if (pais.isEmpty()) {
                Resultado.setText("Digite o nome de um paí");
                return;
            }

            String endereco = "https://restcountries.com/v3.1/name/Brasil" + URLEncoder.encode()
        });
    }
}