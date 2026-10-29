package com.example.aula10;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {
    @GET("ws/{cep}/json/")
    Call<Cep> buscarCep(@Path("cep") String cep);
}
