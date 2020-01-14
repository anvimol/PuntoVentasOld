/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Clases.ImagenBanner;
import Clases.FormatDecimal;
import Clases.Imprimir;
import Clases.TextFieldEvent;
import ModelClass.*;

/**
 *
 * @author avice
 */
public interface IClassModels {
    public ImagenBanner p = new ImagenBanner();
    public TextFieldEvent evento = new TextFieldEvent();
    public Cliente cliente = new Cliente();
    public Proveedor proveedor = new Proveedor();
    public Caja caja = new Caja();
    public Departamento departamento = new Departamento();
    public Compra compra = new Compra();
    public FormatDecimal formato = new FormatDecimal();
    public Imprimir imprimir = new Imprimir();
    public Producto producto = new Producto();
    public Venta venta = new Venta();
}
