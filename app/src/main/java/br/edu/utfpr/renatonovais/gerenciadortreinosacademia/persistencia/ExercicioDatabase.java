package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.Exercicio;

@Database(entities = {Exercicio.class}, version = 1, exportSchema = false)
public abstract class ExercicioDatabase extends RoomDatabase {

    public abstract ExercicioDao getExercicioDao();

    private static ExercicioDatabase instance;

    public static ExercicioDatabase getDatabase(final Context context){

        if (instance == null){

            synchronized (ExercicioDatabase.class){

                if (instance == null){

                    instance = Room.databaseBuilder(context,
                            ExercicioDatabase.class,
                            "exercicios.db").allowMainThreadQueries().build();
                }
            }
        }

        return instance;
    }

}
