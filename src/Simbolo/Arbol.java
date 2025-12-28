/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Simbolo;

import Abstracto.Instruccion;
import Excepciones.Errores;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author crjav
 */
public class Arbol {
    private LinkedList<Instruccion> instrucciones;
    private tablaSimbolos tablaGlobal;
    private LinkedList<Errores> errores;
    private String consolas;
    private LinkedList<Simbolo> historialSimbolos; // Historial de TODOS los símbolos declarados
    private HashMap<String, Instruccion> funcionesMetodos; // Tabla de funciones y métodos

    public Arbol(LinkedList<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
        this.tablaGlobal = new tablaSimbolos();
        this.errores = new LinkedList<>();
        this.consolas = "";
        this.historialSimbolos = new LinkedList<>();
        this.funcionesMetodos = new HashMap<>();
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    public tablaSimbolos getTablaGlobal() {
        return tablaGlobal;
    }

    public LinkedList<Errores> getErrores() {
        return errores;
    }

    public String getConsolas() {
        return consolas;
    }

    public void setInstrucciones(LinkedList<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void setTablaGlobal(tablaSimbolos tablaGlobal) {
        this.tablaGlobal = tablaGlobal;
    }

    public void setErrores(LinkedList<Errores> errores) {
        this.errores = errores;
    }

    public void setConsolas(String consolas) {
        this.consolas = consolas;
    }

    public void Print(String valor) {
        this.consolas += valor + "\n";
    }

    public void agregarError(Errores error) {
        this.errores.add(error);
    }

    public void agregarSimboloHistorial(Simbolo simbolo) {
        if (simbolo == null)
            return;
        // Verificar si ya existe un símbolo con el mismo ID y ENTORNO
        for (Simbolo s : this.historialSimbolos) {
            String entExist = s.getEntorno();
            String entNew = simbolo.getEntorno();
            boolean mismoEntorno = (entExist == null && entNew == null)
                    || (entExist != null && entExist.equals(entNew));
            if (s.getId().equals(simbolo.getId()) && mismoEntorno) {
                return; // Ya existe, no agregar duplicado
            }
        }
        this.historialSimbolos.add(simbolo);
    }

    public LinkedList<Simbolo> obtenerSimbolos(tablaSimbolos tabla) {
        LinkedList<Simbolo> result = new LinkedList<>();
        // Añadir historial primero
        for (Simbolo s : this.historialSimbolos) {
            result.add(s);
        }

        // Añadir símbolos de la tabla y sus tablas padres
        for (tablaSimbolos t = tabla; t != null; t = t.getTablaAnterior()) {
            java.util.Map<String, Object> mapa = t.getTablaActual();
            for (Object value : mapa.values()) {
                if (value instanceof Simbolo) {
                    Simbolo s = (Simbolo) value;
                    boolean existe = false;
                    for (Simbolo ex : result) {
                        String entEx = ex.getEntorno();
                        String entS = s.getEntorno();
                        boolean mismoEntorno = (entEx == null && entS == null) || (entEx != null && entEx.equals(entS));
                        if (ex.getId().equals(s.getId()) && mismoEntorno) {
                            existe = true;
                            break;
                        }
                    }
                    if (!existe)
                        result.add(s);
                }
            }
        }

        return result;
    }

    // ========== MÉTODOS PARA FUNCIONES Y MÉTODOS ==========

    public void setFuncionMetodo(String id, Instruccion funcion) {
        this.funcionesMetodos.put(id.toLowerCase(), funcion);
    }

    public Instruccion getFuncionMetodo(String id) {
        return this.funcionesMetodos.get(id.toLowerCase());
    }

    public HashMap<String, Instruccion> getFuncionesMetodos() {
        return funcionesMetodos;
    }

    // Generar representación DOT del AST
    public String generarDot() {
        StringBuilder nodes = new StringBuilder();
        StringBuilder edges = new StringBuilder();
        int[] contador = new int[] { 0 }; // Contador mutable

        nodes.append("digraph AST {\n");
        nodes.append("node [shape=box, style=filled, fillcolor=white];\n");

        int raiz = nuevoNodo("ROOT", contador, nodes);

        for (Instruccion instr : this.instrucciones) {
            if (instr == null)
                continue;
            int hijo = procesarInstruccion(instr, contador, nodes, edges);
            edges.append(String.format("node%d -> node%d;\n", raiz, hijo));
        }

        nodes.append(edges);
        nodes.append("}\n");
        return nodes.toString();
    }

    private int nuevoNodo(String label, int[] contador, StringBuilder nodes) {
        int id = contador[0]++;
        String safeLabel = label.replace("\"", "\\\"");
        nodes.append(String.format("node%d [label=\"%s\"];\n", id, safeLabel));
        return id;
    }

    private int procesarInstruccion(Instruccion instr, int[] contador, StringBuilder nodes, StringBuilder edges) {
        if (instr instanceof Instrucciones.DeclaracionFuncion) {
            Instrucciones.DeclaracionFuncion df = (Instrucciones.DeclaracionFuncion) instr;
            int idNodo = nuevoNodo("FUNCION: " + df.getIdentificador(), contador, nodes);

            // Parámetros
            int paramsNodo = nuevoNodo("PARAMETROS", contador, nodes);
            edges.append(String.format("node%d -> node%d;\n", idNodo, paramsNodo));
            if (df.getParametros() != null) {
                for (var p : df.getParametros()) {
                    String idp = (String) p.get("id");
                    String tipop = ((Tipo) p.get("tipo")).getTipo().name();
                    int pn = nuevoNodo(idp + ": " + tipop, contador, nodes);
                    edges.append(String.format("node%d -> node%d;\n", paramsNodo, pn));
                }
            }

            // Cuerpo
            int cuerpo = nuevoNodo("CUERPO", contador, nodes);
            edges.append(String.format("node%d -> node%d;\n", idNodo, cuerpo));
            if (df.getInstrucciones() != null) {
                for (Instruccion ins : df.getInstrucciones()) {
                    if (ins == null)
                        continue;
                    int hn = procesarInstruccion(ins, contador, nodes, edges);
                    edges.append(String.format("node%d -> node%d;\n", cuerpo, hn));
                }
            }
            return idNodo;
        } else if (instr instanceof Instrucciones.DeclaracionMetodo) {
            Instrucciones.DeclaracionMetodo dm = (Instrucciones.DeclaracionMetodo) instr;
            int idNodo = nuevoNodo("METODO: " + dm.getIdentificador(), contador, nodes);

            // Parámetros
            int paramsNodo = nuevoNodo("PARAMETROS", contador, nodes);
            edges.append(String.format("node%d -> node%d;\n", idNodo, paramsNodo));
            if (dm.getParametros() != null) {
                for (var p : dm.getParametros()) {
                    String idp = (String) p.get("id");
                    String tipop = ((Tipo) p.get("tipo")).getTipo().name();
                    int pn = nuevoNodo(idp + ": " + tipop, contador, nodes);
                    edges.append(String.format("node%d -> node%d;\n", paramsNodo, pn));
                }
            }

            // Cuerpo
            int cuerpo = nuevoNodo("CUERPO", contador, nodes);
            edges.append(String.format("node%d -> node%d;\n", idNodo, cuerpo));
            if (dm.getInstrucciones() != null) {
                for (Instruccion ins : dm.getInstrucciones()) {
                    if (ins == null)
                        continue;
                    int hn = procesarInstruccion(ins, contador, nodes, edges);
                    edges.append(String.format("node%d -> node%d;\n", cuerpo, hn));
                }
            }
            return idNodo;
        } else if (instr instanceof Instrucciones.Declaracion) {
            Instrucciones.Declaracion d = (Instrucciones.Declaracion) instr;
            String label = "DECLARACION: " + d.getId() + " : " + d.getTipo().getTipo();
            return nuevoNodo(label, contador, nodes);
        } else if (instr instanceof Instrucciones.Start) {
            Instrucciones.Start s = (Instrucciones.Start) instr;
            String label = "START: " + s.getIdentificador();
            return nuevoNodo(label, contador, nodes);
        } else if (instr instanceof Instrucciones.If) {
            // IF representado como nodo simple (no exponer campos privados)
            return nuevoNodo("IF", contador, nodes);
        } else if (instr instanceof Instrucciones.For) {
            // FOR como nodo simple
            return nuevoNodo("FOR", contador, nodes);
        } else if (instr instanceof Instrucciones.While) {
            return nuevoNodo("WHILE", contador, nodes);
        } else if (instr instanceof Instrucciones.DoWhile) {
            return nuevoNodo("DO_WHILE", contador, nodes);
        } else if (instr instanceof Instrucciones.Switch) {
            return nuevoNodo("SWITCH", contador, nodes);
        } else {
            // Nodo genérico con el nombre de la clase
            return nuevoNodo(instr.getClass().getSimpleName(), contador, nodes);
        }

    }
}
