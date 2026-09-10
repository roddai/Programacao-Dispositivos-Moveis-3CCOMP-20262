package com.example.pets;

public class Pet {
    String nome;
    String cor;
    String raca;
    int idade;
    String aniversario;

    Pet(){}

    Pet(String nome,String cor,String raca,int idade,String aniversario){
        this.nome = nome;
        this.cor = cor;
        this.raca = raca;
        this.idade = idade;
        this.aniversario = aniversario;
    };
}
