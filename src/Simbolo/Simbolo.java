package Simbolo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author crjav
 */
public class Simbolo {
    private Tipo tipo;
    private String id;
    private Object valor;
    private int linea;
    private int columna;
    private String entorno; // Nombre del entorno donde se declaró (GLOBAL, WHILE, FOR, etc.)

    public Simbolo(Tipo tipo, String id, Object valor) {
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
        this.linea = 0;
        this.columna = 0;
        this.entorno = "GLOBAL";
    }

    public Simbolo(Tipo tipo, String id) {
        this.tipo = tipo;
        this.id = id;
        this.linea = 0;
        this.columna = 0;
        this.entorno = "GLOBAL";
    }
    
    public Simbolo(Tipo tipo, String id, Object valor, int linea, int columna) {
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
        this.entorno = "GLOBAL";
    }
    
    public Simbolo(Tipo tipo, String id, Object valor, int linea, int columna, String entorno) {
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
        this.entorno = entorno;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public Object getValor() {
        return valor;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getEntorno() {
        return entorno;
    }

    public void setEntorno(String entorno) {
        this.entorno = entorno;
    }
}
