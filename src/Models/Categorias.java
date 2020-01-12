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
public class Categorias {
    private int idCat;
    private String categoria;
    private int dpto_id;

    public Categorias() {
    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getDpto_id() {
        return dpto_id;
    }

    public void setDpto_id(int dpto_id) {
        this.dpto_id = dpto_id;
    }

    @Override
    public String toString() {
        return categoria;
    }
    
    
}
