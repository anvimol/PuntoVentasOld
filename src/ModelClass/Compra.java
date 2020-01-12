/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Calendario;
import Clases.FormatDecimal;
import Clases.RenderCelda;
import Connection.Consult;
import Models.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author avice
 */
public class Compra extends Consult {

    //private List<Tempo_compras> tempoComprasFilter;
    private String sql;
    private Object[] object;
    //private FormatDecimal formato = new FormatDecimal();
    private DefaultTableModel modelo1, modelo2;
    private double deuda = 0, enCaja = 0, pago = 0, deudas = 0;
    private boolean deudaProvee, verificar;
    //private List<Reportes_proveedores> proveedorFilter;

    public void guardarTempoCompra(String des, int cant, String precio) {
        double precio1 = formato.reconstruir(precio);
        String precio2 = formato.decimal(precio1) + "€";

        numTempoCompras = tempoCompras().stream()
                .filter(t -> t.getDescripcion().equals(des)
                && t.getPrecioCompra().equals(precio2))
                .collect(Collectors.toList());
        if (0 < numTempoCompras.size()) {
            int cant1;
            String importe1;
            double importe2, importe3;
            importe2 = precio1 * cant;
            importe1 = numTempoCompras.get(0).getImporte().replace("€", "");
            importe3 = formato.reconstruir(importe1);
            importe2 = importe2 + importe3;
            cant1 = cant + numTempoCompras.get(0).getCantidad();
            sql = "UPDATE tempo_compras SET descripcion=?, cantidad=?, precioCompra=?,"
                    + "importe=? WHERE idCompra=" + numTempoCompras.get(0).getIdCompra();
            object = new Object[]{des, cant1, formato.decimal(precio1) + "€", formato.decimal(importe2) + "€"};
            update(sql, object);
        } else {
            double importe;
            importe = precio1 * cant;
            sql = "INSERT INTO tempo_compras(descripcion,cantidad,precioCompra,importe)"
                    + " VALUES(?,?,?,?)";
            object = new Object[]{des, cant, formato.decimal(precio1) + "€",
                formato.decimal(importe) + "€"};
            insert(sql, object);
        }
    }

    public void searchCompras(JTable table, String campo, int num_registro,
            int reg_por_pagina) {
        String[] registros = new String[5];
        String[] titulos = {"ID", "Descripción", "Cantidad", "Precio compra", "Importe"};
        modelo1 = new DefaultTableModel(null, titulos);
        if (campo.isEmpty()) {
            numTempoCompras = tempoCompras().stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            numTempoCompras = tempoCompras().stream()
                    .filter(t -> t.getDescripcion().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        numTempoCompras.forEach(item -> {
            registros[0] = String.valueOf(item.getIdCompra());
            registros[1] = item.getDescripcion();
            registros[2] = String.valueOf(item.getCantidad());
            registros[3] = item.getPrecioCompra();
            registros[4] = item.getImporte();
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

    public List<Tempo_compras> getTempoCompras() {
        return tempoCompras();
    }

    public void importesTempo(JLabel label1, JLabel label2, JLabel label3) {
        deuda = 0;
        numTempoCompras = tempoCompras().stream()
                .collect(Collectors.toList());
        if (!numTempoCompras.isEmpty()) {
            numTempoCompras.forEach(item -> {
                double importe = formato.reconstruir(item.getImporte().replace("€", ""));
                deuda += importe;
            });
            label1.setText(formato.decimal(deuda) + "€");
            label2.setText(formato.decimal(deuda) + "€");
            label3.setText(formato.decimal(deuda) + "€");
        } else {
            label1.setText("0,00€");
            label2.setText("0,00€");
            label3.setText("0,00€");
        }
    }

    public void updateTempoCompra(int id, String des, int cant, String precio) {
        double precio1, importe;
        precio1 = formato.reconstruir(precio);
        importe = precio1 * cant;
        sql = "UPDATE tempo_compras SET descripcion=?, cantidad=?, precioCompra=?,"
                + "importe=? WHERE idCompra=" + id;
        object = new Object[]{des, cant, formato.decimal(precio1) + "€",
            formato.decimal(importe) + "€"};
        update(sql, object);
    }

    public void searchProveedores(JTable table, String campo) {
        String[] registros = new String[6];
        String[] titulos = {"Id", "ID", "Proveedor", "Email", "Telefono", "Saldo"};
        modelo2 = new DefaultTableModel(null, titulos);
        if (campo.isEmpty()) {
            reportes_proveedores = new ArrayList<>();
        } else {
            reportes_proveedores = reportesProveedores().stream()
                    .filter(P -> P.getProveedor().startsWith(campo)
                    || P.getEmail().startsWith(campo)
                    || P.getTelefono().startsWith(campo))
                    .collect(Collectors.toList());
        }
        reportes_proveedores.forEach(item -> {
            registros[0] = String.valueOf(item.getIdProveedor());
            registros[1] = item.getID();
            registros[2] = item.getProveedor();
            registros[3] = item.getEmail();
            registros[4] = item.getTelefono();
            registros[5] = item.getSaldoActual();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public DefaultTableModel getModelo2() {
        return modelo2;
    }

    public void deleteCompras(int idCompra) {
        if (0 < idCompra) {
            sql = "DELETE FROM tempo_compras WHERE idCompra LIKE ?";
            delete(sql, idCompra);
        } else {
            sql = "DELETE FROM tempo_compras";
            delete(sql, 0);
        }
    }

    public void getIngresos(JLabel label) {
        enCaja = 0;
        cajasIngresos = cajasIngresos().stream()
                .filter(c -> c.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajasIngresos.size()) {
            cajasIngresos.forEach(item -> {
                String data = item.getIngreso().replace("€", "");
                double importes = formato.reconstruir(data.trim());
                enCaja += importes;
            });
            label.setText(formato.decimal(enCaja) + "€");
        } else {
            label.setText("0,00€");
        }
    }

    public boolean verificarPago(JTextField textField, JLabel label, JCheckBox checkBox,
            JLabel label1, JLabel label2, JLabel label3, int idProveeCompra) {
        verificar = false;
        deudaProvee = false;
        if (!textField.getText().equalsIgnoreCase("")) {
            if (idProveeCompra != 0) {
                pago = formato.reconstruir(textField.getText());
                if (enCaja == 0) {
                    if (checkBox.isSelected()) {
                        if (pago > deuda) {
                            label.setText("Se a sobrepasado del pago");
                            label.setForeground(Color.RED);
                        } else {
                            label.setText("Se solicito un credito al proveedor");
                            label.setForeground(Color.RED);
                            deudaProvee = true;
                            verificar = true;
                            pagos(pago, idProveeCompra, label1, label2, label3);
                        }
                    } else {
                        if (pago > deuda) {
                            label.setText("Se a sobrepasado del pago");
                            label.setForeground(Color.RED);
                        } else {
                            label.setText("No hay saldo en caja");
                            label.setForeground(Color.RED);
                            pagos(pago, idProveeCompra, label1, label2, label3);
                        }
                    }
                } else {
                    if (pago > enCaja) {
                        if (checkBox.isSelected()) {
                            if (pago > deuda) {
                                label.setText("Se a sobrepasado del pago");
                                label.setForeground(Color.RED);
                            } else {
                                label.setText("Se genera del sistema al proveedor");
                                label.setForeground(Color.RED);
                                deudaProvee = true;
                                verificar = true;
                                pagos(pago, idProveeCompra, label1, label2, label3);
                            }
                        } else {
                            if (pago > deuda) {
                                label.setText("Se a sobrepasado del pago");
                                label.setForeground(Color.RED);
                            } else {
                                label.setText("No hay ingresos suficientes en caja");
                                label.setForeground(Color.RED);
                                pagos(pago, idProveeCompra, label1, label2, label3);
                            }
                        }
                    } else {
                        if (pago == deuda) {
                            if (checkBox.isSelected()) {
                                if (pago > deuda) {
                                    label.setText("Se a sobrepasado del pago");
                                    label.setForeground(Color.RED);
                                } else {
                                    label.setText("Se genera del sistema al proveedor");
                                    label.setForeground(Color.RED);
                                    deudaProvee = true;
                                    verificar = true;
                                    pagos(pago, idProveeCompra, label1, label2, label3);
                                }
                            } else {
                                label.setText("Monto a pagar");
                                label.setForeground(Color.GREEN);
                                //pagos(pago, idProveeCompra, label1, label2, label3);
                                verificar = true;
                            }
                        } else {
                            if (pago > deuda) {
                                label.setText("Se a sobrepasado del pago");
                                label.setForeground(Color.RED);
                            } else {
                                if (checkBox.isSelected()) {
                                    label.setText("Se genera deuda del sistema al proveedor");
                                    label.setForeground(Color.RED);
                                    deudaProvee = true;
                                    verificar = true;
                                    pagos(pago, idProveeCompra, label1, label2, label3);
                                } else {
                                    label.setText("Pago insuficiente");
                                    label.setForeground(Color.RED);
                                    pagos(pago, idProveeCompra, label1, label2, label3);
                                }
                            }
                        }
                    }
                }
            } else {
                label.setText("Seleccione un proveedor");
                label.setForeground(Color.RED);
                textField.setText("");
            }
        }
        return verificar;
    }

    private void pagos(double pago, int idProveedor, JLabel label1,
            JLabel label2, JLabel label3) {
        double saldo;
        if (enCaja == 0) {
            deudas = deuda;
        } else if (deuda > enCaja) {
            deudas = deuda - enCaja;
        } else if (deuda == pago) {
            deudas = deuda - pago;
        } else {
            deudas = deuda - pago;
        }
        String data = formato.decimal(deudas) + "€";
        label1.setText(data);
        label2.setText(data);
        saldo = formato.reconstruir(getReporte(idProveedor).get(0)
                .getSaldoActual().replace("€", ""));
        saldo += deudas;
        label3.setText(formato.decimal(saldo) + "€");
    }

    public List<Reportes_proveedores> getReporte(int idProveedor) {
        reportes_proveedores = reportesProvee(idProveedor).stream()
                .filter(p -> p.getIdProveedor() == idProveedor)
                .collect(Collectors.toList());
        return reportes_proveedores;
    }

    public void saveCompras(String proveedor, int idProveedor, String usuario,
            int idUsuario, String role) {
        int count1, count2, ini, idRegistro;
        sql = "INSERT INTO compras(producto,cantidad,precio,importe,idProveedor,"
                + "proveedor,idUsuario,usuario,role,dia,mes,year,fecha)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        numTempoCompras = tempoCompras().stream()
                .collect(Collectors.toList());
        if (!numTempoCompras.isEmpty()) {
            numTempoCompras.forEach(item -> {
                object = new Object[]{
                    item.getDescripcion(),
                    item.getCantidad(),
                    item.getPrecioCompra(),
                    item.getImporte(),
                    idProveedor,
                    proveedor,
                    idUsuario,
                    usuario,
                    role,
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getAnyo(),
                    new Calendario().getFecha(),};
                insert(sql, object);
            });
            count1 = numTempoCompras.size();
            if (deudaProvee) {
                reportes_proveedores = getReporte(idProveedor);
                idRegistro = reportes_proveedores.get(0).getIdRegistro();
                sql = "UPDATE reportes_proveedores SET proveedor_id=?,saldoActual=?,"
                        + "fechaActual=?,ultimoPago=?,fechaPago=? "
                        + "WHERE idRegistro=" + idRegistro;
                double saldo = formato.reconstruir(reportes_proveedores.get(0)
                        .getSaldoActual().replace("€", ""));
                String ultimoPago = reportes_proveedores.get(0).getUltimoPago();
                String fechaPago = reportes_proveedores.get(0).getFechaPago();
                saldo += deudas;
                String dataSaldo = formato.decimal(saldo);
                Object[] reporte = new Object[] {
                    idProveedor,
                    dataSaldo,
                    new Calendario().getFecha(),
                    ultimoPago,
                    fechaPago
                };
                update(sql, reporte);
            }
            count2 = compras().size();
            if (count1 == count2) {
                ini = 0;
            } else {
                ini = count2 - count1;
            }
            for (int i = ini; i < count2; i++) {
                sql = "INSERT INTO tempo_productos(idCompra) VALUES(?)";
                object = new Object[] {compras().get(i).getIdCompra()};
                insert(sql, object);
            }
        }
    }
}
