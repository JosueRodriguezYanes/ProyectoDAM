package com.example.proyectofinaldam135;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VentanaI extends AppCompatActivity {

    Button BAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        BAdmin = findViewById(R.id.buttonAdmin);

        BAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VentanaI.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
