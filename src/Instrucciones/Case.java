package Instrucciones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import Abstracto.Instruccion;
import java.util.LinkedList;

/**
 * Case - opción individual del switch
 */
public class Case extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;

    public Case(Instruccion expresion, LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }

    public Instruccion getExpresion() {
        return expresion;
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // El Case no se interpreta directamente, es manejado por Switch
        return null;
    }
}
