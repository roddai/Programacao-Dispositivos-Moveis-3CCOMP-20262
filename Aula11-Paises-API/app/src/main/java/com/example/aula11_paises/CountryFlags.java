package com.example.aula11_paises;

import com.google.gson.annotations.SerializedName;

public class CountryFlags {

    @SerializedName("png")
    private String png;

    @SerializedName("svg")
    private String svg;

    public CountryFlags() {
    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }
}