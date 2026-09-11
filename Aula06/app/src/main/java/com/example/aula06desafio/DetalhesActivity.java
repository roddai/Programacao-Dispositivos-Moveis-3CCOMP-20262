package com.example.aula06desafio;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        textoNome = findViewById(R.id.textoNome);
        textoCor = findViewById(R.id.textoCor);
        textoRaca = findViewById(R.id.textoRaca);
        textoIdade = findViewById(R.id.textoIdade);
        textoAniversario = findViewById(R.id.textoAniversario);

        String nomeRecebido = getIntent().getStringExtra("nome");
        String corRecebida = getIntent().getStringExtra("cor");
        String racaRecebida = getIntent().getStringExtra("raca");
        int idadeRecebida = getIntent().getIntExtra("idade", 0);
        String aniversarioRecebido = getIntent().getStringExtra("aniversario");
        int imagemRecebida = getIntent().getIntExtra("imagem", 0);

        imgPet = findViewById(R.id.imgPet);

        textoNome.setText("Nome " + nomeRecebido);
        textoCor.setText("Cor:" + corRecebida);
        textoRaca.setText("Raça:" + racaRecebida);
        textoIdade.setText("Idade:" + idadeRecebida);
        textoAniversario.setText("Aniversário:" + aniversarioRecebido);

        if (imagemRecebida != 0) {
            imgPet.setImageResource(imagemRecebida);
        }
    }
}
