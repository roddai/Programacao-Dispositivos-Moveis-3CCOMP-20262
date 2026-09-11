package com.example.appsdepets;

public class Pet {
    String nome;
    String cor;
    String raca;
    int idade;
    String aniversario;

    public Pet (String n, String c, String r, int i, String niver){
        this.nome = n;
        this.cor = c;
        this.raca = r;
        this.idade = i;
        this.aniversario = niver;
    }

    public String getInfo(){
        return "Nome: "+nome+"\nCor: "+cor+"\nRaça: "+raca+"\nIdade: "+idade+"\nAniversário: "+aniversario;
    }

}
