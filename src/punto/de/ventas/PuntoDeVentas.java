/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punto.de.ventas;

import View.Login;
import View.Sistema;
import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.UIManager;

/**
 *
 * @author AlexJPZ
 */
public class PuntoDeVentas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        new Login().setVisible(true);
        //maximizar la ventana
//        Sistema main = new Sistema();
//        main.setExtendedState(MAXIMIZED_BOTH);
//        main.setVisible(true);
    }

}
