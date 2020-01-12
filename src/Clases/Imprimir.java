/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author avice
 */
public class Imprimir implements Printable {

    private JPanel panel;
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex == 0) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.scale(0.70, 0.70); // reducción de la impresion al 70%
            this.panel.printAll(graphics);
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
    
    public void imprimirRecibo(JPanel panel) {
        this.panel = panel;
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        // Se pasa la instancia del JFrame al PrinterJob
        printerJob.setPrintable(this);
        // muestra ventana de dialogo para inprimir
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
        }
    }
    
}
