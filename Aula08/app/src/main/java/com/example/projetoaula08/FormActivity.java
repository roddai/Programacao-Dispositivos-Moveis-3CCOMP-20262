package com.example.projetoaula08;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        EditText etNome = findViewById(R.id.etNome);
        EditText etPreco = findViewById(R.id.etPreco);
        RadioButton rbP = findViewById(R.id.rbP);
        RadioButton rbM = findViewById(R.id.rbM);
        RadioButton rbG = findViewById(R.id.rbG);
        CheckBox cbCamiseta = findViewById(R.id.cbCamiseta);
        CheckBox cbCalca = findViewById(R.id.cbCalca);
        CheckBox cbJaqueta = findViewById(R.id.cbJaqueta);
        Spinner spinnerCores = findViewById(R.id.spinnerCores);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        String[] cores = {
                "Azul",
                "Preto",
                "Branco",
                "Vermelho",
                "Verde"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        cores);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerCores.setAdapter(adapter);

        btnEnviar.setOnClickListener(v -> {

            String nome = etNome.getText().toString();

            String preco = etPreco.getText().toString();

            String tamanho = "";

            if (rbP.isChecked()) {
                tamanho = "P";
            }

            if (rbM.isChecked()) {
                tamanho = "M";
            }

            if (rbG.isChecked()) {
                tamanho = "G";
            }

            String tipos = "";

            if (cbCamiseta.isChecked()) {
                tipos += "Camiseta ";
            }

            if (cbCalca.isChecked()) {
                tipos += "Calça ";
            }

            if (cbJaqueta.isChecked()) {
                tipos += "Jaqueta ";
            }

            String cor =
                    spinnerCores.getSelectedItem().toString();

            float avaliacao =
                    ratingBar.getRating();

            String resultado =
                    "Nome da peça: " + nome +
                            "\nPreço: R$ " + preco +
                            "\nTamanho: " + tamanho +
                            "\nCor: " + cor +
                            "\nTipos: " + tipos +
                            "\nAvaliação: " + avaliacao;

            Intent intent =
                    new Intent(
                            FormActivity.this,
                            ResultadoActivity.class);

            intent.putExtra(
                    "resultado",
                    resultado);

            startActivity(intent);

        });

    }
}