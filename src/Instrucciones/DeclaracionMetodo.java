package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * Declaración de Métodos (void)
 * Sintaxis: void saludar(string nombre) { print("Hola " + nombre); }
 */
public class DeclaracionMetodo extends Instruccion {
    private String identificador;
    private LinkedList<HashMap<String, Object>> parametros; // Lista de {tipo, id}
    private LinkedList<Instruccion> instrucciones;

    public DeclaracionMetodo(String identificador, LinkedList<HashMap<String, Object>> parametros,
            LinkedList<Instruccion> instrucciones, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.parametros = parametros;
        this.instrucciones = instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Verificar que no exista una función/método con el mismo nombre
        if (arbol.getFuncionMetodo(identificador) != null) {
            return new Errores("SEMANTICO",
                    "Ya existe una función o método con el nombre '" + identificador + "'",
                    this.linea, this.columna);
        }

        // Guardar el método en la tabla de funciones/métodos del árbol
        arbol.setFuncionMetodo(identificador, this);

        return null;
    }

    /**
     * Ejecutar el método con los parámetros proporcionados
     */
    public Object ejecutar(Arbol arbol, tablaSimbolos tablaActual, LinkedList<Object> valoresParametros) {
        // Validar cantidad de parámetros
        if (parametros.size() != valoresParametros.size()) {
            return new Errores("SEMANTICO",
                    "El método '" + identificador + "' espera " + parametros.size() +
                            " parámetros pero se recibieron " + valoresParametros.size(),
                    this.linea, this.columna);
        }

        // Crear nueva tabla de símbolos para el método
        tablaSimbolos tablaMetodo = new tablaSimbolos(tablaActual);
        tablaMetodo.setNombre("METODO_" + identificador);

        // Asignar valores a los parámetros
        for (int i = 0; i < parametros.size(); i++) {
            HashMap<String, Object> parametro = parametros.get(i);
            String idParam = (String) parametro.get("id");
            Tipo tipoParam = (Tipo) parametro.get("tipo");
            Object valorParam = valoresParametros.get(i);

            // Crear símbolo para el parámetro
            Simbolo simboloParam = new Simbolo(tipoParam, idParam, valorParam, this.linea, this.columna,
                    "METODO_" + identificador);
            tablaMetodo.setVariable(simboloParam);
        }

        // Ejecutar instrucciones del método
        for (Instruccion inst : instrucciones) {
            if (inst == null)
                continue;

            Object resultado = inst.interpretar(arbol, tablaMetodo);

            // Si es un error, retornarlo
            if (resultado instanceof Errores) {
                return resultado;
            }

            // Si encontramos un Return (sin valor), salir
            if (resultado instanceof Return) {
                Return ret = (Return) resultado;

                // Los métodos void NO deben retornar valor
                if (ret.getExpresion() != null) {
                    return new Errores("SEMANTICO",
                            "El método '" + identificador + "' no debe retornar ningún valor",
                            this.linea, this.columna);
                }

                return null; // Salir del método
            }
        }

        return null;
    }

    public String getIdentificador() {
        return identificador;
    }

    public LinkedList<HashMap<String, Object>> getParametros() {
        return parametros;
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }
}