package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.enums.Regiao;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.enums.Tipo;

@Entity
public class Exercicio {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String nome;
    private Tipo tipo;
    private Regiao regiao;
    private boolean requerAnilhaOuHalter;

    public static Comparator comparator = new Comparator<Exercicio>() {
        @Override
        public int compare(Exercicio exercicio1, Exercicio exercicio2) {
            return exercicio1.getNome().compareTo(exercicio2.getNome());
        }
    };

    public Exercicio(String nome, Tipo tipo, Regiao regiao, boolean requerAnilhaOuHalter) {
        this.nome = nome;
        this.tipo = tipo;
        this.regiao = regiao;
        this.requerAnilhaOuHalter = requerAnilhaOuHalter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }

    public boolean isRequerAnilhaOuHalter() {
        return requerAnilhaOuHalter;
    }

    public void setRequerAnilhaOuHalter(boolean requerAnilhaOuHalter) {
        this.requerAnilhaOuHalter = requerAnilhaOuHalter;
    }

    @Override
    public String toString() {
        return getNome() + " - " +
                getTipo() + " - " +
                getRegiao() + " - " +
                (isRequerAnilhaOuHalter() ? "Sim" : "Não");
    }
}
