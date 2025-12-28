package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 * Incremento y Decremento (++ y --)
 */
public class IncrementoDecremento extends Instruccion {
    private String id;
    private boolean esIncremento; // true para ++, false para --

    public IncrementoDecremento(String id, boolean esIncremento, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.id = id;
        this.esIncremento = esIncremento;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar la variable en la tabla de símbolos
        Simbolo variable = tabla.getVariable(id);
        
        if (variable == null) {
            return new Errores("SEMANTICO", "Variable " + id + " no existe", this.linea, this.columna);
        }
        
        // Verificar que sea tipo ENTERO o DECIMAL
        if (variable.getTipo().getTipo() != tipoDato.ENTERO && 
            variable.getTipo().getTipo() != tipoDato.DECIMAL) {
            return new Errores("SEMANTICO", "Incremento/Decremento solo funciona con ENTERO o DECIMAL", this.linea, this.columna);
        }
        
        // Realizar incremento o decremento
        Object valorActual = variable.getValor();
        Object nuevoValor;
        
        if (variable.getTipo().getTipo() == tipoDato.ENTERO) {
            int valor = (int) valorActual;
            nuevoValor = esIncremento ? valor + 1 : valor - 1;
        } else { // DECIMAL
            double valor = (double) valorActual;
            nuevoValor = esIncremento ? valor + 1.0 : valor - 1.0;
        }
        
        // Actualizar variable
        variable.setValor(nuevoValor);
        
        return null;
    }
}
