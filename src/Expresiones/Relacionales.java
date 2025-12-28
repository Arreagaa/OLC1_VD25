/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 *
 * @author crjav
 */
public class Relacionales extends Instruccion {

    private Instruccion cond1;
    private Instruccion cond2;
    private OperadoresRelacionales relacional;

    public Relacionales(Instruccion cond1, Instruccion cond2, OperadoresRelacionales relacional, int linea, int columna) {
        super(new Tipo(tipoDato.BOOLEANO), linea, columna);
        this.cond1 = cond1;
        this.cond2 = cond2;
        this.relacional = relacional;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        var condIzq = this.cond1.interpretar(arbol, tabla);
        if (condIzq instanceof Errores) {
            return condIzq;
        }

        var condDer = this.cond2.interpretar(arbol, tabla);
        if (condDer instanceof Errores) {
            return condDer;
        }

        return switch (relacional) {
            case EQUALS ->
                this.equals(condIzq, condDer);
            case NOTEQUALS ->
                this.notEquals(condIzq, condDer);
            case MENOR ->
                this.menor(condIzq, condDer);
            case MENORIGUAL ->
                this.menorIgual(condIzq, condDer);
            case MAYOR ->
                this.mayor(condIzq, condDer);
            case MAYORIGUAL ->
                this.mayorIgual(condIzq, condDer);
            default ->
                new Errores("SEMANTICO", "Relacional Invalido", this.linea, this.columna);
        };
    }

    // Operador EQUALS: Entero-Entero, Entero-Decimal, Entero-Caracter, Decimal-Entero, Decimal-Caracter, Caracter-Entero, Caracter-Decimal, Caracter-Caracter, Cadena-Cadena
    public Object equals(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 == (int) comp2;
                    case DECIMAL ->
                        (int) comp1 == (double) comp2;
                    case CARACTER ->
                        (int) comp1 == (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 == (int) comp2;
                    case DECIMAL ->
                        (double) comp1 == (double) comp2;
                    case CARACTER ->
                        (double) comp1 == (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) ((Character) comp1) == (int) comp2;
                    case DECIMAL ->
                        (int) ((Character) comp1) == (double) comp2;
                    case CARACTER ->
                        (Character) comp1 == (Character) comp2;
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case CADENA ->
                switch (comparando2) {
                    case CADENA ->
                        comp1.toString().equalsIgnoreCase(comp2.toString());
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case BOOLEANO ->
                switch (comparando2) {
                    case BOOLEANO ->
                        (boolean) comp1 == (boolean) comp2;
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            default ->
                new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
        };
    }

    // Operador NOT EQUALS: Inverso de equals
    public Object notEquals(Object comp1, Object comp2) {
        var resultado = this.equals(comp1, comp2);
        if (resultado instanceof Errores) {
            return resultado;
        }
        return !(boolean) resultado;
    }

    // Operador MENOR: Entero-Entero, Entero-Decimal, Entero-Caracter, Decimal-Entero, Decimal-Decimal, Decimal-Caracter, Caracter-Entero, Caracter-Decimal, Caracter-Caracter
    public Object menor(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 < (int) comp2;
                    case DECIMAL ->
                        (int) comp1 < (double) comp2;
                    case CARACTER ->
                        (int) comp1 < (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 < (int) comp2;
                    case DECIMAL ->
                        (double) comp1 < (double) comp2;
                    case CARACTER ->
                        (double) comp1 < (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) ((Character) comp1) < (int) comp2;
                    case DECIMAL ->
                        (int) ((Character) comp1) < (double) comp2;
                    case CARACTER ->
                        (Character) comp1 < (Character) comp2;
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            default ->
                new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
        };
    }

    // Operador MENOR O IGUAL
    public Object menorIgual(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 <= (int) comp2;
                    case DECIMAL ->
                        (int) comp1 <= (double) comp2;
                    case CARACTER ->
                        (int) comp1 <= (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 <= (int) comp2;
                    case DECIMAL ->
                        (double) comp1 <= (double) comp2;
                    case CARACTER ->
                        (double) comp1 <= (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) ((Character) comp1) <= (int) comp2;
                    case DECIMAL ->
                        (int) ((Character) comp1) <= (double) comp2;
                    case CARACTER ->
                        (Character) comp1 <= (Character) comp2;
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            default ->
                new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
        };
    }

    // Operador MAYOR
    public Object mayor(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 > (int) comp2;
                    case DECIMAL ->
                        (int) comp1 > (double) comp2;
                    case CARACTER ->
                        (int) comp1 > (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 > (int) comp2;
                    case DECIMAL ->
                        (double) comp1 > (double) comp2;
                    case CARACTER ->
                        (double) comp1 > (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) ((Character) comp1) > (int) comp2;
                    case DECIMAL ->
                        (int) ((Character) comp1) > (double) comp2;
                    case CARACTER ->
                        (Character) comp1 > (Character) comp2;
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            default ->
                new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
        };
    }

    // Operador MAYOR O IGUAL
    public Object mayorIgual(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 >= (int) comp2;
                    case DECIMAL ->
                        (int) comp1 >= (double) comp2;
                    case CARACTER ->
                        (int) comp1 >= (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 >= (int) comp2;
                    case DECIMAL ->
                        (double) comp1 >= (double) comp2;
                    case CARACTER ->
                        (double) comp1 >= (int) ((Character) comp2);
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) ((Character) comp1) >= (int) comp2;
                    case DECIMAL ->
                        (int) ((Character) comp1) >= (double) comp2;
                    case CARACTER ->
                        (Character) comp1 >= (Character) comp2;
                    default ->
                        new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
                };
            default ->
                new Errores("SEMANTICO", "Comparacion invalida", this.linea, this.columna);
        };
    }
}
