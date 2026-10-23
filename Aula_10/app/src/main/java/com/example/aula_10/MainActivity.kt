package com.example.aula_10

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var etCep: TextInputEditText
    private lateinit var btnSearch: MaterialButton
    private lateinit var tilNumero: TextInputLayout
    private lateinit var etNumero: TextInputEditText
    private lateinit var tilComplemento: TextInputLayout
    private lateinit var etComplemento: TextInputEditText
    
    private lateinit var cardResult: MaterialCardView
    private lateinit var tvLogradouro: TextView
    private lateinit var tvBairro: TextView
    private lateinit var tvCidadeUf: TextView
    private lateinit var tvRegiao: TextView
    private lateinit var tvDdd: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCep = findViewById(R.id.etCep)
        btnSearch = findViewById(R.id.btnSearch)
        
        tilNumero = findViewById(R.id.tilNumero)
        etNumero = findViewById(R.id.etNumero)
        tilComplemento = findViewById(R.id.tilComplemento)
        etComplemento = findViewById(R.id.etComplemento)
        
        cardResult = findViewById(R.id.cardResult)
        tvLogradouro = findViewById(R.id.tvLogradouro)
        tvBairro = findViewById(R.id.tvBairro)
        tvCidadeUf = findViewById(R.id.tvCidadeUf)
        tvRegiao = findViewById(R.id.tvRegiao)
        tvDdd = findViewById(R.id.tvDdd)

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
                            showResult(cepResponse)
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

    private fun showResult(cep: CepResponse) {
        tvLogradouro.text = "Logradouro: ${cep.logradouro ?: "N/A"}"
        tvBairro.text = "Bairro: ${cep.bairro ?: "N/A"}"
        tvCidadeUf.text = "Cidade: ${cep.localidade ?: "N/A"} - ${cep.uf ?: "N/A"}"
        tvRegiao.text = "Região: ${cep.regiao ?: "N/A"}"
        tvDdd.text = "DDD: ${cep.ddd ?: "N/A"}"
        
        if (!cep.complemento.isNullOrBlank() && etComplemento.text.isNullOrBlank()) {
            etComplemento.setText(cep.complemento)
        }

        // Faz os campos de Número, Complemento e o Card de resultado aparecerem
        tilNumero.visibility = View.VISIBLE
        tilComplemento.visibility = View.VISIBLE
        cardResult.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        // Esconde os campos caso ocorra um erro ou busca inválida
        tilNumero.visibility = View.GONE
        tilComplemento.visibility = View.GONE
        cardResult.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
