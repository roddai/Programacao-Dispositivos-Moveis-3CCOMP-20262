package com.example.aula10;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCep;
    private Button buttonBuscar;
    private TextView textViewLogradouro;
    private ViaCepService viaCepService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCep = findViewById(R.id.editTextCep);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        textViewLogradouro = findViewById(R.id.textViewLogradouro);

        // Configura Retrofit com um cliente OkHttp que ignora erros de SSL (comum em redes de faculdade)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viaCepService = retrofit.create(ViaCepService.class);

        buttonBuscar.setOnClickListener(v -> {
            String cep = editTextCep.getText().toString();
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
                    CepResponse cepResponse = response.body();
                    if (cepResponse.getLogradouro() != null) {
                        textViewLogradouro.setText(cepResponse.getLogradouro());
                    } else {
                        textViewLogradouro.setText("CEP não encontrado");
                    }
                } else {
                    textViewLogradouro.setText("Erro na consulta");
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                textViewLogradouro.setText("Erro de conexão: " + t.getMessage());
            }
        });
    }

    // Método para ignorar a verificação de certificados SSL
    // Apenas para ambiente de desenvolvimento/estudo em redes restritas
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
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
