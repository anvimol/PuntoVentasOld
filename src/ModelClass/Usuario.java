/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Calendario;
import Clases.Encriptar;
import Clases.Paginador;
import Clases.UploadImage;
import Connection.Consult;
import Interfaces.IUploadImage;
import Models.Cajas;
import Models.Roles;
import Models.Usuarios;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author avice
 */
public class Usuario extends Consult implements IUploadImage {

    private Calendar c = new GregorianCalendar();
    private Caja caja = new Caja();
    private List<Usuarios> listUsuarios;
    private List<Cajas> listCaja;
    private DefaultComboBoxModel model;
    private Object[] object;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private JLabel label1, label2, label3, label4, label5, label6, label7, label8, label9, label10;
    private JTable table;
    private JComboBox comboUserRoles;
    private String sql, imagenes;
    private static String fileName, accion = "insert";
    private static DefaultTableModel modelo1, modelo2;
    private static int pageSize = 2, idUsuario;

    public Usuario() {
        listUsuario = new ArrayList<Usuarios>();
        listCaja = new ArrayList<Cajas>();
    }

    public Usuario(Object[] textFieldObject, Object[] labelsObject,
            JComboBox comboUserRoles, JTable table) {
        this.table = table;
        this.comboUserRoles = comboUserRoles;

        textField1 = (JTextField) textFieldObject[0];
        textField2 = (JTextField) textFieldObject[1];
        textField3 = (JTextField) textFieldObject[2];
        textField4 = (JTextField) textFieldObject[3];
        textField5 = (JTextField) textFieldObject[4];
        textField6 = (JTextField) textFieldObject[5];
        textField7 = (JTextField) textFieldObject[6];

        label1 = (JLabel) labelsObject[0];
        label2 = (JLabel) labelsObject[1];
        label3 = (JLabel) labelsObject[2];
        label4 = (JLabel) labelsObject[3];
        label5 = (JLabel) labelsObject[4];
        label6 = (JLabel) labelsObject[5];
        label7 = (JLabel) labelsObject[6];
        label8 = (JLabel) labelsObject[7];
        label9 = (JLabel) labelsObject[8];
        label10 = (JLabel) labelsObject[9];
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

    public DefaultComboBoxModel getRoles(JComboBox combo, String role) {
        model = new DefaultComboBoxModel();
        if (0 < roles().size()) {
            if (role != null) {
                List<Roles> roles = roles().stream()
                        .filter(r -> r.getRole().equalsIgnoreCase(role))
                        .collect(Collectors.toList());
                model.addElement(roles.get(0));
            }
            roles().forEach(item -> {
                if (role != null) {
                    if (!role.equalsIgnoreCase(item.getRole())) {
                        model.addElement(item);
                    }
                } else {
                    model.addElement(item);
                }
            });
            combo.setModel(model);
        }
        return model;
    }

    public boolean registrarUsuario() {
        boolean valor = false;
        if (validarDatos()) {
            if (evento.isEmail(textField5.getText())) {
                int count;
                List<Usuarios> listEmail = usuarios().stream()
                        .filter(u -> u.getEmail().equals(textField5.getText()))
                        .collect(Collectors.toList());
                count = listEmail.size();
                List<Usuarios> listUsers = usuarios().stream()
                        .filter(u -> u.getUsuario().equals(textField6.getText()))
                        .collect(Collectors.toList());
                count += listUsers.size();
                if (count == 2) {
                    if (idUsuario == listUsers.get(0).getIdUsuario()
                            && idUsuario == listEmail.get(0).getIdUsuario()) {
                        valor = true;
                        valor = ejecutar(valor);
                    } else {
                        if (idUsuario != listEmail.get(0).getIdUsuario()) {
                            label5.setText("El Email ya esta registrado");
                            label5.setForeground(Color.RED);
                            textField5.requestFocus();
                            valor = false;
                        }
                        if (idUsuario != listUsers.get(0).getIdUsuario()) {
                            label6.setText("El Usuario ya esta registrado");
                            label6.setForeground(Color.RED);
                            textField6.requestFocus();
                            valor = false;
                        }
                    }
                } else {
                    if (count == 0) {
                        valor = true;
                        valor = ejecutar(valor);
                    } else {
                        if (0 != listEmail.size()) {
                            if (idUsuario == listEmail.get(0).getIdUsuario()) {
                                valor = true;
                            } else {
                                label5.setText("El Email ya esta registrado");
                                label5.setForeground(Color.RED);
                                textField5.requestFocus();
                                valor = false;
                            }
                        }
                        if (0 != listUsers.size()) {
                            if (idUsuario == listUsers.get(0).getIdUsuario()) {
                                valor = true;
                            } else {
                                label6.setText("El Usuario ya esta registrado");
                                label6.setForeground(Color.RED);
                                textField6.requestFocus();
                                valor = false;
                            }
                        }
                        valor = ejecutar(valor);
                    }
                }
            } else {
                label5.setText("El Email no es valido");
                label5.setForeground(Color.RED);
                textField5.requestFocus();
                valor = false;
            }
        }

        return valor;
    }

    private boolean ejecutar(boolean valor) {
        boolean data = false;
        if (valor) {
            data = insertUsuario();
        }
        return data;
    }

    private boolean insertUsuario() {
        boolean valor = false;
        String archivoOrigen = UploadImage.getUrlOrigen();
        imagenes = textField5.getText();
        if (archivoOrigen != null) {
            copiarImagen(imagenes);
        } else {
            if (fileName != null) {
                imagenes = fileName;
            } else {
                copiarImagen(imagenes);
            }
        }
        Roles rol = (Roles) comboUserRoles.getSelectedItem();
        switch (accion) {
            case "insert":
                sql = "INSERT INTO usuarios(nombre,apellidos,telefono,direccion,email,"
                        + "usuario,password,role,imagen) VALUES(?,?,?,?,?,?,?,?,?)";
                try {
                    object = new Object[]{
                        textField1.getText(),
                        textField2.getText(),
                        textField3.getText(),
                        textField4.getText(),
                        textField5.getText(),
                        textField6.getText(),
                        Encriptar.encrypt(textField7.getText()),
                        rol.getRole(),
                        textField5.getText()
                    };
                    insert(sql, object);
                    valor = true;
                } catch (Exception ex) {
                    valor = false;
                }
                break;
            case "update":
                sql = "UPDATE usuarios SET nombre=?,apellidos=?,telefono=?,direccion=?,email=?,"
                        + "usuario=?,password=?,role=?,imagen=? WHERE idUsuario = " + idUsuario;
                try {
                    object = new Object[]{
                        textField1.getText(),
                        textField2.getText(),
                        textField3.getText(),
                        textField4.getText(),
                        textField5.getText(),
                        textField6.getText(),
                        Encriptar.encrypt(textField7.getText()),
                        rol.getRole(),
                        textField5.getText()
                    };
                    update(sql, object);
                    valor = true;
                } catch (Exception ex) {
                    valor = false;
                }
                break;
        }

        return valor;
    }

    public void searchUsuario(JTable table, String campo, int num_registro, int reg_por_pagina) {
        String[] registros = new String[10];
        String[] titulos = {"Id", "Nombre", "Apellidos", "Teléfono", "Dirección",
            "Email", "Usuario", "Contraseña", "Role", "Imagen"};
        modelo1 = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            listUsuario = usuarios().stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            listUsuario = usuarios().stream()
                    .filter(u -> u.getNombre().startsWith(campo)
                    || u.getApellidos().startsWith(campo)
                    || u.getEmail().startsWith(campo)
                    || u.getTelefono().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        listUsuario.forEach(item -> {
            registros[0] = String.valueOf(item.getIdUsuario());
            registros[1] = item.getNombre();
            registros[2] = item.getApellidos();
            registros[3] = item.getTelefono();
            registros[4] = item.getDireccion();
            registros[5] = item.getEmail();
            registros[6] = item.getUsuario();
            registros[7] = item.getPassword();
            registros[8] = item.getRole();
            registros[9] = item.getImagen();
            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setPreferredWidth(0);
        table.getColumnModel().getColumn(9).setMaxWidth(0);
        table.getColumnModel().getColumn(9).setMinWidth(0);
        table.getColumnModel().getColumn(9).setPreferredWidth(0);
    }

    public void dataTableUsuarios() {
        accion = "update";
        int fila = table.getSelectedRow();
        String pass;
        try {
            pass = Encriptar.decrypt((String) modelo1.getValueAt(fila, 7));
            idUsuario = Integer.valueOf((String) modelo1.getValueAt(fila, 0));
            textField1.setText((String) modelo1.getValueAt(fila, 1));
            textField2.setText((String) modelo1.getValueAt(fila, 2));
            textField3.setText((String) modelo1.getValueAt(fila, 3));
            textField4.setText((String) modelo1.getValueAt(fila, 4));
            textField5.setText((String) modelo1.getValueAt(fila, 5));
            textField6.setText((String) modelo1.getValueAt(fila, 6));
            textField7.setText(pass);
            comboUserRoles.setModel(getRoles(comboUserRoles, (String) modelo1.getValueAt(fila, 8)));
            fileName = (String) modelo1.getValueAt(fila, 9);
            String imgDestino = "C:\\Users\\avice\\SynologyDrive\\ProyectosNetBeans\\"
                    + "Punto de ventas\\src\\fotos\\" + fileName + ".png";
            cargarImagen(label9, false, imgDestino);
        } catch (Exception ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        label1.setForeground(new Color(0, 153, 51));
        label2.setForeground(new Color(0, 153, 51));
        label3.setForeground(new Color(0, 153, 51));
        label4.setForeground(new Color(0, 153, 51));
        label5.setForeground(new Color(0, 153, 51));
        label6.setForeground(new Color(0, 153, 51));
        label7.setForeground(new Color(0, 153, 51));
        label8.setForeground(new Color(0, 153, 51));
    }

    public void restablecerUsuarios() {
        accion = "insert";
        idUsuario = 0;
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
        label1.setText("Nombre");
        label1.setForeground(new Color(70, 106, 124));
        label2.setText("Apellidos");
        label2.setForeground(new Color(70, 106, 124));
        label3.setText("Teléfono");
        label3.setForeground(new Color(70, 106, 124));
        label4.setText("Direccion");
        label4.setForeground(new Color(70, 106, 124));
        label5.setText("Email");
        label5.setForeground(new Color(70, 106, 124));
        label6.setText("Usuario");
        label6.setForeground(new Color(70, 106, 124));
        label7.setText("Contraseña");
        label7.setForeground(new Color(70, 106, 124));
        label8.setText("Roles");
        label8.setForeground(new Color(70, 106, 124));
        getRoles(comboUserRoles, null);
        searchUsuario(table, "", 0, pageSize);
        cargarImagen(label9, false, null);
        new Paginador(8, table, label10, 1).primero();
    }

    public boolean validarDatos() {
        if (textField1.getText().equals("")) {
            label1.setText("Ingrese el Nombre");
            label1.setForeground(Color.RED);
            textField1.requestFocus();
            return false;
        } else if (textField2.getText().equals("")) {
            label2.setText("Ingrese los Apellidos");
            label2.setForeground(Color.RED);
            textField2.requestFocus();
            return false;
        } else if (textField3.getText().equals("")) {
            label3.setText("Ingrese el Teléfono");
            label3.setForeground(Color.RED);
            textField3.requestFocus();
            return false;
        } else if (textField4.getText().equals("")) {
            label4.setText("Ingrese la Dirección");
            label4.setForeground(Color.RED);
            textField4.requestFocus();
            return false;
        } else if (textField5.getText().equals("")) {
            label5.setText("Ingrese el Email");
            label5.setForeground(Color.RED);
            textField5.requestFocus();
            return false;
        } else if (textField6.getText().equals("")) {
            label6.setText("Ingrese el Usuario");
            label6.setForeground(Color.RED);
            textField6.requestFocus();
            return false;
        } else if (textField7.getText().equals("")) {
            label7.setText("Ingrese la contraseña");
            label7.setForeground(Color.RED);
            textField7.requestFocus();
            return false;
        }

        return true;
    }

    public List<Usuarios> getUsuarios() {
        return usuarios();
    }

    @Override
    public void cargarImagen(JLabel label, boolean valor, String urlImagen) {
        imagen.cargarImagen(label, valor, urlImagen);
    }

    @Override
    public void copiarImagen(String fileName) {
        imagen.copiarImagen(fileName);
    }
}
