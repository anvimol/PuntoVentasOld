/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Paginador;
import Clases.RenderCelda;
import Connection.Consult;
import Models.Productos;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author avice
 */
public class Inventario extends Consult {

    private static int idRegistro, existencia, pageSize = 2;
    private String sql;
    private Object[] object;
    private List<JLabel> labelBodega;
    private List<JTextField> textFieldBodega;
    private static DefaultTableModel modelo1, modelo2;
    private JTable table, table2;
    private JSpinner spinnerBodega;
    private JCheckBox checkBoxBodega;
    private SpinnerNumberModel model;
    private JTabbedPane tabbedPane;

    public Inventario(Object[] objectBodega, List<JLabel> labelBodega, List<JTextField> textFieldBodega) {
        this.labelBodega = labelBodega;
        this.textFieldBodega = textFieldBodega;
        table = (JTable) objectBodega[0];
        spinnerBodega = (JSpinner) objectBodega[1];
        checkBoxBodega = (JCheckBox) objectBodega[2];
        tabbedPane = (JTabbedPane) objectBodega[3];
    }

    public Inventario() {
    }

    public void getBodegas(String campo, int num_registro, int reg_por_pagina) {
        String[] titulos = {"Id", "Código", "Producto", "Existencia", "Fecha"};
        modelo1 = new DefaultTableModel(null, titulos);
        Number existencia1 = (Number) spinnerBodega.getValue();

        if (0 < getBodega().size()) {
            if (campo.isEmpty()) {
                if (checkBoxBodega.isSelected()) {
                    productos = getBodega().stream()
                            .filter(b -> b.getExistencia() <= existencia1.intValue())
                            .skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());
                } else {
                    productos = getBodega().stream()
                            .skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());;
                }
            } else {
                if (checkBoxBodega.isSelected()) {
                    productos = getBodega().stream()
                            .filter(b -> b.getExistencia() <= existencia1.intValue()
                            && b.getCodigo().startsWith(campo) || b.getExistencia() <= existencia1.intValue()
                            && b.getProducto().startsWith(campo))
                            .skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());
                } else {
                    productos = getBodega().stream()
                            .filter(b -> b.getCodigo().startsWith(campo)
                            && b.getProducto().startsWith(campo))
                            .skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());
                }
            }
        }
        productos.forEach(item -> {
            String[] registros = {
                String.valueOf(item.getId()),
                item.getCodigo(),
                item.getProducto(),
                String.valueOf(item.getExistencia()),
                item.getFecha()
            };
            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(9, existencia1.intValue()));
    }

    public void restablecerBodega() {
        idRegistro = 0;
        existencia = 0;
        switch (tabbedPane.getSelectedIndex()) {
            case 0:
                getBodegas("", 0, pageSize);
                model = new SpinnerNumberModel(
                        new Integer(1), // Dato visualizado al inicio en el spinner 
                        new Integer(1), // Li­mite inferior 
                        new Integer(100), // Li­mite superior 
                        new Integer(1) // incremento-decremento 
                );
                spinnerBodega.setModel(model);
                textFieldBodega.get(0).setText("");
                new Paginador(9, table, labelBodega.get(1), 1).primero();
                break;

        }

    }

    public void dataTableBodega() {
        int fila = table.getSelectedRow();
        idRegistro = Integer.valueOf((String) modelo1.getValueAt(fila, 0));
        existencia = Integer.valueOf((String) modelo1.getValueAt(fila, 3));
        textFieldBodega.get(0).setText(String.valueOf(existencia));
        labelBodega.get(0).setText("Existencia");
        labelBodega.get(0).setForeground(new Color(0, 153, 51));
    }

    public void updateExistencia() {
        if (textFieldBodega.get(0).getText().isEmpty()) {
            labelBodega.get(0).setText("Ingrese el dato en el campo");
            labelBodega.get(0).setForeground(Color.RED);
        } else {
            sql = "UPDATE bodegas SET existencia=? WHERE id=" + idRegistro;
            object = new Object[]{textFieldBodega.get(0).getText()};
            update(sql, object);
        }
    }
    
    public List<Productos> getInventBodega() {
        return getBodega();
    }
}
