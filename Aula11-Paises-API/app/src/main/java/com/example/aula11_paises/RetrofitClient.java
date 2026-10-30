package com.example.aula11_paises;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static final String BASE_URL =
            "https://restcountries.com/v3.1/";

    private static Retrofit retrofit;
    private static CountryApi countryApi;

    private RetrofitClient() {
        // Impede a criação de objetos dessa classe.
    }

    public static CountryApi getCountryApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        if (countryApi == null) {
            countryApi = retrofit.create(CountryApi.class);
        }

        return countryApi;
    }
}