/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Paginador;
import Clases.RenderCelda;
import Connection.Consult;
import Models.Bodegas;
import Models.Productos;
import Models.Ventas;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    private static DefaultTableModel modelo1, modelo2, modelo3;
    private JTable table, table2, table3;
    private JSpinner spinnerBodega;
    private JCheckBox checkBoxBodega, checkBoxMasVendidos;
    private SpinnerNumberModel model;
    private JTabbedPane tabbedPane;
    private DateChooserCombo dateChooserInicio, dateChooserFinal;
    private final SimpleDateFormat formateador;

    public Inventario(Object[] objectBodega, List<JLabel> labelBodega, List<JTextField> textFieldBodega) {
        this.formateador = new SimpleDateFormat("dd/MM/yyyy");
        
        this.labelBodega = labelBodega;
        this.textFieldBodega = textFieldBodega;
        table = (JTable) objectBodega[0];
        spinnerBodega = (JSpinner) objectBodega[1];
        checkBoxBodega = (JCheckBox) objectBodega[2];
        tabbedPane = (JTabbedPane) objectBodega[3];
        table2 = (JTable) objectBodega[4];
        dateChooserInicio = (DateChooserCombo) objectBodega[5];
        dateChooserFinal = (DateChooserCombo) objectBodega[6];
        checkBoxMasVendidos = (JCheckBox) objectBodega[7];
        table3 = (JTable) objectBodega[8];
    }

    public Inventario() {
        this.formateador = new SimpleDateFormat("dd/MM/yyyy");
    }

    // BODEGA
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
            case 1:
                textFieldBodega.get(1).setText("");
                textFieldBodega.get(2).setText("");
                labelBodega.get(2).setText("Lista de Productos");
                labelBodega.get(2).setForeground(new Color(70, 106, 124));
                labelBodega.get(4).setForeground(new Color(70, 106, 124));
                labelBodega.get(5).setForeground(new Color(70, 106, 124));
                getProductos("", 0, pageSize);
                new Paginador(10, table2, labelBodega.get(3), 1).primero();
                break;
            case 2:
                Calendar c = new GregorianCalendar();
                dateChooserInicio.setSelectedDate(c);
                dateChooserFinal.setSelectedDate(c);
                searchVentas("", 0, pageSize);
                new Paginador(11, table3, labelBodega.get(6), 1).primero();
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

    // PRODUCTOS
    public void getProductos(String campo, int num_registro, int reg_por_pagina) {
        String[] titulos = {"Id", "Código", "Producto", "Precio", "Descuento",
            "Departamento", "Categoria"};
        modelo2 = new DefaultTableModel(null, titulos);

        if (0 < productos().size()) {
            if (campo.isEmpty()) {
                productos = productos().stream()
                        .skip(num_registro).limit(reg_por_pagina)
                        .collect(Collectors.toList());
            } else {
                productos = productos().stream()
                        .filter(p -> p.getCodigo().startsWith(campo)
                        || p.getProducto().startsWith(campo))
                        .skip(num_registro).limit(reg_por_pagina)
                        .collect(Collectors.toList());
            }
            productos.forEach(item -> {
                String[] registros = {
                    String.valueOf(item.getIdProducto()),
                    item.getCodigo(),
                    item.getProducto(),
                    item.getPrecio(),
                    item.getDescuento(),
                    item.getDepartamento(),
                    item.getCategoria()
                };
                modelo2.addRow(registros);
            });
            table2.setModel(modelo2);
            table2.setRowHeight(30);
            table2.getColumnModel().getColumn(0).setMaxWidth(0);
            table2.getColumnModel().getColumn(0).setMinWidth(0);
            table2.getColumnModel().getColumn(0).setPreferredWidth(0);
            table2.setDefaultRenderer(Object.class, new RenderCelda(10, 0));
        }
    }

    public void dateTableProductos() {
        int fila = table2.getSelectedRow();
        idRegistro = Integer.valueOf((String) modelo2.getValueAt(fila, 0));
    }

    public void updateProductos() {
        String precio, descuento;
        if (idRegistro != 0) {
            productos = productos().stream()
                    .filter(p -> p.getIdProducto() == idRegistro)
                    .collect(Collectors.toList());
            if (0 < productos.size()) {
                List<Bodegas> bodega = bodegas().stream()
                        .filter(b -> b.getCodigo().equals(productos.get(0).getCodigo()))
                        .collect(Collectors.toList());
                if (bodega.isEmpty()) {
                    labelBodega.get(2).setText("Producto no disponible");
                    labelBodega.get(2).setForeground(Color.RED);
                } else {
                    if (0 == bodega.get(0).getExistencia()) {
                        labelBodega.get(2).setText("Producto no disponible");
                        labelBodega.get(2).setForeground(Color.RED);
                    } else {
                        if (textFieldBodega.get(1).getText().equals("")) {
                            precio = productos.get(0).getPrecio();
                        } else {
                            double data = formato.reconstruir(textFieldBodega.get(1).getText());
                            precio = formato.decimal(data) + "€";
                        }
                        if (textFieldBodega.get(2).getText().equals("")) {
                            descuento = productos.get(0).getDescuento();
                        } else {
                            double data = formato.reconstruir(textFieldBodega.get(2).getText());
                            descuento = formato.decimal(data) + "%";
                        }
                        sql = "UPDATE productos SET precio=?, descuento=? "
                                + "WHERE idProducto=" + idRegistro;
                        object = new Object[]{
                            precio,
                            descuento
                        };
                        update(sql, object);
                        restablecerBodega();
                    }
                }
            }
        } else {
            labelBodega.get(2).setText("Seleccione un producto");
            labelBodega.get(2).setForeground(Color.RED);
        }
    }

    // VENTAS
    public int searchVentas(String campo, int num_registro, int reg_por_pagina) {
        String[] titulos = {"Id", "Código", "Descripción", "Precio", "Cantidad",
            "Importe", "Dia", "Mes", "Año", "Fecha", "Caja"};
        modelo3 = new DefaultTableModel(null, titulos);
        List<Ventas> query = new ArrayList<>();
        int valor = 0;
        try {
            String fechaInicio = dateChooserInicio.getSelectedPeriodSet().toString(); // dateChooserInicio.getSelectedPeriodSet().toString();
            
            Date fechaDate1 = formateador.parse(fechaInicio);
            Date fechaDate2 = formateador.parse(dateChooserFinal.getSelectedPeriodSet().toString());
            if (campo.isEmpty()) {
                if (checkBoxMasVendidos.isSelected()) {
                    if (fechaDate2.before(fechaDate1)) { // before = menor
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser"
                                + " mayor a la fecha de incio", "Error",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        query = maxVentas(filtrarProductosFechas(fechaInicio));
                        valor = query.size();
                    }
                } else {
                    if (fechaDate2.before(fechaDate1)) { // before = menor
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser"
                                + " mayor a la fecha de incio", "Error",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        query = filtrarProductosFechas(fechaInicio);
                        valor = query.size();
                    }
                }
            } else {
                if (checkBoxMasVendidos.isSelected()) {
                    if (fechaDate2.before(fechaDate1)) { // before = menor
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser"
                                + " mayor a la fecha de incio", "Error",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        query = maxVentas(filtrarProductosFechas(fechaInicio)).stream()
                                .filter(p -> p.getCodigo().startsWith(campo)
                                || p.getDescripcion().startsWith(campo))
                                .skip(num_registro).limit(reg_por_pagina)
                                .collect(Collectors.toList());
                        valor = query.size();
                    }
                } else {
                    if (fechaDate2.before(fechaDate1)) { // before = menor
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser"
                                + " mayor a la fecha de incio", "Error",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        query = filtrarProductosFechas(fechaInicio).stream()
                                .filter(p -> p.getCodigo().startsWith(campo)
                                || p.getDescripcion().startsWith(campo))
                                .skip(num_registro).limit(reg_por_pagina)
                                .collect(Collectors.toList());
                        valor = query.size();
                    }
                }
            }
        } catch (Exception e) {
        }
        query.forEach(item -> {
            Object[] registros = {
                item.getIdVenta(),
                item.getCodigo(),
                item.getDescripcion(),
                item.getPrecio(),
                item.getCantidad(),
                item.getImporte(),
                item.getDia(),
                item.getMes(),
                item.getYear(),
                item.getFecha()
            };
            modelo3.addRow(registros);
        });
        table3.setModel(modelo3);
        table3.setRowHeight(30);
        table3.getColumnModel().getColumn(0).setMaxWidth(0);
        table3.getColumnModel().getColumn(0).setMinWidth(0);
        table3.getColumnModel().getColumn(0).setPreferredWidth(0);
        return valor;
    }

    private List<Ventas> maxVentas(List<Ventas> query) {
        List<Ventas> listProduct = new ArrayList<>();
        for (Ventas item : query) {
            if (verificar(item, listProduct)) {
                listProduct.add(item);
            }
        }
        // ORDENAR DE MENOR A MAYOR
        listProduct.sort((v1, v2) -> Integer.valueOf(v1.getCantidad()).compareTo(v2.getCantidad()));
        // MAYOR A MENOR
        Collections.reverse(listProduct);
        return listProduct;
    }

    private boolean verificar(Ventas data, List<Ventas> listProduct) {
        int pos = 0, cant;
        double importe1, importe2, importe3;
        for (Ventas item : listProduct) {
            if (item.getCodigo().equals(data.getCodigo())) {
                importe1 = formato.reconstruir(item.getImporte().replace("€", ""));
                importe2 = formato.reconstruir(data.getImporte().replace("€", ""));
                importe3 = importe1 + importe2;
                String importes = formato.decimal(importe3) + "€";
                cant = item.getCantidad() + data.getCantidad();
                listProduct.remove(pos);
                item.setCantidad(cant);
                item.setImporte(importes);
                listProduct.add(pos, item);
                return false;
            }
            pos++;
            cant = 0;
        }
        return true;
    }

    private List<Ventas> filtrarProductosFechas(String fechaInicio) {
        List<Ventas> listProduct = new ArrayList<>();
        try {
            Ventas venta = new Ventas();
            Date fechaDate1 = formateador.parse(dateChooserInicio.getSelectedPeriodSet().toString());
            Date fechaDate2 = formateador.parse(dateChooserFinal.getSelectedPeriodSet().toString());
            List<Ventas> listdb1 = ventas().stream()
                    .filter(b -> b.getFecha().equals(fechaInicio))
                    .collect(Collectors.toList());
            if (0 < listdb1.size()) {
                List<Ventas> listdb2 = ventas().stream()
                        .filter(b -> b.getIdVenta() >= listdb1.get(0).getIdVenta())
                        .collect(Collectors.toList());
                for (Ventas item : listdb2) {
                    Date fechaDate3 = formateador.parse(item.getFecha());
                    if (fechaDate3.before(fechaDate2)
                            || 0 == fechaDate2.compareTo(fechaDate1)) { // compareTo devuelve 0 se las fechas son iguales
                        venta.setIdVenta(item.getIdVenta());
                        venta.setCodigo(item.getCodigo());
                        venta.setDescripcion(item.getDescripcion());
                        venta.setPrecio(item.getPrecio());
                        venta.setCantidad(item.getCantidad());
                        venta.setImporte(item.getImporte());
                        venta.setDia(item.getDia());
                        venta.setMes(item.getMes());
                        venta.setYear(item.getYear());
                        venta.setFecha(item.getFecha());
                        venta.setCaja(item.getCaja());
                        venta.setIdUsuario(item.getIdUsuario());
                        listProduct.add(venta);
                        venta = new Ventas();
                    } else {
                        break;
                    }
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listProduct;
    }
}
