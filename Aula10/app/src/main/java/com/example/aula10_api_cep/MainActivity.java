package com.example.aula10_api_cep;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;
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

    private TextInputEditText etCepSearch, etCepResult, etLogradouro, etNumero, etComplemento, etBairro, etCidade, etUf;
    private Chip chipRegiao, chipDdd;
    private MaterialTextView tvError;
    private LinearProgressIndicator progressIndicator;
    private MaterialCardView cardResult;

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

        // Inicialização dos componentes
        etCepSearch = findViewById(R.id.etCepSearch);
        progressIndicator = findViewById(R.id.progressIndicator);
        cardResult = findViewById(R.id.cardResult);
        tvError = findViewById(R.id.tvError);

        // Campos do Formulário
        etCepResult = findViewById(R.id.etCepResult);
        etLogradouro = findViewById(R.id.etLogradouroResult);
        etNumero = findViewById(R.id.etNumero);
        etComplemento = findViewById(R.id.etComplemento);
        etBairro = findViewById(R.id.etBairroResult);
        etCidade = findViewById(R.id.etCidadeResult);
        etUf = findViewById(R.id.etUfResult);
        chipRegiao = findViewById(R.id.chipRegiao);
        chipDdd = findViewById(R.id.chipDdd);

        // Esconder o card inicialmente
        cardResult.setVisibility(View.GONE);

        etCepSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                validarEBuscar();
                return true;
            }
            return false;
        });

        // Adiciona o listener para buscar automaticamente ao digitar o 8º número
        etCepSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 8) {
                    validarEBuscar();
                }
            }
        });
    }

    private void validarEBuscar() {
        String cep = etCepSearch.getText().toString().trim();
        if (cep.length() == 8) {
            esconderTeclado();
            buscarCep(cep);
        } else {
            etCepSearch.setError("Digite um CEP válido com 8 números");
            etCepSearch.requestFocus();
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
        tvError.setText("");
        tvError.setVisibility(View.GONE);
        cardResult.setVisibility(View.GONE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                // Alterado para HTTP para evitar problemas de certificado SSL em emuladores
                URL url = new URL("http://viacep.com.br/ws/" + cep + "/json/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

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

                        try {
                            if (jsonObject.has("erro") && jsonObject.getBoolean("erro")) {
                                tvError.setText("CEP não encontrado.");
                                tvError.setVisibility(View.VISIBLE);
                            } else {
                                // Preenchimento do Formulário
                                etCepResult.setText(jsonObject.optString("cep", ""));
                                etLogradouro.setText(jsonObject.optString("logradouro", ""));
                                etBairro.setText(jsonObject.optString("bairro", ""));
                                etCidade.setText(jsonObject.optString("localidade", ""));
                                etUf.setText(jsonObject.optString("uf", ""));
                                chipRegiao.setText("Região: " + jsonObject.optString("regiao", ""));
                                chipDdd.setText("DDD: " + jsonObject.optString("ddd", ""));

                                // Limpar campos manuais
                                etNumero.setText("");
                                etComplemento.setText("");

                                cardResult.setVisibility(View.VISIBLE);
                                etNumero.requestFocus();
                            }
                        } catch (Exception e) {
                            tvError.setText("Erro ao processar resposta.");
                            tvError.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    handler.post(() -> {
                        progressIndicator.setVisibility(View.GONE);
                        tvError.setText("Erro no servidor: " + responseCode);
                        tvError.setVisibility(View.VISIBLE);
                    });
                }
            } catch (Exception e) {
                Log.e("CEP_ERROR", "Erro de conexão", e);
                handler.post(() -> {
                    progressIndicator.setVisibility(View.GONE);
                    tvError.setText("Falha na conexão.");
                    tvError.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Verifique sua internet", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
