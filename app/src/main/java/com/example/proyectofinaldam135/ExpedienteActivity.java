package com.example.proyectofinaldam135;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpedienteActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputEditText etCliente, etTelefono, etDireccion, etProblema;
    private Button btnEnviar, btnAdjuntarFoto;
    private ImageView ivPreview;
    private Spinner spTipoServicio;
    private RadioGroup rgUrgencia;
    private String currentPhotoPath;
    private DatabaseRepository dbHelper;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expediente);

        // Inicialización de vistas
        etCliente = findViewById(R.id.etCliente);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        etProblema = findViewById(R.id.etProblema);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnAdjuntarFoto = findViewById(R.id.btnAdjuntarFoto);
        ivPreview = findViewById(R.id.ivPreview);
        spTipoServicio = findViewById(R.id.spTipoServicio);
        rgUrgencia = findViewById(R.id.rgUrgencia);

        dbHelper = new DatabaseRepository(this);
        sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        // configuracion del spinner
        setupSpinner();

        btnAdjuntarFoto.setOnClickListener(v -> seleccionarFoto());
        btnEnviar.setOnClickListener(v -> enviarExpediente());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_servicio, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoServicio.setAdapter(adapter);
    }

    private void seleccionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivPreview.setImageURI(selectedImage);
            ivPreview.setVisibility(View.VISIBLE);
            currentPhotoPath = selectedImage.toString();
        }
    }

    private void enviarExpediente() {
        // Obtenecion de los valores
        String cliente = etCliente.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String problema = etProblema.getText().toString().trim();
        String tipoServicio = spTipoServicio.getSelectedItem().toString();
        String urgencia = rgUrgencia.getCheckedRadioButtonId() == R.id.rbUrgente ? "Urgente" : "Normal";
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        String usuario = sharedPref.getString("usuario", "");
        String fotoPath = currentPhotoPath != null ? currentPhotoPath : "";

        // aqui se validan los datos antes de insertar
        if (!dbHelper.checkUsername(usuario)) {
            Toast.makeText(this, "ERROR: El usuario no existe en el sistema", Toast.LENGTH_LONG).show();
            Log.e("EXPEDIENTE_ERROR", "Usuario no encontrado: " + usuario);
            return;
        }

        Log.d("DB_DEBUG", "=== DATOS DEL EXPEDIENTE ===");
        Log.d("DB_DEBUG", "Usuario: " + usuario);
        Log.d("DB_DEBUG", "Cliente: " + cliente);
        Log.d("DB_DEBUG", "Teléfono: " + telefono);
        Log.d("DB_DEBUG", "Dirección: " + direccion);
        Log.d("DB_DEBUG", "Problema: " + problema);
        Log.d("DB_DEBUG", "Tipo Servicio: " + tipoServicio);
        Log.d("DB_DEBUG", "Urgencia: " + urgencia);
        Log.d("DB_DEBUG", "Fecha: " + fecha);
        Log.d("DB_DEBUG", "Foto Path: " + (fotoPath.isEmpty() ? "No hay foto" : fotoPath));
        Log.d("DB_DEBUG", "==========================");

        // inserscion
        try {
            boolean success = dbHelper.insertExpediente(
                    usuario,
                    cliente,
                    telefono,
                    direccion,
                    problema,
                    tipoServicio,
                    urgencia,
                    fecha,
                    fotoPath
            );

            if (success) {
                Toast.makeText(this, "Expediente guardado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar. Verifica los datos.", Toast.LENGTH_SHORT).show();
                Log.e("EXPEDIENTE_ERROR", "Error al insertar en BD. Revisar logs anteriores.");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error crítico. Contacta al soporte.", Toast.LENGTH_LONG).show();
            Log.e("EXPEDIENTE_CRASH", "Error: " + e.getMessage(), e);
        }
    }

    private boolean validarDatos(String cliente, String problema, String tipoServicio) {
        boolean valido = true;

        if (cliente.isEmpty()) {
            etCliente.setError("Nombre del cliente es obligatorio");
            valido = false;
        }

        if (problema.isEmpty()) {
            etProblema.setError("Descripción del problema es obligatoria");
            valido = false;
        }

        if (tipoServicio.equals("Seleccione tipo")) {
            Toast.makeText(this, "Seleccione un tipo de servicio", Toast.LENGTH_SHORT).show();
            valido = false;
        }

        return valido;
    }
}