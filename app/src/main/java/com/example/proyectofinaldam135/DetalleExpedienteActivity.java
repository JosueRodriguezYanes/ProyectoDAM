    package com.example.proyectofinaldam135;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import android.os.Handler;
    import android.app.AlertDialog;

    public class DetalleExpedienteActivity extends AppCompatActivity {
        private TextView TCliente, TUsuario, TTelefono, TDireccion, TTipoServicio;
        private TextView TUrgencia, TFecha, TProblema;
        private Button btnRealizado;
        private DBHelper dbHelper;
        private int idExpediente;
        private Button btnEliminar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detalle_expediente);
            dbHelper = new DBHelper(this);

            // Inicializacion de vistas

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

            // Obtenecion de los datos del intent
            Intent intent = getIntent();
            if (intent != null) {
                idExpediente = intent.getIntExtra("id", -1); // Obtener el ID del expediente

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
                    new AlertDialog.Builder(this)
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Estás seguro de que deseas eliminar este expediente?")
                            .setPositiveButton("Eliminar", (dialog, which) -> {
                                boolean exito = dbHelper.eliminarExpediente(idExpediente);
                                if (exito) {
                                    Toast.makeText(this, "Expediente eliminado", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    Toast.makeText(this, "Error al eliminar expediente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                }
            });
            // Configurar botónes
            btnAtras.setOnClickListener(v -> finish());

            // Configurar botón Realizado
            btnRealizado.setOnClickListener(v -> {
                if (idExpediente != -1) {
                    boolean exito = dbHelper.marcarExpedienteComoRealizado(idExpediente);

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

        @Override
        protected void onDestroy() {
            dbHelper.close();
            super.onDestroy();
        }
    }