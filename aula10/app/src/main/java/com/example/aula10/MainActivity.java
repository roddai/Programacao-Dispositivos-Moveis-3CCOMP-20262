package com.example.aula10;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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

    private TextInputEditText etCep;
    private Button btnBuscar;
    private TextView tvLogradouro;
    private ViaCepService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCep = findViewById(R.id.etCep);
        btnBuscar = findViewById(R.id.btnBuscar);
        tvLogradouro = findViewById(R.id.tvLogradouro);

        // Configuração do Retrofit com cliente que ignora SSL para redes com Proxy/Faculdade
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ViaCepService.class);

        btnBuscar.setOnClickListener(v -> {
            String cepStr = etCep.getText().toString().trim();
            if (cepStr.length() == 8) {
                buscarCep(cepStr);
            } else {
                Toast.makeText(this, "Por favor, digite um CEP válido com 8 dígitos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCep(String cepStr) {
        tvLogradouro.setText("Buscando...");
        service.buscarCep(cepStr).enqueue(new Callback<Cep>() {
            @Override
            public void onResponse(Call<Cep> call, Response<Cep> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Cep cep = response.body();
                    if (cep.getLogradouro() != null && !cep.getLogradouro().isEmpty()) {
                        tvLogradouro.setText("Logradouro: " + cep.getLogradouro());
                    } else {
                        tvLogradouro.setText("CEP não encontrado.");
                    }
                } else {
                    tvLogradouro.setText("Erro na busca.");
                }
            }

            @Override
            public void onFailure(Call<Cep> call, Throwable t) {
                tvLogradouro.setText("Erro de conexão (SSL): Verifique o Wi-Fi.");
            }
        });
    }

    // Método para ignorar a validação de certificados SSL (Apenas para desenvolvimento)
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
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
