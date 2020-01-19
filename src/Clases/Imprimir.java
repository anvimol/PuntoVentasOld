/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Connection.Consult;
import java.awt.Color;
import java.awt.Font;
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
public class Imprimir extends Consult implements Printable {

    private JPanel panel;
    private int pos = 10;
    private double importe = 0;

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex == 0) {
            if (panel != null) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.scale(0.70, 0.70); // reducción de la impresion al 70%
                this.panel.printAll(graphics);
            } else {
                Font normalFont1 = new Font("serif", Font.BOLD, 12);
                Font normalFont2 = new Font("serif", Font.PLAIN, 8);
                graphics.setFont(normalFont1);
                graphics.drawString("Factura Punto de Ventas", 25, 30);
                graphics.setFont(normalFont2);
                graphics.drawString("Producto", 25, 50);
                graphics.drawString("Cantidad", 100, 50);
                graphics.drawString("Importe", 180, 50);
                tempoVentas = tempoVentas();
                if (!tempoVentas.isEmpty()) {
                    tempoVentas.forEach(item -> {
                        graphics.setColor(Color.BLACK);
                        graphics.drawString(item.getDescripcion(), 25, pos);
                        graphics.drawString(String.valueOf(item.getCantidad()), 100, pos);
                        graphics.setColor(new Color(0, 153, 102));
                        graphics.drawString(item.getImporte(), 180, pos);
                        pos += 15;
                        importe += formato.reconstruir(item.getImporte().replace("€", ""));
                    });
                    pos += 15;
                    graphics.setColor(Color.BLACK);
                    graphics.drawString("Total", 25, pos);
                    graphics.drawString(formato.decimal(importe) + "€", 100, pos);
                    pos += 15;
                }
                graphics.drawString("@anvimol", 110, pos);
                importe = 0;
            }

            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }

    public void imprimirRecibo(JPanel panel) {
        this.panel = null;
        this.pos = 10;
        this.importe = 0;
        
        if (panel != null) {
            this.panel = panel;
        }

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
