package com.example.aula11;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editMoeda;
    private TextView textDolar, textEuro, textReal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMoeda = findViewById(R.id.editMoeda);
        Button btnBuscar = findViewById(R.id.btnBuscar);
        textDolar = findViewById(R.id.textDolar);
        textEuro = findViewById(R.id.textEuro);
        textReal = findViewById(R.id.textReal);

        btnBuscar.setOnClickListener(v -> buscarCotacao());
    }

    private void buscarCotacao() {
        String moeda = editMoeda.getText().toString().trim().toUpperCase();

        if (moeda.isEmpty()) {
            Toast.makeText(this, "Digite uma moeda", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://open.er-api.com/v6/latest/" + moeda;

        RequestQueue fila = Volley.newRequestQueue(this);

        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                resposta -> {
                    try {
                        JSONObject taxas = resposta.getJSONObject("rates");

                        textDolar.setText("Dólar: " + taxas.getDouble("USD"));
                        textEuro.setText("Euro: " + taxas.getDouble("EUR"));
                        textReal.setText("Real: " + taxas.getDouble("BRL"));

                    } catch (Exception e) {
                        Toast.makeText(this, "Erro nos dados", Toast.LENGTH_SHORT).show();
                    }
                },
                erro -> Toast.makeText(
                        this,
                        "Erro ao buscar cotação",
                        Toast.LENGTH_SHORT
                ).show()
        );

        fila.add(requisicao);
    }
}