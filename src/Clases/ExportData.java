/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author avice
 */
public class ExportData extends JFrame {

    private File archivo;
    private JFileChooser abrirArchivo = new JFileChooser();
    private TableModel tableModel;
    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

    public void exportarDataExcel(JTable table, String[] titles, int[] colum,
            String pagina) {
        abrirArchivo.setFileFilter(new FileNameExtensionFilter("Excel", "xls"));
        abrirArchivo.setDialogTitle("Especifique un archivo para guardar");
        int respuesta = abrirArchivo.showSaveDialog(this);
        WritableSheet excelSheet = null;
        WritableWorkbook excelWorkbook = null;
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            archivo = new File(abrirArchivo.getSelectedFile().getPath() + ".xls");
            try {
                excelWorkbook = Workbook.createWorkbook(archivo);
                excelWorkbook.createSheet(pagina, 0);
                excelSheet = excelWorkbook.getSheet(0);
                tableModel = table.getModel();
                int cols = tableModel.getColumnCount();
                int filas = tableModel.getRowCount();
                int fila = 1;
                int column = 0;
                for (int i = 0; i < titles.length; i++) {
                    Label title = new Label(i, 0, titles[i]);
                    excelSheet.addCell(title);
                }
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < cols; j++) {
                        boolean valor = true;
                        for (int k = 0; k < colum.length; k++) {
                            if (j == colum[k]) {
                                valor = false;
                            }
                        }
                        if (valor) {
                            Label datos = new Label(column, fila, tableModel.getValueAt(i, j).toString());
                            excelSheet.addCell(datos);
                            column++;
                        }
                    }
                    fila++;
                    column = 0;
                }
                excelWorkbook.write();
                excelWorkbook.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
            } catch (WriteException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
            }

        }
    }

    public void exportarDataPDF(JTable tables, String[] titles, int[] colum,
            String pagina) {
        abrirArchivo.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
        abrirArchivo.setDialogTitle("Especifique un archivo para guardar");
        int respuesta = abrirArchivo.showSaveDialog(this);
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            archivo = new File(abrirArchivo.getSelectedFile().getPath() + ".pdf");
            // Creamos el documento e indicamos el nombre del fichero
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(archivo));
                document.open();
                // Añadimos los metadatos del PDF
                document.addKeywords("Java, PDF");
                document.addAuthor("Antonio Vicent");
                document.addCreator("Antonio Vicent");
                
                // Primera pagina
                Anchor anchor = new Anchor(pagina, categoryFont);
                Chapter catPart = new Chapter(new Paragraph(anchor), 1);
                Paragraph subPara = new Paragraph("Realizado por @anvimol", subcategoryFont);
                Section subCatPart = catPart.addSection(subPara);
                subCatPart.add(new Paragraph("Lista de Productos"));
                subCatPart.add(new Paragraph("\n"));
                // Creamos la tabla
                PdfPTable table = new PdfPTable(titles.length);
                // Llenamos las filas de Table
                PdfPCell columnHeader;
                // Rellenampos las cabeceras de las columnas de la tabla
                for (int column = 0; column < titles.length; column++) {
                    columnHeader = new PdfPCell(new Phrase(titles[column]));
                    table.addCell(columnHeader);
                }
                table.setHeaderRows(1);
                // Rellenamos las filas de la tabla
                for (int row = 0; row < tables.getRowCount(); row++) {
                    for (int column = 0; column < tables.getColumnCount(); column++) {
                        boolean valor = true;
                        for (int i = 0; i < colum.length; i++) {
                            if (column == colum[i]) {
                                valor = false;
                            }
                        }
                        if (valor) {
                            table.addCell(tables.getValueAt(row, column).toString());
                        }
                    }
                }
                subCatPart.add(table);
                document.add(catPart);
                document.close();
                
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
            } catch (DocumentException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
            }
        }
    }
}
