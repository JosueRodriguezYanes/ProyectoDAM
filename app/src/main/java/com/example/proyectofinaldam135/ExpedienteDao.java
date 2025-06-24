package com.example.proyectofinaldam135;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpedienteDao {
    @Insert
    long insertExpediente(Expediente expediente);

    @Update
    int updateExpediente(Expediente expediente);

    @Delete
    int deleteExpediente(Expediente expediente);

    @Query("SELECT * FROM expedientes ORDER BY fecha DESC")
    LiveData<List<Expediente>> obtenerTodosExpedientes();

    @Query("SELECT * FROM expedientes WHERE usuario = :usuario ORDER BY fecha DESC")
    LiveData<List<Expediente>> getExpedientesPorUsuario(String usuario);

    @Query("UPDATE expedientes SET urgencia = 'Realizado' WHERE id = :idExpediente")
    int marcarComoRealizado(int idExpediente);

    @Query("SELECT * FROM expedientes WHERE usuario = :usuario")
    List<Expediente> getExpedientesPorUsuarioSync(String usuario);
}