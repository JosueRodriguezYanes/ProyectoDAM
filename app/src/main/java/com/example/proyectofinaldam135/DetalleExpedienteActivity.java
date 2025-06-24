package com.example.proyectofinaldam135;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DetalleExpedienteActivity extends AppCompatActivity {
    private TextView TCliente, TUsuario, TTelefono, TDireccion, TTipoServicio;
    private TextView TUrgencia, TFecha, TProblema;
    private Button btnRealizado;
    private DatabaseRepository databaseRepository;
    private int idExpediente;
    private Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_expediente);
        databaseRepository = new DatabaseRepository(this);

        // Configuración de la foto
        String fotoPath = getIntent().getStringExtra("foto");
        if (fotoPath != null && !fotoPath.isEmpty()) {
            ImageView ivFoto = findViewById(R.id.ivFotoDetalle);
            ivFoto.setVisibility(View.VISIBLE);
            ivFoto.setImageURI(Uri.parse(fotoPath));
        } else {
            ImageView ivFoto = findViewById(R.id.ivFotoDetalle);
            ivFoto.setVisibility(View.GONE);
        }

        // Inicialización de vistas
        TCliente = findViewById(R.id.tvClienteDetalle);
        TUsuario = findViewById(R.id.tvUsuarioDetalle);
        TTelefono = findViewById(R.id.tvTelefonoDetalle);
        TDireccion = findViewById(R.id.tvDireccionDetalle);
        TTipoServicio = findViewById(R.id.tvTipoServicioDetalle);
        TUrgencia = findViewById(R.id.tvUrgenciaDetalle);
        TFecha = findViewById(R.id.tvFechaDetalle);
        TProblema = findViewById(R.id.tvProblemaDetalle);
        btnRealizado = findViewById(R.id.btnrealizado);
        Button btnAtras = findViewById(R.id.btnatras);

        // Obtención de los datos del intent
        Intent intent = getIntent();
        if (intent != null) {
            idExpediente = intent.getIntExtra("id", -1);
            String usuario = intent.getStringExtra("usuario");
            String cliente = intent.getStringExtra("cliente");
            String telefono = intent.getStringExtra("telefono");
            String direccion = intent.getStringExtra("direccion");
            String tipo = intent.getStringExtra("tipo");
            String urgencia = intent.getStringExtra("urgencia");
            String fecha = intent.getStringExtra("fecha");
            String problema = intent.getStringExtra("problema");

            // Mostrar datos en las vistas
            TUsuario.setText("Registrado por: " + usuario);
            TCliente.setText(cliente);
            TTelefono.setText(telefono != null ? telefono : "N/D");
            TDireccion.setText("Dirección: " + (direccion != null ? direccion : "N/D"));
            TTipoServicio.setText(tipo != null ? tipo : "N/D");
            TUrgencia.setText(urgencia);
            TFecha.setText(fecha);
            TProblema.setText(problema);

            if ("Realizado".equals(urgencia)) {
                btnRealizado.setEnabled(false);
                btnRealizado.setText("Ya realizado");
            }
        }

        btnEliminar = findViewById(R.id.btneliminar);
        btnEliminar.setOnClickListener(v -> {
            if (idExpediente != -1) {
                new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que deseas eliminar este expediente?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            boolean exito = databaseRepository.eliminarExpediente(idExpediente);
                            if (exito) {
                                Toast.makeText(this, "Expediente eliminado", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        // Configurar botones
        btnAtras.setOnClickListener(v -> finish());

        // Configurar botón Realizado
        btnRealizado.setOnClickListener(v -> {
            if (idExpediente != -1) {
                boolean exito = databaseRepository.marcarExpedienteComoRealizado(idExpediente);

                if (exito) {
                    // Actualizar la UI inmediatamente
                    TUrgencia.setText("Realizado");
                    btnRealizado.setEnabled(false);
                    btnRealizado.setText("Ya realizado");

                    Toast.makeText(this, "Expediente marcado como Realizado", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);

                    // Opcional: cerrar la actividad después de un breve retraso
                    new Handler().postDelayed(this::finish, 1000);
                } else {
                    Toast.makeText(this, "Error al marcar como realizado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}