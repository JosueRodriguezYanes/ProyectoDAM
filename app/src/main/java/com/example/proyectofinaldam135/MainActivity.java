package com.example.proyectofinaldam135;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.Context;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button BSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BSalir = findViewById(R.id.buttonSalir);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BSalir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Desea cerrar la sesión y salir de la aplicación?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);


            }
        });

    }
}