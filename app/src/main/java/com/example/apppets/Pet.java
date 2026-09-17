package com.example.apppets;

public class Pet {

    private String nome, cor, raca, aniversario;
    private int idade;


    public Pet(String nome, String cor, String raca,
               String aniversario, int idade) {

        this.nome = nome;
        this.cor = cor;
        this.raca = raca;
        this.aniversario = aniversario;
        this.idade = idade;
    }
    public String getNome() {
        return nome;
    }

    public String getCor() {
        return cor;
    }

    public String getRaca() {
        return raca;
    }

    public String getAniversario() {
        return aniversario;
    }

    public int getIdade() {
        return idade;
    }
}