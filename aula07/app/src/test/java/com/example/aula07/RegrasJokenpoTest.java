package com.example.aula07;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class RegrasJokenpoTest {
    @Test public void verificaTodasAsNoveCombinacoes() {
        int[][] esperado = {{0, -1, 1}, {1, 0, -1}, {-1, 1, 0}};
        for (int jogador=0; jogador<3; jogador++)
            for (int maquina=0; maquina<3; maquina++)
                assertEquals(esperado[jogador][maquina], RegrasJokenpo.resultado(jogador, maquina));
    }
    @Test(expected=IllegalArgumentException.class) public void rejeitaJogadaInvalida() {
        RegrasJokenpo.resultado(3, 0);
    }
}
