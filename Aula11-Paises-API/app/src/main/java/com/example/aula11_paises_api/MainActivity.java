package com.example.aula11_paises_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etPais;
    private Button btnBuscar;
    private TextView txtInfo;
    private ImageView imgBandeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPais = findViewById(R.id.etPais);
        btnBuscar = findViewById(R.id.btnBuscar);
        txtInfo = findViewById(R.id.txtInfo);
        imgBandeira = findViewById(R.id.imgBandeira);

        btnBuscar.setOnClickListener(v -> buscarPais());
    }

    private void buscarPais() {

        String pais = etPais.getText().toString().trim();

        if (pais.isEmpty()) {
            Toast.makeText(
                    this,
                    "Digite o nome de um país",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String url = "https://restcountries.com/v3.1/name/" + pais;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(() ->
                        txtInfo.setText("Erro de conexão com a API.")
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    String json = response.body().string();

                    JSONArray array = new JSONArray(json);

                    JSONObject obj = array.getJSONObject(0);

                    String nome = obj.getJSONObject("name")
                            .getString("common");

                    String capital = obj.getJSONArray("capital")
                            .getString(0);

                    String regiao = obj.getString("region");

                    long populacao = obj.getLong("population");

                    String bandeira = obj.getJSONObject("flags")
                            .getString("png");

                    runOnUiThread(() -> {

                        txtInfo.setText(
                                "🌎 País: " + nome +
                                        "\n\n🏛 Capital: " + capital +
                                        "\n\n📍 Região: " + regiao +
                                        "\n\n👥 População: " + populacao
                        );

                        Picasso.get()
                                .load(bandeira)
                                .into(imgBandeira);

                    });

                } catch (Exception e) {

                    runOnUiThread(() -> {
                        txtInfo.setText("País não encontrado.");
                        imgBandeira.setImageDrawable(null);
                    });

                }

            }
        });

    }
}