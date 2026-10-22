package com.example.aula10_api_cep;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etCep;
    private MaterialButton btnBuscaCep;
    private MaterialTextView tvLogradouro;
    private CircularProgressIndicator progressIndicator;

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
        btnBuscaCep = findViewById(R.id.btnBuscaCep);
        tvLogradouro = findViewById(R.id.tvLogradouro);
        progressIndicator = findViewById(R.id.progressIndicator);

        btnBuscaCep.setOnClickListener(v -> validarEBuscar());

        etCep.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                validarEBuscar();
                return true;
            }
            return false;
        });
    }

    private void validarEBuscar() {
        String cep = etCep.getText().toString().trim();
        if (cep.length() == 8) {
            esconderTeclado();
            buscarCep(cep);
        } else {
            etCep.setError("Digite um CEP válido com 8 números");
            etCep.requestFocus();
        }
    }

    private void esconderTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void buscarCep(String cep) {
        progressIndicator.setVisibility(View.VISIBLE);
        tvLogradouro.setText("Buscando endereço...");
        btnBuscaCep.setEnabled(false);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                // Tentando HTTP em vez de HTTPS para evitar problemas de certificado em proxies de faculdade
                // Já habilitamos usesCleartextTraffic="true" no Manifest para isso
                URL url = new URL("http://viacep.com.br/ws/" + cep + "/json/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                
                // Cabeçalhos para simular um navegador e evitar bloqueios simples de firewall
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                conn.setRequestProperty("Accept", "application/json");
                
                // Timeouts agressivos para redes instáveis
                conn.setConnectTimeout(25000);
                conn.setReadTimeout(25000);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    
                    handler.post(() -> {
                        progressIndicator.setVisibility(View.GONE);
                        btnBuscaCep.setEnabled(true);
                        
                        try {
                            if (jsonObject.has("erro") && jsonObject.getBoolean("erro")) {
                                tvLogradouro.setText("CEP não encontrado.");
                            } else {
                                String logradouro = jsonObject.optString("logradouro", "");
                                String localidade = jsonObject.optString("localidade", "");
                                String uf = jsonObject.optString("uf", "");

                                if (logradouro.isEmpty()) {
                                    tvLogradouro.setText(String.format("%s - %s\n(CEP Geral da Cidade)", localidade, uf));
                                } else {
                                    tvLogradouro.setText(logradouro + "\n" + localidade + " - " + uf);
                                }
                            }
                        } catch (Exception e) {
                            tvLogradouro.setText("Erro ao processar resposta.");
                        }
                    });
                } else {
                    handler.post(() -> {
                        progressIndicator.setVisibility(View.GONE);
                        btnBuscaCep.setEnabled(true);
                        tvLogradouro.setText("Erro HTTP: " + responseCode);
                    });
                }
            } catch (Exception e) {
                Log.e("CEP_ERROR", "Erro de conexão", e);
                String errorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
                handler.post(() -> {
                    progressIndicator.setVisibility(View.GONE);
                    btnBuscaCep.setEnabled(true);
                    // Mostramos o tipo do erro para diagnosticar (ex: SocketTimeoutException, UnknownHostException)
                    tvLogradouro.setText("Falha na conexão:\n" + errorMessage);
                    Toast.makeText(MainActivity.this, "Erro de Rede. Tente outro Wi-Fi ou 4G.", Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}