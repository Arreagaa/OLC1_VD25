package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.ArrayList;

/**
 * Método append() - Agrega elementos al final de la lista
 * Sintaxis: miLista.append(1);
 */
public class ListaAppend extends Instruccion {
    private String identificador;
    private Instruccion valor;

    public ListaAppend(String identificador, Instruccion valor, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar la lista en la tabla de símbolos
        Simbolo lista = tabla.getVariable(identificador);
        
        if (lista == null) {
            return new Errores("SEMANTICO", "La lista '" + identificador + "' no ha sido declarada", this.linea, this.columna);
        }

        // Interpretar el valor a agregar
        Object nuevoValor = this.valor.interpretar(arbol, tabla);
        
        if (nuevoValor instanceof Errores) {
            return nuevoValor;
        }
        
        // Obtener valor de la lista
        Object valorLista = lista.getValor();
        
        if (!(valorLista instanceof ArrayList)) {
            return new Errores("SEMANTICO", "'" + identificador + "' no es una lista", this.linea, this.columna);
        }
        
        @SuppressWarnings("unchecked")
        ArrayList<Object> arrayList = (ArrayList<Object>) valorLista;
        
        // Validar tipo compatible
        tipoDato tipoLista = lista.getTipo().getTipo();
        tipoDato tipoValor = this.valor.tipo.getTipo();
        
        if (!validarTipo(tipoValor, tipoLista)) {
            return new Errores("SEMANTICO", 
                "Tipo incompatible. No se puede agregar " + tipoValor + " a lista de tipo " + tipoLista,
                this.linea, this.columna);
        }
        
        // Agregar valor al final de la lista
        arrayList.add(nuevoValor);
        
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
