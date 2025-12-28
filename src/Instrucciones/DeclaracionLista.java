package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.ArrayList;

/**
 * Declaración de Listas Dinámicas
 * Sintaxis: List<int> miLista = new List();
 */
public class DeclaracionLista extends Instruccion {
    private String identificador;
    private Tipo tipoElemento;

    public DeclaracionLista(String identificador, Tipo tipoElemento, int linea, int columna) {
        super(tipoElemento, linea, columna);
        this.identificador = identificador;
        this.tipoElemento = tipoElemento;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Verificar si la variable ya existe
        if (tabla.getVariable(identificador) != null) {
            return new Errores("SEMANTICO", "La lista '" + identificador + "' ya fue declarada", this.linea, this.columna);
        }

        // Crear un ArrayList Java para almacenar los elementos
        ArrayList<Object> lista = new ArrayList<>();
        
        // Crear símbolo de la lista
        String nombreEntorno = tabla.getNombre() != null ? tabla.getNombre() : "GLOBAL";
        Simbolo simbolo = new Simbolo(this.tipo, this.identificador, lista, this.linea, this.columna, nombreEntorno);
        tabla.setVariable(simbolo);
        arbol.agregarSimboloHistorial(simbolo);
        
        return null;
    }
}
