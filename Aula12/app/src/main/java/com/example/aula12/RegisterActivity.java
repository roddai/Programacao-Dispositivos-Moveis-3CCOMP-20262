package com.example.aula12;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private ImageView ivSelectPhoto;
    private TextView tvPhotoHint;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegister;
    private Button btnBackToLogin;
    private ProgressBar progressBar;

    private String selectedPhotoBase64 = "";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final ActivityResultLauncher<String> photoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
                        if (originalBitmap != null) {
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 300, 300, true);
                            ivSelectPhoto.setImageBitmap(scaledBitmap);

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            selectedPhotoBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            Toast.makeText(this, "Foto selecionada!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Erro ao carregar imagem selecionada", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivSelectPhoto = findViewById(R.id.ivSelectPhoto);
        tvPhotoHint = findViewById(R.id.tvPhotoHint);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        progressBar = findViewById(R.id.progressBar);

        View.OnClickListener choosePhotoClickListener = v -> openPhotoGallery();
        ivSelectPhoto.setOnClickListener(choosePhotoClickListener);
        if (tvPhotoHint != null) {
            tvPhotoHint.setOnClickListener(choosePhotoClickListener);
        }

        btnRegister.setOnClickListener(v -> performRegister());

        // Ação para voltar para a tela de login
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void openPhotoGallery() {
        photoPickerLauncher.launch("image/*");
    }

    private void performRegister() {
        // Lógica de cadastro existente...
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.err_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.err_invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        executor.execute(() -> {
            try {
                URL url = new URL(ApiConfig.URL_REGISTER);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                String postData = "nome=" + URLEncoder.encode(name, "UTF-8")
                        + "&email=" + URLEncoder.encode(email, "UTF-8")
                        + "&password=" + URLEncoder.encode(password, "UTF-8")
                        + "&foto=" + URLEncoder.encode(selectedPhotoBase64, "UTF-8");

                try (OutputStream os = conn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
                    writer.write(postData);
                    writer.flush();
                }

                int responseCode = conn.getResponseCode();
                InputStream is = (responseCode == HttpURLConnection.HTTP_OK) ? conn.getInputStream() : conn.getErrorStream();

                StringBuilder response = new StringBuilder();
                if (is != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        String currentLine;
                        while ((currentLine = reader.readLine()) != null) {
                            response.append(currentLine);
                        }
                    }
                }

                String result = response.toString().trim();

                handler.post(() -> {
                    setLoading(false);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        boolean success = jsonObject.optBoolean("success", false);
                        String message = jsonObject.optString("message", "");

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                        if (success) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivity.this, "Resposta do Servidor: " + result, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                handler.post(() -> {
                    setLoading(false);
                    Toast.makeText(RegisterActivity.this, "Erro de Conexão: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
        btnBackToLogin.setEnabled(!isLoading);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}