package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 * Sentencia Return - Retorna un valor desde una función o sale de un método
 * Sintaxis: return;
 * return expresion;
 */
public class Return extends Instruccion {
    private Instruccion expresion; // null si es return sin valor
    private Object valorEvaluado = null;

    // Constructor para return sin valor (métodos void)
    public Return(int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.expresion = null;
    }

    // Constructor para return con valor (funciones)
    public Return(Instruccion expresion, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Si tiene expresión, evaluarla UNA SOLA VEZ
        if (this.expresion != null) {
            // Evaluar y guardar DIRECTAMENTE en valorEvaluado
            this.valorEvaluado = this.expresion.interpretar(arbol, tabla);

            if (this.valorEvaluado instanceof Errores) {
                return this.valorEvaluado;
            }

            // Guardar el tipo
            this.tipo = this.expresion.tipo;

            // Retornar el objeto Return (señal)
            return this;
        }

        // Return sin valor (para métodos void)
        this.tipo = new Tipo(tipoDato.VOID);
        return this;
    }

    public Instruccion getExpresion() {
        return expresion;
    }

    public Object getValorRetorno(Arbol arbol, tablaSimbolos tabla) {
        // NUNCA re-evaluar la expresión - solo retornar el valor ya guardado
        return this.valorEvaluado;
    }
}