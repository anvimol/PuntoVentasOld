/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author avice
 */
public class Rventas {
    private final String codigo;
    private final String descripcion;
    private final String precioVenta;
    private final int cantidad;
    private final String precioCompra;
    private final String ganancias;

    public Rventas(String codigo, String descripcion, String precioVenta, 
            int cantidad, String precioCompra, String ganancias) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.ganancias = ganancias;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecioVenta() {
        return precioVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getPrecioCompra() {
        return precioCompra;
    }

    public String getGanancias() {
        return ganancias;
    }
    
    
}
