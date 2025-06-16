package com.example.proyectofinaldam135;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private static final String TAG = "HistorialActivity";
    private RecyclerView recyclerView;
    private ExpedienteAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_historial);

        Log.d(TAG, "Activity created");

        try {
            // 1. Inicialización segura de vistas
            initializeComponents();

            // 2. Obtener usuario actual con verificación
            String usuario = getCurrentUser();
            if (usuario == null || usuario.isEmpty()) {
                Log.w(TAG, "No se encontró usuario en SharedPreferences");
                showErrorAndFinish("No hay usuario autenticado");
                return;
            }

            // 3. Cargar y mostrar datos
            loadAndDisplayData(usuario);

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate", e);
            showErrorAndFinish("Error al cargar el historial");
            finishAffinity(); // Cierra completamente la app para evitar estados corruptos
        }
    }

    private void initializeComponents() {
        try {
            recyclerView = findViewById(R.id.recyclerView);
            if (recyclerView == null) {
                throw new IllegalStateException("RecyclerView no encontrado en el layout");
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            dbHelper = new DBHelper(this);
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando componentes", e);
            throw e;
        }
    }

    private String getCurrentUser() {
        try {
            SharedPreferences sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            return sharedPref.getString("usuario", "");
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo usuario", e);
            return "";
        }
    }

    private void loadAndDisplayData(String usuario) {
        try {
            List<Expediente> expedientes = dbHelper.getExpedientesPorUsuario(usuario);
            Log.d(TAG, "Expedientes obtenidos: " + (expedientes != null ? expedientes.size() : "null"));

            if (expedientes == null) {
                showErrorAndFinish("Error al cargar datos");
                return;
            }

            if (expedientes.isEmpty()) {
                showEmptyState();
                return;
            }

            setupRecyclerView(expedientes);
        } catch (Exception e) {
            Log.e(TAG, "Error cargando datos", e);
            throw e;
        }
    }

    private void setupRecyclerView(List<Expediente> expedientes) {
        try {
            adapter = new ExpedienteAdapter(expedientes, null);
            recyclerView.setAdapter(adapter);
            Log.d(TAG, "RecyclerView configurado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error configurando RecyclerView", e);
            throw e;
        }
    }

    private void showEmptyState() {
        runOnUiThread(() -> {
            Toast.makeText(this, "No hay expedientes registrados", Toast.LENGTH_LONG).show();
            // Opcional: Mostrar una vista alternativa
        });
    }

    private void showErrorAndFinish(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity destruida");
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}