/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Interfaces.IUploadImage;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author avice
 */
public class UploadImage extends javax.swing.JFrame implements IUploadImage {

    private File archivo;
    private JFileChooser abrirArchivo;
    private static String urlOrigen = null;

    @Override
    public void cargarImagen(JLabel label, boolean valor, String imagen) {
        if (imagen == null) {
            if (valor) {
                abrirArchivo = new JFileChooser();
                abrirArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de Imagen",
                        "jpg", "jpeg", "png", "gif"));
                int respuesta = abrirArchivo.showOpenDialog(this);
                if (respuesta == JFileChooser.APPROVE_OPTION) {
                    archivo = abrirArchivo.getSelectedFile();
                    urlOrigen = archivo.getAbsolutePath();
                }
            } else {
                urlOrigen = "C:\\Users\\avice\\SynologyDrive\\ProyectosNetBeans"
                        + "\\Punto de ventas\\src\\fotos\\users.png";
            }
        } else {
            urlOrigen = imagen;
        }
        Image foto = getToolkit().getImage(urlOrigen);
        foto = foto.getScaledInstance(216, 258, 1);
        label.setIcon(new ImageIcon(foto));
    }

    @Override
    public void copiarImagen(String fileName) {
        String imgDestino = "C:\\Users\\avice\\SynologyDrive\\ProyectosNetBeans\\"
                + "Punto de ventas\\src\\fotos\\" + fileName + ".png";
        try {
            if (urlOrigen == null) {
                urlOrigen = "C:\\Users\\avice\\SynologyDrive\\ProyectosNetBeans"
                        + "\\Punto de ventas\\src\\fotos\\users.png";
            }
            if (!urlOrigen.equals(imgDestino)) {
                FileInputStream fregis = new FileInputStream(urlOrigen);
                FileOutputStream fsalida = new FileOutputStream(imgDestino, true);
                int b = fregis.read();
                while (b != -1) {
                    fsalida.write(b);
                    b = fregis.read();
                }
                fsalida.flush();
                fsalida.close();
                fregis.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al generar la copia de la imagen");
        }
        urlOrigen = null;
    }

    public static String getUrlOrigen() {
        return urlOrigen;
    }

}
