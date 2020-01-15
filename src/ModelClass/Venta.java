/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.RenderCelda;
import Connection.Consult;
import Models.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author avice
 */
public class Venta extends Consult {

    private String sql;
    private Object[] object;
    private DefaultTableModel modelo1, modelo2;
    private double importe = 0, totalPagar = 0;

    public List<Bodegas> searchBodega(String codigo) {
        return bodegas().stream()
                .filter(b -> b.getCodigo().equals(codigo))
                .collect(Collectors.toList());
    }

    public void saveVentasTempo(String codigo, int funcion, int caja, int idUsuario) {
        String importe, precios;
        int idTempo, cantidad = 1, existencia;
        double descuento, precio, importes;

        tempoVentas = tempoVentas().stream()
                .filter(t -> t.getCodigo().equals(codigo) && t.getCaja() == caja
                && t.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
        productos = productos().stream()
                .filter(t -> t.getCodigo().equals(codigo))
                .collect(Collectors.toList());
        descuento = formato.reconstruir(productos.get(0).getDescuento().replace("%", ""));
        precio = formato.reconstruir(productos.get(0).getPrecio().replace("€", ""));
        descuento = descuento / 100;
        descuento = precio * descuento;
        precio = precio - descuento;
        precios = formato.decimal(precio) + "€";
        if (0 < tempoVentas.size()) {
            cantidad = tempoVentas.get(0).getCantidad();
            if (funcion == 0) {
                cantidad++;
            } else {
                cantidad--;
            }
            importes = precio * cantidad;
            importe = formato.decimal(importes) + "€";
            sql = "UPDATE tempo_ventas SET codigo=?, descripcion=?, precio=?, cantidad=?,"
                    + "importe=?, caja=?, idUsuario=? "
                    + "WHERE idTempo=" + tempoVentas.get(0).getIdTempo();
            object = new Object[]{
                productos.get(0).getCodigo(),
                productos.get(0).getProducto(),
                precios,
                cantidad,
                importe,
                caja,
                idUsuario
            };
            update(sql, object);
        } else {
            sql = "INSERT INTO tempo_ventas(codigo, descripcion, precio, cantidad,"
                    + "importe, caja, idUsuario) VALUES(?,?,?,?,?,?,?)";
            object = new Object[]{
                productos.get(0).getCodigo(),
                productos.get(0).getProducto(),
                precios,
                1,
                precios,
                caja,
                idUsuario
            };
            insert(sql, object);
        }
        bodega = bodegas().stream()
                .filter(b -> b.getCodigo().equals(codigo))
                .collect(Collectors.toList());
        existencia = bodega.get(0).getExistencia();

        existencia--;
        sql = "UPDATE bodegas SET producto_id=?, codigo=?, existencia=?, dia=?, "
                + "mes=?, year=?, fecha=? WHERE id=" + bodega.get(0).getId();
        object = new Object[]{
            bodega.get(0).getProducto_id(),
            bodega.get(0).getCodigo(),
            existencia,
            bodega.get(0).getDia(),
            bodega.get(0).getMes(),
            bodega.get(0).getYear(),
            bodega.get(0).getFecha()
        };
        update(sql, object);
    }

    public void searchVentaTempo(JTable table, int num_registro, int reg_por_pagina,
            int cajas, int idUsuario) {
        String[] registros = new String[6];
        String[] titulos = {"ID", "Código", "Descripción", "Precio", "Cantidad", "Importe"};
        modelo1 = new DefaultTableModel(null, titulos);

        tempoVentas = tempoVentas().stream()
                .filter(t -> t.getCaja() == cajas && t.getIdUsuario() == idUsuario)
                .skip(num_registro).limit(reg_por_pagina)
                .collect(Collectors.toList());
        if (0 < tempoVentas.size()) {
            tempoVentas.forEach(item -> {
                registros[0] = String.valueOf(item.getIdTempo());
                registros[1] = item.getCodigo();
                registros[2] = item.getDescripcion();
                registros[3] = item.getPrecio();
                registros[4] = String.valueOf(item.getCantidad());
                registros[5] = item.getImporte();
                modelo1.addRow(registros);
            });
        }
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(0));

    }

    public DefaultTableModel getModelo() {
        return modelo1;
    }

    public void importes(JLabel label, int caja, int idUsuario) {
        importe = 0;
        tempoVentas = tempoVentas().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
        if (0 < tempoVentas.size()) {
            tempoVentas.forEach(item -> {
                importe += formato.reconstruir(item.getImporte().replace("€", ""));
            });
            label.setText(formato.decimal(importe) + "€");
        } else {
            label.setText("0,00€");
        }
    }

    public void deleteVentaTempo(String codigo, int cant, int caja, int idUsuario) {
        int cantidad = 0, existencia = 0;
        tempoVentas = tempoVentas().stream()
                .filter(t -> t.getCodigo().equals(codigo) && t.getCaja() == caja
                && t.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
        if (tempoVentas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos en la lista de ventas");
        } else {
            cantidad = tempoVentas.get(0).getCantidad();
            bodega = bodegas().stream()
                    .filter(b -> b.getCodigo().equals(codigo))
                    .collect(Collectors.toList());
            if (bodega.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay productos en Bodega");
            } else {
                existencia = bodega.get(0).getExistencia();
                if (cant == 1) {
                    existencia += cantidad;
                    sql = "DELETE FROM tempo_ventas WHERE idTempo LIKE ?";
                    delete(sql, tempoVentas.get(caja).getIdTempo());
                } else {
                    existencia++;
                    saveVentasTempo(codigo, 1, caja, idUsuario);
                }
                sql = "UPDATE bodegas SET producto_id=?, codigo=?, existencia=?, dia=?,"
                        + "mes=?, year=?, fecha=? WHERE id=" + bodega.get(0).getId();
                object = new Object[]{
                    bodega.get(0).getProducto_id(),
                    bodega.get(0).getCodigo(),
                    existencia,
                    bodega.get(0).getDia(),
                    bodega.get(0).getMes(),
                    bodega.get(0).getYear(),
                    bodega.get(0).getFecha()
                };
                update(sql, object);
            }
        }
    }
}
