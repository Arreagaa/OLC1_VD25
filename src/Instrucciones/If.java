/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instrucciones;

import Abstracto.Instruccion;
import java.util.LinkedList;
import Simbolo.*;
import Excepciones.Errores;

/**
 *
 * @author crjav
 */
public class If extends Instruccion {

    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;
    private LinkedList<Instruccion> instruccioneselseif;
    private LinkedList<Instruccion> instruccioneselse;

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, LinkedList<Instruccion> instruccioneselse, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.instruccioneselse = instruccioneselse;
    }

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, LinkedList<Instruccion> instruccioneselseif, LinkedList<Instruccion> instruccioneselse, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.instruccioneselseif = instruccioneselseif;
        this.instruccioneselse = instruccioneselse;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Evaluar la condición del IF
        var condicion = this.expresion.interpretar(arbol, tabla);

        // Verificar si hubo error en la evaluación
        if (condicion instanceof Errores) {
            return condicion;
        }

        // Verificar que la condición sea booleana
        if (this.expresion.tipo.getTipo() != tipoDato.BOOLEANO) {
            return new Errores(
                    "SEMANTICO",
                    "La condición del IF debe ser de tipo BOOLEANO",
                    this.linea,
                    this.columna
            );
        }

        boolean ejecutarIf = (Boolean) condicion;

        // EJECUTAR BLOQUE IF (si la condición es verdadera)
        if (ejecutarIf) {
            var nuevaTabla = new tablaSimbolos(tabla);
            nuevaTabla.setNombre("IF");

            for (var inst : this.instrucciones) {
                if (inst == null) {
                    continue;
                }

                var resultado = inst.interpretar(arbol, nuevaTabla);

                if (resultado instanceof Errores) {
                    return resultado; // Retornar el error
                }
                
                // Propagar Return, Break y Continue hacia arriba
                if (resultado instanceof Return) {
                    return resultado;
                }
                if (resultado instanceof Break || resultado instanceof Continue) {
                    return resultado;
                }
            }
            return true; // IF ejecutado exitosamente, indicar que se ejecutó
        }

        // EJECUTAR BLOQUES ELSE IF (si los hay)
        if (this.instruccioneselseif != null) {
            for (Instruccion elseif : this.instruccioneselseif) {
                var resultado = elseif.interpretar(arbol, tabla);

                if (resultado instanceof Errores) {
                    return resultado; // Retornar el error
                }

                // Propagar Return hacia arriba
                if (resultado instanceof Return) {
                    return resultado;
                }

                // Si algún ELSE IF se ejecutó (retornó true), salir
                if (resultado instanceof Boolean && (Boolean) resultado) {
                    return true;
                }
            }
        }

        // EJECUTAR BLOQUE ELSE (si existe)
        if (this.instruccioneselse != null) {
            var nuevaTabla = new tablaSimbolos(tabla);
            nuevaTabla.setNombre("ELSE");

            for (var inst : this.instruccioneselse) {
                if (inst == null) {
                    continue;
                }

                var resultado = inst.interpretar(arbol, nuevaTabla);

                if (resultado instanceof Errores) {
                    return resultado; // Retornar el error
                }
                
                // Propagar Return, Break y Continue hacia arriba
                if (resultado instanceof Return) {
                    return resultado;
                }
                if (resultado instanceof Break || resultado instanceof Continue) {
                    return resultado;
                }
            }
        }

        return null;
    }
}
