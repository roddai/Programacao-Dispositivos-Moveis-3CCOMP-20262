package com.example.apppets;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DetalhesActivity extends AppCompatActivity {

    private TextView tvInfoPet;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes);

        tvInfoPet = findViewById(R.id.tvInfoPet);
        btnVoltar = findViewById(R.id.btnVoltar);

        String info = getIntent().getStringExtra("Info");
        if (info != null) {
            tvInfoPet.setText(info);
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}