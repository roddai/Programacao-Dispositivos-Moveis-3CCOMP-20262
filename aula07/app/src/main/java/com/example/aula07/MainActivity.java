package com.example.aula07;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
public class MainActivity extends AppCompatActivity {
    private final Random sorteio = new Random();
    private final int[] figuras = {R.drawable.pedra, R.drawable.papel, R.drawable.tesoura};
    private final String[] nomes = {"Pedra", "Papel", "Tesoura"};
    private int vitorias, derrotas, empates, jogador = -1, maquina = -1, avatar;
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

        if (state != null) {
            vitorias=state.getInt("v"); derrotas=state.getInt("d"); empates=state.getInt("e");
            jogador=state.getInt("j", -1); maquina=state.getInt("m", -1); avatar=state.getInt("a");
        }
        findViewById(R.id.imgPedra).setOnClickListener(v -> jogar(0));
        findViewById(R.id.imgPapel).setOnClickListener(v -> jogar(1));
        findViewById(R.id.imgTesoura).setOnClickListener(v -> jogar(2));
        findViewById(R.id.avatar1).setOnClickListener(v -> { avatar=0; atualizar(); });
        findViewById(R.id.avatar2).setOnClickListener(v -> { avatar=1; atualizar(); });
        findViewById(R.id.reiniciar).setOnClickListener(v -> {
            vitorias=derrotas=empates=0; jogador=maquina=-1; atualizar();
        });
        atualizar();
    }
    private void jogar(int escolha) {
        jogador=escolha; maquina=sorteio.nextInt(3);
        int resultado=RegrasJokenpo.resultado(jogador, maquina);
        if(resultado==0) empates++; else if(resultado==1) vitorias++; else derrotas++;
        atualizar();
    }
    private void atualizar() {
        ((ImageView)findViewById(R.id.imgAvatar)).setImageResource(avatar==0?R.drawable.usuario_masc:R.drawable.usuario_fem);
        ImageView escolha = findViewById(R.id.imgJogador), computador=findViewById(R.id.imgMaquina);
        escolha.setImageResource(jogador<0?R.drawable.interrogacao:figuras[jogador]);
        computador.setImageResource(maquina<0?R.drawable.interrogacao:figuras[maquina]);
        escolha.setContentDescription(jogador<0?"Sua jogada":"Você: "+nomes[jogador]);
        computador.setContentDescription(maquina<0?"Jogada do computador":"Computador: "+nomes[maquina]);
        String mensagem="Escolha pedra, papel ou tesoura";
        if(jogador>=0) {
            int r=RegrasJokenpo.resultado(jogador,maquina);
            mensagem=(r==0?"Empate!":r==1?"Você venceu!":"O computador venceu!")+"\nVocê: "+nomes[jogador]+" • Computador: "+nomes[maquina];
        }
        ((TextView)findViewById(R.id.txtResultado)).setText(mensagem);
        ((TextView)findViewById(R.id.placar)).setText("Vitórias: "+vitorias+" • Derrotas: "+derrotas+" • Empates: "+empates);
    }
    @Override protected void onSaveInstanceState(Bundle state) {
        state.putInt("v",vitorias); state.putInt("d",derrotas); state.putInt("e",empates);
        state.putInt("j",jogador); state.putInt("m",maquina); state.putInt("a",avatar);
        super.onSaveInstanceState(state);
    }
}
