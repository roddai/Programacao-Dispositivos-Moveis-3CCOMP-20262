package com.example.aula10;

import com.google.gson.annotations.SerializedName;

public class CepResponse {
    @SerializedName("cep")
    private String cep;
    @SerializedName("logradouro")
    private String logradouro;
    @SerializedName("complemento")
    private String complemento;
    @SerializedName("bairro")
    private String bairro;
    @SerializedName("localidade")
    private String localidade;
    @SerializedName("uf")
    private String uf;

    public String getLogradouro() {
        return logradouro;
    }

    // Add other getters if needed, but for now logradouro is enough
}
