package com.example.aula05;

public final class CalculoImc {
    private CalculoImc() { }
    public static double calcular(double peso, double altura) {
        if (!Double.isFinite(peso) || !Double.isFinite(altura) || peso <= 0 || altura <= 0)
            throw new IllegalArgumentException("Peso e altura devem ser positivos e finitos");
        double valor = peso / (altura * altura);
        if (!Double.isFinite(valor) || valor <= 0) throw new IllegalArgumentException("Valores fora do intervalo");
        return valor;
    }
    public static int faixa(double imc) {
        if (!Double.isFinite(imc) || imc <= 0) throw new IllegalArgumentException("IMC inválido");
        if (imc < 18.5) return 0;
        if (imc < 25) return 1;
        if (imc < 30) return 2;
        if (imc < 35) return 3;
        if (imc < 40) return 4;
        return 5;
    }
}
