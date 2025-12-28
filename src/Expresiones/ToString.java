package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 * Función toString() - Convierte valores a String
 * Sintaxis: toString(42) retorna "42"
 * toString(3.14) retorna "3.14"
 * toString(true) retorna "true"
 */
public class ToString extends Instruccion {
    private Instruccion expresion;

    public ToString(Instruccion expresion, int linea, int columna) {
        super(new Tipo(tipoDato.CADENA), linea, columna);
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Evaluar la expresión
        Object valor = this.expresion.interpretar(arbol, tabla);

        if (valor instanceof Errores) {
            return valor;
        }

        tipoDato tipoValor = this.expresion.tipo.getTipo();

        // Tipos permitidos: int, double, char, bool
        switch (tipoValor) {
            case ENTERO -> {
                this.tipo = new Tipo(tipoDato.CADENA);
                return String.valueOf(valor);
            }
            case DECIMAL -> {
                this.tipo = new Tipo(tipoDato.CADENA);
                return String.valueOf(valor);
            }
            case CARACTER -> {
                this.tipo = new Tipo(tipoDato.CADENA);
                return String.valueOf(valor);
            }
            case BOOLEANO -> {
                this.tipo = new Tipo(tipoDato.CADENA);
                return String.valueOf(valor);
            }
            case CADENA -> {
                // Si ya es string, retornarlo tal cual
                this.tipo = new Tipo(tipoDato.CADENA);
                return (String) valor;
            }
            default -> {
                return new Errores(
                        "SEMANTICO",
                        "toString() solo acepta tipos: int, double, char, bool. Se recibió: " + tipoValor,
                        this.linea,
                        this.columna);
            }
        }
    }
}