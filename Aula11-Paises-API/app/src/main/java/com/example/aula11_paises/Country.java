package com.example.aula11_paises;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Country {

    @SerializedName("name")
    private CountryName name;

    @SerializedName("capital")
    private List<String> capital;

    @SerializedName("region")
    private String region;

    @SerializedName("population")
    private Long population;

    @SerializedName("flags")
    private CountryFlags flags;

    public Country() {
    }

    public CountryName getName() {
        return name;
    }

    public void setName(CountryName name) {
        this.name = name;
    }

    public List<String> getCapital() {
        return capital;
    }

    public void setCapital(List<String> capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public CountryFlags getFlags() {
        return flags;
    }

    public void setFlags(CountryFlags flags) {
        this.flags = flags;
    }
}