/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Models.Cajas;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author avice
 */
public class Caja extends Consult {
    private String sql;
    private Object[] object;
    private List<Cajas> listCajas;
    
    public List<Cajas> getCajas() {
        listCajas = cajas().stream()
                .filter(c -> c.isEstado() == true)
                .collect(Collectors.toList());
        return listCajas;
    }
    
    public void insertCajaRegistro(int idUsuario, String nombre, String apellidos, 
            String usuario, String role, int idCaja, int caja, boolean estado, 
            String hora, String fecha) {
        sql = "INSERT INTO cajas_registros(idUsuario,nombre,apellidos,usuario,"
                + "role,idCaja,caja,estado,hora,fecha) VALUES(?,?,?,?,?,?,?,?,?,?)";
        object = new Object[]{idUsuario,nombre,apellidos,usuario,role,idCaja,
            caja,estado,hora,fecha};
        insert(sql, object);
    }
    
    public void updateCaja(int idCaja, boolean estado) {
        sql = "UPDATE cajas SET estado=? WHERE idCaja=" + idCaja;
        object = new Object[] {estado};
        update(sql, object);
    }
}
