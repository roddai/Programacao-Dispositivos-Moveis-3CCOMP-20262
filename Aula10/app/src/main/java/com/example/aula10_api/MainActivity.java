package com.example.aula10_api;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etCep;
    private MaterialButton btnBuscaCep;
    private TextView tvLogradouro;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referenciando os componentes Material
        etCep = findViewById(R.id.etCep);
        btnBuscaCep = findViewById(R.id.btnBuscaCep);
        tvLogradouro = findViewById(R.id.tvLogradouro);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnBuscaCep.setOnClickListener(v -> {
            String cep = etCep.getText().toString();
            if (cep.length() == 8) {
                buscarEndereco(cep);
            } else {
                etCep.setError("CEP deve ter 8 dígitos");
            }
        });
    }

    private void buscarEndereco(String cep) {
        ViaCepService service = retrofit.create(ViaCepService.class);
        Call<Cep> call = service.buscarCep(cep);

        call.enqueue(new Callback<Cep>() {
            @Override
            public void onResponse(Call<Cep> call, Response<Cep> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Cep resultado = response.body();
                    if (resultado.getLogradouro() != null) {
                        tvLogradouro.setText(resultado.getLogradouro());
                    } else {
                        tvLogradouro.setText("CEP não encontrado.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Cep> call, Throwable t) {
                // Imprime o erro real no Logcat para você depurar
                android.util.Log.e("API_ERROR", "Falha: " + t.getMessage());

                Toast.makeText(MainActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}