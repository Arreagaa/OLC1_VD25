package Instrucciones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import Abstracto.Instruccion;
import Excepciones.Errores;
import java.util.LinkedList;

/**
 * Sentencia For - ejecuta instrucciones N veces
 */
public class For extends Instruccion {
    private Instruccion asignacion;
    private Instruccion condicion;
    private Instruccion actualizacion;
    private LinkedList<Instruccion> instrucciones;

    public For(Instruccion asignacion, Instruccion condicion, Instruccion actualizacion, LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.asignacion = asignacion;
        this.condicion = condicion;
        this.actualizacion = actualizacion;
        this.instrucciones = instrucciones;
    }
    
    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        // Crear tabla para el FOR (incluye la variable de control)
        var newTabla = new tablaSimbolos(tabla);
        newTabla.setNombre("FOR");
        
        // 1. Ejecutar asignacion o declaracion (i=0 o var i:int=0)
        var res1 = this.asignacion.interpretar(arbol, newTabla);
        if (res1 instanceof Errores){
            return res1;
        } 
        
        // 2. Ejecucion de condicional (i<5)
        var cond = this.condicion.interpretar(arbol, newTabla);
        if (cond instanceof Errores){
            return cond;
        }
        
        // Validar que la condicion sea booleana
        if (this.condicion.tipo.getTipo() != tipoDato.BOOLEANO) {
            return new Errores("SEMANTICO", "La condicion del FOR debe ser tipo BOOLEANO", this.linea, this.columna);
        }
        
        // 3. Ejecutar ciclo mientras condicion sea verdadera
        while ((boolean)cond){
            // Tabla para bloque de instrucciones
            var tablaInstrucciones = new tablaSimbolos(newTabla);
            tablaInstrucciones.setNombre("FOR");
            
            // Ejecutar instrucciones en for
            for (var ins: instrucciones){
                Object resultado = ins.interpretar(arbol, tablaInstrucciones);
                
                // Si la instrucción retorna Return, propagarla hacia arriba
                if (resultado instanceof Return) {
                    return resultado;
                }
                // Si la instrucción retorna Break, salimos del ciclo
                if (resultado instanceof Break) {
                    return null;
                }
                
                // Si encontramos Continue, saltamos a la actualización
                if (resultado instanceof Continue) {
                    break; // Sale del for de instrucciones
                }
                
                // Validar errores
                if (resultado instanceof Errores){
                    return resultado;
                }
            }
            
            // 4. Ejecutar actualizacion (i++ o i=i+1) con newTabla
            var resActual = this.actualizacion.interpretar(arbol, newTabla);
            if (resActual instanceof Errores){
                return resActual;
            }
            
            // 5. Volver a evaluar condicion
            cond = this.condicion.interpretar(arbol, newTabla);
            if (cond instanceof Errores){
                return cond;
            }
        }
        
        return null;
    }
}
