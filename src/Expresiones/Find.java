package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.ArrayList;

/**
 * Función Find() - Busca un elemento en vector o lista
 * Sintaxis: arr.Find(1) retorna true si existe, false si no
 */
public class Find extends Instruccion {
    private String identificador;
    private Instruccion valorBuscar;

    public Find(String identificador, Instruccion valorBuscar, int linea, int columna) {
        super(new Tipo(tipoDato.BOOLEANO), linea, columna);
        this.identificador = identificador;
        this.valorBuscar = valorBuscar;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar el vector/lista en la tabla de símbolos
        Simbolo variable = tabla.getVariable(identificador);

        if (variable == null) {
            return new Errores(
                    "SEMANTICO",
                    "La variable '" + identificador + "' no ha sido declarada",
                    this.linea,
                    this.columna);
        }

        // Evaluar el valor a buscar
        Object valorABuscar = this.valorBuscar.interpretar(arbol, tabla);

        if (valorABuscar instanceof Errores) {
            return valorABuscar;
        }

        Object valorVariable = variable.getValor();

        // CASO 1: Lista dinámica (ArrayList)
        if (valorVariable instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<Object> lista = (ArrayList<Object>) valorVariable;

            this.tipo = new Tipo(tipoDato.BOOLEANO);

            // Buscar en la lista
            for (Object elemento : lista) {
                if (sonIguales(elemento, valorABuscar)) {
                    return true;
                }
            }
            return false;
        }

        // CASO 2: Vector 1D (Object[])
        if (valorVariable instanceof Object[] && !(valorVariable instanceof Object[][])) {
            Object[] array = (Object[]) valorVariable;

            this.tipo = new Tipo(tipoDato.BOOLEANO);

            // Buscar en el vector
            for (Object elemento : array) {
                if (sonIguales(elemento, valorABuscar)) {
                    return true;
                }
            }
            return false;
        }

        // CASO 3: Vector 2D (Object[][])
        if (valorVariable instanceof Object[][]) {
            Object[][] matriz = (Object[][]) valorVariable;

            this.tipo = new Tipo(tipoDato.BOOLEANO);

            // Buscar en la matriz
            for (Object[] fila : matriz) {
                for (Object elemento : fila) {
                    if (sonIguales(elemento, valorABuscar)) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Tipo no soportado
        return new Errores(
                "SEMANTICO",
                "Find() solo funciona con vectores o listas",
                this.linea,
                this.columna);
    }

    /**
     * Compara dos valores según su tipo
     */
    private boolean sonIguales(Object valor1, Object valor2) {
        if (valor1 == null || valor2 == null) {
            return valor1 == valor2;
        }

        // Comparar por tipo
        if (valor1 instanceof Integer && valor2 instanceof Integer) {
            return ((Integer) valor1).equals((Integer) valor2);
        }
        if (valor1 instanceof Double && valor2 instanceof Double) {
            return ((Double) valor1).equals((Double) valor2);
        }
        if (valor1 instanceof Boolean && valor2 instanceof Boolean) {
            return ((Boolean) valor1).equals((Boolean) valor2);
        }
        if (valor1 instanceof Character && valor2 instanceof Character) {
            return ((Character) valor1).equals((Character) valor2);
        }
        if (valor1 instanceof String && valor2 instanceof String) {
            return ((String) valor1).equalsIgnoreCase((String) valor2);
        }

        // Comparación genérica
        return valor1.equals(valor2);
    }
}