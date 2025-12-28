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
public class Declaracion extends Instruccion {

    private String identificador;
    private Instruccion valor;

    public Declaracion(String identificador, Instruccion valor, Tipo tipo, int linea, int columna) {
        super(tipo, linea, columna);
        this.identificador = identificador;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Verificar si la variable ya existe en el ENTORNO ACTUAL (permitir shadowing en tablas superiores)
        Object existente = tabla.getTablaActual().get(identificador.toLowerCase());
        if (existente != null) {
            return new Errores("SEMANTICO", "La variable '" + identificador + "' ya fue declarada", this.linea, this.columna);
        }

        // Si tiene valor de inicialización
        if (this.valor != null) {
            Object valorInterpretado = this.valor.interpretar(arbol, tabla);
            
            if (valorInterpretado instanceof Errores) {
                return valorInterpretado;
            }

            // Verificar que el tipo del valor sea compatible con el tipo declarado
            tipoDato tipoValor = this.valor.tipo.getTipo();
            tipoDato tipoDeclarado = this.tipo.getTipo();

            // Validar compatibilidad de tipos
            if (!validarTipos(tipoDeclarado, tipoValor)) {
                return new Errores("SEMANTICO", 
                    "Tipo de dato incompatible. No se puede asignar " + tipoValor + " a " + tipoDeclarado, 
                    this.linea, this.columna);
            }

            // Crear el símbolo con su valor
            String nombreEntorno = tabla.getNombre() != null ? tabla.getNombre() : "GLOBAL";
            Simbolo simbolo = new Simbolo(this.tipo, this.identificador, valorInterpretado, this.linea, this.columna, nombreEntorno);
            tabla.setVariable(simbolo);
            arbol.agregarSimboloHistorial(simbolo); // Agregar al historial
        } else {
            // Declaración sin inicialización, asignar valor por defecto
            Object valorDefault = obtenerValorDefault(this.tipo.getTipo());
            String nombreEntorno = tabla.getNombre() != null ? tabla.getNombre() : "GLOBAL";
            Simbolo simbolo = new Simbolo(this.tipo, this.identificador, valorDefault, this.linea, this.columna, nombreEntorno);
            tabla.setVariable(simbolo);
            arbol.agregarSimboloHistorial(simbolo); // Agregar al historial
        }

        return null;
    }

    private boolean validarTipos(tipoDato tipoDeclarado, tipoDato tipoValor) {
        // El tipo debe ser exactamente igual
        if (tipoDeclarado == tipoValor) {
            return true;
        }

        // Permitir int a double
        if (tipoDeclarado == tipoDato.DECIMAL && tipoValor == tipoDato.ENTERO) {
            return true;
        }

        return false;
    }

    private Object obtenerValorDefault(tipoDato tipo) {
        return switch (tipo) {
            case ENTERO -> 0;
            case DECIMAL -> 0.0;
            case BOOLEANO -> true;
            case CARACTER -> '\u0000';
            case CADENA -> "";
            default -> null;
        };
    }

    public String getId() {
        return this.identificador;
    }

    public Tipo getTipo() {
        return this.tipo;
    }
}

