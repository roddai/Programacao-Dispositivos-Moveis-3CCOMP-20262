package com.example.pets;

public class Pet {
    private String nome;
    private String cor;
    private String raca;
    private int idade;
    private String aniversario;

    Pet(String nome,String cor,String raca,int idade,String aniversario){
        this.nome = nome;
        this.cor = cor;
        this.raca = raca;
        this.idade = idade;
        this.aniversario = aniversario;
    };

    public String getInfo() {
        return "Nome: "+ nome + "\nCor: "+cor + "\nRaca: " + raca + "\nIdade: " + idade +"\nAniversário: " +aniversario;
    }

}
