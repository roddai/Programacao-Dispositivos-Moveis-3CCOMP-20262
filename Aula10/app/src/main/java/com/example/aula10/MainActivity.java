package com.example.aula10;

import android.os.Bundle;
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
    private ViaCepService viaCepService;

    // Ajustado para TextInputEditText para bater com o seu novo layout
    private TextInputEditText etLogradouro, etComplemento, etBairro, etLocalidade, etUf, etEstado, etRegiao, etDdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização dos campos de entrada
        etCep = findViewById(R.id.etCep);
        btnSearch = findViewById(R.id.btnSearch);

        // Inicialização correta usando os IDs "et..." do seu XML
        etLogradouro = findViewById(R.id.etLogradouro);
        etComplemento = findViewById(R.id.etComplemento);
        etBairro = findViewById(R.id.etBairro);
        etLocalidade = findViewById(R.id.etLocalidade);
        etUf = findViewById(R.id.etUf);
        etEstado = findViewById(R.id.etEstado);
        etRegiao = findViewById(R.id.etRegiao);
        etDdd = findViewById(R.id.etDdd);

        // Configuração para rede restrita da faculdade (HTTP)
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
                Toast.makeText(this, getString(R.string.error_cep_invalido), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCep(String cep) {
        setAllFieldsText(getString(R.string.msg_buscando));

        viaCepService.getEndereco(cep).enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CepResponse res = response.body();
                    if (res.getLogradouro() != null) {
                        // Preenche cada caixa de texto com o resultado
                        etLogradouro.setText(res.getLogradouro());
                        etComplemento.setText(res.getComplemento() != null && !res.getComplemento().isEmpty() ? res.getComplemento() : "");
                        etBairro.setText(res.getBairro());
                        etLocalidade.setText(res.getLocalidade());
                        etUf.setText(res.getUf());
                        etEstado.setText(res.getEstado());
                        etRegiao.setText(res.getRegiao());
                        etDdd.setText(res.getDdd());
                    } else {
                        setAllFieldsText(getString(R.string.error_nao_encontrado));
                    }
                } else {
                    setAllFieldsText(getString(R.string.error_api));
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                setAllFieldsText("Erro de Rede: Verifique a conexão da faculdade");
            }
        });
    }

    // Método auxiliar para limpar ou avisar erro em todos os campos
    private void setAllFieldsText(String text) {
        etLogradouro.setText(text);
        etComplemento.setText("");
        etBairro.setText("");
        etLocalidade.setText("");
        etUf.setText("");
        etEstado.setText("");
        etRegiao.setText("");
        etDdd.setText("");
    }
}