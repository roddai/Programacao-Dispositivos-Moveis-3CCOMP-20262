package com.example.aula05;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ResultadoObesidadeClasse2 extends AppCompatActivity {

    Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultado_obesidade_2);
        android.view.View conteudo = ((android.view.ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        int esquerda=conteudo.getPaddingLeft(), topo=conteudo.getPaddingTop();
        int direita=conteudo.getPaddingRight(), base=conteudo.getPaddingBottom();
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(conteudo, (view, insets) -> {
            androidx.core.graphics.Insets barras=insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            view.setPadding(esquerda+barras.left, topo+barras.top, direita+barras.right, base+barras.bottom);
            return insets;
        });
        androidx.core.view.ViewCompat.requestApplyInsets(conteudo);


        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
            });
    }
}