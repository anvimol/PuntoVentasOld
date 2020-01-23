/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Calendario;
import Clases.PaintLabel;
import Clases.RenderCelda;
import Connection.Consult;
import Models.Categorias;
import Models.Departamentos;
import Models.Productos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.jbarcodebean.model.Interleaved25;

/**
 *
 * @author avice
 */
public class Producto extends Consult {

    private DefaultTableModel modelo1, modelo2;
    //private List<Clientes> cliente, clienteFilter;
    private int idDpto;
    private String sql, precios, codeBarra;
    private Object[] object;
    private DefaultComboBoxModel modelCombo;
    private Departamentos dpt = null;
    private Categorias cat = null;

    public void getProductos(JTable table, String campo) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "Producto", "Cantidad", "Precio", "Importe",
            "Proveedor", "Fecha"};
        modelo1 = new DefaultTableModel(null, titulos);
        if (campo.isEmpty()) {
            tempo_productos = tempoProductos().stream()
                    .collect(Collectors.toList());
        } else {
            tempo_productos = tempoProductos().stream()
                    .filter(p -> p.getProducto().startsWith(campo))
                    .collect(Collectors.toList());
        }

        tempo_productos.forEach(item -> {
            registros[0] = String.valueOf(item.getCompra_id());
            registros[1] = item.getProducto();
            registros[2] = String.valueOf(item.getCantidad());
            registros[3] = item.getPrecio();
            registros[4] = item.getImporte();
            registros[5] = item.getProveedor();
            registros[6] = item.getFecha();
            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(4,0));
    }

    public DefaultTableModel getModelo() {
        return modelo1;
    }

    public void codeBarra(JLabel label, String codigoBarra, String producto, String precio) {
        int codigo = 0, count = 0;
        if (codigoBarra.equals("0")) {
            if (precio.equals("")) {
                precios = "0,00";
            } else {
                precios = precio;
            }
            double precio1 = formato.reconstruir(precios);
            productos = productos().stream()
                    .filter(p -> p.getProducto().equals(producto)
                    && p.getPrecio().equals(formato.decimal(precio1) + "€"))
                    .collect(Collectors.toList());
            if (0 < productos.size()) {
                codeBarra = productos.get(0).getCodigo();
                codigo(label);
            } else {
                do {
                    codigo = ThreadLocalRandom.current().nextInt(100000, 1000000000 + 1);
                    codeBarra = String.valueOf(codigo);
                    productos = productos().stream()
                            .filter(p -> p.getCodigo().equals(codeBarra))
                            .collect(Collectors.toList());
                    count = productos.size();
                } while (count > 0);
                codigo(label);
            }
        } else {
            codeBarra = codigoBarra;
            codigo(label);
        }
    }

    private void codigo(JLabel label) {
        // muestro que tipo de código de barra
        barcode.setCodeType(new Interleaved25());
        barcode.setCode(codeBarra);
        barcode.setCheckDigit(true);
        BufferedImage bufferedImage = barcode.draw(new BufferedImage(250, 110,//160, 80, 
                BufferedImage.TYPE_INT_RGB));
        PaintLabel p = new PaintLabel(bufferedImage);
        label.removeAll();
        label.add(p);
        label.repaint();
    }

    public boolean verificarPrecioVenta(JLabel label, String precioVenta,
            String precioCompra, int funcion) {
        double venta, compra;
        boolean verificar = false;

        if (funcion == 1) {
            if (!precioVenta.isEmpty() && !precioCompra.isEmpty()) {
                compra = formato.reconstruir(precioCompra.replace("€", ""));
                venta = formato.reconstruir(precioVenta);
                if (compra > venta || compra == venta) {
                    label.setText("Ingrese un precio mayor al de compra");
                    label.setForeground(Color.RED);
                    verificar = false;
                } else {
                    label.setText("Precio venta");
                    label.setForeground(new Color(0, 153, 51));
                    verificar = true;
                }
            }
        } else {
            label.setText("Precio venta");
            label.setForeground(new Color(0, 153, 51));
            verificar = true;
        }
        return verificar;
    }

    public Departamentos getDepartamento(JComboBox combo, String departa) {
        modelCombo = new DefaultComboBoxModel();
        departamentos = departamentos();
        if (0 < departamentos.size()) {
            departamentos.forEach(item -> {
                modelCombo.addElement(item);
                if (departa.equals(item.getDepartamento())) {
                    dpt = item;
                }
            });
            combo.setModel(modelCombo);
            modelCombo = new DefaultComboBoxModel();
        }
        return dpt;
    }

    public Categorias getCategorias(JComboBox combo1, JComboBox combo2,
            int idDptos, String categori) {
        if (idDptos == 0) {
            Departamentos dpt = (Departamentos) combo1.getSelectedItem();
            idDpto = dpt.getIdDpto();
        } else {
            idDpto = idDptos;
        }
        categorias = categorias();
        if (0 < categorias.size()) {
            List<Categorias> categoria = categorias().stream()
                    .filter(c -> c.getDpto_id() == idDpto)
                    .collect(Collectors.toList());
            categoria.forEach(item -> {
                modelCombo.addElement(item);
                if (categori.equals(item.getCategoria())) {
                    cat = item;
                }
            });
            combo2.setModel(modelCombo);
        }
        return cat;
    }

    public void saveProducto(String producto, int cantidad, String precio,
            String departamento, String categoria, String accion, int idProducto) {
        int count = 0, cant;
        double precio1 = formato.reconstruir(precio);
        switch (accion) {
            case "insert":
                productos = productos().stream()
                        .filter(p -> p.getProducto().equals(producto)
                        && p.getPrecio().equals(formato.decimal(precio1) + "€"))
                        .collect(Collectors.toList());
                if (productos.isEmpty()) {
                    sql = "INSERT INTO productos(codigo,producto,precio,descuento,"
                            + "departamento,categoria) VALUES(?,?,?,?,?,?)";
                    object = new Object[]{
                        codeBarra,
                        producto,
                        formato.decimal(precio1) + "€",
                        "0,00%",
                        departamento,
                        categoria
                    };
                    insert(sql, object);
                }
                bodega = bodegas().stream()
                        .filter(b -> b.getCodigo().equals(codeBarra))
                        .collect(Collectors.toList());
                if (0 < bodega.size()) {
                    cant = cantidad + bodega.get(0).getExistencia();
                    sql = "UPDATE bodegas SET producto_id=?,codigo=?,existencia=?,"
                            + "dia=?,mes=?,year=?,fecha=? WHERE id=" + bodega.get(0).getId();
                    object = new Object[]{
                        bodega.get(0).getProducto_id(),
                        bodega.get(0).getCodigo(),
                        cant,
                        new Calendario().getDia(),
                        new Calendario().getMes(),
                        new Calendario().getAnyo(),
                        new Calendario().getFecha()
                    };
                    update(sql, object);
                } else {
                    productos = productos();
                    count = productos.size();
                    if (0 < count) {
                        count--;
                        sql = "INSERT INTO bodegas(producto_id,codigo,existencia,dia,mes,"
                                + "year,fecha) VALUES(?,?,?,?,?,?,?)";
                        object = new Object[]{
                            productos.get(count).getIdProducto(),
                            codeBarra,
                            cantidad,
                            new Calendario().getDia(),
                            new Calendario().getMes(),
                            new Calendario().getAnyo(),
                            new Calendario().getFecha()
                        };
                        insert(sql, object);
                    }
                }
                deleteProducto(idProducto);
                break;

            case "update":
                productos = productos().stream()
                        .filter(p -> p.getIdProducto() == idProducto)
                        .collect(Collectors.toList());
                sql = "UPDATE productos SET codigo=?, producto=?, precio=?,"
                        + "descuento=?, departamento=?, categoria=? "
                        + "WHERE idProducto=" + idProducto;
                object = new Object[]{
                    codeBarra,
                    producto,
                    formato.decimal(precio1) + "€",
                    productos.get(0).getDescuento(),
                    departamento,
                    categoria
                };
                update(sql, object);
                break;
        }
    }

    public void searchProductos(JTable table, String campo, int num_registro,
            int reg_por_pagina) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "Código", "Producto", "Precio", "Descuento",
            "Departamento", "Categoria"};
        modelo2 = new DefaultTableModel(null, titulos);
        if (campo.isEmpty()) {
            productos = productos().stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            productos = productos().stream()
                    .filter(p -> p.getProducto().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        productos.forEach(item -> {
            registros[0] = String.valueOf(item.getIdProducto());
            registros[1] = item.getCodigo();
            registros[2] = item.getProducto();
            registros[3] = item.getPrecio();
            registros[4] = item.getDescuento();
            registros[5] = item.getDepartamento();
            registros[6] = item.getCategoria();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(4,0));
    }

    public DefaultTableModel getModelo2() {
        return modelo2;
    }
    
    public List<Productos> producto() {
        return productos();
    }
    
    public void deleteProducto(int idProd) {
        sql = "DELETE FROM tempo_productos WHERE compra_id LIKE ?";
        delete(sql, idProd);
    }
}
