package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 * Asignación de valores en vectores Y LISTAS
 * Sintaxis 1D: vector[0] = valor; o lista[0] = valor;
 * Sintaxis 2D: vector[0][0] = valor;
 */
public class AsignacionVector extends Instruccion {
    private String identificador;
    private Instruccion indice1;
    private Instruccion indice2; // null si es 1D
    private Instruccion valor;

    // Constructor para vectores/listas 1D
    public AsignacionVector(String identificador, Instruccion indice1, Instruccion valor, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.indice1 = indice1;
        this.indice2 = null;
        this.valor = valor;
    }

    // Constructor para vectores 2D
    public AsignacionVector(String identificador, Instruccion indice1, Instruccion indice2, Instruccion valor, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.indice1 = indice1;
        this.indice2 = indice2;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar el vector/lista en la tabla de símbolos
        Simbolo variable = tabla.getVariable(identificador);
        
        if (variable == null) {
            return new Errores("SEMANTICO", "La variable '" + identificador + "' no ha sido declarada", this.linea, this.columna);
        }

        // Interpretar el valor a asignar
        Object nuevoValor = this.valor.interpretar(arbol, tabla);
        
        if (nuevoValor instanceof Errores) {
            return nuevoValor;
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
            return asignarLista(variable, indiceInt, nuevoValor, arbol, tabla);
        }
        
        // Asignación a vector 1D
        if (this.indice2 == null) {
            Object valorVector = variable.getValor();
            
            if (!(valorVector instanceof Object[])) {
                return new Errores("SEMANTICO", "'" + identificador + "' no es un vector o lista", this.linea, this.columna);
            }
            
            Object[] array = (Object[]) valorVector;
            
            // Validar rango del índice
            if (indiceInt < 0 || indiceInt >= array.length) {
                return new Errores("SEMANTICO", "Índice fuera de rango. El vector tiene " + array.length + " elementos", this.linea, this.columna);
            }
            
            // Validar tipo compatible
            tipoDato tipoVector = variable.getTipo().getTipo();
            tipoDato tipoValor = this.valor.tipo.getTipo();
            
            if (!validarTipo(tipoValor, tipoVector)) {
                return new Errores("SEMANTICO", "Tipo incompatible. No se puede asignar " + tipoValor + " a vector de tipo " + tipoVector, this.linea, this.columna);
            }
            
            // Asignar valor
            array[indiceInt] = nuevoValor;
            
            return null;
        }
        // Asignación a vector 2D
        else {
            Object valorVector = variable.getValor();
            
            if (!(valorVector instanceof Object[][])) {
                return new Errores("SEMANTICO", "'" + identificador + "' no es un vector 2D", this.linea, this.columna);
            }
            
            Object[][] matriz = (Object[][]) valorVector;
            
            // Validar primer índice
            if (indiceInt < 0 || indiceInt >= matriz.length) {
                return new Errores("SEMANTICO", "Índice de fila fuera de rango. El vector tiene " + matriz.length + " filas", this.linea, this.columna);
            }
            
            // Interpretar segundo índice
            Object idx2Valor = this.indice2.interpretar(arbol, tabla);
            
            if (idx2Valor instanceof Errores) {
                return idx2Valor;
            }
            
            // Validar que el segundo índice sea entero
            if (this.indice2.tipo.getTipo() != tipoDato.ENTERO) {
                return new Errores("SEMANTICO", "El índice del vector debe ser de tipo ENTERO", this.linea, this.columna);
            }
            
            int indice2Int = (int) idx2Valor;
            
            // Validar segundo índice
            if (indice2Int < 0 || indice2Int >= matriz[indiceInt].length) {
                return new Errores("SEMANTICO", "Índice de columna fuera de rango. La fila tiene " + matriz[indiceInt].length + " elementos", this.linea, this.columna);
            }
            
            // Validar tipo compatible
            tipoDato tipoVector = variable.getTipo().getTipo();
            tipoDato tipoValor = this.valor.tipo.getTipo();
            
            if (!validarTipo(tipoValor, tipoVector)) {
                return new Errores("SEMANTICO", "Tipo incompatible. No se puede asignar " + tipoValor + " a vector de tipo " + tipoVector, this.linea, this.columna);
            }
            
            // Asignar valor
            matriz[indiceInt][indice2Int] = nuevoValor;
            
            return null;
        }
    }
    
    private Object asignarLista(Simbolo lista, int indice, Object nuevoValor, Arbol arbol, tablaSimbolos tabla) {
        @SuppressWarnings("unchecked")
        java.util.ArrayList<Object> arrayList = (java.util.ArrayList<Object>) lista.getValor();
        
        // Validar rango del índice
        if (indice < 0 || indice >= arrayList.size()) {
            return new Errores("SEMANTICO", "Índice fuera de rango. La lista tiene " + arrayList.size() + " elementos", this.linea, this.columna);
        }
        
        // Validar tipo compatible
        tipoDato tipoLista = lista.getTipo().getTipo();
        tipoDato tipoValor = this.valor.tipo.getTipo();
        
        if (!validarTipo(tipoValor, tipoLista)) {
            return new Errores("SEMANTICO", 
                "Tipo incompatible. No se puede asignar " + tipoValor + " a lista de tipo " + tipoLista,
                this.linea, this.columna);
        }
        
        // Asignar valor
        arrayList.set(indice, nuevoValor);
        
        return null;
    }

    private boolean validarTipo(tipoDato tipoValor, tipoDato tipoDeclarado) {
        if (tipoValor == tipoDeclarado) {
            return true;
        }
        // Permitir int a double
        if (tipoDeclarado == tipoDato.DECIMAL && tipoValor == tipoDato.ENTERO) {
            return true;
        }
        return false;
    }
}
