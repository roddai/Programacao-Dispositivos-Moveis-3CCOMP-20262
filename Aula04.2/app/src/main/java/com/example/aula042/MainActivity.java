package com.example.aula042;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private EditText editNome;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.view.View conteudo = ((android.view.ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        int esquerda=conteudo.getPaddingLeft(), topo=conteudo.getPaddingTop();
        int direita=conteudo.getPaddingRight(), base=conteudo.getPaddingBottom();
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(conteudo, (view, insets) -> {
            androidx.core.graphics.Insets barras=insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            view.setPadding(esquerda+barras.left, topo+barras.top, direita+barras.right, base+barras.bottom);
            return insets;
        });
        androidx.core.view.ViewCompat.requestApplyInsets(conteudo);


        editNome = findViewById(R.id.editNome); // onde digitamos nosso nome
        btnEnviar = findViewById(R.id.btnEnviar); // botão

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeDigitado = editNome.getText().toString().trim();
                if (nomeDigitado.isEmpty()) {
                    editNome.setError("Informe seu nome");
                    return;
                }

                Intent irParaSegundaTela = new Intent(MainActivity.this, SegundaActivity.class);
                irParaSegundaTela.putExtra("nomeUsuario", nomeDigitado);
                startActivity(irParaSegundaTela);
            }
        });
    }
}