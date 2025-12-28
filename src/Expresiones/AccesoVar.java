/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 *
 * @author crjav
 */
public class AccesoVar extends Instruccion {

    private String identificador;

    public AccesoVar(String identificador, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Buscar la variable en la tabla de símbolos
        Simbolo variable = tabla.getVariable(identificador);

        if (variable == null) {
            return new Errores("SEMANTICO", "La variable '" + identificador + "' no ha sido declarada", this.linea, this.columna);
        }

        // Establecer el tipo de la expresión
        this.tipo = variable.getTipo();

        // Retornar el valor de la variable
        return variable.getValor();
    }
}
