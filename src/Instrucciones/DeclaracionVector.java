package Instrucciones;

import Abstracto.Instruccion;
import Simbolo.*;
import Excepciones.Errores;
import java.util.LinkedList;

/**
 * Declaración de Vectores (Arrays)
 * Sintaxis: var vector1 : string [] = ["Hola", "Mundo"];
 * Sintaxis 2D: var vector2 : int [][] = [[1, 2], [3, 4]];
 */
public class DeclaracionVector extends Instruccion {
    private String identificador;
    private Tipo tipoElemento;
    private int dimensiones; // 1 o 2
    private LinkedList<Instruccion> valores; // Para 1D: lista de expresiones
    private LinkedList<LinkedList<Instruccion>> valores2D; // Para 2D: lista de listas

    // Constructor para vectores 1D
    public DeclaracionVector(String identificador, Tipo tipoElemento, LinkedList<Instruccion> valores, int linea, int columna) {
        super(tipoElemento, linea, columna);
        this.identificador = identificador;
        this.tipoElemento = tipoElemento;
        this.valores = valores;
        this.dimensiones = 1;
        this.valores2D = null;
    }

    // Constructor para vectores 2D
    public DeclaracionVector(String identificador, Tipo tipoElemento, LinkedList<LinkedList<Instruccion>> valores2D, int linea, int columna, boolean is2D) {
        super(tipoElemento, linea, columna);
        this.identificador = identificador;
        this.tipoElemento = tipoElemento;
        this.valores2D = valores2D;
        this.dimensiones = 2;
        this.valores = null;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Verificar si la variable ya existe
        if (tabla.getVariable(identificador) != null) {
            return new Errores("SEMANTICO", "La variable '" + identificador + "' ya fue declarada", this.linea, this.columna);
        }

        if (dimensiones == 1) {
            return interpretarVector1D(arbol, tabla);
        } else {
            return interpretarVector2D(arbol, tabla);
        }
    }

    private Object interpretarVector1D(Arbol arbol, tablaSimbolos tabla) {
        // Crear arreglo Java para almacenar los valores
        Object[] arrayValores = new Object[valores.size()];
        
        // Interpretar cada valor de la lista
        for (int i = 0; i < valores.size(); i++) {
            Instruccion expr = valores.get(i);
            Object valorInterpretado = expr.interpretar(arbol, tabla);
            
            if (valorInterpretado instanceof Errores) {
                return valorInterpretado;
            }
            
            // Validar tipo del elemento
            if (!validarTipo(expr.tipo.getTipo(), tipoElemento.getTipo())) {
                return new Errores("SEMANTICO", 
                    "Tipo incompatible en vector. Se esperaba " + tipoElemento.getTipo() + " pero se encontró " + expr.tipo.getTipo(),
                    this.linea, this.columna);
            }
            
            arrayValores[i] = valorInterpretado;
        }
        
        // Crear símbolo del vector
        String nombreEntorno = tabla.getNombre() != null ? tabla.getNombre() : "GLOBAL";
        Simbolo simbolo = new Simbolo(this.tipo, this.identificador, arrayValores, this.linea, this.columna, nombreEntorno);
        tabla.setVariable(simbolo);
        arbol.agregarSimboloHistorial(simbolo);
        
        return null;
    }

    private Object interpretarVector2D(Arbol arbol, tablaSimbolos tabla) {
        // Crear matriz Java para almacenar los valores
        Object[][] matriz = new Object[valores2D.size()][];
        
        // Interpretar cada fila
        for (int i = 0; i < valores2D.size(); i++) {
            LinkedList<Instruccion> fila = valores2D.get(i);
            Object[] filaValores = new Object[fila.size()];
            
            // Interpretar cada elemento de la fila
            for (int j = 0; j < fila.size(); j++) {
                Instruccion expr = fila.get(j);
                Object valorInterpretado = expr.interpretar(arbol, tabla);
                
                if (valorInterpretado instanceof Errores) {
                    return valorInterpretado;
                }
                
                // Validar tipo del elemento
                if (!validarTipo(expr.tipo.getTipo(), tipoElemento.getTipo())) {
                    return new Errores("SEMANTICO", 
                        "Tipo incompatible en vector. Se esperaba " + tipoElemento.getTipo() + " pero se encontró " + expr.tipo.getTipo(),
                        this.linea, this.columna);
                }
                
                filaValores[j] = valorInterpretado;
            }
            
            matriz[i] = filaValores;
        }
        
        // Crear símbolo del vector 2D
        String nombreEntorno = tabla.getNombre() != null ? tabla.getNombre() : "GLOBAL";
        Simbolo simbolo = new Simbolo(this.tipo, this.identificador, matriz, this.linea, this.columna, nombreEntorno);
        tabla.setVariable(simbolo);
        arbol.agregarSimboloHistorial(simbolo);
        
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
