package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 * Casteo - Conversión explícita de tipos
 * Sintaxis: (tipo) expresion
 */
public class Casteo extends Instruccion {
    private Tipo tipoDestino;
    private Instruccion expresion;

    public Casteo(Tipo tipoDestino, Instruccion expresion, int linea, int columna) {
        super(tipoDestino, linea, columna);
        this.tipoDestino = tipoDestino;
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Evaluar la expresión a castear
        Object valor = this.expresion.interpretar(arbol, tabla);
        
        if (valor instanceof Errores) {
            return valor;
        }
        
        tipoDato tipoOrigen = this.expresion.tipo.getTipo();
        tipoDato tipoNuevo = this.tipoDestino.getTipo();
        
        // === INT A DOUBLE ===
        if (tipoOrigen == tipoDato.ENTERO && tipoNuevo == tipoDato.DECIMAL) {
            this.tipo = new Tipo(tipoDato.DECIMAL);
            return ((Integer) valor).doubleValue();
        }
        
        // === DOUBLE A INT ===
        if (tipoOrigen == tipoDato.DECIMAL && tipoNuevo == tipoDato.ENTERO) {
            this.tipo = new Tipo(tipoDato.ENTERO);
            return ((Double) valor).intValue(); // Trunca el decimal
        }
        
        // === INT A STRING ===
        if (tipoOrigen == tipoDato.ENTERO && tipoNuevo == tipoDato.CADENA) {
            this.tipo = new Tipo(tipoDato.CADENA);
            return String.valueOf(valor);
        }
        
        // === INT A CHAR ===
        if (tipoOrigen == tipoDato.ENTERO && tipoNuevo == tipoDato.CARACTER) {
            this.tipo = new Tipo(tipoDato.CARACTER);
            int ascii = (Integer) valor;
            return (char) ascii; // Convierte código ASCII a char
        }
        
        // === DOUBLE A STRING ===
        if (tipoOrigen == tipoDato.DECIMAL && tipoNuevo == tipoDato.CADENA) {
            this.tipo = new Tipo(tipoDato.CADENA);
            return String.valueOf(valor);
        }
        
        // === CHAR A INT ===
        if (tipoOrigen == tipoDato.CARACTER && tipoNuevo == tipoDato.ENTERO) {
            this.tipo = new Tipo(tipoDato.ENTERO);
            return (int) ((Character) valor); // Código ASCII del char
        }
        
        // === CHAR A DOUBLE ===
        if (tipoOrigen == tipoDato.CARACTER && tipoNuevo == tipoDato.DECIMAL) {
            this.tipo = new Tipo(tipoDato.DECIMAL);
            return (double) ((Character) valor); // Código ASCII como double
        }
        
        // Casteo no permitido
        return new Errores(
            "SEMANTICO", 
            "No se puede castear de " + tipoOrigen + " a " + tipoNuevo, 
            this.linea, 
            this.columna
        );
    }
}
