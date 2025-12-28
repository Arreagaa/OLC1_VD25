package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.ArrayList;

/**
 * Acceso a elementos de listas dinámicas
 * Sintaxis: miLista[0]
 */
public class AccesoLista extends Instruccion {
    private String identificador;
    private Instruccion indice;

    public AccesoLista(String identificador, Instruccion indice, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.indice = indice;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar la lista en la tabla de símbolos
        Simbolo lista = tabla.getVariable(identificador);
        
        if (lista == null) {
            return new Errores("SEMANTICO", "La lista '" + identificador + "' no ha sido declarada", this.linea, this.columna);
        }

        // Interpretar el índice
        Object idxValor = this.indice.interpretar(arbol, tabla);
        
        if (idxValor instanceof Errores) {
            return idxValor;
        }
        
        // Validar que el índice sea entero
        if (this.indice.tipo.getTipo() != tipoDato.ENTERO) {
            return new Errores("SEMANTICO", "El índice de la lista debe ser de tipo ENTERO", this.linea, this.columna);
        }
        
        int indiceInt = (int) idxValor;
        
        // Obtener valor de la lista
        Object valorLista = lista.getValor();
        
        if (!(valorLista instanceof ArrayList)) {
            return new Errores("SEMANTICO", "'" + identificador + "' no es una lista", this.linea, this.columna);
        }
        
        @SuppressWarnings("unchecked")
        ArrayList<Object> arrayList = (ArrayList<Object>) valorLista;
        
        // Validar rango del índice
        if (indiceInt < 0 || indiceInt >= arrayList.size()) {
            return new Errores("SEMANTICO", "Índice fuera de rango. La lista tiene " + arrayList.size() + " elementos", this.linea, this.columna);
        }
        
        // Obtener elemento
        Object elemento = arrayList.get(indiceInt);
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
