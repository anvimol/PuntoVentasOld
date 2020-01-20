/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import javax.swing.JLabel;

/**
 *
 * @author avice
 */
public interface IUploadImage {
    void cargarImagen(JLabel label, boolean valor, String imagen);
    
    void copiarImagen(String fileName);
}
