/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author crjav
 */
public class Errores {
    private String tipo;
    private String desc;
    private int linea;
    private int columna;

    public Errores(String tipo, String desc, int linea, int columna) {
        this.tipo = tipo;
        this.desc = desc;
        this.linea = linea;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDesc() {
        return desc;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
    
    public String toString(){
        return "Error: tipo=" + tipo + ", decripcion= " + desc + ", linea=" + linea + ", Columna=" + columna;
    }
}
