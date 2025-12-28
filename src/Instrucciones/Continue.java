package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;

/**
 * Sentencia Continue - detiene la iteración actual y salta a la siguiente
 */
public class Continue extends Instruccion {

    public Continue(int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Continue solo retorna su propia instancia para ser detectado en ciclos
        return this;
    }
}
