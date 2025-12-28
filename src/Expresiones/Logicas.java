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
public class Logicas extends Instruccion {

    private Instruccion operando1;
    private Instruccion operando2;
    private OperadoresLogicos operacion;
    private Instruccion operandoUnico;

    // Constructor para operaciones unarias (NOT)
    public Logicas(OperadoresLogicos operacion, Instruccion operandoUnico, int linea, int columna) {
        super(new Tipo(tipoDato.BOOLEANO), linea, columna);
        this.operacion = operacion;
        this.operandoUnico = operandoUnico;
    }

    // Constructor para operaciones binarias (OR, AND, XOR)
    public Logicas(Instruccion operando1, Instruccion operando2, OperadoresLogicos operacion, int linea, int columna) {
        super(new Tipo(tipoDato.BOOLEANO), linea, columna);
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operacion = operacion;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        Object opIzq = null, opDer = null, Unico = null;
        
        if (this.operandoUnico != null) {
            Unico = this.operandoUnico.interpretar(arbol, tabla);
            if (Unico instanceof Errores) {
                return Unico;
            }
        } else {
            opIzq = this.operando1.interpretar(arbol, tabla);
            if (opIzq instanceof Errores) {
                return opIzq;
            }
            opDer = this.operando2.interpretar(arbol, tabla);
            if (opDer instanceof Errores) {
                return opDer;
            }
        }

        return switch (operacion) {
            case OR ->
                this.or(opIzq, opDer);
            case AND ->
                this.and(opIzq, opDer);
            case XOR ->
                this.xor(opIzq, opDer);
            case NOT ->
                this.not(Unico);
            default ->
                new Errores("SEMANTICO", "Operador logico invalido", this.linea, this.columna);
        };
    }

    // Operador OR (||): Si al menos uno es verdadero, retorna true
    public Object or(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        if (tipo1 == tipoDato.BOOLEANO && tipo2 == tipoDato.BOOLEANO) {
            this.tipo.setTipo(tipoDato.BOOLEANO);
            return (boolean) op1 || (boolean) op2;
        }

        return new Errores("SEMANTICO", "OR solo acepta booleanos", this.linea, this.columna);
    }

    // Operador AND (&&): Si ambos son verdaderos, retorna true
    public Object and(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        if (tipo1 == tipoDato.BOOLEANO && tipo2 == tipoDato.BOOLEANO) {
            this.tipo.setTipo(tipoDato.BOOLEANO);
            return (boolean) op1 && (boolean) op2;
        }

        return new Errores("SEMANTICO", "AND solo acepta booleanos", this.linea, this.columna);
    }

    // Operador XOR (^): Si son diferentes, retorna true
    public Object xor(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        if (tipo1 == tipoDato.BOOLEANO && tipo2 == tipoDato.BOOLEANO) {
            this.tipo.setTipo(tipoDato.BOOLEANO);
            return (boolean) op1 ^ (boolean) op2;
        }

        return new Errores("SEMANTICO", "XOR solo acepta booleanos", this.linea, this.columna);
    }

    // Operador NOT (!): Invierte el valor booleano
    public Object not(Object op1) {
        var tipoOp = this.operandoUnico.tipo.getTipo();

        if (tipoOp == tipoDato.BOOLEANO) {
            this.tipo.setTipo(tipoDato.BOOLEANO);
            return !(boolean) op1;
        }

        return new Errores("SEMANTICO", "NOT solo acepta booleanos", this.linea, this.columna);
    }
}
