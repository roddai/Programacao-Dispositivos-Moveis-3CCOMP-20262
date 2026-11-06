package com.example.aula12

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class CadastroActivity : AppCompatActivity() {

    private val baseUrl = "http://127.0.0.1:8080/mobile_app/"

    private lateinit var etNome: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnCadastrar: MaterialButton
    private lateinit var btnBackLogin: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        etNome      = findViewById(R.id.etNome)
        etEmail     = findViewById(R.id.etEmail)
        etPassword  = findViewById(R.id.etPassword)
        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnBackLogin = findViewById(R.id.btnBackLogin)

        btnCadastrar.setOnClickListener { doCadastro() }

        btnBackLogin.setOnClickListener { finish() }
    }

    private fun doCadastro() {
        val nome     = etNome.text.toString().trim()
        val email    = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validações locais
        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 4) {
            Toast.makeText(this, "A senha deve ter ao menos 4 caracteres.", Toast.LENGTH_SHORT).show()
            return
        }

        setLoading(loading = true)

        val url = "${baseUrl}register.php"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(
            POST, url,
            { response ->
                setLoading(loading = false)
                try {
                    val json = JSONObject(response)
                    val success = json.getBoolean("success")
                    val message = json.getString("message")

                    if (success) {
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show()
                        // Retorna para tela de Login
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Erro ao processar resposta do servidor.", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                setLoading(loading = false)
                val msg = when {
                    error.networkResponse != null ->
                        "Erro ${error.networkResponse.statusCode}: verifique o servidor."
                    else -> "Falha na conexão. Verifique se o servidor está ativo."
                }
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return mutableMapOf(
                    "usu_nome"     to nome,
                    "usu_email"    to email,
                    "usu_password" to password
                )
            }
        }

        queue.add(request)
    }

    private fun setLoading(loading: Boolean) {
        btnCadastrar.isEnabled = !loading
        btnCadastrar.text = if (loading) "Aguarde..." else getString(R.string.btn_register)
    }
}
