package com.example.aula10_api;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

    private TextInputEditText etCep, etLogradouro, etBairro, etCidade, etUf, etRegiao, etDdd;
    private MaterialButton btnBuscaCep;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização
        etCep = findViewById(R.id.etCep);
        btnBuscaCep = findViewById(R.id.btnBuscaCep);

        // Novos campos editáveis
        etLogradouro = findViewById(R.id.etLogradouro);
        etBairro = findViewById(R.id.etBairro);
        etCidade = findViewById(R.id.etCidade);
        etUf = findViewById(R.id.etUf);
        etRegiao = findViewById(R.id.etRegiao);
        etDdd = findViewById(R.id.etDdd);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        etCep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnBuscaCep.setVisibility(s.length() == 8 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnBuscaCep.setOnClickListener(v -> buscarEndereco(etCep.getText().toString()));
    }

    private void buscarEndereco(String cep) {
        ViaCepService service = retrofit.create(ViaCepService.class);
        Call<Cep> call = service.buscarCep(cep);

        call.enqueue(new Callback<Cep>() {
            @Override
            public void onResponse(Call<Cep> call, Response<Cep> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Dentro do onResponse do Retrofit:
                    Cep res = response.body();
                    etLogradouro.setText(res.getLogradouro());
                    etBairro.setText(res.getBairro());
                    etCidade.setText(res.getLocalidade());
                    etUf.setText(res.getUf());
                    etRegiao.setText(res.getRegiao());
                    etDdd.setText(res.getDdd());
                }else {
                    Toast.makeText(MainActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cep> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}