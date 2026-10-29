package com.example.provani1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Forms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forms_activity);

        RadioGroup resp1 = findViewById(R.id.resp1);
        RadioGroup resp2 = findViewById(R.id.resp2);
        RadioGroup resp3 = findViewById(R.id.resp3);
        RadioButton op1 = findViewById(R.id.op1);
        RadioButton op2 = findViewById(R.id.op2);
        RadioButton op3 = findViewById(R.id.op3);
        RadioButton opt1 = findViewById(R.id.opt1);
        RadioButton opt2 = findViewById(R.id.opt2);
        RadioButton opt3 = findViewById(R.id.opt3);
        RadioButton opc1 = findViewById(R.id.opc1);
        RadioButton opc2 = findViewById(R.id.opc2);
        RadioButton opc3 = findViewById(R.id.opc3);
        Button btnResult = findViewById(R.id.btnResult);


        btnResult.setOnClickListener(v -> {

            Integer result1 = op1.isChecked() ? 1 : op2.isChecked() ? 2 : op3.isChecked() ? 3 : 0;
            Integer result2 = opt1.isChecked() ? 1 : opt2.isChecked() ? 2 : opt3.isChecked() ? 3 : 0;
            Integer result3 = opc1.isChecked() ? 1 : opc2.isChecked() ? 2 : opc3.isChecked() ? 3 : 0;

            Integer result4 = result1 + result2 + result3;
            String resultado = "";

            if (result4 >= 1 && result4 <= 3) {
                resultado = "Você é uma pessoa aberta, extrovertida e organizada";
            } else if (result4 >= 4 && result4 <= 6) {
                resultado = "Você é uma pessoa adaptável e moderada";
            } else if (result4 >= 7 && result4 <= 9) {
                resultado = "Você é uma pessoa introspectiva, rígida ou espontânea";
            } else {
                resultado = "Resultado inconclusivo.";
            }


            Intent irParaResultado = new Intent(Forms.this, resultadoInfo.class);
            irParaResultado.putExtra("resultado", resultado);
            startActivity(irParaResultado);

        });
    }
}
