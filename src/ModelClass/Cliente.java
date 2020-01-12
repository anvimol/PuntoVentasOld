/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.GenerarNumeros;
import Connection.Consult;
import Models.Clientes;
import Models.Reportes_Clientes;
import java.sql.SQLException;
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
public class Cliente extends Consult {

    private DefaultTableModel modelo, modelo2;
    private List<Clientes> cliente, clienteFilter;
    private int idCliente;
    private String Id, sql;
    private Object[] object;

    public boolean insertCliente(String ID, String nombre, String apellidos,
            String direccion, String email, String telefono, String estado) {
        boolean valor = false;
        clienteFilter = clientes().stream()
                .filter(c -> c.getID().equals(ID))
                .collect(Collectors.toList());
        if (clienteFilter.isEmpty()) {
            sql = "INSERT INTO clientes(ID,nombre,apellidos,direccion,email,telefono, estado)"
                    + " VALUES(?,?,?,?,?,?,?)";
            object = new Object[]{ID, nombre, apellidos, direccion, email, telefono, estado};
            insert(sql, object);
            cliente = clientes();
            cliente.forEach(item -> {
                idCliente = item.getIdCliente();
                Id = item.getID();
            });

            sql = "INSERT INTO reportes_clientes(cliente_id,saldoActual,fechaActual,"
                    + "ultimoPago,fechaPago,IDCli) VALUES(?,?,?,?,?,?)";
            object = new Object[]{idCliente, "0,00€", "Sin fecha", "0,00€", "Sin fecha", Id};
            insert(sql, object);
            valor = true;
        } else {
            JOptionPane.showMessageDialog(null, "El ID ya existe", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return valor;
        }
        return valor;
    }

    public List<Clientes> getClientes() {
        return clientes();
    }

    public void searchClientes(JTable table, String campo, int num_registro,
            int reg_por_pagina) {
        String[] registros = new String[8];
        String[] titulos = {"Id", "ClienteID", "Nombre", "Apellidos", "Dirección",
            "Email", "Teléfono", "Estado"};
        modelo = new DefaultTableModel(null, titulos);
        cliente = clientes();
        if (campo.equals("")) {
            clienteFilter = cliente.stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = cliente.stream()
                    .filter(C -> C.getID().startsWith(campo)
                    || C.getNombre().startsWith(campo)
                    || C.getApellidos().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        clienteFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdCliente());
            registros[1] = item.getID();
            registros[2] = item.getNombre();
            registros[3] = item.getApellidos();
            registros[4] = item.getDireccion();
            registros[5] = item.getEmail();
            registros[6] = item.getTelefono();
            registros[7] = item.getEstado();
            modelo.addRow(registros);
        });
        table.setModel(modelo);
//        table.setRowHeight(30);
//        table.getColumnModel().getColumn(0).setMaxWidth(0);
//        table.getColumnModel().getColumn(0).setMinWidth(0);
//        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public DefaultTableModel reportesCliente(JTable table, int idCliente) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "Nombre", "Apellidos", "Saldo actual", "Fecha actual",
            "Ultimo pago", "Fecha pago"};
        modelo2 = new DefaultTableModel(null, titulos);
        List<Reportes_Clientes> reportes = reportesClientes(idCliente);
        reportes.forEach(item -> {
            registros[0] = String.valueOf(item.getIdRegistro());
            registros[1] = item.getNombre();
            registros[2] = item.getApellidos();
            registros[3] = item.getSaldoActual();
            registros[4] = item.getFechaActual();
            registros[5] = item.getUltimoPago();
            registros[6] = item.getFechaPago();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        return modelo2;
    }

    public boolean updateCliente(String id, String nombre, String apellidos,
            String direccion, String email, String telefono,
            String estado, int idCliente) {
        boolean valor = false;
        clienteFilter = clientes().stream()
                .filter(c -> c.getID().equals(id))
                .collect(Collectors.toList());
        int count = clienteFilter.size();
        clienteFilter = clientes().stream()
                .filter(c -> c.getIdCliente() == idCliente)
                .collect(Collectors.toList());
        if (0 == count || id.equals(clienteFilter.get(0).getID())) {

            sql = "UPDATE clientes SET ID=?, nombre=?, apellidos=?, direccion=?,"
                    + "email=?, telefono=?, estado=? WHERE idCliente=" + idCliente;
            Object[] clie = new Object[]{id, nombre, apellidos, direccion, email, telefono, estado};
            update(sql, clie);

            List<Reportes_Clientes> report = reportesClientes(idCliente);
            int idRegistro = report.get(0).getIdRegistro();
            int idClie = report.get(0).getIdCliente();
            String saldoAct = report.get(0).getSaldoActual();
            String fechaAct = report.get(0).getFechaActual();
            String ultPago = report.get(0).getUltimoPago();
            String fecPago = report.get(0).getFechaPago();
            String ID = id;
            sql = "UPDATE reportes_clientes SET cliente_id=?, saldoActual=?, fechaActual=?,"
                    + "ultimoPago=?, fechaPago=?, IDCli=? WHERE idRegistro=" + idRegistro;
            Object[] repor = new Object[]{idClie, saldoAct, fechaAct, ultPago, fecPago, ID};
            update(sql, repor);
            valor = true;
        }
        return valor;
    }

    public void activarCliente(JRadioButton _radioActivo, int idCliente) {

        final QueryRunner qr = new QueryRunner(true);
        String estado;
        if (_radioActivo.isSelected()) {
            estado = "Activo";
        } else {
            estado = "Inactivo";
        }
        if (idCliente != 0) {
            int confir = JOptionPane.showConfirmDialog(null, "Estas seguro de Activar / Desactivar "
                    + "este registro", "Confirmar", 2);
            if (confir == 0) {
                String sqlCli = "UPDATE clientes SET estado=? "
                        + "WHERE idCliente = " + idCliente;
                Object[] dataCli = {
                    estado
                };

                try {
                    qr.update(getConn(), sqlCli, dataCli);
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

//    public void deleteCliente(int idCliente, int idRegistro, String estado) {
//        sql = "UPDATE clientes SET estado=? WHERE idCliente=" + idCliente;
//        Object[] dele1 = new Object[]{estado};
//        delete(sql, dele1);
//    }
    public void updateReportes(String saldoActual, String fecha, String pago, int idCliente) {
        List<Reportes_Clientes> reportes = reportesClientes(idCliente);
        int idRegistro = reportes.get(0).getIdRegistro();
        int idCli = reportes.get(0).getIdCliente();
        String ID = reportes.get(0).getID();
        sql = "UPDATE reportes_clientes SET cliente_id=?, saldoActual=?, fechaActual=?,"
                + "ultimoPago=?, fechaPago=?, IDCli=? WHERE idRegistro = " + idRegistro;
        Object[] report = new Object[]{idCli, saldoActual + "€", fecha, pago + "€",
            fecha, ID};
        update(sql, report);
    }

    public void numeros(JTextField textfield) {
        int j;
        String c = "";
        // String SQL="select count(*) from productos";
        //String SQL = "SELECT MAX(codigo_cliente) AS cod_cli FROM cliente";
        //String SQL="SELECT @@identity AS ID";
        List<Clientes> cliente = clientes();
        if (cliente.isEmpty()) {
            j = 0;
            c = null;
            textfield.setText("CLI-00000001");
        } else {
            j = cliente.get(cliente.size() - 1).getIdCliente();
            c = cliente.get(j - 1).getID();

            if (c == null) {
                textfield.setText("CLI-00000001");
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
                gen.generar(var);

                textfield.setText(gen.serie());
            }
        }
    }

}
