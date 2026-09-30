package com.example.apppets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String editNome, editCor, editRaca, editAniversario;
    private int editidade;
    private Button btnCachorro1, BtnCachorro2, btnCachorro3, btnGato1, btnGato2, btnGato3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        Button btnCachorro1 = findViewById(R.id.btnCachorro1);
        btnCachorro1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, Cachorro1Activity.class);
                                                startActivity(intent);

                                            }
                                        });
         Button btnCachorro2 = findViewById(R.id.btnCachorro2);
        btnCachorro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cachorro2Activity.class);
                startActivity(intent);

            }
            });
        Button btnCachorro3 = findViewById(R.id.btnCachorro3);
        btnCachorro3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, Cachorro3Activity.class);
                                                startActivity(intent);

                                            }
                                        });
        Button btnGato1 = findViewById(R.id.btnGato);
        btnGato1.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, Gato1Activity.class);
                startActivity(intent);

            }
            });
        Button btnGato2 = findViewById(R.id.btnGato2);
        btnGato2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, Gato2Activity.class);
                                            startActivity(intent);

                                        }
                                    });
        Button btnGato3 = findViewById(R.id.btnGato3);
        btnGato3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Gato3Activity.class);
                startActivity(intent);
            }
        });
    }
}


