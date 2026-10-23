package com.example.aula10api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText etCep;
    private MaterialButton btnBuscaCep;
    private TextView tvResultado;
    private TextView tvLabelLogradouro;
    private LinearProgressIndicator progressIndicator;
    private MaterialCardView cardResultado;

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
        tvResultado = findViewById(R.id.tvResultado);
        tvLabelLogradouro = findViewById(R.id.tvLabelLogradouro);
        progressIndicator = findViewById(R.id.progressIndicator);
        cardResultado = findViewById(R.id.cardResultado);

        btnBuscaCep.setOnClickListener(v -> {
            String cep = etCep.getText().toString().trim();
            if (cep.length() == 8) {
                buscarCepComBypass(cep);
            } else {
                Toast.makeText(this, "Digite um CEP válido (8 dígitos)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCepComBypass(String cep) {
        runOnUiThread(() -> {
            progressIndicator.setVisibility(View.VISIBLE);
            cardResultado.setVisibility(View.GONE);
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                // BYPASS: Usando HTTP simples e cabeçalhos de navegador real para evitar bloqueios de proxy/firewall
                // HTTPS é mais propenso a inspeção de pacotes e bloqueio de certificados em redes acadêmicas.
                URL url = new URL("http://viacep.com.br/ws/" + cep + "/json/");
                conn = (HttpURLConnection) url.openConnection();
                
                conn.setRequestMethod("GET");
                // Mascara a requisição como vinda de um navegador real
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
                conn.setRequestProperty("Accept", "application/json");
                
                // Redes de faculdade costumam ter latência alta; aumentamos o timeout para 15 segundos.
                conn.setConnectTimeout(15000); 
                conn.setReadTimeout(15000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JSONObject jsonObject = new JSONObject(sb.toString());
                    if (jsonObject.has("erro")) {
                        updateUI("CEP não encontrado na base de dados oficial.", false);
                    } else {
                        String logradouro = jsonObject.optString("logradouro", "Logradouro não informado");
                        updateUI(logradouro, true);
                    }
                } else {
                    updateUI("A rede restringiu o acesso ao servidor (Código: " + responseCode + ").", false);
                }

            } catch (Exception e) {
                Log.e("API_CEP", "Erro ao conectar", e);
                updateUI("Falha total na conexão. O firewall da instituição está impedindo a comunicação.", false);
            } finally {
                if (conn != null) conn.disconnect();
            }
        });
    }

    private void updateUI(String mensagem, boolean sucesso) {
        runOnUiThread(() -> {
            progressIndicator.setVisibility(View.INVISIBLE);
            cardResultado.setVisibility(View.VISIBLE);
            
            if (sucesso) {
                tvLabelLogradouro.setText("Logradouro encontrado:");
                tvLabelLogradouro.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                tvResultado.setText(mensagem);
                tvResultado.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            } else {
                tvLabelLogradouro.setText("Erro de Conexão:");
                tvLabelLogradouro.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                tvResultado.setText(mensagem);
                tvResultado.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            }
        });
    }
}
