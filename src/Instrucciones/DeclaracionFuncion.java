package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * Declaración de Funciones
 * Sintaxis: int suma(int a, int b) { return a + b; }
 */
public class DeclaracionFuncion extends Instruccion {
    private String identificador;
    private LinkedList<HashMap<String, Object>> parametros; // Lista de {tipo, id}
    private LinkedList<Instruccion> instrucciones;

    public DeclaracionFuncion(Tipo tipo, String identificador, LinkedList<HashMap<String, Object>> parametros,
            LinkedList<Instruccion> instrucciones, int linea, int columna) {
        super(tipo, linea, columna);
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

        // Guardar la función en la tabla de funciones/métodos del árbol
        arbol.setFuncionMetodo(identificador, this);

        return null;
    }

    /**
     * Ejecutar la función con los parámetros proporcionados
     */
    public Object ejecutar(Arbol arbol, tablaSimbolos tablaActual, LinkedList<Object> valoresParametros) {
        // Validar cantidad de parámetros
        if (parametros.size() != valoresParametros.size()) {
            return new Errores("SEMANTICO",
                    "La función '" + identificador + "' espera " + parametros.size() +
                            " parámetros pero se recibieron " + valoresParametros.size(),
                    this.linea, this.columna);
        }

        // Crear nueva tabla de símbolos para la función
        tablaSimbolos tablaFuncion = new tablaSimbolos(tablaActual);
        tablaFuncion.setNombre("FUNCION_" + identificador);

        // Asignar valores a los parámetros
        for (int i = 0; i < parametros.size(); i++) {
            HashMap<String, Object> parametro = parametros.get(i);
            String idParam = (String) parametro.get("id");
            Tipo tipoParam = (Tipo) parametro.get("tipo");
            Object valorParam = valoresParametros.get(i);

            // Crear símbolo para el parámetro
            Simbolo simboloParam = new Simbolo(tipoParam, idParam, valorParam, this.linea, this.columna,
                    "FUNCION_" + identificador);
            tablaFuncion.setVariable(simboloParam);
        }

        // Ejecutar instrucciones de la función
        for (Instruccion inst : instrucciones) {
            if (inst == null)
                continue;

            Object resultado = inst.interpretar(arbol, tablaFuncion);

            // Si es un error, retornarlo
            if (resultado instanceof Errores) {
                return resultado;
            }

            // Si encontramos un Return, obtener su valor y salir
            if (resultado instanceof Return) {
                Return ret = (Return) resultado;
                Object valorRetorno = ret.getValorRetorno(arbol, tablaFuncion);

                // Validar que el tipo del return coincida con el tipo de la función
                if (ret.getExpresion() != null) {
                    tipoDato tipoRetorno = ret.getExpresion().tipo.getTipo();
                    tipoDato tipoFuncion = this.tipo.getTipo();

                    if (!validarTipos(tipoFuncion, tipoRetorno)) {
                        return new Errores("SEMANTICO",
                                "La función '" + identificador + "' debe retornar " + tipoFuncion +
                                        " pero se encontró " + tipoRetorno,
                                this.linea, this.columna);
                    }
                }

                return valorRetorno;
            }
        }

        // Si llegamos aquí y no hubo return, es un error
        return new Errores("SEMANTICO",
                "La función '" + identificador + "' debe retornar un valor de tipo " + this.tipo.getTipo(),
                this.linea, this.columna);
    }

    private boolean validarTipos(tipoDato tipoEsperado, tipoDato tipoRecibido) {
        if (tipoEsperado == tipoRecibido)
            return true;
        // Permitir int a double
        if (tipoEsperado == tipoDato.DECIMAL && tipoRecibido == tipoDato.ENTERO)
            return true;
        return false;
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