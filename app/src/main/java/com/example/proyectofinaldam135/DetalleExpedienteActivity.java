package com.example.proyectofinaldam135;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class DetalleExpedienteActivity extends AppCompatActivity {

    private TextView TCliente, TUsuario, TTelefono, TDireccion, TTipoServicio;
    private TextView TUrgencia, TFecha, TProblema;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_expediente);

        TCliente = findViewById(R.id.tvClienteDetalle);
        TUsuario = findViewById(R.id.tvUsuarioDetalle);
        TTelefono = findViewById(R.id.tvTelefonoDetalle);
        TDireccion = findViewById(R.id.tvDireccionDetalle);
        TTipoServicio = findViewById(R.id.tvTipoServicioDetalle);
        TUrgencia = findViewById(R.id.tvUrgenciaDetalle);
        TFecha = findViewById(R.id.tvFechaDetalle);
        TProblema = findViewById(R.id.tvProblemaDetalle);

        Intent intent = getIntent();
        if (intent != null) {
            String usuario = intent.getStringExtra("usuario");
            String cliente = intent.getStringExtra("cliente");
            String telefono = intent.getStringExtra("telefono");
            String direccion = intent.getStringExtra("direccion");
            String tipo = intent.getStringExtra("tipo");
            String urgencia = intent.getStringExtra("urgencia");
            String fecha = intent.getStringExtra("fecha");
            String problema = intent.getStringExtra("problema");

            TUsuario.setText("Registrado por: " + usuario);
            TCliente.setText(cliente);
            TTelefono.setText(telefono != null ? telefono : "N/D");
            TDireccion.setText("Dirección: " + (direccion != null ? direccion : "N/D"));
            TTipoServicio.setText(tipo != null ? tipo : "N/D");
            TUrgencia.setText(urgencia);
            TFecha.setText(fecha);
            TProblema.setText(problema);

        }

    }

}