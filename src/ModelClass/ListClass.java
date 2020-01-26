/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.FormatDecimal;
import Clases.TextFieldEvent;
import Clases.UploadImage;
import Models.*;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.jbarcodebean.JBarcodeBean;

/**
 *
 * @author avice
 */
public class ListClass {
    public FormatDecimal formato = new FormatDecimal();
    public JBarcodeBean barcode = new JBarcodeBean(); 
    public TextFieldEvent evento = new TextFieldEvent();
    public UploadImage imagen = new UploadImage();
    public static Inventario inventario;
    
    public List<Clientes> numCliente = new ArrayList<>();
    public List<Proveedores> numProveedor = new ArrayList<>();
    public List<Proveedores> dataProveedor = new ArrayList<>();
    public List<Tempo_compras> numTempoCompras = new ArrayList<>();
    public List<Usuarios> listUsuario = new ArrayList<>();
    public List<Cajas> listCaja = new ArrayList<>();
    public List<Reportes_Clientes> reportes_clientes = new ArrayList<>();
    public List<Reportes_proveedores> reportes_proveedores = new ArrayList<>();
    public List<Departamentos> departamentos = new ArrayList<>();
    public List<Categorias> categorias = new ArrayList<>();
    public List<Cajas_ingresos> cajasIngresos = new ArrayList<>();
    public List<Compras> compras = new ArrayList<>();
    public List<Tempo_productos> tempo_productos = new ArrayList<>();
    public List<Productos> productos = new ArrayList<>();
    public List<Bodegas> bodega = new ArrayList<>();
    public List<Tempo_ventas> tempoVentas = new ArrayList<>();
    public List<Cajas_ingresos> cajaIngresoInicial = new ArrayList<>();
    public List<Cajas_ingresos> cajaIngresoVenta = new ArrayList<>();
    public List<Roles> roles = new ArrayList<>();
    public List<Cajas_registros> cajasRegistro = new ArrayList<>();
    public List<Ventas> ventas = new ArrayList<>();
}
