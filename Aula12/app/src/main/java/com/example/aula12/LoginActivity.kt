package com.example.aula12

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    // ⚠️ Troque pelo IP da sua máquina na rede local (ex: 192.168.x.x)
    // Use 10.0.2.2 se for rodar no emulador Android padrão
    private val BASE_URL = "http://127.0.0.1:8080/mobile_app/"

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnGoRegister: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail      = findViewById(R.id.etEmail)
        etPassword   = findViewById(R.id.etPassword)
        btnLogin     = findViewById(R.id.btnLogin)
        btnGoRegister = findViewById(R.id.btnGoRegister)

        btnLogin.setOnClickListener { doLogin() }

        btnGoRegister.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    private fun doLogin() {
        val email    = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validações locais
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
            return
        }

        setLoading(true)

        val url = "${BASE_URL}login.php"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                setLoading(false)
                try {
                    val json = JSONObject(response)
                    val success = json.getBoolean("success")
                    val message = json.getString("message")

                    if (success) {
                        // Navega para tela de sucesso passando o e-mail
                        val intent = Intent(this, RegisterActivity::class.java)
                        intent.putExtra("USER_EMAIL", email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Erro ao processar resposta do servidor.", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                setLoading(false)
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
                    "usu_email"    to email,
                    "usu_password" to password
                )
            }
        }

        queue.add(request)
    }

    private fun setLoading(loading: Boolean) {
        btnLogin.isEnabled = !loading
        btnLogin.text = if (loading) "Aguarde..." else getString(R.string.btn_login)
    }
}
