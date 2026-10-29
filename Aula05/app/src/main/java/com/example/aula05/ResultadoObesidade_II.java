package com.example.aula05;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

    public class ResultadoObesidade_II extends AppCompatActivity {

        Button btnvoltar;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_resultado_obesidade2);

            btnvoltar = findViewById(R.id.btnvoltar);

            btnvoltar.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                finish();
                }
            });



            }
    }
