/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Calendario;
import Clases.RenderCelda;
import Connection.Consult;
import Models.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author avice
 */
public class Venta extends Consult {

    private String sql;
    private Object[] object;
    private DefaultTableModel modelo1, modelo2;
    private double importe = 0, totalPagar = 0, ingresosTotales = 0;
    private boolean suCambio = false, verificar = false;
    private int caja, idUsuario;

    public void start(int caja, int idUsuario) {
        this.caja = caja;
        this.idUsuario = idUsuario;
    }

    public List<Bodegas> searchBodega(String codigo) {
        return bodegas().stream()
                .filter(b -> b.getCodigo().equals(codigo))
                .collect(Collectors.toList());
    }

    public void saveVentasTempo(String codigo, int funcion) {
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
            sql = "UPDATE tempo_ventas SET precio=?, cantidad=?,"
                    + "importe=?, caja=?, idUsuario=? "
                    + "WHERE idTempo=" + tempoVentas.get(0).getIdTempo();
            object = new Object[]{
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

        if (existencia > 0) {
            existencia--;
            sql = "UPDATE bodegas SET existencia=? "
                    + "WHERE id=" + bodega.get(0).getId();
            object = new Object[]{
                existencia
            };
            update(sql, object);
        }

    }

    public void searchVentaTempo(JTable table, int num_registro, int reg_por_pagina) {
        String[] registros = new String[6];
        String[] titulos = {"ID", "Código", "Descripción", "Precio", "Cantidad", "Importe"};
        modelo1 = new DefaultTableModel(null, titulos);

        tempoVentas = tempoVentas().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario)
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
                    saveVentasTempo(codigo, 1);
                }
                sql = "UPDATE bodegas SET existencia=? WHERE id=" + bodega.get(0).getId();
                object = new Object[]{
                    existencia
                };
                update(sql, object);
            }
        }
    }

    public void pagosCliente(JTextField textField, JLabel label1, JLabel label2,
            JLabel label3, JCheckBox checkBox) {
        double pago, pagar;
        if (textField.getText().isEmpty()) {
            label1.setText("Su cambio");
            label1.setForeground(new Color(0, 153, 51));
            label2.setText("0,00€");
        } else {
            pagar = importe;
            pago = formato.reconstruir(textField.getText());
            if (pago >= pagar) {
                totalPagar = pago - pagar;
                if (totalPagar > ingresosTotales) {
                    label1.setText("No hay ingresos en caja");
                    label1.setForeground(Color.RED);
                    verificar = false;
                    suCambio = false;
                } else {
                    if (checkBox.isSelected()) {
                        label1.setText("Desmarque la opción crédito");
                        label1.setForeground(Color.RED);
                        verificar = false;
                        suCambio = false;
                    } else {
                        label1.setText("Su cambio");
                        label1.setForeground(new Color(0, 153, 51));
                        totalPagar = pago - pagar;
                        verificar = true;
                        suCambio = true;
                    }
                }
            }
            if (pago < pagar) {
                label1.setText("Pago insuficiente");
                label1.setForeground(Color.RED);
                totalPagar = pagar - pago;
                if (checkBox.isSelected()) {
                    verificar = true;
                } else {
                    verificar = false;
                }
                suCambio = false;
            }
            label2.setText(formato.decimal(totalPagar) + "€");
        }
        label3.setText("Pagó con");
        label3.setForeground(new Color(0, 153, 51));
    }

    public void reportesClientes(JTable table, String campo) {
        String[] registros = new String[8];
        String[] titulos = {"Id", "ID", "Nombre", "Apellidos", "Saldo actual",
            "Fecha actual", "Ultimo pago", "Fecha pago"};
        modelo2 = new DefaultTableModel(null, titulos);
        if (campo.isEmpty()) {
            reportes_clientes = new ArrayList<>();
        } else {
            reportes_clientes = reportesClientes(0, 2).stream()
                    .filter(t -> t.getID().startsWith(campo)
                    || t.getNombre().startsWith(campo))
                    .collect(Collectors.toList());
        }
        if (!reportes_clientes.isEmpty()) {
            reportes_clientes.forEach(item -> {
                registros[0] = String.valueOf(item.getIdRegistro());
                registros[1] = item.getID();
                registros[2] = item.getNombre();
                registros[3] = item.getApellidos();
                registros[4] = item.getSaldoActual();
                registros[5] = item.getFechaActual();
                registros[6] = item.getUltimoPago();
                registros[7] = item.getFechaPago();
                modelo2.addRow(registros);
            });
        }
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(1));
    }

    public void dataCliente(JCheckBox checkBox, JTextField txtPagos,
            JTextField txtBuscar, JTable table, List<JLabel> labels) {
        String deuda1, nombre, apellidos;
        double deuda2, deudaTotal;
        if (checkBox.isSelected()) {
            if (txtPagos.getText().isEmpty()) {
                if (checkBox.isSelected() == false) {
                    labels.get(0).setText("0,00€");
                    labels.get(1).setText("0,00€");
                    labels.get(2).setText(labels.get(0).getText());
                    labels.get(3).setText("Nombre");
                    labels.get(4).setText("0,00€");
                    labels.get(5).setText("0,00€");
                    labels.get(6).setText("--/--/----");
                }
            } else {
                if (verificar) {
                    if (!txtBuscar.getText().isEmpty()) {
                        int fila = table.getSelectedRow();
                        deuda1 = (String) modelo2.getValueAt(fila, 4);
                        deuda2 = formato.reconstruir(deuda1.replace("€", ""));
                        deudaTotal = deuda2 + totalPagar;
                        labels.get(0).setText(formato.decimal(deudaTotal) + "€");
                        nombre = (String) modelo2.getValueAt(fila, 2);
                        apellidos = (String) modelo2.getValueAt(fila, 3);

                        labels.get(1).setText(formato.decimal(totalPagar) + "€");
                        labels.get(2).setText(formato.decimal(deudaTotal) + "€");
                        labels.get(3).setText(nombre + " " + apellidos);
                        labels.get(4).setText(deuda1);
                        labels.get(5).setText((String) modelo2.getValueAt(fila, 6));
                        labels.get(6).setText(new Calendario().getFecha());
                    }
                } else {

                }
            }
        } else {
            labels.get(0).setText("0,00€");
            labels.get(1).setText("0,00€");
            labels.get(2).setText(labels.get(0).getText());
            labels.get(3).setText("");
            labels.get(4).setText("0,00€");
            labels.get(5).setText("0,00€");
            labels.get(6).setText("--/--/----");
        }
    }

    public void ingresosCaja(JLabel label1, JLabel label2, JLabel label3) {
        ingresosTotales = 0;
        cajaIngresoInicial = cajasIngresos().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                && t.getType().equals("Inicial") && t.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajaIngresoInicial.size()) {
            String data = cajaIngresoInicial.get(0).getIngreso();
            label1.setText(data);
            label1.setForeground(new Color(70, 106, 124));
            ingresosTotales = formato.reconstruir(data.replace("€", ""));
        } else {
            label1.setText("0,00€");
            label1.setForeground(Color.RED);
        }
        cajaIngresoVenta = cajasIngresos().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                && t.getType().equals("Ventas") && t.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajaIngresoVenta.size()) {
            String data = cajaIngresoVenta.get(0).getIngreso();
            label2.setText(data);
            label2.setForeground(new Color(70, 106, 124));
            ingresosTotales += formato.reconstruir(data.replace("€", ""));
        } else {
            label2.setText("0,00€");
            label2.setForeground(Color.RED);
        }
        label3.setText(formato.decimal(ingresosTotales) + "€");
        label3.setForeground(new Color(70, 106, 124));
    }

    public boolean cobrar(JCheckBox checkBox, JTextField txtPagos, JTable table,
            List<JLabel> labels) {
        boolean valor = false;
        if (txtPagos.getText().isEmpty()) {
            labels.get(7).setText("Ingrese el pago");
            labels.get(7).setForeground(Color.RED);
            txtPagos.requestFocus();
        } else {
            if (verificar) {
                String saldoActual, fechaPago, IDCliente = null;
                double deuda = 0, deudaActual, pago, pagos, ingresos = 0;
                int idRegistro = 0;
                pagos = formato.reconstruir(txtPagos.getText());
                if (checkBox.isSelected()) {
                    if (table.getSelectedRows().length > 0) {
                        int fila = table.getSelectedRow();
                        idRegistro = Integer.valueOf((String) modelo2.getValueAt(fila, 0));
                        IDCliente = (String) modelo2.getValueAt(fila, 1);
                        saldoActual = (String) modelo2.getValueAt(fila, 4);
                        deudaActual = formato.reconstruir(saldoActual.replace("€", ""));
                        deuda = totalPagar + deudaActual;
                        valor = insertVentas(checkBox, idRegistro, deuda, pagos,
                                IDCliente, labels);
                    } else {
                        if (verificar) {
                            labels.get(8).setText("Seleccione un cliente");
                            labels.get(8).setForeground(Color.RED);
                        }
                    }
                } else {
                    if (verificar) {
                        if (pagos >= importe) {
                            valor = insertVentas(checkBox, idRegistro, deuda, pagos,
                                    IDCliente, labels);
                        }
                    }
                }
            }
        }
        return valor;
    }

    private boolean insertVentas(JCheckBox checkBox, int idRegistro, double deuda,
            double pagos, String IDCliente, List<JLabel> labels) {
        boolean valor = false;
        tempoVentas = tempoVentas().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
        if (!tempoVentas.isEmpty()) {
            sql = "INSERT INTO ventas(codigo,descripcion,precio,cantidad,importe,"
                    + "dia,mes,year,fecha,caja,idUsuario) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            tempoVentas.forEach(item -> {
                object = new Object[]{
                    item.getCodigo(),
                    item.getDescripcion(),
                    item.getPrecio(),
                    item.getCantidad(),
                    item.getImporte(),
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getAnyo(),
                    new Calendario().getFecha(),
                    caja,
                    idUsuario
                };
                insert(sql, object);
            });
            if (checkBox.isSelected()) {
                sql = "UPDATE reportes_clientes SET saldoActual=?, fechaActual=?"
                        + " WHERE idRegistro=" + idRegistro;
                Object[] reporte = new Object[]{
                    formato.decimal(deuda) + "€",
                    new Calendario().getFecha()
                };
                update(sql, reporte);
                sql = "INSERT INTO creditos_ventas(total,pago,credito,dia,mes,year,"
                        + "fecha,cliente,caja,idUsuario) VALUES(?,?,?,?,?,?,?,?,?,?)";
                object = new Object[]{
                    formato.decimal(importe) + "€",
                    formato.decimal(pagos) + "€",
                    formato.decimal(totalPagar) + "€",
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getAnyo(),
                    new Calendario().getFecha(),
                    IDCliente,
                    caja,
                    idUsuario
                };
                insert(sql, object);
            }
            cajaIngresoVenta = cajasIngresos().stream()
                    .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                    && t.getType().equals("Ventas") && t.getFecha().equals(new Calendario().getFecha()))
                    .collect(Collectors.toList());
            if (cajaIngresoVenta.isEmpty()) {
                sql = "INSERT INTO cajas_ingresos(caja,ingreso,type,idUsuario,dia,"
                        + "mes,anio,fecha) VALUES(?,?,?,?,?,?,?,?)";
                object = new Object[]{
                    caja,
                    formato.decimal(pagos) + "€",
                    "Ventas",
                    idUsuario,
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getAnyo(),
                    new Calendario().getFecha()
                };
                insert(sql, object);
            } else {
                double ingresos = pagos + formato.reconstruir(
                        cajaIngresoVenta.get(0).getIngreso().replace("€", ""));
                sql = "UPDATE cajas_ingresos SET ingreso=?"
                        + " WHERE id=" + cajaIngresoVenta.get(0).getId();
                object = new Object[]{
                    formato.decimal(ingresos) + "€"
                };
                update(sql, object);
            }
            valor = true;
            if (suCambio) {
                double ingresosInicial = 0;
                cajaIngresoInicial = cajasIngresos().stream()
                        .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                        && t.getType().equals("Inicial") && t.getFecha().equals(new Calendario().getFecha()))
                        .collect(Collectors.toList());
                if (cajaIngresoInicial.isEmpty()) {
                    valor = ingresosVentas(labels);
                } else {
                    String ingreso = cajaIngresoInicial.get(0).getIngreso().replace("€", "");
                    ingresosInicial = formato.reconstruir(ingreso);
                    if (0 < ingresosInicial) {
                        if (ingresosInicial > totalPagar || ingresosInicial == totalPagar) {
                            ingresosInicial -= totalPagar;
                            sql = "UPDATE cajas_ingresos SET ingreso=?"
                                    + " WHERE id=" + cajaIngresoInicial.get(0).getId();
                            object = new Object[]{
                                formato.decimal(ingresosInicial) + "€"
                            };
                            update(sql, object);
                        } else {
                            valor = ingresosVentas(labels);
                        }
                    } else {
                        valor = ingresosVentas(labels);
                    }
                }
            }
        }
        return valor;
    }

    private boolean ingresosVentas(List<JLabel> labels) {
        double ingresoVenta = 0, ingresoInicial = 0;
        boolean valor = false;
        cajaIngresoVenta = cajasIngresos().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                && t.getType().equals("Ventas") && t.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (cajaIngresoVenta.isEmpty()) {
            labels.get(9).setText("No hay ingresos");
            labels.get(9).setForeground(Color.RED);
            valor = false;
        } else {
            String ingreso = cajaIngresoVenta.get(0).getIngreso().replace("€", "");
            ingresoVenta = formato.reconstruir(ingreso);
            if (ingresoVenta > totalPagar || ingresoVenta == totalPagar) {
                if (0 < cajaIngresoInicial.size()) {
                    String ingresoIni = cajaIngresoInicial.get(0).getIngreso().replace("€", "");
                    ingresoInicial = formato.reconstruir(ingresoIni);
                    totalPagar -= ingresoInicial;

                    sql = "UPDATE cajas_ingresos SET ingreso=?"
                            + " WHERE id=" + cajaIngresoInicial.get(0).getId();
                    object = new Object[]{
                        "0,00€"
                    };
                    update(sql, object);
                }

                ingresoVenta -= totalPagar;
                sql = "UPDATE cajas_ingresos SET ingreso=?"
                        + " WHERE id=" + cajaIngresoVenta.get(0).getId();
                object = new Object[]{
                    formato.decimal(ingresoVenta) + "€"
                };
                update(sql, object);

                valor = true;
            } else {
                if (totalPagar < ingresosTotales || ingresosTotales == totalPagar) {
                    if (0 < cajaIngresoInicial.size()) {
                        String ingresoIni = cajaIngresoInicial.get(0).getIngreso().replace("€", "");
                        ingresoInicial = formato.reconstruir(ingresoIni);
                        totalPagar -= ingresoInicial;

                        sql = "UPDATE cajas_ingresos SET ingreso=?"
                                + " WHERE id=" + cajaIngresoInicial.get(0).getId();
                        object = new Object[]{
                            "0,00€"
                        };
                        update(sql, object);
                    }

                    ingresoVenta -= totalPagar;
                    sql = "UPDATE cajas_ingresos SET ingreso=?"
                            + " WHERE id=" + cajaIngresoVenta.get(0).getId();
                    object = new Object[]{
                        formato.decimal(ingresoVenta) + "€"
                    };
                    update(sql, object);
                    valor = true;
                } else {
                    labels.get(9).setText("No hay ingresos");
                    labels.get(9).setForeground(Color.RED);
                    valor = false;
                }
            }
        }

        return valor;
    }

    public List<Tempo_ventas> getTempoVenta() {
        return tempoVentas().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
    }
}
