package com.example.proyectofinaldam135;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirmPassword;
    Button btnRegister;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aactivity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUsername.getText().toString();
                String pass = etPassword.getText().toString();
                String confirm = etConfirmPassword.getText().toString();

                if(user.equals("") || pass.equals("") || confirm.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean userExists = dbHelper.checkUsername(user);
                    if (userExists) {
                        Toast.makeText(RegisterActivity.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                    } else {
                        // Dentro del onClick del botón de registro en RegisterActivity.java

                        Boolean insert = dbHelper.insertUser(user, pass, "usuario");
                        if (insert) {
                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class)); // Redirige al login
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
