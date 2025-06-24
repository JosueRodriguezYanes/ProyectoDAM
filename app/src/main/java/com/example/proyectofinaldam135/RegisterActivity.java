package com.example.proyectofinaldam135;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private DatabaseRepository databaseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        databaseRepository = new DatabaseRepository(this);

        btnRegister.setOnClickListener(v -> {
            String user = etUsername.getText().toString();
            String pass = etPassword.getText().toString();
            String confirm = etConfirmPassword.getText().toString();

            if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(RegisterActivity.this,
                        "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(confirm)) {
                Toast.makeText(RegisterActivity.this,
                        "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                boolean userExists = databaseRepository.checkUsername(user);
                if (userExists) {
                    Toast.makeText(RegisterActivity.this,
                            "El usuario ya existe", Toast.LENGTH_SHORT).show();
                } else {
                    boolean insert = databaseRepository.insertUser(user, pass, "usuario");
                    if (insert) {
                        Toast.makeText(RegisterActivity.this,
                                "Registro exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}