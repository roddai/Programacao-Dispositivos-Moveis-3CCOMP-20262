package com.example.aula11_paises_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editPais;
    private TextView textNome;
    private TextView textCapital;
    private TextView textRegiao;
    private TextView textPopulacao;
    private ImageView imageBandeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPais = findViewById(R.id.editPais);
        Button btnBuscarPais = findViewById(R.id.btnBuscarPais);
        textNome = findViewById(R.id.textNome);
        textCapital = findViewById(R.id.textCapital);
        textRegiao = findViewById(R.id.textRegiao);
        textPopulacao = findViewById(R.id.textPopulacao);
        imageBandeira = findViewById(R.id.imageBandeira);

        btnBuscarPais.setOnClickListener(v -> buscarPais());
    }

    private void buscarPais() {
        String pais = editPais.getText().toString().trim();

        if (pais.isEmpty()) {
            Toast.makeText(this, "Digite o nome de um país", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://restcountries.com/v3.1/name/" + pais;

        RequestQueue fila = Volley.newRequestQueue(this);

        JsonArrayRequest requisicao = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                resposta -> mostrarPais(resposta),
                erro -> Toast.makeText(
                        this,
                        "Erro ao buscar país",
                        Toast.LENGTH_SHORT
                ).show()
        );

        fila.add(requisicao);
    }

    private void mostrarPais(JSONArray resposta) {
        try {
            JSONObject pais = resposta.getJSONObject(0);

            String nome = pais
                    .getJSONObject("name")
                    .getString("common");

            JSONArray capitais = pais.getJSONArray("capital");
            String capital = capitais.getString(0);

            String regiao = pais.getString("region");
            long populacao = pais.getLong("population");

            String bandeira = pais
                    .getJSONObject("flags")
                    .getString("png");

            textNome.setText("Nome: " + nome);
            textCapital.setText("Capital: " + capital);
            textRegiao.setText("Região: " + regiao);
            textPopulacao.setText("População: " + populacao);

            Glide.with(this)
                    .load(bandeira)
                    .into(imageBandeira);

        } catch (Exception e) {
            Toast.makeText(this, "Erro nos dados do país", Toast.LENGTH_SHORT).show();
        }
    }
}