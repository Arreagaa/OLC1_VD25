package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import Expresiones.Llamada;
import java.util.LinkedList;

/**
 * Sentencia START - Punto de entrada del programa
 * Sintaxis: start main();
 * start iniciar(5, "test");
 */
public class Start extends Instruccion {
    private String identificador;
    private LinkedList<Instruccion> parametros;

    public Start(String identificador, LinkedList<Instruccion> parametros, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.parametros = parametros;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Crear una llamada a la función/método especificada
        Llamada llamada = new Llamada(identificador, parametros, this.linea, this.columna);

        // Ejecutar la llamada
        Object resultado = llamada.interpretar(arbol, tabla);

        if (resultado instanceof Errores) {
            return resultado;
        }

        return null;
    }

    public String getIdentificador() {
        return identificador;
    }
}