/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.PaintLabel;
import Clases.RenderCelda;
import Connection.Consult;
import Models.Categorias;
import Models.Departamentos;
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

    public void getProductos(JTable table) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "Producto", "Cantidad", "Precio", "Importe",
            "Proveedor", "Fecha"};
        modelo1 = new DefaultTableModel(null, titulos);

        tempo_productos = tempoProductos().stream()
                .collect(Collectors.toList());
        tempo_productos.forEach(item -> {
            registros[0] = String.valueOf(item.getId());
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
        table.setDefaultRenderer(Object.class, new RenderCelda(4));
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
            productos = productos().stream()
                    .filter(p -> p.getProducto().equals(producto)
                    && p.getPrecio().equals(formato.decimal(Double.valueOf(precios)) + "€"))
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

    public void getDepartamento(JComboBox combo1, JComboBox combo2, int idDptos) {
        modelCombo = new DefaultComboBoxModel();
        departamentos = departamentos();
        if (0 < departamentos.size()) {
            departamentos.forEach(item -> {
                modelCombo.addElement(item);
            });
            combo1.setModel(modelCombo);
            modelCombo = new DefaultComboBoxModel();
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
                });
                combo2.setModel(modelCombo);
            }
        }
    }
    
    public void saveProducto(String producto, int cantidad, String precio,
            String departamento, String categoria, String accion, int idProducto) {
        int count = 0, cant;
        switch (accion) {
            case "insert":
                double precio1 = formato.reconstruir(precio);
                productos = productos().stream()
                    .filter(p -> p.getProducto().equals(producto)
                    && p.getPrecio().equals(formato.decimal(precio1) + "€"))
                    .collect(Collectors.toList());
                if (productos.isEmpty()) {
                    sql = "INSERT INTO productos(codigo,producto,precio,descuento,"
                            + "departamento,categoria) VALUES(?,?,?,?,?,?)";
                    object = new Object[] {
                        codeBarra,
                        producto,
                        formato.decimal(precio1) + "€",
                        "0,00%",
                        departamento,
                        categoria
                    };
                }
                break;
        }
    }
}
