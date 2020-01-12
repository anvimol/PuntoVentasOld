/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Calendario;
import Clases.Encriptar;
import Connection.Consult;
import Models.Cajas;
import Models.Usuarios;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author avice
 */
public class Usuario extends Consult {

    private Calendar c = new GregorianCalendar();
    private Caja caja = new Caja();
    private List<Usuarios> usuarioList, listUsuarios, listUsuario;
    private List<Cajas> listCajas, listCaja;

    public Usuario() {
        listUsuario = new ArrayList<Usuarios>();
        listCaja = new ArrayList<Cajas>();
    }

    public Object[] login(String usuario, String password) {
        listUsuario.clear();
        listUsuarios = usuarios().stream()
                .filter(P -> P.getUsuario().equals(usuario))
                .collect(Collectors.toList());
        if (!listUsuarios.isEmpty()) {
            String pass = "";
            try {
                pass = Encriptar.decrypt(listUsuarios.get(0).getPassword());
                if (pass.equals(password)) {
                    listUsuario = listUsuarios;
                    int idUsuario = listUsuarios.get(0).getIdUsuario();
                    String nombre = listUsuarios.get(0).getNombre();
                    String apellido = listUsuarios.get(0).getApellidos();
                    String user = listUsuarios.get(0).getUsuario();
                    String role = listUsuarios.get(0).getRole();
                    if (role.equals("Admin")) {
                        caja.insertCajaRegistro(idUsuario, nombre, apellido,
                                user, role, 0, 0, false, new Calendario().getHora(), 
                                new Calendario().getFecha());
                    } else {
                        listCaja = caja.getCajas();
                        int idCaja = listCaja.get(0).getIdCaja();
                        int cajas = listCaja.get(0).getCaja();
                        boolean estado = listCaja.get(0).isEstado();
                        caja.updateCaja(idCaja, false);
                        caja.insertCajaRegistro(idUsuario, nombre, apellido,
                                user, role, idCaja, cajas, estado, 
                                new Calendario().getHora(), new Calendario().getFecha());
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Object[] objects = {listUsuario, listCaja};
        return objects;
    }
}
