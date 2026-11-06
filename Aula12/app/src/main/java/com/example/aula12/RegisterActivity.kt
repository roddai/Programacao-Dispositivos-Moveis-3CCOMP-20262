package com.example.aula12

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Recupera o e-mail passado pelo LoginActivity
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        val tvMsg = findViewById<TextView>(R.id.tvSuccessMsg)
        if (userEmail.isNotEmpty()) {
            tvMsg.text = "Você está logado como:\n$userEmail"
        }

        // Botão de logout — retorna para o Login limpando a pilha
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
