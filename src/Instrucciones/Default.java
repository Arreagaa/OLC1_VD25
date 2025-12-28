package Instrucciones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import Abstracto.Instruccion;
import java.util.LinkedList;

/**
 * Default - caso por defecto del switch
 */
public class Default extends Instruccion {
    private LinkedList<Instruccion> instrucciones;

    public Default(LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.instrucciones = instrucciones;
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // El Default no se interpreta directamente, es manejado por Switch
        return null;
    }
}
