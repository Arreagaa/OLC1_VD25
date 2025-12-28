package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import Instrucciones.DeclaracionFuncion;
import Instrucciones.DeclaracionMetodo;
import java.util.LinkedList;

/**
 * Llamada a función o método
 * Sintaxis: suma(5, 3)
 *           saludar("Juan")
 */
public class Llamada extends Instruccion {
    private String identificador;
    private LinkedList<Instruccion> parametros;

    public Llamada(String identificador, LinkedList<Instruccion> parametros, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.parametros = parametros;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar la función/método en el árbol
        Instruccion funcionMetodo = arbol.getFuncionMetodo(identificador);

        if (funcionMetodo == null) {
            return new Errores("SEMANTICO", 
                "La función o método '" + identificador + "' no ha sido declarada",
                this.linea, this.columna);
        }

        // Evaluar los parámetros
        LinkedList<Object> valoresParametros = new LinkedList<>();
        
        for (Instruccion param : parametros) {
            Object valorParam = param.interpretar(arbol, tabla);
            
            if (valorParam instanceof Errores) {
                return valorParam;
            }
            
            valoresParametros.add(valorParam);
        }

        // Ejecutar según sea función o método
        if (funcionMetodo instanceof DeclaracionFuncion) {
            DeclaracionFuncion funcion = (DeclaracionFuncion) funcionMetodo;
            Object resultado = funcion.ejecutar(arbol, tabla, valoresParametros);
            
            if (resultado instanceof Errores) {
                return resultado;
            }
            
            // Establecer el tipo de retorno de la función
            this.tipo = funcion.tipo;
            return resultado;
            
        } else if (funcionMetodo instanceof DeclaracionMetodo) {
            DeclaracionMetodo metodo = (DeclaracionMetodo) funcionMetodo;
            Object resultado = metodo.ejecutar(arbol, tabla, valoresParametros);
            
            if (resultado instanceof Errores) {
                return resultado;
            }
            
            // Los métodos no retornan valor
            this.tipo = new Tipo(tipoDato.VOID);
            return null;
        }

        return new Errores("SEMANTICO", 
            "Error al ejecutar '" + identificador + "'",
            this.linea, this.columna);
    }
}