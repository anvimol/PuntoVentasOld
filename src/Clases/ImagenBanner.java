/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 *
 * @author AlexJPZ
 */
public class ImagenBanner extends javax.swing.JPanel{
     public ImagenBanner() {
          this.setSize(1920,65);
      }
    @Override
      public void paintComponent(Graphics g){
        Dimension tam = getSize();
        ImageIcon ImagenFondo = new ImageIcon(new ImageIcon(getClass().getResource("../Images/Banner.png")).getImage());
        g.drawImage(ImagenFondo.getImage(), 0,0, tam.width, tam.height,null);
        setOpaque(false);
        super.paintComponents(g);
      }
}
