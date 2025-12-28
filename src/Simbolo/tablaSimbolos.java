/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Simbolo;

import java.util.HashMap;

/**
 *
 * @author crjav
 */
public class tablaSimbolos {
    private tablaSimbolos tablaAnterior;
    private HashMap<String, Object> tablaActual;
    private String nombre;
    
    public tablaSimbolos(){
        this.tablaActual = new HashMap<>();
        this.nombre = "";
    }

    public tablaSimbolos(tablaSimbolos tablaAnterior){
        this.tablaAnterior = tablaAnterior;
        this.tablaActual = new HashMap<>();       
        this.nombre = "";
    }

    public tablaSimbolos getTablaAnterior() {
        return tablaAnterior;
    }

    public HashMap<String, Object> getTablaActual() {
        return tablaActual;
    }

    public String getNombre() {
        return nombre;
    }

    public void setTablaAnterior(tablaSimbolos tablaAnterior) {
        this.tablaAnterior = tablaAnterior;
    }

    public void setTablaActual(HashMap<String, Object> tablaActual) {
        this.tablaActual = tablaActual;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public boolean setVariable(Simbolo simbolo){
        Simbolo busqueda = (Simbolo) this.tablaActual.get(simbolo.getId().toLowerCase());
        if (busqueda == null){
            // Asignar el nombre del entorno al símbolo al momento de insertarlo.
            // Si el nombre de la tabla está vacío o es nulo, usar "GLOBAL" por defecto.
            if (simbolo != null) {
                String entornoAsignar = (this.nombre != null && !this.nombre.trim().isEmpty()) ? this.nombre : "GLOBAL";
                simbolo.setEntorno(entornoAsignar);
            }
            this.tablaActual.put(simbolo.getId().toLowerCase(), simbolo);
            return true;
        }
        return false;
    }
    
    public Simbolo getVariable(String id) {
        for (tablaSimbolos i = this; i != null; i = i.getTablaAnterior()) {
            Simbolo busqueda = (Simbolo) i.tablaActual.get(id.toLowerCase());
            if (busqueda != null) {
                return busqueda;
            }
        }
        return null;
    }
    
}
