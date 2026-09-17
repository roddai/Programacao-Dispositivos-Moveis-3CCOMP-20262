package com.example.aula06desafio;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aula06desafio.R;

public class DetalhesActivity extends AppCompatActivity {

    private TextView textoNome;
    private TextView textoCor;
    private TextView textoRaca;
    private TextView textoIdade;
    private TextView textoAniversario;
    private ImageView imgPet;
    Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        textoNome = findViewById(R.id.textoNome);
        textoCor = findViewById(R.id.textoCor);
        textoRaca = findViewById(R.id.textoRaca);
        textoIdade = findViewById(R.id.textoIdade);
        textoAniversario = findViewById(R.id.textoAniversario);
        imgPet = findViewById(R.id.imgPet);
        btnVoltar = findViewById(R.id.btnVoltar);

        String nomeRecebido = getIntent().getStringExtra("nome");
        String corRecebida = getIntent().getStringExtra("cor");
        String racaRecebida = getIntent().getStringExtra("raca");
        int idadeRecebida = getIntent().getIntExtra("idade", 0);
        String aniversarioRecebido = getIntent().getStringExtra("aniversario");
        int imagemRecebida = getIntent().getIntExtra("fotoPet", 0);

        textoNome.setText("Nome " + nomeRecebido);
        textoCor.setText("Cor:" + corRecebida);
        textoRaca.setText("Raça:" + racaRecebida);
        textoIdade.setText("Idade:" + idadeRecebida);
        textoAniversario.setText("Aniversário:" + aniversarioRecebido);
        if (imagemRecebida != 0) {
            imgPet.setImageResource(imagemRecebida);
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(DetalhesActivity.this, MainActivity.class);
                startActivity(voltar);
            }
        });
    }
}
