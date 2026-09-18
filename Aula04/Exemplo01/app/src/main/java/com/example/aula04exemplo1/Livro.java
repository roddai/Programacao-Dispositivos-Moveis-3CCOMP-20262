package com.example.aula04exemplo1;

public class Livro {
    private String titulo;
    private String autor;
    private Integer anoPublicacao;

    public Livro(String titulo, String autor, Integer anoPublicacao){
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
    }

    public String getInfo(){
        return "Título: " + titulo + "\n Autor: " + autor + "\n Ano de publicação: " + anoPublicacao;
    }




}
