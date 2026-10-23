package com.example.provani1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class resultadoInfo extends AppCompatActivity {

    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.resultado_info);

        TextView resultfinal = findViewById(R.id.resultfinal);

        String resultado = getIntent().getStringExtra("resultado");

        resultfinal.setText(resultado);

    }
}