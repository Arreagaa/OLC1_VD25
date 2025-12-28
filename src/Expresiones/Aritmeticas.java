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
public class Aritmeticas extends Instruccion {

    private Instruccion operando1;
    private Instruccion operando2;
    private OperadoresAritmeticos operaciones;
    private Instruccion operandoUnico;

    public Aritmeticas(OperadoresAritmeticos operaciones, Instruccion operandoUnico, int linea, int columna) {
        super(new Tipo(tipoDato.ENTERO), linea, columna);
        this.operaciones = operaciones;
        this.operandoUnico = operandoUnico;
    }

    public Aritmeticas(Instruccion operando1, Instruccion operando2, OperadoresAritmeticos operaciones, int linea, int columna) {
        super(new Tipo(tipoDato.ENTERO), linea, columna);
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operaciones = operaciones;
    }

    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        Object opIzq = null, opDer = null, Unico = null;
        if (this.operandoUnico != null) {
            Unico = this.operandoUnico.interpretar(arbol, tabla);
            if (Unico instanceof Errores) {
                return Unico;
            }
        } else {
            opIzq = this.operando1.interpretar(arbol, tabla);
            if (opIzq instanceof Errores) {
                return opIzq;
            }
            opDer = this.operando2.interpretar(arbol, tabla);
            if (opDer instanceof Errores) {
                return opDer;
            }
        }
        return switch (operaciones) {
            case SUMA ->
                this.suma(opIzq, opDer);
            case RESTA ->
                this.resta(opIzq, opDer);
            case MULTIPLICACION ->
                this.multiplicacion(opIzq, opDer);
            case DIVISION ->
                this.division(opIzq, opDer);
            case POTENCIA ->
                this.potencia(opIzq, opDer);
            case MODULO ->
                this.modulo(opIzq, opDer);
            case NEGACION ->
                this.negacion(Unico);
            default ->
                new Errores("SEMANTICO", "Operador aritmetico invalido", this.linea, this.columna);

        };
    }

    public Object suma(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (int) op1 + (double) op2;
                    }
                    case BOOLEANO -> {
                        // Booleano: true=1, false=0
                        this.tipo.setTipo(tipoDato.ENTERO);
                        int valorBool = (boolean) op2 ? 1 : 0;
                        return (int) op1 + valorBool;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 + (int) ((Character) op2);
                    }
                    case CADENA -> {
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Suma erronea", this.linea, this.columna);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 + (double) op2;
                    }
                    case BOOLEANO -> {
                        // Booleano: true=1, false=0
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        int valorBool = (boolean) op2 ? 1 : 0;
                        return (double) op1 + (double) valorBool;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 + (int) ((Character) op2);
                    }
                    case CADENA -> {
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Suma erronea", this.linea, this.columna);
                    }
                }
            }
            case BOOLEANO -> {
                // Booleano solo puede sumarse con cadena
                switch (tipo2) {
                    case CADENA -> {
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Suma erronea", this.linea, this.columna);
                    }
                }
            }
            case CARACTER -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) ((Character) op1) + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) ((Character) op1) + (double) op2;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                    }
                    case CADENA -> {
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Suma erronea", this.linea, this.columna);
                    }
                }
            }
            case CADENA -> {
                this.tipo.setTipo(tipoDato.CADENA);
                return op1.toString() + op2.toString();
            }

            default -> {
                return new Errores("SEMANTICO", "Suma erronea", this.linea, this.columna);
            }
        }

    }

    public Object negacion(Object op1) {
        var opU = this.operandoUnico.tipo.getTipo();
        switch (opU) {
            case ENTERO -> {
                this.tipo.setTipo(tipoDato.ENTERO);
                return (int) op1 * -1;
            }
            case DECIMAL -> {
                this.tipo.setTipo(tipoDato.DECIMAL);
                return (double) op1 * -1;
            }
            default -> {
                return new Errores("SEMANTICO", "Negacion erronea", this.linea, this.columna);
            }
        }
    }

    // Resta: Entero-Entero, Entero-Decimal, Decimal-Entero, Decimal-Decimal, Caracter-Entero, Caracter-Decimal, Entero-Caracter, Decimal-Caracter
    public Object resta(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 - (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (int) op1 - (double) op2;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 - (int) ((Character) op2);
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Resta erronea", this.linea, this.columna);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 - (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 - (double) op2;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 - (int) ((Character) op2);
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Resta erronea", this.linea, this.columna);
                    }
                }
            }
            case CARACTER -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) ((Character) op1) - (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (int) ((Character) op1) - (double) op2;
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Resta erronea", this.linea, this.columna);
                    }
                }
            }
            default -> {
                return new Errores("SEMANTICO", "Resta erronea", this.linea, this.columna);
            }
        }
    }

    // Multiplicacion: Entero-Entero, Entero-Decimal, Decimal-Entero, Decimal-Decimal, Caracter-Entero, Caracter-Decimal, Entero-Caracter, Decimal-Caracter
    public Object multiplicacion(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 * (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (int) op1 * (double) op2;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 * (int) ((Character) op2);
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Multiplicacion erronea", this.linea, this.columna);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 * (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 * (double) op2;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 * (int) ((Character) op2);
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Multiplicacion erronea", this.linea, this.columna);
                    }
                }
            }
            case CARACTER -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) ((Character) op1) * (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (int) ((Character) op1) * (double) op2;
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Multiplicacion erronea", this.linea, this.columna);
                    }
                }
            }
            default -> {
                return new Errores("SEMANTICO", "Multiplicacion erronea", this.linea, this.columna);
            }
        }
    }

    // Division: SIEMPRE retorna Decimal
    public Object division(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        if ((int) op2 == 0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) (int) op1 / (double) (int) op2;
                    }
                    case DECIMAL -> {
                        if ((double) op2 == 0.0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) (int) op1 / (double) op2;
                    }
                    case CARACTER -> {
                        int val = (int) ((Character) op2);
                        if (val == 0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) (int) op1 / (double) val;
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Division erronea", this.linea, this.columna);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        if ((int) op2 == 0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 / (double) (int) op2;
                    }
                    case DECIMAL -> {
                        if ((double) op2 == 0.0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 / (double) op2;
                    }
                    case CARACTER -> {
                        int val = (int) ((Character) op2);
                        if (val == 0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 / (double) val;
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Division erronea", this.linea, this.columna);
                    }
                }
            }
            case CARACTER -> {
                int val1 = (int) ((Character) op1);
                switch (tipo2) {
                    case ENTERO -> {
                        if ((int) op2 == 0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) val1 / (double) (int) op2;
                    }
                    case DECIMAL -> {
                        if ((double) op2 == 0.0) {
                            return new Errores("SEMANTICO", "Division por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) val1 / (double) op2;
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Division erronea", this.linea, this.columna);
                    }
                }
            }
            default -> {
                return new Errores("SEMANTICO", "Division erronea", this.linea, this.columna);
            }
        }
    }

    // Potencia: Entero-Entero, Entero-Decimal, Decimal-Entero, Decimal-Decimal
    public Object potencia(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) Math.pow((int) op1, (int) op2);
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return Math.pow((int) op1, (double) op2);
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Potencia erronea", this.linea, this.columna);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return Math.pow((double) op1, (int) op2);
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return Math.pow((double) op1, (double) op2);
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Potencia erronea", this.linea, this.columna);
                    }
                }
            }
            default -> {
                return new Errores("SEMANTICO", "Potencia erronea", this.linea, this.columna);
            }
        }
    }

    // Modulo: Entero-Entero, Entero-Decimal, Decimal-Entero, Decimal-Decimal (siempre retorna Decimal)
    public Object modulo(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        if ((int) op2 == 0) {
                            return new Errores("SEMANTICO", "Modulo por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) ((int) op1 % (int) op2);
                    }
                    case DECIMAL -> {
                        if ((double) op2 == 0.0) {
                            return new Errores("SEMANTICO", "Modulo por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) (int) op1 % (double) op2;
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Modulo erroneo", this.linea, this.columna);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        if ((int) op2 == 0) {
                            return new Errores("SEMANTICO", "Modulo por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 % (double) (int) op2;
                    }
                    case DECIMAL -> {
                        if ((double) op2 == 0.0) {
                            return new Errores("SEMANTICO", "Modulo por cero", this.linea, this.columna);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 % (double) op2;
                    }
                    default -> {
                        return new Errores("SEMANTICO", "Modulo erroneo", this.linea, this.columna);
                    }
                }
            }
            default -> {
                return new Errores("SEMANTICO", "Modulo erroneo", this.linea, this.columna);
            }
        }
    }
}
