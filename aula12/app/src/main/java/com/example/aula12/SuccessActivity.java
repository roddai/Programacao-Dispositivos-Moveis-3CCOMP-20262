package com.example.aula12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuccessActivity extends AppCompatActivity {

    private ImageView imgFotoSucesso;
    private TextView tvOlaNome;
    private TextView tvEmailSucesso;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_success);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgFotoSucesso = findViewById(R.id.imgFotoSucesso);
        tvOlaNome = findViewById(R.id.tvOlaNome);
        tvEmailSucesso = findViewById(R.id.tvEmailSucesso);
        btnSair = findViewById(R.id.btnSair);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);

        Intent intent = getIntent();
        String nome = intent != null ? intent.getStringExtra("usu_nome") : null;
        String email = intent != null ? intent.getStringExtra("usu_email") : null;

        if (nome == null || nome.trim().isEmpty()) {
            nome = prefs.getString("usu_nome", "Usuário");
        }
        if (email == null || email.trim().isEmpty()) {
            email = prefs.getString("usu_email", "");
        }

        tvOlaNome.setText("Olá, " + nome + "!");
        tvEmailSucesso.setText(email);

        // Carregar foto de perfil salva na sessão
        carregarFotoPerfil(prefs);

        btnSair.setOnClickListener(v -> realizarLogout(prefs));
    }

    private void carregarFotoPerfil(SharedPreferences prefs) {
        String fotoUriStr = prefs.getString("foto_uri", null);
        if (fotoUriStr != null && !fotoUriStr.trim().isEmpty()) {
            try {
                Uri fotoUri = Uri.parse(fotoUriStr);
                imgFotoSucesso.setImageURI(fotoUri);
                if (imgFotoSucesso.getDrawable() == null) {
                    imgFotoSucesso.setImageResource(R.drawable.ic_default_avatar);
                }
            } catch (Exception e) {
                imgFotoSucesso.setImageResource(R.drawable.ic_default_avatar);
            }
        } else {
            imgFotoSucesso.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    private void realizarLogout(SharedPreferences prefs) {
        // Limpar dados de sessão
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("logged_in");
        editor.remove("usu_nome");
        editor.remove("usu_email");
        editor.remove("foto_uri");
        editor.apply();

        // Retornar para LoginActivity impedindo voltar para a tela de sucesso
        Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
