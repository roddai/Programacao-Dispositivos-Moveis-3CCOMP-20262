package com.example.aula_10

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var etCep: TextInputEditText
    private lateinit var btnSearch: MaterialButton
    private lateinit var tvResult: TextView
    private lateinit var cardResult: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCep = findViewById(R.id.etCep)
        btnSearch = findViewById(R.id.btnSearch)
        tvResult = findViewById(R.id.tvResult)
        cardResult = findViewById(R.id.cardResult)

        // Alterado para http para evitar erro de certificado SSL em alguns emuladores/redes
        val retrofit = Retrofit.Builder()
            .baseUrl("http://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ViaCepService::class.java)

        btnSearch.setOnClickListener {
            val cep = etCep.text.toString()
            if (cep.length == 8) {
                buscarCep(service, cep)
            } else {
                Toast.makeText(this, "Digite um CEP válido com 8 dígitos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarCep(service: ViaCepService, cep: String) {
        service.getCep(cep).enqueue(object : Callback<CepResponse> {
            override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {
                if (response.isSuccessful) {
                    val cepResponse = response.body()
                    if (cepResponse != null) {
                        if (cepResponse.erro == true) {
                            showError("CEP não encontrado.")
                        } else {
                            showResult(cepResponse.logradouro ?: "Logradouro não disponível")
                        }
                    } else {
                        showError("Erro ao processar resposta.")
                    }
                } else {
                    showError("Erro na busca: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CepResponse>, t: Throwable) {
                showError("Falha na rede: ${t.message}")
            }
        })
    }

    private fun showResult(logradouro: String) {
        tvResult.text = logradouro
        cardResult.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        cardResult.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
