package com.example.aula08_escolhaderoupas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class EscolhaActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha);

        EditText editNome = findViewById(R.id.editNome);
        EditText editIdade = findViewById(R.id.editIdade);
        RadioButton rbP = findViewById(R.id.rbP);
        RadioButton rbM = findViewById(R.id.rbM);
        RadioButton rbG = findViewById(R.id.rbG);
        Spinner spinnerColor  = findViewById(R.id.spinnerColor);
        CheckBox cbCamisa  = findViewById(R.id.cbCamisa);
        CheckBox cbCalca  = findViewById(R.id.cbCalca);
        CheckBox cbJaqueta  = findViewById(R.id.cbJaqueta);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        String[] cores = {"Vermelho", "Azul", "Verde", "Amarelo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapter);

        btnEnviar.setOnClickListener(v -> {

            String nome = editNome.getText().toString();
            String idade = editIdade.getText().toString();

            String opcoes = "";
            if (cbCamisa.isChecked()) opcoes += "Camisa";
            if (cbCalca.isChecked()) opcoes += "Calça";
            if (cbJaqueta.isChecked()) opcoes += "Jaqueta";

            String simNao = rbP.isChecked() ? "P" : rbM.isChecked() ? "M" : rbG.isChecked() ? "G" : "N/A";

            String corSelecionada = spinnerColor.getSelectedItem().toString();
            float avaliacao = ratingBar.getRating();

            String resultado = "Nome: " + nome +
                    "\nIdade: " + idade +
                    "\nRoupas marcadas: " + opcoes +
                    "\nEscolha Tamanho: " + simNao +
                    "\nCor escolhida: " + corSelecionada +
                    "\nAvaliação: " + avaliacao;

            Intent intent = new Intent(EscolhaActivity.this, ResultadoActivity.class);
            intent.putExtra("resultado", resultado);
            startActivity(intent);
        });



    }
}
