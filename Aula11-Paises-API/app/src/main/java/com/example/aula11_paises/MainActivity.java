package com.example.aula11_paises;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.aula11_paises.databinding.ActivityMainBinding;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Call<List<Country>> currentCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureListeners();
    }

    private void configureListeners() {
        binding.btnSearch.setOnClickListener(view -> validateAndSearch());

        binding.etCountryName.setOnEditorActionListener(
                (textView, actionId, keyEvent) -> {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        validateAndSearch();
                        return true;
                    }

                    return false;
                }
        );
    }

    private void validateAndSearch() {
        String countryName = "";

        if (binding.etCountryName.getText() != null) {
            countryName = binding.etCountryName
                    .getText()
                    .toString()
                    .trim();
        }

        binding.inputLayoutCountry.setError(null);
        hideError();

        if (countryName.isEmpty()) {
            binding.inputLayoutCountry.setError(
                    "Informe o nome de um país."
            );
            return;
        }

        if (countryName.length() < 2) {
            binding.inputLayoutCountry.setError(
                    "Digite pelo menos 2 caracteres."
            );
            return;
        }

        if (!countryName.matches("^[\\p{L}\\s'-]+$")) {
            binding.inputLayoutCountry.setError(
                    "Use somente letras, espaços, hífen ou apóstrofo."
            );
            return;
        }

        searchCountry(countryName);
    }

    private void searchCountry(String countryName) {
        /*
         * Cancela a consulta anterior caso o usuário pesquise
         * outro país antes de a resposta chegar.
         */
        if (currentCall != null) {
            currentCall.cancel();
        }

        showLoading();

        String fields = "name,capital,region,population,flags";

        currentCall = RetrofitClient
                .getCountryApi()
                .searchCountry(
                        countryName,
                        "name,capital,region,population,flags"
                );

        currentCall.enqueue(new Callback<List<Country>>() {

            @Override
            public void onResponse(
                    Call<List<Country>> call,
                    Response<List<Country>> response
            ) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<Country> countries = response.body();

                    if (countries == null || countries.isEmpty()) {
                        showError(
                                "Nenhum país foi encontrado com esse nome."
                        );
                        return;
                    }

                    Country country = chooseBestMatch(
                            countryName,
                            countries
                    );

                    showCountry(country);
                    return;
                }

                int statusCode = response.code();

                if (statusCode == 404) {
                    showError(
                            "País não encontrado. Verifique o nome informado."
                    );
                } else if (statusCode >= 500 && statusCode <= 599) {
                    showError(
                            "A API está indisponível no momento. " +
                                    "Tente novamente mais tarde."
                    );
                } else {
                    showError(
                            "Erro na consulta. Código HTTP: " + statusCode
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<List<Country>> call,
                    Throwable throwable
            ) {
                hideLoading();

                if (call.isCanceled()) {
                    return;
                }

                if (throwable instanceof UnknownHostException) {
                    showError(
                            "Não foi possível acessar a internet. " +
                                    "Verifique sua conexão."
                    );
                } else if (throwable instanceof SocketTimeoutException) {
                    showError(
                            "A conexão demorou muito para responder. " +
                                    "Tente novamente."
                    );
                } else if (throwable instanceof SSLHandshakeException) {
                    showError(
                            "Não foi possível estabelecer uma conexão segura. " +
                                    "Verifique a data e a hora do dispositivo."
                    );
                } else if (throwable instanceof IOException) {
                    showError(
                            "Falha de conexão. Verifique sua internet " +
                                    "e tente novamente."
                    );
                } else {
                    showError(
                            "Erro inesperado ao consultar o país."
                    );
                }
            }
        });
    }

    private Country chooseBestMatch(
            String searchedName,
            List<Country> countries
    ) {
        for (Country country : countries) {
            CountryName countryName = country.getName();

            if (countryName == null) {
                continue;
            }

            String commonName = countryName.getCommon();
            String officialName = countryName.getOfficial();

            boolean commonMatches =
                    commonName != null &&
                            commonName.equalsIgnoreCase(searchedName);

            boolean officialMatches =
                    officialName != null &&
                            officialName.equalsIgnoreCase(searchedName);

            if (commonMatches || officialMatches) {
                return country;
            }
        }

        /*
         * Se não houver uma correspondência exata,
         * utiliza o primeiro país retornado pela API.
         */
        return countries.get(0);
    }

    private void showCountry(Country country) {
        hideError();

        String name = getCountryName(country);
        String capital = getCapital(country);
        String region = getRegion(country);
        String population = getPopulation(country);
        String flagUrl = getFlagUrl(country);

        binding.tvCountryName.setText(name);
        binding.tvCapital.setText("Capital: " + capital);
        binding.tvRegion.setText("Região: " + region);
        binding.tvPopulation.setText(
                "População: " + population + " habitantes"
        );

        Glide.with(this)
                .load(flagUrl)
                .fitCenter()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .into(binding.ivFlag);

        binding.ivFlag.setContentDescription(
                "Bandeira de " + name
        );

        binding.cardCountry.setVisibility(View.VISIBLE);
    }

    private String getCountryName(Country country) {
        if (country == null || country.getName() == null) {
            return "Nome não informado";
        }

        String commonName = country.getName().getCommon();

        if (commonName == null || commonName.trim().isEmpty()) {
            return "Nome não informado";
        }

        return commonName;
    }

    private String getCapital(Country country) {
        if (country == null) {
            return "Não informada";
        }

        List<String> capitals = country.getCapital();

        if (capitals == null || capitals.isEmpty()) {
            return "Não informada";
        }

        String capital = capitals.get(0);

        if (capital == null || capital.trim().isEmpty()) {
            return "Não informada";
        }

        return capital;
    }

    private String getRegion(Country country) {
        if (country == null) {
            return "Não informada";
        }

        String region = country.getRegion();

        if (region == null || region.trim().isEmpty()) {
            return "Não informada";
        }

        return region;
    }

    private String getPopulation(Country country) {
        if (country == null) {
            return "Não informada";
        }

        Long population = country.getPopulation();

        if (population == null) {
            return "Não informada";
        }

        NumberFormat numberFormat =
                NumberFormat.getNumberInstance(
                        new Locale("pt", "BR")
                );

        return numberFormat.format(population);
    }

    private String getFlagUrl(Country country) {
        if (country == null) {
            return null;
        }

        CountryFlags flags = country.getFlags();

        if (flags == null) {
            return null;
        }

        if (flags.getPng() != null &&
                !flags.getPng().trim().isEmpty()) {

            return flags.getPng();
        }

        if (flags.getSvg() != null &&
                !flags.getSvg().trim().isEmpty()) {

            return flags.getSvg();
        }

        return null;
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnSearch.setEnabled(false);
        binding.cardCountry.setVisibility(View.GONE);
        binding.tvError.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnSearch.setEnabled(true);
    }

    private void showError(String message) {
        binding.cardCountry.setVisibility(View.GONE);
        binding.tvError.setText(message);
        binding.tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        binding.tvError.setText("");
        binding.tvError.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (currentCall != null) {
            currentCall.cancel();
        }

        binding = null;

        super.onDestroy();
    }
}