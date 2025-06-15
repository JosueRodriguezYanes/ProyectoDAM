package com.example.proyectofinaldam135;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpedienteAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewAdmin);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSessionAdmin);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBHelper(this);

        // Cargar todos los expedientes
        cargarExpedientes();

        // Listener para cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cargarExpedientes() {
        List<Expediente> todosExpedientes = dbHelper.obtenerTodosExpedientes();
        adapter = new ExpedienteAdapter(todosExpedientes);
        recyclerView.setAdapter(adapter);
    }

    private void cerrarSesion() {
        // Limpiar SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        sharedPref.edit().clear().apply();

        // Redirigir a MainActivity y limpiar el stack
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}