package Interprete;

import Simbolo.*;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.List;

public class ReporteSimbolos extends javax.swing.JFrame {
    
    public ReporteSimbolos(LinkedList<Simbolo> simbolos) {
        initComponents();
        setLocationRelativeTo(null);
        cargarSimbolos(simbolos);
    }
    
    private void cargarSimbolos(LinkedList<Simbolo> simbolos) {
        DefaultTableModel modelo = (DefaultTableModel) tablaSimbolos.getModel();
        modelo.setRowCount(0); // Limpiar tabla
        
        int contador = 1;
        for (Simbolo simbolo : simbolos) {
            String tipoElemento = "Variable";
            Object valorSim = simbolo.getValor();
            if (valorSim instanceof String) {
                if ("FUNCION".equals(valorSim)) tipoElemento = "Función";
                else if ("METODO".equals(valorSim)) tipoElemento = "Método";
            }

            String valorFormateado = formatearValor(valorSim);

            modelo.addRow(new Object[]{
                contador++,
                simbolo.getId(),
                tipoElemento,
                (simbolo.getTipo() != null) ? obtenerNombreTipo(simbolo.getTipo().getTipo()) : "unknown",
                (simbolo.getEntorno() != null && !simbolo.getEntorno().trim().isEmpty()) ? simbolo.getEntorno() : "GLOBAL",
                valorFormateado,
                simbolo.getLinea() > 0 ? simbolo.getLinea() : "N/A",
                simbolo.getColumna() > 0 ? simbolo.getColumna() : "N/A"
            });
        }
    }
    
    private String obtenerNombreTipo(tipoDato tipo) {
        return switch (tipo) {
            case ENTERO -> "int";
            case DECIMAL -> "double";
            case BOOLEANO -> "bool";
            case CARACTER -> "char";
            case CADENA -> "string";
            case VOID -> "void";
            default -> "unknown";
        };
    }

    private String formatearValor(Object valor) {
        if (valor == null) return "null";

        // Si es la marca especial para función/método, devolverla tal cual
        if (valor instanceof String && ("FUNCION".equals(valor) || "METODO".equals(valor) || "INSTRUCCION".equals(valor))) {
            return (String) valor;
        }

        // Listas
        if (valor instanceof List) {
            return valor.toString();
        }

        // Arrays (incluye arrays primitivos)
        Class<?> cls = valor.getClass();
        if (cls.isArray()) {
            int len = Array.getLength(valor);
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < len; i++) {
                Object elem = Array.get(valor, i);
                sb.append(formatearValor(elem));
                if (i < len - 1) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }

        // Otros tipos
        return valor.toString();
    }
    
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaSimbolos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reporte de Tabla de Símbolos - JavaUSAC");
        setMinimumSize(new java.awt.Dimension(1000, 500));

        tablaSimbolos.setFont(new java.awt.Font("Segoe UI", 0, 12));
        tablaSimbolos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "#", "Id", "Tipo", "Tipo de Dato", "Entorno", "Valor", "Línea", "Columna"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaSimbolos.setRowHeight(25);
        tablaSimbolos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaSimbolos);
        if (tablaSimbolos.getColumnModel().getColumnCount() > 0) {
            tablaSimbolos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaSimbolos.getColumnModel().getColumn(0).setMaxWidth(50);
            tablaSimbolos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tablaSimbolos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaSimbolos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaSimbolos.getColumnModel().getColumn(4).setPreferredWidth(100);
            tablaSimbolos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tablaSimbolos.getColumnModel().getColumn(6).setPreferredWidth(80);
            tablaSimbolos.getColumnModel().getColumn(7).setPreferredWidth(80);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("Reporte de Tabla de Símbolos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        pack();
    }

    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaSimbolos;
}
