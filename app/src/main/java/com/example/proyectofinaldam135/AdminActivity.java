package com.example.proyectofinaldam135;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExpedienteAdapter adapter;
    private DatabaseRepository databaseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_admin);

        recyclerView = findViewById(R.id.recyclerViewAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseRepository = new DatabaseRepository(this);

        Button btnCerrarSesion = findViewById(R.id.btnCerrarSessionAdmin);
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());

        cargarExpedientes();
    }

    private void cargarExpedientes() {
        databaseRepository.getTodosExpedientesLiveData().observe(this, new Observer<List<Expediente>>() {
            @Override
            public void onChanged(List<Expediente> expedientes) {
                if (adapter == null) {
                    adapter = new ExpedienteAdapter(expedientes, expediente -> {
                        Intent intent = new Intent(AdminActivity.this, DetalleExpedienteActivity.class);
                        intent.putExtra("id", expediente.getId());
                        intent.putExtra("usuario", expediente.getUsuario());
                        intent.putExtra("cliente", expediente.getCliente());
                        intent.putExtra("telefono", expediente.getTelefono());
                        intent.putExtra("direccion", expediente.getDireccion());
                        intent.putExtra("problema", expediente.getProblema());
                        intent.putExtra("fecha", expediente.getFecha());
                        intent.putExtra("tipo", expediente.getTipoServicio());
                        intent.putExtra("urgencia", expediente.getUrgencia());
                        intent.putExtra("foto", expediente.getFoto());
                        startActivityForResult(intent, 1);
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.expedientes = expedientes;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            cargarExpedientes();
        }
    }

    private void cerrarSesion() {
        SharedPreferences sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        sharedPref.edit().clear().apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}