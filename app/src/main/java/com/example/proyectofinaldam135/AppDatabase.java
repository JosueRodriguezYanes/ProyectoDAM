package com.example.proyectofinaldam135;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Expediente.class, Usuario.class}, version = 4) // Incrementa a versión 4
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ExpedienteDao expedienteDao();
    public abstract UsuarioDao usuarioDao();

    // Migración de versión 3 a 4
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 1. Crear tabla temporal
            database.execSQL(
                    "CREATE TABLE expedientes_temp (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "usuario TEXT NOT NULL, " +
                            "cliente TEXT, " +
                            "telefono TEXT, " +
                            "direccion TEXT, " +
                            "problema TEXT NOT NULL, " +
                            "tipoServicio TEXT, " +
                            "urgencia TEXT, " +
                            "fecha TEXT, " +
                            "foto TEXT)");


            database.execSQL(
                    "INSERT INTO expedientes_temp (id, usuario, cliente, telefono, direccion, " +
                            "problema, tipoServicio, urgencia, fecha, foto) " +
                            "SELECT id, usuario, cliente, telefono, direccion, " +
                            "problema, tipoServicio, urgencia, fecha, foto FROM expedientes");


            database.execSQL("DROP TABLE expedientes");


            database.execSQL("ALTER TABLE expedientes_temp RENAME TO expedientes");
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "ProyectoDAM.db")
                    .addMigrations(MIGRATION_3_4)
                    .fallbackToDestructiveMigration() // Solo para desarrollo
                    .allowMainThreadQueries() // Solo para desarrollo
                    .build();

            // Insertar admin por defecto
            instance.runInTransaction(() -> {
                if (instance.usuarioDao().checkUsername("admin") == 0) {
                    instance.usuarioDao().insertUser(new Usuario("admin", "admin123", "admin"));
                }
            });
        }
        return instance;
    }
}