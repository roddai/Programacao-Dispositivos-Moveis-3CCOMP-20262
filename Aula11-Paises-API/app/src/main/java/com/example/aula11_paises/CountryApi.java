package com.example.aula11_paises;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CountryApi {

    @GET("name/{countryName}")
    Call<List<Country>> searchCountry(
            @Path("countryName") String countryName,
            @Query("fields") String fields
    );
}