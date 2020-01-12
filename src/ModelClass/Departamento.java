/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Models.Categorias;
import Models.Departamentos;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author avice
 */
public class Departamento extends Consult {

    private String sql;
    private Object[] object;
    private DefaultTableModel modelo, modelo2;
    private List<Departamentos> departamentoFilter;
    private List<Categorias> categoriaFilter;

    public boolean insertDptoCat(String dptocat, int idDpto, String type) {
        boolean valor = false;
        if (type.equals("dpto")) {
            departamentoFilter = departamentos().stream()
                    .filter(d -> d.getDepartamento().equals(dptocat))
                    .collect(Collectors.toList());
            if (departamentoFilter.isEmpty()) {
                sql = "INSERT INTO departamentos(departamento) VALUES(?)";
                object = new Object[]{dptocat};
                insert(sql, object);
                valor = true;
            }
            departamentoFilter.clear();
        } else {
            categoriaFilter = categorias().stream()
                    .filter(c -> c.getCategoria().equals(dptocat))
                    .collect(Collectors.toList());
            if (categoriaFilter.isEmpty()) {
                sql = "INSERT INTO categorias(categoria, dpto_id) VALUES(?,?)";
                object = new Object[]{dptocat, idDpto};
                insert(sql, object);
                valor = true;
            }
            categoriaFilter.clear();
        }
        return valor;
    }

    public void searchDepartamentos(JTable table, String campo) {
        String[] registros = new String[2];
        String[] titulos = {"Id", "Departamento"};
        modelo = new DefaultTableModel(null, titulos);
        if (campo.isEmpty()) {
            departamentoFilter = departamentos().stream()
                    .collect(Collectors.toList());
        } else {
            departamentoFilter = departamentos().stream()
                    .filter(d -> d.getDepartamento().startsWith(campo))
                    .collect(Collectors.toList());
        }
        departamentoFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdDpto());
            registros[1] = item.getDepartamento();
            modelo.addRow(registros);
        });
        table.setModel(modelo);
        table.getTableHeader().setFont(new Font("Tahoma", 1, 14)); // Configurar cabecera de la Tabla
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public boolean updateDptoCat(String dptocat, int idDpto, int idCat, String type) {
        boolean valor = false;
        if (type.equals("dpto")) {
            departamentoFilter = departamentos().stream()
                    .filter(d -> d.getDepartamento().equals(dptocat))
                    .collect(Collectors.toList());
            if (departamentoFilter.isEmpty() || idDpto == departamentoFilter.get(0).getIdDpto()) {
                sql = "UPDATE departamentos SET departamento=? WHERE idDpto=" + idDpto;
                object = new Object[]{dptocat};
                update(sql, object);
                valor = true;
            }
        } else {
            categoriaFilter = categorias().stream()
                    .filter(c -> c.getCategoria().equals(dptocat))
                    .collect(Collectors.toList());
            if (categoriaFilter.isEmpty() || idCat == categoriaFilter.get(0).getIdCat()) {
                sql = "UPDATE categorias SET categoria=?,dpto_id=?  WHERE idCat=" + idCat;
                object = new Object[]{dptocat, idDpto};
                update(sql, object);
                valor = true;
            }
        }
        return valor;
    }

    public void getCategorias(JTable table, int idDpto) {
        String[] registros = new String[3];
        String[] titulos = {"Id", "Categorias", "IdDpto"};
        modelo2 = new DefaultTableModel(null, titulos);

        categoriaFilter = categorias().stream()
                .filter(c -> c.getDpto_id() == idDpto)
                .collect(Collectors.toList());

        categoriaFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdCat());
            registros[1] = item.getCategoria();
            registros[2] = String.valueOf(item.getDpto_id());
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.getTableHeader().setFont(new Font("Tahoma", 1, 14)); // Configurar cabecera de la Tabla
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);
    }

    public DefaultTableModel getModeloCat() {
        return modelo2;
    }
    
    public void deleteDptoCat(int idDpto, int idCat, String type) {
        if (type.equals("dpto")) {
            sql = "DELETE FROM departamentos WHERE idDpto LIKE ?";
            delete(sql, idDpto);
            sql = "DELETE FROM categorias WHERE dpto_id LIKE ?";
            delete(sql, idDpto);
        } else {
            sql = "DELETE FROM categorias WHERE idCat LIKE ?";
            delete(sql, idCat);
        }
    }
}
