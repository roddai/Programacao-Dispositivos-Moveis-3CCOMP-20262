package com.example.projetoaula08;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity {
    private ImageView imageView;
    private int roupaSelecionada;
    private final int[] fotosRoupas = {
            R.drawable.camisa_branca, R.drawable.images, R.drawable.calca
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        EditText etNome = findViewById(R.id.etNome);
        EditText etQuantidade = findViewById(R.id.etQuantidade);
        CheckBox cbOpcao1 = findViewById(R.id.cbOpcao1);
        CheckBox cbOpcao2 = findViewById(R.id.cbOpcao2);
        RadioButton rbSim = findViewById(R.id.rbSim);
        Spinner spinnerRoupas = findViewById(R.id.spinnerRoupas);
        Spinner spinnerCores = findViewById(R.id.spinnerCores);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        imageView = findViewById(R.id.imageView);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.cores, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCores.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapterRoupas = ArrayAdapter.createFromResource(
                this, R.array.roupas, android.R.layout.simple_spinner_item);
        adapterRoupas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoupas.setAdapter(adapterRoupas);
        spinnerRoupas.setSelection(roupaSelecionada);
        imageView.setImageResource(fotosRoupas[roupaSelecionada]);
        spinnerRoupas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roupaSelecionada = position;
                imageView.setImageResource(fotosRoupas[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnEnviar.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            if (nome.isEmpty()) {
                etNome.setError(getString(R.string.nome_obrigatorio));
                etNome.requestFocus();
                return;
            }
            int quantidade;
            try {
                quantidade = Integer.parseInt(etQuantidade.getText().toString().trim());
                if (quantidade < 1 || quantidade > 9999) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                etQuantidade.setError(getString(R.string.quantidade_invalida));
                etQuantidade.requestFocus();
                return;
            }

            List<String> opcoes = new ArrayList<>();
            if (cbOpcao1.isChecked()) opcoes.add(getString(R.string.checkbox_opcao1));
            if (cbOpcao2.isChecked()) opcoes.add(getString(R.string.checkbox_opcao2));
            String caracteristicas = opcoes.isEmpty()
                    ? getString(R.string.sem_caracteristicas) : TextUtils.join(", ", opcoes);
            String nova = getString(rbSim.isChecked() ? R.string.radio_sim : R.string.radio_nao);
            String cor = spinnerCores.getSelectedItem().toString();
            float estrelas = ratingBar.getRating();
            String avaliacao = estrelas == 0 ? getString(R.string.nao_avaliada)
                    : getString(R.string.avaliacao_valor, estrelas);

            String resultado = getString(R.string.resumo, nome, quantidade,
                    caracteristicas, nova, cor, avaliacao, spinnerRoupas.getSelectedItem().toString());
            Intent intent = new Intent(FormActivity.this, ResultadoActivity.class);
            intent.putExtra("resultado", resultado);
            startActivity(intent);
        });
    }

}
