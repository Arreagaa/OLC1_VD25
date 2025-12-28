package Instrucciones;

import Simbolo.*;
import Abstracto.Instruccion;
import Excepciones.Errores;
import java.util.LinkedList;

/**
 * Sentencia While - ejecuta instrucciones mientras la condición sea verdadera
 */
public class While extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;

    public While(Instruccion expresion, LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }
    
    @Override 
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        // se ejecuta condicion inicial del while
        Object condicion = this.expresion.interpretar(arbol, tabla);
        
        // validamos que la condicion no tenga errores
        if (condicion instanceof Errores){
            return condicion;
        }
        
        // verificar que condicion sea un booleano
        if (this.expresion.tipo.getTipo() != tipoDato.BOOLEANO) {
            return new Errores("SEMANTICO", "La condicion del WHILE debe ser tipo BOOLEANO", this.linea, this.columna);
        }
        
        // se ejecutan instrucciones mientras condicion sea verdadero
        while ((boolean)condicion){
            tablaSimbolos tablaWhile = new tablaSimbolos(tabla);
            tablaWhile.setNombre("WHILE");

            for (var ins: instrucciones){
                Object resultado = ins.interpretar(arbol, tablaWhile);
                
                // Si la instrucción retorna Return, propagarla hacia arriba
                if (resultado instanceof Return) {
                    return resultado;
                }
                // Si la instrucción retorna Break, salimos del ciclo
                if (resultado instanceof Break) {
                    return null;
                }
                
                // Si encontramos Continue, saltamos a la siguiente iteración
                if (resultado instanceof Continue) {
                    break; // Sale del for para ir a la siguiente iteración del while
                }
                
                // validamos que la instruccion no traiga un error
                if (resultado instanceof Errores){
                    return resultado;
                } 
            }
            
            // se ejecuta condicion nuevamente despues del bloque de instrucciones
            condicion = this.expresion.interpretar(arbol, tablaWhile);
            
            // validamos que la condicion no tenga errores
            if (condicion instanceof Errores){
                return condicion;
            }
        }
        return null;
    }
}
