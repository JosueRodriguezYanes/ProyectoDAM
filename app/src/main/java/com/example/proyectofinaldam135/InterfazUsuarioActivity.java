package com.example.proyectofinaldam135;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InterfazUsuarioActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnNewExpedient, btnHistory;
    private ImageView btnMenu;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // Inicializar vistas
        tvWelcome = findViewById(R.id.tvWelcome);
        btnNewExpedient = findViewById(R.id.btnNewExpedient);
        btnHistory = findViewById(R.id.btnHistory);
        btnMenu = findViewById(R.id.btnMenu);
        sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        // Mostrar nombre de usuario
        String username = sharedPref.getString("usuario", "Usuario");
        tvWelcome.setText("Bienvenido, " + username + "!");

        // Configurar listeners
        btnNewExpedient.setOnClickListener(v -> {
            startActivity(new Intent(this, ExpedienteActivity.class));
        });

        btnHistory.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(InterfazUsuarioActivity.this, HistorialActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(InterfazUsuarioActivity.this,
                        "Error al abrir el historial", Toast.LENGTH_SHORT).show();
                Log.e("INTERFAZ_USUARIO", "Error al iniciar HistorialActivity", e);
            }
        });

        // Configurar menú
        btnMenu.setOnClickListener(this::showMenu);
    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_usuario, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_logout) {
                logout();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void logout() {
        // Limpiar preferencias
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        // Redirigir al login
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}