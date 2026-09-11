package com.example.aula06_pets;

public class Pet {

    private String nome;

    private String cor;

    private String raça;

    private int idade;

    private String aniversario;
    public Pet(String nome, String cor, String raça, int idade, String aniversario){
        this.nome =nome;
        this.cor = cor;
        this.raça = raça;
        this.idade = idade;
        this.aniversario = aniversario;
    }

    public String getInfo(){
        return "Nome: " + nome + "\nCor:" + cor + "\nRaça:" + raça + "\nIdade:" + idade + "\nAniversario:" + aniversario;
    }
}

