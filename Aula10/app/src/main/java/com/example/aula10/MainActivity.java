package com.example.aula10;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etCep;
    private MaterialButton btnSearch;
    private TextView tvResult;
    private ViaCepService viaCepService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCep = findViewById(R.id.etCep);
        btnSearch = findViewById(R.id.btnSearch);
        tvResult = findViewById(R.id.tvResult);

        // Usamos HTTP (sem o S) para contornar bloqueios de certificado em redes de faculdade.
        // O AndroidManifest ja esta configurado para aceitar Cleartext.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viaCepService = retrofit.create(ViaCepService.class);

        btnSearch.setOnClickListener(v -> {
            String cep = etCep.getText().toString().trim();
            if (cep.length() == 8) {
                buscarCep(cep);
            } else {
                Toast.makeText(this, "Digite um CEP com 8 numeros", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCep(String cep) {
        tvResult.setText("Buscando...");
        viaCepService.getEndereco(cep).enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CepResponse res = response.body();
                    if (res.getLogradouro() != null) {
                        tvResult.setText(res.getLogradouro());
                    } else {
                        tvResult.setText("CEP nao encontrado.");
                    }
                } else {
                    tvResult.setText("Erro na resposta da API.");
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                // Exibe o erro para facilitar o diagnostico na rede restrita
                tvResult.setText("Erro de Conexao: " + t.getMessage());
            }
        });
    }
}
