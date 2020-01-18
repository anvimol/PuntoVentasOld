/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author avice
 */
public class RenderCelda extends DefaultTableCellRenderer {
    
    private int colum;
    
    public RenderCelda(int colum) {
        this.colum = colum;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        switch (colum) {
            case 0:
                if (column == 5 || column == 3) {
                    cell.setForeground(new Color(0, 153, 102));
                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
            case 1:
                if (column == 4 || column == 6) {
                    cell.setForeground(new Color(0, 153, 102));
                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
            case 4:
                if (column == colum || column == 3) {
                    cell.setForeground(new Color(0, 153, 102));
                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
        }
        return cell;
    }
    
}
