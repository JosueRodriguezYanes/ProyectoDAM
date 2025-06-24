package com.example.proyectofinaldam135;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DatabaseRepository {
    private ExpedienteDao expedienteDao;
    private UsuarioDao usuarioDao;
    private LiveData<List<Expediente>> todosExpedientes;

    public DatabaseRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        expedienteDao = db.expedienteDao();
        usuarioDao = db.usuarioDao();
        todosExpedientes = expedienteDao.obtenerTodosExpedientes();
    }

    // Métodos para usuarios
    public boolean insertUser(String usuario, String contrasena, String rol) {
        try {
            long resultado = usuarioDao.insertUser(new Usuario(usuario, contrasena, rol));
            return resultado != -1;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error al insertar usuario", e);
            return false;
        }
    }

    public boolean checkUsername(String usuario) {
        return usuarioDao.checkUsername(usuario) > 0;
    }

    public boolean checkUserPassword(String usuario, String contrasena) {
        return usuarioDao.checkUserPassword(usuario, contrasena) != null;
    }

    public String getUserRole(String usuario) {
        try {
            String rol = usuarioDao.getUserRole(usuario);
            return rol != null ? rol : "user";
        } catch (Exception e) {
            e.printStackTrace();
            return "user";
        }
    }

    // Métodos para expedientes
    public boolean insertExpediente(String usuario, String cliente, String telefono,
                                    String direccion, String problema, String tipoServicio,
                                    String urgencia, String fecha, String fotoPath) {
        try {
            Expediente expediente = new Expediente(usuario, cliente, telefono, direccion,
                    problema, tipoServicio, urgencia, fecha, fotoPath);
            long result = expedienteDao.insertExpediente(expediente);
            return result != -1;
        } catch (Exception e) {
            Log.e("DB_EXCEPTION", "Error al insertar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Expediente> getExpedientesPorUsuario(String usuario) {
        return expedienteDao.getExpedientesPorUsuarioSync(usuario);
    }

    public List<Expediente> obtenerTodosExpedientes() {
        return expedienteDao.obtenerTodosExpedientes().getValue(); // Cuidado con esto en producción
    }

    public boolean marcarExpedienteComoRealizado(int idExpediente) {
        try {
            int rowsAffected = expedienteDao.marcarComoRealizado(idExpediente);
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error al marcar como realizado", e);
            return false;
        }
    }

    public boolean eliminarExpediente(int idExpediente) {
        try {
            Expediente expediente = new Expediente("", "", "", "", "", "", "", "", "");
            expediente.setId(idExpediente);
            int rowsAffected = expedienteDao.deleteExpediente(expediente);
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error al eliminar expediente", e);
            return false;
        }
    }

    public LiveData<List<Expediente>> getTodosExpedientesLiveData() {
        return todosExpedientes;
    }

}