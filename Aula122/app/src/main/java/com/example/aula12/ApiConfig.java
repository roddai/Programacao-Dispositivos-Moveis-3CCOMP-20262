package com.example.aula12;

public class ApiConfig {
    /**
     * ATENÇÃO COM A URL:
     * - No Emulador Android: use "http://10.0.2.2/..." para acessar o localhost do XAMPP no PC.
     * - No Celular Físico: use o IP do seu computador na rede Wi-Fi (ex: "http://192.168.1.15/...").
     * - Se seus scripts estiverem dentro de uma pasta no htdocs (ex: htdocs/meu_app/),
     *   adicione o nome da pasta: "http://10.0.2.2/meu_app/"
     */
    public static final String BASE_URL = "http://10.1.32.254/mobile_app/";

    public static final String URL_LOGIN = BASE_URL + "login.php";
    public static final String URL_REGISTER = BASE_URL + "register.php";
}
