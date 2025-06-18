    package com.example.proyectofinaldam135;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;
    import java.util.List;
    import java.util.ArrayList;


    public class DBHelper extends SQLiteOpenHelper {
        public static final String DBNAME = "ProyectoDAM.db";

        public DBHelper(Context context) {
            super(context, DBNAME, null, 2); // Cambiado de 1 a 2
            checkAdminUser();
        }


        public String getUserRole(String usuario) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT rol FROM usuarios WHERE usuario = ?", new String[]{usuario});
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(0);
                }
                return "user";
            } catch (Exception e) {
                e.printStackTrace();
                return "user";
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Tabla de usuarios
            db.execSQL("CREATE TABLE usuarios (" +
                    "usuario TEXT PRIMARY KEY, " +
                    "contrasena TEXT, " +
                    "rol TEXT)");

            // Tabla de expedientes (versión actualizada)
            db.execSQL("CREATE TABLE expedientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "usuario TEXT NOT NULL, " +
                    "cliente TEXT, " +
                    "telefono TEXT, " +
                    "direccion TEXT, " +
                    "problema TEXT NOT NULL, " +
                    "tipoServicio TEXT, " +
                    "urgencia TEXT, " +
                    "fecha TEXT, " +
                    "foto TEXT)");



            // Insertar admin por defecto
            ContentValues admin = new ContentValues();
            admin.put("usuario", "admin");
            admin.put("contrasena", "admin123");
            admin.put("rol", "admin");
            db.insert("usuarios", null, admin);
        }

        public boolean marcarExpedienteComoRealizado(int idExpediente) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("urgencia", "Realizado");

            try {
                int rowsAffected = db.update(
                        "expedientes",
                        values,
                        "id = ?",
                        new String[]{String.valueOf(idExpediente)}
                );
                return rowsAffected > 0;
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error al marcar como realizado", e);
                return false;
            } finally {

            }
        }
        public boolean eliminarExpediente(int idExpediente) {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                int rowsAffected = db.delete(
                        "expedientes",
                        "id = ?",
                        new String[]{String.valueOf(idExpediente)}
                );
                return rowsAffected > 0;
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error al eliminar expediente", e);
                return false;
            }
        }
        public void checkAdminUser() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = 'admin'", null);
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    // Insertar admin si no existe
                    ContentValues values = new ContentValues();
                    values.put("usuario", "admin");
                    values.put("contrasena", "admin123");
                    values.put("rol", "admin");
                    db.insert("usuarios", null, values);
                    Log.d("DB_DEBUG", "Usuario admin creado");
                } else {
                    Log.d("DB_DEBUG", "Admin ya existe");
                }
                cursor.close();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS usuarios");
            db.execSQL("DROP TABLE IF EXISTS expedientes");
            onCreate(db);
        }

        // Métodos para usuarios
        public boolean insertUser(String usuario, String contrasena, String rol) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("usuario", usuario);
            values.put("contrasena", contrasena);
            values.put("rol", rol);
            long resultado = db.insert("usuarios", null, values);
            return resultado != -1;
        }

        public boolean checkUsername(String usuario) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", new String[]{usuario});
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        }

        public boolean checkUserPassword(String usuario, String contrasena) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?",
                        new String[]{usuario, contrasena});
                return cursor.getCount() > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        public List<Expediente> getExpedientesPorUsuario(String usuario) {
            List<Expediente> expedientes = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.rawQuery("SELECT * FROM expedientes WHERE usuario = ?", new String[]{usuario});

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        try {
                            Expediente expediente = new Expediente(
                                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("usuario")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("cliente")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("problema")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("tipoServicio")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("urgencia")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                            );
                            expedientes.add(expediente);
                        } catch (IllegalArgumentException e) {
                            Log.e("DB_ERROR", "Error al leer columna: " + e.getMessage());
                            // Manejar el error o saltar este registro
                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error general al obtener expedientes", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return expedientes;
        }
        public void verificarEstructuraTabla() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("PRAGMA table_info(expedientes)", null);
                Log.d("DB_STRUCTURE", "=== ESTRUCTURA DE TABLA EXPEDIENTES ===");
                while (cursor.moveToNext()) {
                    String colName = cursor.getString(1);
                    String colType = cursor.getString(2);
                    Log.d("DB_STRUCTURE", "Columna: " + colName + " | Tipo: " + colType);
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }


        public List<Expediente> obtenerTodosExpedientes() {
            List<Expediente> expedientes = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.rawQuery("SELECT * FROM expedientes ORDER BY fecha DESC", null);

                if (cursor.moveToFirst()) {
                    do {
                        Expediente expediente = new Expediente(
                                cursor.getInt(0),    // id
                                cursor.getString(1), // usuario
                                cursor.getString(2), // cliente
                                cursor.getString(3), // telefono
                                cursor.getString(4), // direccion
                                cursor.getString(5), // problema
                                cursor.getString(6), // tipoServicio
                                cursor.getString(7), // urgencia
                                cursor.getString(8)  // fecha
                        );

                        expedientes.add(expediente);
                    } while (cursor.moveToNext());
                }
            } finally {
                if (cursor != null) cursor.close();
            }
            return expedientes;
        }
        // Metodo
        public boolean insertExpediente(String usuario, String cliente, String telefono,
                                        String direccion, String problema, String tipoServicio,
                                        String urgencia, String fecha, String fotoPath) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            // Validación reforzada

            if (usuario == null || usuario.isEmpty()) {
                Log.e("DB_ERROR", "Usuario no puede ser nulo");
                return false;
            }

            Log.d("DB_DEBUG", "Intentando insertar expediente para usuario: " + usuario);

            try {
                // Asignación segura de valores
                values.put("usuario", usuario);
                values.put("cliente", cliente != null ? cliente : "");
                values.put("telefono", telefono != null ? telefono : "");
                values.put("direccion", direccion != null ? direccion : "");
                values.put("problema", problema != null ? problema : "");
                values.put("tipoServicio", tipoServicio != null ? tipoServicio : "General");
                values.put("urgencia", urgencia != null ? urgencia : "Normal");
                values.put("fecha", fecha != null ? fecha : "");
                values.put("foto", fotoPath != null ? fotoPath : "");

                // Transacción explícita
                db.beginTransaction();
                try {
                    long result = db.insert("expedientes", null, values);
                    db.setTransactionSuccessful();

                    if (result == -1) {
                        Log.e("DB_ERROR", "Error en insert. Código: " + result);
                        // Verifica constraints
                        Cursor cursor = db.rawQuery("SELECT sql FROM sqlite_master WHERE name = 'expedientes'", null);
                        if (cursor.moveToFirst()) {
                            Log.d("DB_STRUCTURE", "Estructura tabla: " + cursor.getString(0));
                        }
                        cursor.close();
                        return false;
                    }

                    Log.d("DB_SUCCESS", "Expediente insertado. ID: " + result);
                    return true;
                } finally {
                    db.endTransaction();
                }
            } catch (Exception e) {
                Log.e("DB_EXCEPTION", "Error al insertar: " + e.getMessage());
                e.printStackTrace();
                return false;
            }

        }
    }