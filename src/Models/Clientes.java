/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author antonio
 */
public class Clientes {
    private int idCliente;
    private String ID;
    //private String dni;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String email;
    private String telefono;
    private String estado;

    public Clientes() {
    }

//    public Clientes(int idCliente, String ID, String nombre, String apellidos, 
//            String direccion, String email, String telefono) {
//        this.idCliente = idCliente;
//        this.ID = ID;
//        this.nombre = nombre;
//        this.apellidos = apellidos;
//        this.direccion = direccion;
//        this.email = email;
//        this.telefono = telefono;
//    }
    
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
