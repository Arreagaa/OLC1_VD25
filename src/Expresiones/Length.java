package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.ArrayList;

/**
 * Función length() - Retorna el tamaño de vectores, listas o cadenas
 * Sintaxis: length(miVector)
 * length(miLista)
 * length("Hola")
 */
public class Length extends Instruccion {
    private Instruccion expresion;

    public Length(Instruccion expresion, int linea, int columna) {
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

        // CASO 1: String (cadena)
        if (tipoValor == tipoDato.CADENA) {
            String cadena = (String) valor;
            this.tipo = new Tipo(tipoDato.ENTERO);
            return cadena.length();
        }

        // CASO 2: Vector 1D (Object[])
        if (valor instanceof Object[] && !(valor instanceof Object[][])) {
            Object[] array = (Object[]) valor;
            this.tipo = new Tipo(tipoDato.ENTERO);
            return array.length;
        }

        // CASO 3: Vector 2D (Object[][])
        if (valor instanceof Object[][]) {
            Object[][] matriz = (Object[][]) valor;
            this.tipo = new Tipo(tipoDato.ENTERO);
            return matriz.length; // Retorna número de filas
        }

        // CASO 4: Lista dinámica (ArrayList)
        if (valor instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<Object> lista = (ArrayList<Object>) valor;
            this.tipo = new Tipo(tipoDato.ENTERO);
            return lista.size();
        }

        // Tipo no soportado
        return new Errores(
                "SEMANTICO",
                "length() solo acepta vectores, listas o cadenas. Se recibió: " + tipoValor,
                this.linea,
                this.columna);
    }
}