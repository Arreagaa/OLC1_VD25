package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.ArrayList;

/**
 * Asignación de valores en listas dinámicas
 * Sintaxis: miLista[0] = 2;
 */
public class AsignacionLista extends Instruccion {
    private String identificador;
    private Instruccion indice;
    private Instruccion valor;

    public AsignacionLista(String identificador, Instruccion indice, Instruccion valor, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.indice = indice;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar la lista en la tabla de símbolos
        Simbolo lista = tabla.getVariable(identificador);
        
        if (lista == null) {
            return new Errores("SEMANTICO", "La lista '" + identificador + "' no ha sido declarada", this.linea, this.columna);
        }

        // Interpretar el valor a asignar
        Object nuevoValor = this.valor.interpretar(arbol, tabla);
        
        if (nuevoValor instanceof Errores) {
            return nuevoValor;
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
        
        // Validar tipo compatible
        tipoDato tipoLista = lista.getTipo().getTipo();
        tipoDato tipoValor = this.valor.tipo.getTipo();
        
        if (!validarTipo(tipoValor, tipoLista)) {
            return new Errores("SEMANTICO", 
                "Tipo incompatible. No se puede asignar " + tipoValor + " a lista de tipo " + tipoLista,
                this.linea, this.columna);
        }
        
        // Asignar valor
        arrayList.set(indiceInt, nuevoValor);
        
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
