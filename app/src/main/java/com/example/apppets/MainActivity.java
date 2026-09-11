package com.example.apppets;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Pet rex, luna, skibi, oggy, tom, larry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top,
                    systemBars.right, systemBars.bottom);
            return insets;
        });

        rex = new Pet("Rex", "Marrom", "Labrador", "10/05/2023", 3);
        luna = new Pet("Luna", "Branca", "Poodle", "15/08/2023", 2);
        skibi = new Pet("Skibi", "Preto", "Vira-lata", "20/02/2024", 1);

        oggy = new Pet("Oggy", "Cinza", "Siamês", "12/01/2022", 4);
        tom = new Pet("Tom", "Laranja", "Persa", "18/06/2023", 3);
        larry = new Pet("Larry", "Branco", "Angorá", "05/09/2023", 2);
    }
}