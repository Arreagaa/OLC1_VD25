package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;

/**
 * Sentencia Break - sale del ciclo o switch actual
 */
public class Break extends Instruccion {

    public Break(int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Break solo retorna su propia instancia para ser detectado en ciclos/switch
        return this;
    }
}
