package com.example.myapplication;

public class Livro {
    // Variáveis de Instância (Atributos)
    private String titulo;
    private String autor;
    private int anoPublicacao;

    // Construtor: cria o livro com seus dados[cite: 1]
    public Livro(String titulo, String autor, int anoPublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
    }

    // Método que retorna as informações do livro[cite: 1]
    public String getInfo() {
        return "Título: " + titulo + "\nAutor: " + autor + "\nAno: " + anoPublicacao;
    }
}