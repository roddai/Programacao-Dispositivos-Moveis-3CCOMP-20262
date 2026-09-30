package com.example.aula05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
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

        EditText peso = findViewById(R.id.editPeso);
        EditText altura = findViewById(R.id.editAltura);
        findViewById(R.id.btnCalcular).setOnClickListener(v -> {
            Double p = ler(peso, "Informe um peso positivo em kg");
            Double a = ler(altura, "Informe uma altura positiva em metros");
            if (p == null || a == null) return;
            double imc;
            try { imc = CalculoImc.calcular(p, a); }
            catch (IllegalArgumentException e) { altura.setError("Confira os valores informados"); return; }
            int faixa = CalculoImc.faixa(imc);
            Intent intent;
            if (faixa == 0) {
                intent = new Intent(this, ResultadoAbaixoDoPesoActivity.class);
            } else if (faixa == 1) {
                intent = new Intent(this, ResultadoNormalActivity.class);
            } else if (faixa == 2) {
                intent = new Intent(this, ResultadoSobrepesoActivity.class);
            } else if (faixa == 3) {
                intent = new Intent(this, ResultadoObesidadeClasse1Activity.class);
            } else if (faixa == 4) {
                intent = new Intent(this, ResultadoObesidadeClasse2Activity.class);
            } else {
                intent = new Intent(this, ResultadoObesidadeClasse3Activity.class);
            }
            intent.putExtra("imc", imc);
            startActivity(intent);
        });
    }
    private Double ler(EditText campo, String erro) {
        try {
            double valor = Double.parseDouble(campo.getText().toString().trim().replace(',', '.'));
            if (!Double.isFinite(valor) || valor <= 0) throw new NumberFormatException();
            campo.setError(null);
            return valor;
        } catch (NumberFormatException e) { campo.setError(erro); return null; }
    }
}
