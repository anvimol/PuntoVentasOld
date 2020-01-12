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
public class Proveedores {
    private int idProveedor;
    private String ID;
    private String proveedor;
    private String direccion;
    private String email;
    private String telefono;
    private String estado;

    public Proveedores() {
    }

    public Proveedores(int idProveedor, String ID, String proveedor, 
            String direccion, String email, String telefono, String estado) {
        this.idProveedor = idProveedor;
        this.ID = ID;
        this.proveedor = proveedor;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}
