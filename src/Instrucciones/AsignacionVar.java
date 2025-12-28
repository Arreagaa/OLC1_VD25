/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;

/**
 *
 * @author crjav
 */
public class AsignacionVar extends Instruccion {

    private String identificador;
    private Instruccion valor;

    public AsignacionVar(String identificador, Instruccion valor, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.identificador = identificador;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Obtener la variable de la tabla de símbolos
        Simbolo variable = tabla.getVariable(identificador);

        if (variable == null) {
            return new Errores("SEMANTICO", "La variable '" + identificador + "' no ha sido declarada", this.linea, this.columna);
        }

        // Interpretar el nuevo valor
        Object nuevoValor = this.valor.interpretar(arbol, tabla);
        
        if (nuevoValor instanceof Errores) {
            return nuevoValor;
        }

        // Obtener el tipo del nuevo valor
        tipoDato tipoNuevoValor = this.valor.tipo.getTipo();
        tipoDato tipoVariable = variable.getTipo().getTipo();

        // Validar que el tipo sea compatible
        if (!validarTipos(tipoVariable, tipoNuevoValor)) {
            return new Errores("SEMANTICO", 
                "Tipo de dato incompatible. No se puede asignar " + tipoNuevoValor + " a variable de tipo " + tipoVariable, 
                this.linea, this.columna);
        }

        // Actualizar el valor de la variable
        variable.setValor(nuevoValor);

        return null;
    }

    private boolean validarTipos(tipoDato tipoVariable, tipoDato tipoValor) {
        // El tipo debe ser exactamente igual
        if (tipoVariable == tipoValor) {
            return true;
        }

        // Permitir int a double
        if (tipoVariable == tipoDato.DECIMAL && tipoValor == tipoDato.ENTERO) {
            return true;
        }

        return false;
    }
}
