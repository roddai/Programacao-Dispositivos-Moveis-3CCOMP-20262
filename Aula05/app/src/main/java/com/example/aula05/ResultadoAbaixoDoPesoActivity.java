package com.example.aula05;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
public class ResultadoAbaixoDoPesoActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_resultado_abaixo_peso);
        android.view.View conteudo = ((android.view.ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        int esquerda=conteudo.getPaddingLeft(), topo=conteudo.getPaddingTop();
        int direita=conteudo.getPaddingRight(), base=conteudo.getPaddingBottom();
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(conteudo, (view, insets) -> {
            androidx.core.graphics.Insets barras=insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            view.setPadding(esquerda+barras.left, topo+barras.top, direita+barras.right, base+barras.bottom);
            return insets;
        });
        androidx.core.view.ViewCompat.requestApplyInsets(conteudo);

        ((TextView)findViewById(R.id.valorImc)).setText(String.format(Locale.getDefault(), "IMC: %.2f", getIntent().getDoubleExtra("imc", 0)));
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }
}
