package com.example.aula08;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); setContentView(R.layout.activity_main);
        android.view.View conteudo = ((android.view.ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        int esquerda=conteudo.getPaddingLeft(), topo=conteudo.getPaddingTop();
        int direita=conteudo.getPaddingRight(), base=conteudo.getPaddingBottom();
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(conteudo, (view, insets) -> {
            androidx.core.graphics.Insets barras=insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            view.setPadding(esquerda+barras.left, topo+barras.top, direita+barras.right, base+barras.bottom);
            return insets;
        });
        androidx.core.view.ViewCompat.requestApplyInsets(conteudo);

        findViewById(R.id.abrir).setOnClickListener(v -> startActivity(new Intent(this, FormActivity.class)));
    }
}
