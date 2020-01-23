/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Interfaces.IClassModels;
import ModelClass.ListClass;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author avice
 */
public class Paginador extends ListClass implements IClassModels {

    private static int pageSize = 2, tab, maxReg, pageCount;
    private static int num_registro = 0, numPagi = 1, boton;
    private int fun;
    private JTable table;
    private JLabel label;

    public Paginador() {
    }

    public Paginador(int tab, JTable table, JLabel label, int fun) {
        this.tab = tab;
        this.fun = fun;
        this.table = table;
        this.label = label;
        cargarDatos();
    }

    public void cargarDatos() {
        switch (tab) {
            case 0:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                tempoVentas = venta.getTempoVenta();
                venta.searchVentaTempo(table, num_registro, pageSize);
                maxReg = tempoVentas.size();
                break;
            case 1:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                numCliente = cliente.getClientes();
                cliente.searchClientes(table, "", num_registro, pageSize);
                maxReg = numCliente.size();
                break;
            case 2:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                numProveedor = proveedor.getProveedores();
                proveedor.searchProveedores(table, "", num_registro, pageSize);
                maxReg = numProveedor.size();
                break;
            case 4:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                productos = producto.producto();
                producto.searchProductos(table, "", num_registro, pageSize);
                maxReg = productos.size();
                break;
            case 5:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                numTempoCompras = compra.getTempoCompras();
                compra.searchCompras(table, "", num_registro, pageSize);
                maxReg = numTempoCompras.size();
                break;
            case 8:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                //listUsuario = usuarios.getUsuarios();
                usuarios.searchUsuario(table, "", num_registro, pageSize);
                maxReg = usuarios.getUsuarios().size();
                break;
            case 9:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                //listUsuario = usuarios.getUsuarios();
                inventario.getBodegas("", num_registro, pageSize);
                maxReg = inventario.getInventBodega().size();
                break;
        }
        pageCount = (maxReg / pageSize);
        // Ajuste el número de la página si la ultima página contiene una parte de la página
        if ((maxReg % pageSize) > 0) {
            pageCount += 1;
        }

        label.setText("Página " + "1" + "/" + String.valueOf(pageCount));
    }

    public void primero() {
        numPagi = 0;
        label.setText("Página " + "1" + "/" + String.valueOf(pageCount));
        switch (tab) {
            case 0:
                venta.searchVentaTempo(table, numPagi, pageSize);
                break;
            case 1:
                cliente.searchClientes(table, "", numPagi, pageSize);
                break;
            case 2:
                proveedor.searchProveedores(table, "", numPagi, pageSize);
                break;
            case 4:
                producto.searchProductos(table, "", numPagi, pageSize);
                break;
            case 5:
                compra.searchCompras(table, "", numPagi, pageSize);
                break;
            case 8:
                usuarios.searchUsuario(table, "", numPagi, pageSize);
                break;
            case 9:
                inventario.getBodegas("", numPagi, pageSize);
                break;
        }

        boton = 1;
    }

    public void anterior() {
        if (pageCount != 1 || boton == 1) {
            if (numPagi > 0) {
                if (boton == 3 || boton == 4) {
                    numPagi -= 1;
                }
            }
            label.setText("Página " + String.valueOf(numPagi) + "/"
                    + String.valueOf(pageCount));
            numPagi -= 1;

            num_registro = pageSize * numPagi;
            switch (tab) {
                case 0:
                    venta.searchVentaTempo(table, num_registro, pageSize);
                    break;
                case 1:
                    cliente.searchClientes(table, "", num_registro, pageSize);
                    break;
                case 2:
                    proveedor.searchProveedores(table, "", num_registro, pageSize);
                    break;
                case 4:
                    producto.searchProductos(table, "", num_registro, pageSize);
                    break;
                case 5:
                    compra.searchCompras(table, "", num_registro, pageSize);
                    break;
                case 8:
                    usuarios.searchUsuario(table, "", num_registro, pageSize);
                    break;
                case 9:
                    inventario.getBodegas("", num_registro, pageSize);
                    break;
            }
            boton = 2;
        }
    }

    public void siguiente() {
        if (pageCount != 1) {
            if (numPagi < pageCount) {
                if (boton == 2 || boton == 1) {
                    if (numPagi == 0) {
                        numPagi += 1;
                    } else {
                        if (numPagi >= 1) {
                            numPagi += 1;
                        }
                    }
                }
                boton = 3;
                num_registro = pageSize * numPagi;
                switch (tab) {
                    case 0:
                        venta.searchVentaTempo(table, num_registro, pageSize);
                        break;
                    case 1:
                        cliente.searchClientes(table, "", num_registro, pageSize);
                        break;
                    case 2:
                        proveedor.searchProveedores(table, "", num_registro, pageSize);
                        break;
                    case 4:
                        producto.searchProductos(table, "", num_registro, pageSize);
                        break;
                    case 5:
                        compra.searchCompras(table, "", num_registro, pageSize);
                        break;
                    case 8:
                        usuarios.searchUsuario(table, "", num_registro, pageSize);
                        break;
                    case 9:
                        inventario.getBodegas("", num_registro, pageSize);
                        break;
                }
                numPagi += 1;
                label.setText("Página " + String.valueOf(numPagi) + "/"
                        + String.valueOf(pageCount));
            } else {
                label.setText("Página " + String.valueOf(pageCount) + "/"
                        + String.valueOf(pageCount));
            }
        }
    }

    public void ultimo() {
        numPagi = pageCount;
        numPagi--;
        num_registro = pageSize * numPagi;
        label.setText("Página " + String.valueOf(pageCount) + "/"
                + String.valueOf(pageCount));
        switch (tab) {
            case 0:
                venta.searchVentaTempo(table, num_registro, pageSize);
                break;
            case 1:
                cliente.searchClientes(table, "", num_registro, pageSize);
                break;
            case 2:
                proveedor.searchProveedores(table, "", num_registro, pageSize);
                break;
            case 4:
                producto.searchProductos(table, "", num_registro, pageSize);
                break;
            case 5:
                compra.searchCompras(table, "", num_registro, pageSize);
                break;
            case 8:
                usuarios.searchUsuario(table, "", num_registro, pageSize);
                break;
            case 9:
                inventario.getBodegas("", num_registro, pageSize);
                break;
        }
        numPagi = pageCount;
        boton = 4;
    }
}
