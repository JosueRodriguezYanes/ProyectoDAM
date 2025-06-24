package com.example.proyectofinaldam135;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etLoginUser, etLoginPassword;
    private Button btnLogin, btnGoToRegister;
    private DatabaseRepository databaseRepository;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_main);

        // Inicialización
        etLoginUser = findViewById(R.id.etLoginUser);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        databaseRepository = new DatabaseRepository(this);
        sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        // Verificar sesión existente
        if (isUserLoggedIn()) {
            redirectToApp();
        }

        btnLogin.setOnClickListener(v -> handleLogin());
        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private boolean isUserLoggedIn() {
        return !sharedPref.getString("usuario", "").isEmpty();
    }

    private void handleLogin() {
        String user = etLoginUser.getText().toString().trim();
        String pass = etLoginPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showToast("Usuario y contraseña obligatorios");
            return;
        }

        if (databaseRepository.checkUserPassword(user, pass)) {
            saveUserSession(user);
            redirectToApp();
        } else {
            showToast("Credenciales incorrectas");
        }
    }

    private void saveUserSession(String user) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("usuario", user);
        editor.putString("rol", databaseRepository.getUserRole(user));
        editor.apply();
    }

    private void redirectToApp() {
        String rol = sharedPref.getString("rol", "user");
        Intent intent;

        if (rol.equals("admin")) {
            intent = new Intent(this, AdminActivity.class);
            Toast.makeText(this, "Modo Administrador", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(this, InterfazUsuarioActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}