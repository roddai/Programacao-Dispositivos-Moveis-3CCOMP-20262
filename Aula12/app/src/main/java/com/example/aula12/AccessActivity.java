package com.example.aula12;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_access);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        TextView tvSuccessMessage = findViewById(R.id.tvSuccessMessage);
        TextView tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Define a mensagem conforme requisito: "Usuário autenticado com sucesso"
        tvSuccessMessage.setText(R.string.auth_success_msg);

        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null && !userName.isEmpty()) {
            tvWelcomeUser.setText(getString(R.string.welcome_user, userName));
            tvWelcomeUser.setVisibility(View.VISIBLE);
        } else {
            tvWelcomeUser.setVisibility(View.GONE);
        }

        String userFotoBase64 = getIntent().getStringExtra("USER_FOTO");
        if (userFotoBase64 != null && !userFotoBase64.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(userFotoBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (bitmap != null) {
                    ivProfilePhoto.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                // Caso ocorra falha ao carregar a imagem, o ícone padrão será mantido
            }
        }

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(AccessActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
