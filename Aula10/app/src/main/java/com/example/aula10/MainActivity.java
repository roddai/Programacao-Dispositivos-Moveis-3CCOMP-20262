package com.example.aula10;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCepBusca;
    private Button buttonBuscar;
    private NestedScrollView nestedScrollView;
    
    // Campos de resultado
    private EditText editResCep, editResLogradouro, editResComplemento, editResBairro, 
                     editResLocalidade, editResUf, editResRegiao, editResDdd, editResEstado, editResNumero;

    private ViaCepService viaCepService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização dos componentes de busca
        editTextCepBusca = findViewById(R.id.editTextCep);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        // Inicialização dos componentes de resultado
        editResCep = findViewById(R.id.editTextCepResult);
        editResLogradouro = findViewById(R.id.editTextLogradouro);
        editResNumero = findViewById(R.id.editTextNumero);
        editResComplemento = findViewById(R.id.editTextComplemento);
        editResBairro = findViewById(R.id.editTextBairro);
        editResLocalidade = findViewById(R.id.editTextLocalidade);
        editResUf = findViewById(R.id.editTextUf);
        editResRegiao = findViewById(R.id.editTextRegiao);
        editResDdd = findViewById(R.id.editTextDdd);
        editResEstado = findViewById(R.id.editTextEstado);

        // Configura Retrofit com cliente inseguro para redes restritas
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viaCepService = retrofit.create(ViaCepService.class);

        buttonBuscar.setOnClickListener(v -> {
            String cep = editTextCepBusca.getText().toString();
            if (cep.length() == 8) {
                buscarCep(cep);
            } else {
                Toast.makeText(MainActivity.this, "Digite um CEP válido com 8 dígitos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCep(String cep) {
        Call<CepResponse> call = viaCepService.getEndereco(cep);
        call.enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CepResponse res = response.body();
                    
                    if (res.getLogradouro() != null || res.getLocalidade() != null) {
                        // Preenche apenas campos que o usuário não deve editar
                        editResCep.setText(cep);
                        editResLogradouro.setText(res.getLogradouro());
                        editResBairro.setText(res.getBairro());
                        editResLocalidade.setText(res.getLocalidade());
                        editResUf.setText(res.getUf());
                        editResRegiao.setText(res.getRegiao());
                        editResDdd.setText(res.getDdd());
                        editResEstado.setText(res.getEstado());

                        // Limpa os campos de Número e Complemento para o usuário digitar manualmente
                        editResNumero.setText("");
                        editResComplemento.setText("");

                        // Mostra a área de resultados
                        nestedScrollView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(MainActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                        nestedScrollView.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Erro na consulta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
