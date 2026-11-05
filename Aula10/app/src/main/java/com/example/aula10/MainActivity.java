package com.example.aula10;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText editCep, editLogradouro, editNumero, editComplemento, editBairro, editCidade, editUf, editRegiao, editDdd;
    private Button btnBuscar;
    private static final String TAG = "BuscaCEP";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Inicialização de todos os campos
        editCep = findViewById(R.id.edit_cep);
        btnBuscar = findViewById(R.id.btn_buscar);
        editLogradouro = findViewById(R.id.edit_logradouro);
        editNumero = findViewById(R.id.edit_numero);
        editComplemento = findViewById(R.id.edit_complemento);
        editBairro = findViewById(R.id.edit_bairro);
        editCidade = findViewById(R.id.edit_cidade);
        editUf = findViewById(R.id.edit_uf);
        editRegiao = findViewById(R.id.edit_regiao);
        editDdd = findViewById(R.id.edit_ddd);

        // Habilita a seleção de todo o texto ao clicar ou ganhar foco no CEP
        editCep.setSelectAllOnFocus(true);
        editCep.setOnClickListener(v -> editCep.selectAll());

        btnBuscar.setOnClickListener(v -> {
            String rawInput = editCep.getText().toString().trim();
            String input = rawInput.replaceAll("[^0-9]", "");
            
            if (input.length() == 8) {
                buscarCep(input);
            } else {
                Toast.makeText(this, "O CEP deve ter 8 números", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCep(String cep) {
        executorService.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://viacep.com.br/ws/" + cep + "/json/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) response.append(line);
                    in.close();

                    JSONObject json = new JSONObject(response.toString());
                    
                    if (json.has("erro")) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show());
                    } else {
                        // Extração de todos os dados da API
                        String logradouro = json.optString("logradouro", "");
                        String bairro = json.optString("bairro", "");
                        String cidade = json.optString("localidade", "");
                        String uf = json.optString("uf", "");
                        String regiao = json.optString("regiao", "");
                        String ddd = json.optString("ddd", "");

                        runOnUiThread(() -> {
                            // Autocomplete dos campos
                            editLogradouro.setText(logradouro);
                            editBairro.setText(bairro);
                            editCidade.setText(cidade);
                            editUf.setText(uf);
                            editRegiao.setText(regiao);
                            editDdd.setText(ddd);
                            
                            // Coloca o foco no campo Número para o usuário continuar o preenchimento
                            editNumero.requestFocus();
                        });
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Falha na busca", e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}