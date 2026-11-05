package com.example.aula08;

import android.app.assist.AssistStructure;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        EditText etNome = findViewById(R.id.etNome);
        EditText etIdade = findViewById(R.id.etIdade);
        CheckBox cbOpcao1 = findViewById(R.id.cbOpcao1);
        CheckBox cbOpcao2 = findViewById(R.id.cbOpcao2);
        CheckBox cbOpcao3 = findViewById(R.id.cbOpcao3);
        RadioButton rbP = findViewById(R.id.rbP);
        RadioButton rbM = findViewById(R.id.rbM);
        RadioButton rbG = findViewById(R.id.rbG);
        Spinner spinnerCores  = findViewById(R.id.spinnerCores);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        String[] cores = {"Vermelho", "Azul", "Verde", "Preto", "Cinza", "Branco"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCores.setAdapter(adapter);

        btnEnviar.setOnClickListener(v -> {

            String nome = etNome.getText().toString();
            String idade = etIdade.getText().toString();

            String opcoes = "";
            if (cbOpcao1.isChecked()) opcoes += "\nCamiseta ";
            if (cbOpcao2.isChecked()) opcoes += "\nCalça";
            if (cbOpcao3.isChecked()) opcoes += "\nJaqueta";

            String tamanho = rbP.isChecked() ? "P" : rbM.isChecked() ? "M" : rbG.isChecked() ? "G": "N/A";

            String corSelecionada = spinnerCores.getSelectedItem().toString();
            float avaliacao = ratingBar.getRating();

            String resultado = "Nome: " + nome +
                    "\nIdade: " + idade +
                    "\nOpções marcadas: " + opcoes +
                    "\nEscolha tamanho: " + tamanho +
                    "\nCor escolhida: " + corSelecionada +
                    "\nAvaliação: " + avaliacao;

            Intent intent = new Intent(FormActivity.this, ResultadoActivity.class);
            intent.putExtra("resultado", resultado);
            startActivity(intent);
        });
    }
}
