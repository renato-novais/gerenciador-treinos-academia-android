package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.enums;

public enum Regiao {
    Peito, //ordinal 0
    Costas, //ordinal 1
    Ombros, //ordinal 2
    Bracos, //ordinal 3
    Pernas, //ordinal 4
    Abdomen, //ordinal 5
    Aerobica; //ordinal 6

    public static Regiao fromString(String regiao) {
        switch (regiao.toLowerCase()) {
            case "peito":
                return Peito;

            case "costas":
                return Costas;

            case "ombros":
                return Ombros;

            case "bracos":
            case "braços":
                return Bracos;

            case "pernas":
                return Pernas;

            case "abdomen":
            case "abdômen":
                return Abdomen;

            case "aerobica":
            case "aeróbica":
                return Aerobica;

            default:
                throw new IllegalArgumentException("Região inválida: " + regiao);
        }
    }
}
