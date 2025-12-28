package Instrucciones;

import Simbolo.*;
import Abstracto.Instruccion;
import Excepciones.Errores;
import java.util.LinkedList;

/**
 * Sentencia Do-While - ejecuta instrucciones al menos una vez, luego mientras la condición sea verdadera
 */
public class DoWhile extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;

    public DoWhile(LinkedList<Instruccion> instrucciones, Instruccion expresion, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }
    
    @Override 
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        
        // DO-WHILE ejecuta al menos una vez
        do {
            tablaSimbolos tablaDoWhile = new tablaSimbolos(tabla);
            tablaDoWhile.setNombre("DO-WHILE");

            for (var ins: instrucciones){
                Object resultado = ins.interpretar(arbol, tablaDoWhile);
                
                // Si la instrucción retorna Return, propagarla hacia arriba
                if (resultado instanceof Return) {
                    return resultado;
                }
                // Si la instrucción retorna Break, salimos del ciclo
                if (resultado instanceof Break) {
                    return null;
                }
                
                // Si encontramos Continue, saltamos a la verificación de condición
                if (resultado instanceof Continue) {
                    break; // Sale del for para evaluar condición
                }
                
                // validamos que la instruccion no traiga un error
                if (resultado instanceof Errores){
                    return resultado;
                } 
            }
            
            // se ejecuta condicion despues del bloque de instrucciones
            Object condicion = this.expresion.interpretar(arbol, tablaDoWhile);
            
            // validamos que la condicion no tenga errores
            if (condicion instanceof Errores){
                return condicion;
            }
            
            // verificar que condicion sea un booleano
            if (this.expresion.tipo.getTipo() != tipoDato.BOOLEANO) {
                return new Errores("SEMANTICO", "La condicion del DO-WHILE debe ser tipo BOOLEANO", this.linea, this.columna);
            }
            
            // Si la condición es falsa, salimos
            if (!(boolean)condicion) {
                break;
            }
            
        } while (true); // El control real está con el break interno
        
        return null;
    }
}
