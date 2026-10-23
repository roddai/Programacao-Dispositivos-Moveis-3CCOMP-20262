package com.example.aula10;

import android.os.Bundle;
import android.widget.Button;
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

    private TextInputEditText etCep, tvCepResult, tvLogradouro, tvComplemento, tvBairro, tvLocalidade, tvUf, tvEstado, tvRegiao, tvDdd;
    private Button btnBuscar;
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

        // Inicialização dos campos
        etCep = findViewById(R.id.etCep);
        btnBuscar = findViewById(R.id.btnBuscar);
        
        tvCepResult = findViewById(R.id.tvCepResult);
        tvLogradouro = findViewById(R.id.tvLogradouro);
        tvComplemento = findViewById(R.id.tvComplemento);
        tvBairro = findViewById(R.id.tvBairro);
        tvLocalidade = findViewById(R.id.tvLocalidade);
        tvUf = findViewById(R.id.tvUf);
        tvEstado = findViewById(R.id.tvEstado);
        tvRegiao = findViewById(R.id.tvRegiao);
        tvDdd = findViewById(R.id.tvDdd);

        // Retrofit com SSL inseguro para rede da faculdade
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
                Toast.makeText(this, "CEP inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCep(String cepStr) {
        limparCampos();
        service.buscarCep(cepStr).enqueue(new Callback<Cep>() {
            @Override
            public void onResponse(Call<Cep> call, Response<Cep> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Cep cep = response.body();
                    if (cep.getLogradouro() != null) {
                        exibirDados(cep, cepStr);
                    } else {
                        Toast.makeText(MainActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Cep> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exibirDados(Cep cep, String cepOriginal) {
        tvCepResult.setText(cepOriginal);
        tvLogradouro.setText(cep.getLogradouro());
        tvComplemento.setText(cep.getComplemento());
        tvBairro.setText(cep.getBairro());
        tvLocalidade.setText(cep.getLocalidade());
        tvUf.setText(cep.getUf());
        tvEstado.setText(cep.getEstado());
        tvRegiao.setText(cep.getRegiao());
        tvDdd.setText(cep.getDdd());
    }

    private void limparCampos() {
        tvCepResult.setText("");
        tvLogradouro.setText("");
        tvComplemento.setText("");
        tvBairro.setText("");
        tvLocalidade.setText("");
        tvUf.setText("");
        tvEstado.setText("");
        tvRegiao.setText("");
        tvDdd.setText("");
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
