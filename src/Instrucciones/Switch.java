package Instrucciones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import Abstracto.Instruccion;
import Excepciones.Errores;
import java.util.LinkedList;

/**
 * Sentencia Switch - evaluación de casos múltiples
 */
public class Switch extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Case> casos;
    private Default casoDefault;

    public Switch(Instruccion expresion, LinkedList<Case> casos, Default casoDefault, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.casos = casos;
        this.casoDefault = casoDefault;
    }
    
    // Constructor sin default
    public Switch(Instruccion expresion, LinkedList<Case> casos, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.casos = casos;
        this.casoDefault = null;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Evaluar la expresión del switch
        var valorSwitch = this.expresion.interpretar(arbol, tabla);
        
        if (valorSwitch instanceof Errores) {
            return valorSwitch;
        }
        
        // Crear nueva tabla para el switch
        var tablaSwitch = new tablaSimbolos(tabla);
        tablaSwitch.setNombre("SWITCH");
        
        boolean casoEncontrado = false;
        boolean ejecutarSiguientes = false; // Para el comportamiento de fall-through
        
        // Recorrer todos los casos
        for (Case caso : casos) {
            // Evaluar condición del case
            var valorCase = caso.getExpresion().interpretar(arbol, tabla);
            
            if (valorCase instanceof Errores) {
                return valorCase;
            }
            
            // Verificar si coincide o si ya estamos en fall-through
            boolean coincide = false;
            
            if (!ejecutarSiguientes) {
                // Comparar valores según tipo
                if (this.expresion.tipo.getTipo() == caso.getExpresion().tipo.getTipo()) {
                    coincide = valorSwitch.equals(valorCase);
                } else {
                    return new Errores("SEMANTICO", "Tipo de case no coincide con tipo de switch", this.linea, this.columna);
                }
            } else {
                coincide = true; // Fall-through: ejecutar sin comparar
            }
            
            if (coincide || ejecutarSiguientes) {
                casoEncontrado = true;
                ejecutarSiguientes = true;
                
                // Ejecutar instrucciones del case
                for (var ins : caso.getInstrucciones()) {
                    var resultado = ins.interpretar(arbol, tablaSwitch);
                    
                    if (resultado instanceof Return) {
                        return resultado;
                    }

                    if (resultado instanceof Break) {
                        return null; // Salir del switch
                    }
                    
                    if (resultado instanceof Errores) {
                        return resultado;
                    }
                }
            }
        }
        
        // Si no se encontró ningún case y hay default, ejecutarlo
        if (!casoEncontrado && casoDefault != null) {
            for (var ins : casoDefault.getInstrucciones()) {
                var resultado = ins.interpretar(arbol, tablaSwitch);
                
                if (resultado instanceof Return) {
                    return resultado;
                }

                if (resultado instanceof Break) {
                    return null;
                }
                
                if (resultado instanceof Errores) {
                    return resultado;
                }
            }
        } else if (ejecutarSiguientes && casoDefault != null) {
            // Fall-through también ejecuta default si no hubo break
            for (var ins : casoDefault.getInstrucciones()) {
                var resultado = ins.interpretar(arbol, tablaSwitch);

                if (resultado instanceof Return) {
                    return resultado;
                }

                if (resultado instanceof Break) {
                    return null;
                }

                if (resultado instanceof Errores) {
                    return resultado;
                }
            }
        }
        
        return null;
    }
}
