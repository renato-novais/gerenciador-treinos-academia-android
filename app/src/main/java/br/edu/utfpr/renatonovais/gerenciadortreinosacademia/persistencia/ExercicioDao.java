package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.Exercicio;

@Dao
public interface ExercicioDao {

    @Insert
    long insert(Exercicio exercicio);

    @Delete
    int delete(Exercicio exercicio);

    @Update
    int update(Exercicio exercicio);

    @Query("SELECT * FROM exercicio WHERE id = :id")
    Exercicio queryForId(long id);

    @Query("SELECT * FROM exercicio ORDER BY nome ASC")
    List<Exercicio> queryAllAscending();

    @Query("SELECT * FROM exercicio ORDER BY nome DESC")
    List<Exercicio> queryAllDownward();

}
