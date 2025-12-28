package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 * Función round() - Redondea decimales
 * Sintaxis: round(5.7) retorna 6
 * round(5.4) retorna 5
 */
public class Round extends Instruccion {
    private Instruccion expresion;

    public Round(Instruccion expresion, int linea, int columna) {
        super(new Tipo(tipoDato.ENTERO), linea, columna);
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Evaluar la expresión
        Object valor = this.expresion.interpretar(arbol, tabla);

        if (valor instanceof Errores) {
            return valor;
        }

        tipoDato tipoValor = this.expresion.tipo.getTipo();

        // Round solo funciona con ENTERO o DECIMAL
        if (tipoValor == tipoDato.ENTERO) {
            // Si ya es entero, retornarlo tal cual
            this.tipo = new Tipo(tipoDato.ENTERO);
            return (int) valor;
        } else if (tipoValor == tipoDato.DECIMAL) {
            // Aplicar reglas de redondeo
            double decimal = (double) valor;

            // Si el decimal es >= 0.5, redondea hacia arriba
            // Si el decimal es < 0.5, redondea hacia abajo
            int resultado = (int) Math.round(decimal);

            this.tipo = new Tipo(tipoDato.ENTERO);
            return resultado;
        } else {
            return new Errores(
                    "SEMANTICO",
                    "round() solo acepta valores numéricos (int o double). Se recibió: " + tipoValor,
                    this.linea,
                    this.columna);
        }
    }
}