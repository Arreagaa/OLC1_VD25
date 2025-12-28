package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 * Acceso a elementos de vectores Y LISTAS
 * Sintaxis 1D: vector[0] o lista[0]
 * Sintaxis 2D: vector[0][0]
 */
public class AccesoVector extends Instruccion {
    private String identificador;
    private Instruccion indice1;
    private Instruccion indice2; // null si es 1D

    // Constructor para vectores/listas 1D
    public AccesoVector(String identificador, Instruccion indice1, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.indice1 = indice1;
        this.indice2 = null;
    }

    // Constructor para vectores 2D
    public AccesoVector(String identificador, Instruccion indice1, Instruccion indice2, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.indice1 = indice1;
        this.indice2 = indice2;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar el vector/lista en la tabla de símbolos
        Simbolo variable = tabla.getVariable(identificador);

        if (variable == null) {
            return new Errores("SEMANTICO", "La variable '" + identificador + "' no ha sido declarada", this.linea,
                    this.columna);
        }

        // Interpretar el primer índice
        Object idx1Valor = this.indice1.interpretar(arbol, tabla);

        if (idx1Valor instanceof Errores) {
            return idx1Valor;
        }

        // Validar que el índice sea entero
        if (this.indice1.tipo.getTipo() != tipoDato.ENTERO) {
            return new Errores("SEMANTICO", "El índice debe ser de tipo ENTERO", this.linea, this.columna);
        }

        int indiceInt = (int) idx1Valor;

        // VERIFICAR SI ES LISTA (ArrayList)
        if (variable.getValor() instanceof java.util.ArrayList && this.indice2 == null) {
            return accederLista(variable, indiceInt);
        }

        // Acceso a vector 1D
        if (this.indice2 == null) {
            Object valorVector = variable.getValor();

            if (!(valorVector instanceof Object[])) {
                return new Errores("SEMANTICO", "'" + identificador + "' no es un vector o lista", this.linea,
                        this.columna);
            }

            Object[] array = (Object[]) valorVector;

            // Validar rango del índice
            if (indiceInt < 0 || indiceInt >= array.length) {
                return new Errores("SEMANTICO", "Índice fuera de rango. El vector tiene " + array.length + " elementos",
                        this.linea, this.columna);
            }

            // Determinar el tipo del elemento
            Object elemento = array[indiceInt];
            this.tipo = inferirTipo(elemento);

            return elemento;
        }
        // Acceso a vector 2D
        else {
            Object valorVector = variable.getValor();

            if (!(valorVector instanceof Object[][])) {
                return new Errores("SEMANTICO", "'" + identificador + "' no es un vector 2D", this.linea, this.columna);
            }

            Object[][] matriz = (Object[][]) valorVector;

            // Validar primer índice
            if (indiceInt < 0 || indiceInt >= matriz.length) {
                return new Errores("SEMANTICO",
                        "Índice de fila fuera de rango. El vector tiene " + matriz.length + " filas", this.linea,
                        this.columna);
            }

            // Interpretar segundo índice
            Object idx2Valor = this.indice2.interpretar(arbol, tabla);

            if (idx2Valor instanceof Errores) {
                return idx2Valor;
            }

            // Validar que el segundo índice sea entero
            if (this.indice2.tipo.getTipo() != tipoDato.ENTERO) {
                return new Errores("SEMANTICO", "El índice del vector debe ser de tipo ENTERO", this.linea,
                        this.columna);
            }

            int indice2Int = (int) idx2Valor;

            // Validar segundo índice
            if (indice2Int < 0 || indice2Int >= matriz[indiceInt].length) {
                return new Errores("SEMANTICO",
                        "Índice de columna fuera de rango. La fila tiene " + matriz[indiceInt].length + " elementos",
                        this.linea, this.columna);
            }

            // Obtener elemento
            Object elemento = matriz[indiceInt][indice2Int];
            this.tipo = inferirTipo(elemento);

            return elemento;
        }
    }

    private Object accederLista(Simbolo lista, int indice) {
        @SuppressWarnings("unchecked")
        java.util.ArrayList<Object> arrayList = (java.util.ArrayList<Object>) lista.getValor();

        // Validar rango del índice
        if (indice < 0 || indice >= arrayList.size()) {
            return new Errores("SEMANTICO", "Índice fuera de rango. La lista tiene " + arrayList.size() + " elementos",
                    this.linea, this.columna);
        }

        // Obtener elemento
        Object elemento = arrayList.get(indice);
        this.tipo = inferirTipo(elemento);

        return elemento;
    }

    private Tipo inferirTipo(Object valor) {
        if (valor instanceof Integer) {
            return new Tipo(tipoDato.ENTERO);
        } else if (valor instanceof Double) {
            return new Tipo(tipoDato.DECIMAL);
        } else if (valor instanceof Boolean) {
            return new Tipo(tipoDato.BOOLEANO);
        } else if (valor instanceof Character) {
            return new Tipo(tipoDato.CARACTER);
        } else if (valor instanceof String) {
            return new Tipo(tipoDato.CADENA);
        }
        return new Tipo(tipoDato.VOID);
    }
}
