/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import ModelClass.ListClass;
import Models.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author avice
 */
public class Consult extends Conexion {

    private QueryRunner QR = new QueryRunner();
//    private List<Clientes> cliente;
//    private List<Reportes_Clientes> reportes_clientes;
//    private List<Reportes_proveedores> reportes_proveedores;
//    private List<Proveedores> proveedor;
//    private List<Usuarios> usuarios;
//    private List<Cajas> cajas;
//    private List<Departamentos> departamentos;
//    private List<Categorias> categorias;
//    private List<Tempo_compras> tempocompras;

    public List<Clientes> clientes() {
        try {
            numCliente = (List<Clientes>) QR.query(getConn(), "SELECT * FROM clientes",
                    new BeanListHandler(Clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return numCliente;
    }

    public void insert(String sql, Object[] data) {
        final QueryRunner qr = new QueryRunner(true);
        try {
            qr.insert(getConn(), sql, new ColumnListHandler(), data);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }

    public List<Reportes_Clientes> reportesClientes(int idCli) {
        String condicion = " clientes.idCliente = reportes_clientes.cliente_id ";
        String campos = " clientes.idCliente, clientes.ID, clientes.nombre, clientes.apellidos,"
                + "reportes_clientes.idRegistro, reportes_clientes.cliente_id, reportes_clientes.saldoActual,"
                + "reportes_clientes.fechaActual, reportes_clientes.ultimoPago,"
                + "reportes_clientes.fechaPago, reportes_clientes.IDCli ";
        try {
            reportes_clientes = (List<Reportes_Clientes>) QR.query(getConn(),
                    "SELECT " + campos + " FROM reportes_clientes INNER JOIN clientes ON "
                    + condicion + " WHERE reportes_clientes.cliente_id = " + idCli,
                    new BeanListHandler(Reportes_Clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return reportes_clientes;
    }

    public void update(String sql, Object[] data) {
        final QueryRunner qr = new QueryRunner(true);
        try {
            qr.update(getConn(), sql, data);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }

    public void delete(String sql, int id) {
        final QueryRunner qr = new QueryRunner(true);

        try {
            if (0 < id) {
                qr.update(getConn(), sql, "%" + id + "%");
            } else {
                qr.update(getConn(), sql);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }

    public List<Proveedores> proveedores() {
        try {
            numProveedor = (List<Proveedores>) QR.query(getConn(), "SELECT * FROM proveedores",
                    new BeanListHandler(Proveedores.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return numProveedor;
    }

    public List<Reportes_proveedores> reportesProvee(int idProveedor) {
        String condicion = " proveedores.idProveedor = reportes_proveedores.proveedor_id";
        String campos = "proveedores.idProveedor,proveedores.ID,proveedores.proveedor,"
                + "reportes_proveedores.idRegistro,reportes_proveedores.saldoActual,"
                + "reportes_proveedores.fechaActual,reportes_proveedores.ultimoPago,"
                + "reportes_proveedores.fechaPago";
        try {
            reportes_proveedores = (List<Reportes_proveedores>) QR.query(getConn(),
                    "SELECT " + campos + " FROM reportes_proveedores INNER JOIN proveedores ON"
                    + condicion + " WHERE reportes_proveedores.proveedor_id="
                    + idProveedor, new BeanListHandler(Reportes_proveedores.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return reportes_proveedores;
    }

    public List<Usuarios> usuarios() {
        try {
            listUsuario = (List<Usuarios>) QR.query(getConn(), "SELECT * FROM usuarios",
                    new BeanListHandler(Usuarios.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return listUsuario;
    }

    public List<Cajas> cajas() {
        try {
            listCaja = (List<Cajas>) QR.query(getConn(), "SELECT * FROM cajas",
                    new BeanListHandler(Cajas.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return listCaja;
    }

    public List<Departamentos> departamentos() {
        try {
            departamentos = (List<Departamentos>) QR.query(getConn(), "SELECT * FROM departamentos",
                    new BeanListHandler(Departamentos.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return departamentos;
    }

    public List<Categorias> categorias() {
        try {
            categorias = (List<Categorias>) QR.query(getConn(), "SELECT * FROM categorias",
                    new BeanListHandler(Categorias.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return categorias;
    }

    public List<Tempo_compras> tempoCompras() {
        try {
            numTempoCompras = (List<Tempo_compras>) QR.query(getConn(), "SELECT * FROM tempo_compras",
                    new BeanListHandler(Tempo_compras.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return numTempoCompras;
    }

    public List<Reportes_proveedores> reportesProveedores() {
        String condicion = " proveedores.idProveedor = reportes_proveedores.proveedor_id";
        String campos = "proveedores.idProveedor,proveedores.ID,proveedores.proveedor,"
                + "proveedores.telefono,proveedores.email,"
                + "reportes_proveedores.idRegistro,reportes_proveedores.saldoActual";
        try {
            reportes_proveedores = (List<Reportes_proveedores>) QR.query(getConn(),
                    "SELECT " + campos + " FROM reportes_proveedores INNER JOIN proveedores ON"
                    + condicion, new BeanListHandler(Reportes_proveedores.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return reportes_proveedores;
    }

    public List<Cajas_ingresos> cajasIngresos() {
        try {
            cajasIngresos = (List<Cajas_ingresos>) QR.query(getConn(), "SELECT * FROM cajas_ingresos",
                    new BeanListHandler(Cajas_ingresos.class));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return cajasIngresos;
    }

    public List<Compras> compras() {
        try {
            compras = (List<Compras>) QR.query(getConn(), "SELECT * FROM compras",
                    new BeanListHandler(Compras.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return compras;
    }
    
    public List<Tempo_productos> tempoProductos() {
        String condicion = " tempo_productos.compra_id = compras.idCompra "; 
        String campos = "tempo_productos.compra_id,compras.idCompra,compras.producto,"
                + "compras.cantidad,compras.precio,compras.importe,compras.proveedor,"
                + "compras.fecha";
        try {
            tempo_productos = (List<Tempo_productos>) QR.query(getConn(),
                    "SELECT " + campos + " FROM tempo_productos INNER JOIN compras ON"
                            + condicion, new BeanListHandler(Tempo_productos.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return tempo_productos;
    }
    
    public List<Productos> productos() {
        try {
            productos = (List<Productos>) QR.query(getConn(), "SELECT * FROM productos",
                    new BeanListHandler(Productos.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return productos;
    }
    
    public List<Bodegas> bodegas() {
        try {
            bodega = (List<Bodegas>) QR.query(getConn(), "SELECT * FROM bodegas",
                    new BeanListHandler(Bodegas.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return bodega;
    }
}
