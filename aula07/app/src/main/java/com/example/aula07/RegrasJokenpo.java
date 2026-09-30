package com.example.aula07;
public final class RegrasJokenpo {
    private RegrasJokenpo() { }
    // 0 = pedra, 1 = papel, 2 = tesoura; retorno: 0 empate, 1 vitória, -1 derrota.
    public static int resultado(int jogador, int maquina) {
        if (jogador < 0 || jogador > 2 || maquina < 0 || maquina > 2)
            throw new IllegalArgumentException("Jogada inválida");
        if (jogador == maquina) return 0;
        if ((jogador == 0 && maquina == 2)
                || (jogador == 1 && maquina == 0)
                || (jogador == 2 && maquina == 1)) {
            return 1;
        }
        return -1;
    }
}
