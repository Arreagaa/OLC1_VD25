package Instrucciones;

import Abstracto.Instruccion;
import Excepciones.Errores;
import Simbolo.*;

/**
 *
 * @author crjav
 */
public class Print extends Instruccion {
    private Instruccion expresion;

    public Print(Instruccion expresion, int linea, int columna) {
        super(new Tipo(tipoDato.VOID), linea, columna);
        this.expresion = expresion;
    }
    
    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        var resultado = this.expresion.interpretar(arbol, tabla);
        
        // Si la expresión generó un error, devolverlo en lugar de imprimirlo
        if(resultado instanceof Errores){
            return resultado;
        }
        
        arbol.Print(resultado.toString());
        return null;
    }
}
