package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilCep;
    private TextInputEditText etCep, etNumero, etComplemento;
    private MaterialButton btnSearch;
    private TextView tvResult;
    private MaterialCardView cardResult;
    private LinearProgressIndicator progressIndicator;
    
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização dos componentes Material
        tilCep = findViewById(R.id.tilCep);
        etCep = findViewById(R.id.etCep);
        etNumero = findViewById(R.id.etNumero);
        etComplemento = findViewById(R.id.etComplemento);
        btnSearch = findViewById(R.id.btnSearch);
        tvResult = findViewById(R.id.tvResult);
        cardResult = findViewById(R.id.cardResult);
        progressIndicator = findViewById(R.id.progressIndicator);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = etCep.getText().toString().trim();
                String numero = etNumero.getText().toString().trim();
                String complemento = etComplemento.getText().toString().trim();
                
                if (validateCep(cep)) {
                    hideKeyboard();
                    searchCep(cep, numero, complemento);
                }
            }
        });
    }

    private boolean validateCep(String cep) {
        tilCep.setError(null); // Limpa erro anterior
        
        if (cep.isEmpty()) {
            tilCep.setError("Campo obrigatório");
            return false;
        }
        if (cep.length() != 8) {
            tilCep.setError("O CEP deve ter 8 dígitos");
            return false;
        }
        return true;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchCep(String cep, String numeroDigitado, String complementoDigitado) {
        // Estado inicial da busca
        progressIndicator.setVisibility(View.VISIBLE);
        cardResult.setVisibility(View.GONE);
        btnSearch.setEnabled(false);
        
        executorService.execute(() -> {
            String resultText;
            boolean success = false;
            
            try {
                // Usando http para evitar problemas de SSL em ambientes de rede restritos
                URL url = new URL("http://viacep.com.br/ws/" + cep + "/json/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    
                    if (jsonResponse.has("erro") && jsonResponse.getBoolean("erro")) {
                        resultText = "CEP não encontrado na base de dados.";
                    } else {
                        success = true;
                        String logradouro = jsonResponse.optString("logradouro", "Não informado");
                        String bairro = jsonResponse.optString("bairro", "Não informado");
                        String localidade = jsonResponse.optString("localidade", "");
                        String uf = jsonResponse.optString("uf", "");
                        String regiao = jsonResponse.optString("regiao", "Não informada");
                        String ddd = jsonResponse.optString("ddd", "Não informado");
                        
                        StringBuilder sb = new StringBuilder();
                        sb.append("📍 Logradouro: ").append(logradouro);
                        if (!numeroDigitado.isEmpty()) {
                            sb.append(", nº ").append(numeroDigitado);
                        }
                        
                        if (!complementoDigitado.isEmpty()) {
                            sb.append("\n📝 Complemento: ").append(complementoDigitado);
                        } else {
                            String compApi = jsonResponse.optString("complemento", "");
                            if (!compApi.isEmpty()) {
                                sb.append("\n📝 Complemento: ").append(compApi);
                            }
                        }
                        
                        sb.append("\n🏠 Bairro: ").append(bairro)
                          .append("\n🌆 Cidade: ").append(localidade).append(" - ").append(uf)
                          .append("\n🌍 Região: ").append(regiao)
                          .append("\n📞 DDD: ").append(ddd);
                        
                        resultText = sb.toString();
                    }
                } else {
                    resultText = "Erro no servidor (Código: " + responseCode + ")";
                }
            } catch (Exception e) {
                resultText = "Falha na conexão. Verifique sua internet.";
                e.printStackTrace();
            }

            final String finalResult = resultText;
            final boolean finalSuccess = success;
            
            runOnUiThread(() -> {
                progressIndicator.setVisibility(View.GONE);
                btnSearch.setEnabled(true);
                
                tvResult.setText(finalResult);
                cardResult.setVisibility(View.VISIBLE);
                
                if (!finalSuccess) {
                    tilCep.setError("Erro na consulta");
                }
            });
        });
    }
}