package Interprete;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Ventana para mostrar el AST generado en formato Graphviz (PNG si está disponible).
 * Si no se dispone de la utilidad dot, muestra el contenido DOT en texto.
 */
public class ASTViewer extends javax.swing.JFrame {

    public ASTViewer(String dot) throws Exception {
        initComponents();
        setTitle("AST - JavaUSAC");
        setSize(new Dimension(900, 700));
        setLocationRelativeTo(null);

        // Toolbar con botones de export
        JPanel toolbar = new JPanel();
        JButton btnExportPNG = new JButton("Exportar PNG");
        JButton btnExportDOT = new JButton("Exportar DOT");
        toolbar.add(btnExportPNG);
        toolbar.add(btnExportDOT);
        getContentPane().add(toolbar, BorderLayout.NORTH);

        // Intentar generar imagen con dot
        File tempDot = File.createTempFile("javausac_ast_", ".dot");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempDot))) {
            bw.write(dot);
        }

        File tempPng = new File(tempDot.getAbsolutePath() + ".png");

        boolean rendered = false;
        boolean dotAvailable = false;
        try {
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", tempDot.getAbsolutePath(), "-o", tempPng.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            int exitCode = p.waitFor();
            if (exitCode == 0 && tempPng.exists()) {
                Image img = ImageIO.read(tempPng);
                if (img != null) {
                    ImageIcon icon = new ImageIcon(img);
                    JLabel label = new JLabel(icon);
                    JScrollPane scroll = new JScrollPane(label);
                    getContentPane().add(scroll, BorderLayout.CENTER);
                    rendered = true;
                    dotAvailable = true;
                }
            }
        } catch (Exception ex) {
            // dot no disponible
            dotAvailable = false;
        }

        if (!rendered) {
            JTextArea text = new JTextArea(dot);
            text.setEditable(false);
            text.setFont(new java.awt.Font("Monospaced", 0, 12));
            JScrollPane scroll = new JScrollPane(text);
            getContentPane().add(scroll, BorderLayout.CENTER);
        }

        // Acciones botones
        final boolean dotOk = dotAvailable;
        btnExportDOT.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("ast.dot"));
            int r = fc.showSaveDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File out = fc.getSelectedFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
                    bw.write(dot);
                    javax.swing.JOptionPane.showMessageDialog(this, "Archivo DOT guardado: " + out.getAbsolutePath());
                } catch (IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar DOT: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnExportPNG.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("ast.png"));
            int r = fc.showSaveDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File out = fc.getSelectedFile();
                try {
                    if (dotOk) {
                        // Usar dot para generar PNG directamente
                        ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", tempDot.getAbsolutePath(), "-o", out.getAbsolutePath());
                        pb.redirectErrorStream(true);
                        Process p = pb.start();
                        int code = p.waitFor();
                        if (code == 0 && out.exists()) {
                            javax.swing.JOptionPane.showMessageDialog(this, "PNG generado: " + out.getAbsolutePath());
                        } else {
                            throw new IOException("dot falló o no generó el PNG");
                        }
                    } else {
                        // Fallback: renderizar el texto DOT en una imagen PNG
                        createTextImage(dot, out);
                        javax.swing.JOptionPane.showMessageDialog(this, "dot no está disponible. Se generó PNG con el texto DOT: " + out.getAbsolutePath());
                    }
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al exportar PNG: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Forzar revalidación
        revalidate();
        repaint();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    // Crea una imagen PNG con el texto DOT (fallback si dot no está disponible)
    private void createTextImage(String dot, File out) throws IOException {
        String[] lines = dot.split("\n");
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        int lineHeight = 16;

        int maxCols = 0;
        for (String l : lines) maxCols = Math.max(maxCols, l.length());
        int width = Math.max(400, maxCols * 7 + 20);
        int height = Math.max(200, lines.length * lineHeight + 20);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(java.awt.Color.BLACK);
        g.setFont(font);

        int y = 16;
        for (String l : lines) {
            g.drawString(l, 8, y);
            y += lineHeight;
        }
        g.dispose();

        ImageIO.write(img, "png", out);
    }
}
