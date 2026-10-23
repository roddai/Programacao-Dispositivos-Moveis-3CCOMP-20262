package com.example.aula04exemplo01;

public class Livro {
    private String titulo;
    private String autor;
    private int anoPubli;

    public Livro (String titulo, String autor, int anoPubli) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPubli = anoPubli;
    }

    public String getInfo() {
        return "Título: " + titulo + "\nAutor: " + autor + "\nAno de publicação: " + anoPubli;
    }

}
