package com.example.aula05;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class CalculoImcTest {
    @Test public void calculaComAlturaEmMetros() { assertEquals(25, CalculoImc.calcular(100, 2), 0.00001); }
    @Test public void respeitaLimitesDasFaixas() {
        double[] valores={18.49,18.5,24.99,25,29.99,30,34.99,35,39.99,40};
        int[] faixas={0,1,1,2,2,3,3,4,4,5};
        for(int i=0;i<valores.length;i++) assertEquals(faixas[i],CalculoImc.faixa(valores[i]));
    }
    @Test(expected=IllegalArgumentException.class) public void rejeitaAlturaZero() { CalculoImc.calcular(70,0); }
    @Test(expected=IllegalArgumentException.class) public void rejeitaPesoNegativo() { CalculoImc.calcular(-70,1.7); }
    @Test(expected=IllegalArgumentException.class) public void rejeitaNaN() { CalculoImc.calcular(Double.NaN,1.7); }
    @Test(expected=IllegalArgumentException.class) public void rejeitaInfinito() { CalculoImc.calcular(70,Double.POSITIVE_INFINITY); }
}
