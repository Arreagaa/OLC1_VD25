package Interprete;

import Excepciones.Errores;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReporteErrores extends javax.swing.JFrame {
    
    public ReporteErrores(LinkedList<Errores> errores) {
        initComponents();
        setLocationRelativeTo(null);
        cargarErrores(errores);
    }
    
    private void cargarErrores(LinkedList<Errores> errores) {
        DefaultTableModel modelo = (DefaultTableModel) tablaErrores.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        if (errores == null || errores.isEmpty()) {
            return;
        }

        // Mostrar únicamente el primer error (ordenado por línea)
        errores.sort((e1, e2) -> Integer.compare(e1.getLinea(), e2.getLinea()));
        Errores error = errores.get(0);
        modelo.addRow(new Object[]{
            1,
            error.getTipo(),
            error.getDesc(),
            error.getLinea(),
            error.getColumna()
        });
    }
    
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaErrores = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reporte de Errores - JavaUSAC");
        setMinimumSize(new java.awt.Dimension(800, 400));

        tablaErrores.setFont(new java.awt.Font("Segoe UI", 0, 12));
        tablaErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "#", "Tipo", "Descripción", "Línea", "Columna"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaErrores.setRowHeight(25);
        tablaErrores.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaErrores);
        if (tablaErrores.getColumnModel().getColumnCount() > 0) {
            tablaErrores.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaErrores.getColumnModel().getColumn(0).setMaxWidth(50);
            tablaErrores.getColumnModel().getColumn(1).setPreferredWidth(100);
            tablaErrores.getColumnModel().getColumn(2).setPreferredWidth(400);
            tablaErrores.getColumnModel().getColumn(3).setPreferredWidth(80);
            tablaErrores.getColumnModel().getColumn(4).setPreferredWidth(80);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("Reporte de Errores");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        pack();
    }

    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaErrores;
}
