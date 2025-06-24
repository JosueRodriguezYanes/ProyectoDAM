package com.example.proyectofinaldam135;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {
    @Insert
    long insertUser(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario")
    Usuario getUser(String usuario);

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND contrasena = :contrasena")
    Usuario checkUserPassword(String usuario, String contrasena);

    @Query("SELECT rol FROM usuarios WHERE usuario = :usuario")
    String getUserRole(String usuario);

    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :usuario")
    int checkUsername(String usuario);
}