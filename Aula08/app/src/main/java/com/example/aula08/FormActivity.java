package com.example.aula08;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
public class FormActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); setContentView(R.layout.activity_form);
        android.view.View conteudo = ((android.view.ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        int esquerda=conteudo.getPaddingLeft(), topo=conteudo.getPaddingTop();
        int direita=conteudo.getPaddingRight(), base=conteudo.getPaddingBottom();
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(conteudo, (view, insets) -> {
            androidx.core.graphics.Insets barras=insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            view.setPadding(esquerda+barras.left, topo+barras.top, direita+barras.right, base+barras.bottom);
            return insets;
        });
        androidx.core.view.ViewCompat.requestApplyInsets(conteudo);

        EditText nome=findViewById(R.id.nome), idade=findViewById(R.id.idade);
        RadioGroup tamanho=findViewById(R.id.tamanho);
        Spinner cor=findViewById(R.id.cor);
        RatingBar avaliacao=findViewById(R.id.avaliacao);
        TextView erro=findViewById(R.id.erro);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.cores,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); cor.setAdapter(adapter);
        findViewById(R.id.enviar).setOnClickListener(v -> {
            erro.setText("");
            String pessoa=nome.getText().toString().trim();
            if(pessoa.isEmpty()) { nome.setError("Informe seu nome"); nome.requestFocus(); return; }
            int anos;
            try {
                anos=Integer.parseInt(idade.getText().toString().trim());
                if(anos<1 || anos>120) throw new NumberFormatException();
            } catch(NumberFormatException e) { idade.setError("Informe uma idade de 1 a 120"); idade.requestFocus(); return; }
            if(tamanho.getCheckedRadioButtonId()==-1) { erro.setText("Escolha um tamanho"); return; }
            List<String> pecas=new ArrayList<>();
            for(int id:new int[]{R.id.camiseta,R.id.calca,R.id.jaqueta}) {
                CheckBox p=findViewById(id); if(p.isChecked()) pecas.add(p.getText().toString());
            }
            if(pecas.isEmpty()) { erro.setText("Selecione pelo menos uma peça"); return; }
            RadioButton tamanhoEscolhido=findViewById(tamanho.getCheckedRadioButtonId());
            String nota=avaliacao.getRating()==0?"Não informada":((int)avaliacao.getRating())+" de 5";
            String resumo="Nome: "+pessoa+"\nIdade: "+anos+"\nTamanho: "+tamanhoEscolhido.getText()
                +"\nPeças: "+TextUtils.join(", ",pecas)+"\nCor: "+cor.getSelectedItem()+"\nAvaliação: "+nota;
            startActivity(new Intent(this,ResultadoActivity.class).putExtra("resumo",resumo));
        });
    }
}
