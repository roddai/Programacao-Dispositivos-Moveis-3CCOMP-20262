package com.example.aula11_paises;

import com.google.gson.annotations.SerializedName;

public class CountryName {

    @SerializedName("common")
    private String common;

    @SerializedName("official")
    private String official;

    public CountryName() {
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getOfficial() {
        return official;
    }

    public void setOfficial(String official) {
        this.official = official;
    }
}