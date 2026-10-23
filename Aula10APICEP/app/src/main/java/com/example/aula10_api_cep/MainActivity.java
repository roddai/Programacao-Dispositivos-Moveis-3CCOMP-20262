package com.example.aula10_api_cep;

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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText etCepBusca;
    private MaterialButton btnBuscaCep;
    private LinearProgressIndicator progressIndicator;
    private MaterialCardView cardResultado;

    // Blocos de resultado editáveis (TextInputEditText)
    private TextView tvLabelHeader;
    private TextInputEditText etResCep, etResLogradouro, etResBairro, etResCidade, etResUf, etResRegiao, etResDdd;
    private TextInputEditText etNumero, etComplemento;

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

        // Inicialização dos campos de busca
        etCepBusca = findViewById(R.id.etCepBusca);
        btnBuscaCep = findViewById(R.id.btnBuscaCep);
        progressIndicator = findViewById(R.id.progressIndicator);
        cardResultado = findViewById(R.id.cardResultado);
        tvLabelHeader = findViewById(R.id.tvLabelHeader);

        // Inicialização dos campos do Card (conforme os IDs definidos no XML)
        etResCep = findViewById(R.id.etResCep);
        etResLogradouro = findViewById(R.id.etResLogradouro);
        etResBairro = findViewById(R.id.etResBairro);
        etResCidade = findViewById(R.id.etResCidade);
        etResUf = findViewById(R.id.etResUf);
        etResRegiao = findViewById(R.id.etResRegiao);
        etResDdd = findViewById(R.id.etResDdd);
        etNumero = findViewById(R.id.etNumero);
        etComplemento = findViewById(R.id.etComplemento);

        btnBuscaCep.setOnClickListener(v -> {
            String cep = etCepBusca.getText().toString().trim();
            if (cep.length() == 8) {
                buscarCepComBypass(cep);
            } else {
                Toast.makeText(this, "Digite um CEP válido com 8 dígitos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCepComBypass(String cep) {
        runOnUiThread(() -> {
            progressIndicator.setVisibility(View.VISIBLE);
            cardResultado.setVisibility(View.GONE);
            // Limpa campos de preenchimento manual
            etNumero.setText("");
            etComplemento.setText("");
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                // Bypass via HTTP para ambiente restrito
                URL url = new URL("http://viacep.com.br/ws/" + cep + "/json/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(15000); 
                conn.setReadTimeout(15000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    if (json.has("erro")) {
                        updateUIError("CEP não encontrado.");
                    } else {
                        updateUISucesso(
                            json.optString("cep", cep),
                            json.optString("logradouro", ""),
                            json.optString("bairro", ""),
                            json.optString("localidade", ""),
                            json.optString("uf", ""),
                            json.optString("regiao", ""),
                            json.optString("ddd", "")
                        );
                    }
                } else {
                    updateUIError("Rede bloqueada (Código: " + responseCode + ")");
                }
            } catch (Exception e) {
                Log.e("API_CEP", "Erro", e);
                updateUIError("Falha de conexão com o servidor.");
            } finally {
                if (conn != null) conn.disconnect();
            }
        });
    }

    private void updateUISucesso(String cep, String log, String bairro, String cid, String uf, String reg, String ddd) {
        runOnUiThread(() -> {
            progressIndicator.setVisibility(View.INVISIBLE);
            cardResultado.setVisibility(View.VISIBLE);
            
            tvLabelHeader.setText("Endereço Encontrado");
            tvLabelHeader.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));

            // Preenche os campos editáveis
            etResCep.setText(cep);
            etResLogradouro.setText(log);
            etResBairro.setText(bairro);
            etResCidade.setText(cid);
            etResUf.setText(uf);
            etResRegiao.setText(reg);
            etResDdd.setText(ddd);
        });
    }

    private void updateUIError(String mensagem) {
        runOnUiThread(() -> {
            progressIndicator.setVisibility(View.INVISIBLE);
            cardResultado.setVisibility(View.VISIBLE);
            
            tvLabelHeader.setText("Erro na busca");
            tvLabelHeader.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            
            etResCep.setText(mensagem);
            etResLogradouro.setText("");
            etResBairro.setText("");
            etResCidade.setText("");
            etResUf.setText("");
            etResRegiao.setText("");
            etResDdd.setText("");
        });
    }
}