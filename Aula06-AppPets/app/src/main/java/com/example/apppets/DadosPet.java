package com.example.apppets;

import java.util.Date;

public class DadosPet {

   private String Nome;
   private String Cor;
   private String Raca;
   private int Idade;
   private String Aniversario;


   public DadosPet (String Nome, String Raca, String Cor, int Idade, String Aniversario){
   this.Nome = Nome;
   this.Raca = Raca;
   this.Cor = Cor;
   this.Idade = Idade;
   this.Aniversario = Aniversario;

   }
   public String getInfo(){
       return "Nome: " + Nome + "\nRaça: " + Raca + "\nCor: " + Cor + "\nIdade: " + Idade + "\nAniversário: " + Aniversario;
   }

}
