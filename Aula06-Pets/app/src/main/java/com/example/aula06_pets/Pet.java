package com.example.aula06_pets;

public class Pet {

    private String nome;

    private String cor;


    private String raca;


    private int idade;


    private int dataDeAniversario;

    public Pet(String nome, String cor, String raca, int idade, int dataDeAniversario){
        this.nome = nome;
        this.cor = cor;
        this.raca = raca;
        this.idade = idade;
        this.dataDeAniversario = dataDeAniversario;
    }

    public String getInfo(){
        return "Nome: " + nome + "\nCor: " + cor + "\nRaça" + raca + "\nIdade: " + idade + "\nData de Aniversário: " + dataDeAniversario;
    }


    public static class Detalhes {
    }
}
