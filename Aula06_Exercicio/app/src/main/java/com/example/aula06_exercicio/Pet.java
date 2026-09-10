package com.example.aula06_exercicio;

public class Pet {
    public String nome;
    public String cor;
    public String raca;
    public Integer idade;
    public String aniversario;

    public Pet (String nome, String cor, String raca, Integer idade, String aniversario) {
        this.nome = nome;
        this.cor = cor;
        this.raca = raca;
        this.idade = idade;
        this.aniversario = aniversario;
    }

    public String getinfo(){
        return "Nome: " + nome + "Cor: " + cor + "Raça: " + raca + "Idade: " + idade + "Aniversário: " + aniversario;
    }


}
