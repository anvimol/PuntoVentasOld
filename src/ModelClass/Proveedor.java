/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.GenerarNumeros;
import Connection.Consult;
import Models.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author avice
 */
public class Proveedor extends Consult {

    private DefaultTableModel modelo1, modelo2;
    private List<Proveedores> proveedor, proveedor1, proveedor2, proveedorFilter;
    private String sql;
    private Object[] object;

    public List<Proveedores> insertProveedor(String ID, String prov, String direccion,
            String email, String telefono, String estado) {
        //proveedor = proveedores();
        proveedor1 = proveedores().stream()
                .filter(p -> p.getTelefono().equals(telefono)
                || p.getEmail().equals(email))
                .collect(Collectors.toList());
        if (proveedor1.isEmpty()) { // 0 == proveedor1.size()
            sql = "INSERT INTO proveedores(ID,proveedor,direccion,email,"
                    + "telefono,estado) VALUES(?,?,?,?,?,?)";
            object = new Object[]{ID, prov, direccion, email, telefono, estado};
            insert(sql, object);

            sql = "INSERT INTO reportes_proveedores(proveedor_id,saldoActual,"
                    + "fechaActual,ultimoPago,fechaPago,IDPro) VALUES(?,?,?,?,?,?)";
            proveedor = proveedores();
            int pos = proveedor.size();
            pos--;
            int idProveed = proveedor.get(pos).getIdProveedor();
            object = new Object[]{idProveed, "0,00€", "Sin fecha", "0,00€", "Sin fecha", ID};
            insert(sql, object);
        }
        return proveedor1;
    }

    public List<Proveedores> getProveedores() {
        return proveedores();
    }

    public void searchProveedores(JTable table, String campo, int num_registro,
            int reg_por_pagina) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "ID", "Proveedor", "Dirección", "Email", "Telefono", "Estado"};
        modelo1 = new DefaultTableModel(null, titulos);
        proveedor = proveedores();
        if (campo.isEmpty()) {
            proveedorFilter = proveedor.stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            proveedorFilter = proveedor.stream()
                    .filter(P -> P.getProveedor().startsWith(campo)
                    || P.getEmail().startsWith(campo)
                    || P.getTelefono().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        proveedorFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdProveedor());
            registros[1] = item.getID();
            registros[2] = item.getProveedor();
            registros[3] = item.getDireccion();
            registros[4] = item.getEmail();
            registros[5] = item.getTelefono();
            registros[6] = item.getEstado();
            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
//        table.setRowHeight(30);
//        table.getColumnModel().getColumn(0).setMaxWidth(0);
//        table.getColumnModel().getColumn(0).setMinWidth(0);
//        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public DefaultTableModel getModelo() {
        return modelo1;
    }

    public DefaultTableModel reportesProveedor(JTable table, int idProveedor) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "Proveedor", "Saldo actual", "Fecha actual",
            "Ultimo pago", "fecha pago"};
        modelo2 = new DefaultTableModel(null, titulos);
        List<Reportes_proveedores> reportes = reportesProvee(idProveedor);
        reportes.forEach(item -> {
            registros[0] = String.valueOf(item.getIdRegistro());
            registros[1] = item.getProveedor();
            registros[2] = item.getSaldoActual();
            registros[3] = item.getFechaActual();
            registros[4] = item.getUltimoPago();
            registros[5] = item.getFechaPago();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        return modelo2;
    }

    public List<Proveedores> updateProveedor(int idProv, String ID, String proveedor,
            String direccion, String email, String telefono, String estado) {
        proveedor1 = proveedores().stream()
                .filter(P -> P.getTelefono().equals(telefono))
                .collect(Collectors.toList());
        proveedor2 = proveedores().stream()
                .filter(P -> P.getEmail().equals(email))
                .collect(Collectors.toList());
        List<Proveedores> listFinal = new ArrayList<>();
        listFinal.addAll(proveedor1);
        listFinal.addAll(proveedor2);
        if (2 == listFinal.size()) {
            if (idProv == proveedor1.get(0).getIdProveedor()
                    && idProv == proveedor2.get(0).getIdProveedor()) {
                updateProv(idProv, ID, proveedor, direccion, email, telefono, estado);
                listFinal.clear();
            }
        } else {
            if (listFinal.isEmpty()) {
                updateProv(idProv, ID, proveedor, direccion, email, telefono, estado);
                listFinal.clear();
            } else {
                if (!proveedor1.isEmpty()) {
                    if (idProv == proveedor1.get(0).getIdProveedor()) {
                        updateProv(idProv, ID, proveedor, direccion, email, telefono, estado);
                        listFinal.clear();
                    }
                }
                if (!proveedor2.isEmpty()) {
                    if (idProv == proveedor2.get(0).getIdProveedor()) {
                        updateProv(idProv, ID, proveedor, direccion, email, telefono, estado);
                        listFinal.clear();
                    }
                }
            }
        }
        return listFinal;
    }

    private void updateProv(int idProve, String ID, String provee, String direccion,
            String email, String telefono, String estado) {
        sql = "UPDATE proveedores SET ID=?, proveedor=?, direccion=?, email=?,"
                + "telefono=?, estado=? WHERE idProveedor=" + idProve;
        Object[] pro = new Object[] {ID,provee,direccion,email,telefono,estado};
        update(sql, pro);
    }
    
    public void activarProveedor(JRadioButton _radioActivo, int idProv) {

        final QueryRunner qr = new QueryRunner(true);
        String estado;
        if (_radioActivo.isSelected()) {
            estado = "Activo";
        } else {
            estado = "Inactivo";
        }
        if (idProv != 0) {
            int confir = JOptionPane.showConfirmDialog(null, "Estas seguro de Activar / Desactivar "
                    + "este registro", "Confirmar", 2);
            if (confir == 0) {
                String sqlProv = "UPDATE proveedores SET estado=? "
                        + "WHERE idProveedor = " + idProv;
                Object[] dataProv = {
                    estado
                };

                try {
                    qr.update(getConn(), sqlProv, dataProv);
                    //restablecer();
                } catch (SQLException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateReporProve(String saldoActual, String fecha, String pago,
            int idProvee) {
        List<Reportes_proveedores> report = reportesProvee(idProvee);
        int idRegis = report.get(0).getIdRegistro();
        sql = "UPDATE reportes_proveedores SET proveedor_id=?,saldoActual=?,"
                + "fechaActual=?,ultimoPago=?,fechaPago=? WHERE idRegistro=" + idRegis;
        Object[] repor = new Object[] {idProvee,saldoActual+"€",fecha,pago+"€",fecha};
        update(sql, repor);
    }

    public void numeros(JTextField textfield) {
        int j;
        String c = "";
        // String SQL="select count(*) from productos";
        //String SQL = "SELECT MAX(codigo_cliente) AS cod_cli FROM cliente";
        //String SQL="SELECT @@identity AS ID";
        List<Proveedores> proveedores = proveedores();
        if (proveedores.isEmpty()) {
            j = 0;
            c = null;
            textfield.setText("PRO-00000001");
        } else {
            j = proveedores.get(proveedores.size() - 1).getIdProveedor();
            c = proveedores.get(j - 1).getID();

            if (c == null) {
                textfield.setText("PRO-00000001");
            } else {
                char r1 = c.charAt(4);
                char r2 = c.charAt(5);
                char r3 = c.charAt(6);
                char r4 = c.charAt(7);
                char r5 = c.charAt(8);
                char r6 = c.charAt(9);
                char r7 = c.charAt(10);
                char r8 = c.charAt(11);
                //System.out.print("" + r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8);
                String juntar = "" + r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8;
                int var = Integer.parseInt(juntar);
                //System.out.print("\n este lo que vale numericamente" + var);
                GenerarNumeros gen = new GenerarNumeros();
                gen.generarPro(var);

                textfield.setText(gen.serie());
            }
        }
    }

}
