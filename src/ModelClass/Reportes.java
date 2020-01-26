/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Clases.Calendario;
import Connection.Consult;
import Models.Bodegas;
import Models.Categorias;
import Models.Departamentos;
import Models.Productos;
import Models.Rventas;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author avice
 */
public class Reportes extends Consult {

    private JComboBox ComboBoxReport_Depto, ComboBoxReport_Cat;
    private DefaultTableModel modelo1, modelo2, modelo3;
    private DefaultComboBoxModel model;
    private JTable table1, table2, table3;
    private JLabel grafico_Product;

    public Reportes(Object[] objectReport) {
        ComboBoxReport_Depto = (JComboBox) objectReport[0];
        ComboBoxReport_Cat = (JComboBox) objectReport[1];
        table1 = (JTable) objectReport[2];
        grafico_Product = (JLabel) objectReport[3];
        table2 = (JTable) objectReport[4];
        table3 = (JTable) objectReport[5];
    }

    public void restablecer() {
        getDepartamentos();
        getCategorias(0);
        getProductos("");
        graficarProducto(null);
    }

    public void getDepartamentos() {
        model = new DefaultComboBoxModel();
        if (0 < departamentos().size()) {
            departamentos().forEach(item -> {
                model.addElement(item);
            });
            ComboBoxReport_Depto.setModel(model);
        }
    }

    public void getCategorias(int idDptos) {
        int idDpto;
        model = new DefaultComboBoxModel();
        if (idDptos == 0) {
            Departamentos dpt = (Departamentos) ComboBoxReport_Depto.getSelectedItem();
            idDpto = dpt.getIdDpto();
        } else {
            idDpto = idDptos;
        }
        categorias = categorias();
        if (0 < categorias.size()) {
            List<Categorias> categoria = categorias.stream()
                    .filter(c -> c.getDpto_id() == idDpto)
                    .collect(Collectors.toList());
            categoria.forEach(item -> {
                model.addElement(item);
            });
            ComboBoxReport_Cat.setModel(model);
        }
    }

    public void getProductos(String campo) {
        List<Productos> list;
        String[] titulos = {"IdProducto", "Codigo", "Producto"};
        modelo1 = new DefaultTableModel(null, titulos);
        Departamentos dpt = (Departamentos) ComboBoxReport_Depto.getSelectedItem();
        Categorias cat = (Categorias) ComboBoxReport_Cat.getSelectedItem();
        if (campo.equals("")) {
            list = productos().stream()
                    .filter(p -> p.getDepartamento().equals(dpt.getDepartamento())
                    && p.getCategoria().equals(cat.getCategoria()))
                    .collect(Collectors.toList());
        } else {
            list = productos().stream()
                    .filter(p -> p.getDepartamento().equals(dpt.getDepartamento())
                    && p.getProducto().startsWith(campo) || p.getDepartamento().equals(dpt.getDepartamento())
                    && p.getCodigo().startsWith(campo) || p.getCategoria().equals(cat.getCategoria())
                    && p.getProducto().startsWith(campo) || p.getCategoria().equals(cat.getCategoria())
                    && p.getCodigo().startsWith(campo))
                    .collect(Collectors.toList());
        }
        if (0 < list.size()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getIdProducto(),
                    item.getCodigo(),
                    item.getProducto()
                };
                modelo1.addRow(registros);
            });
        }
        table1.setModel(modelo1);
        table1.setRowHeight(30);
        table1.getColumnModel().getColumn(0).setMaxWidth(0);
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private String str_mes;
    private int cantProduct, int_mes, cant;
    private Rventas info;

    public void graficarProducto(String codigo) {
        cantProduct = 0;
        cant = 0;

        //Se almacenan los datos que seran usados en el grÃ¡fico
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        String[] Meses = {"ene.", "feb.", "mar.", "apr.", "may.", "jun.",
            "jul.", "aug.", "sep.", "oct.", "nov.", "dec."};
        cantProduct = 0;

        for (int i = 0; i <= 11; i++) {
            if (codigo != null) {
                cantProduct = 0;
                int_mes = i;
                int_mes++;
                if (i <= 9) {
                    str_mes = "0" + String.valueOf(int_mes);
                } else {
                    str_mes = String.valueOf(int_mes);
                }
                ventas = ventas().stream()
                        .filter(v -> v.getCodigo().equals(codigo)
                        && v.getMes().equals(str_mes)
                        && v.getYear().equals(new Calendario().getAnyo()))
                        .collect(Collectors.toList());
                if (0 < ventas.size()) {
                    ventas.forEach(item -> {
                        //cant += item.getCantidad();
                        compras = compras().stream()
                                .filter(c -> c.getCodigo().equals(item.getCompra())
                                && c.getYear().equals(new Calendario().getAnyo()))
                                .collect(Collectors.toList());
                        cantProduct += item.getCantidad();
                        int pos = compras.size() - 1;
                        double costos = cantProduct * formato.reconstruir(compras.get(pos)
                                .getPrecio().replace("€", ""));
                        double importe = cantProduct * formato.reconstruir(item.getPrecio()
                                .replace("€", ""));
                        double ganancias = importe - costos;
                        info = new Rventas(
                                item.getCodigo(),
                                item.getDescripcion(),
                                item.getPrecio(),
                                cantProduct,
                                compras.get(pos).getPrecio(),
                                formato.decimal(ganancias) + "€"
                        );
                    });
                    datos.setValue(cantProduct, "", Meses[i]);
                } else {
                    datos.setValue(0, "", Meses[i]);
                }
            }
        }
        //Se crea el grÃ¡fico y se asignan algunas caracteristicas
        JFreeChart grafico_barras = ChartFactory.createBarChart3D("Graficas por mes del año",
                "Meses", "Ventas", datos, PlotOrientation.VERTICAL, true, true, false);
        //Se guarda el grafico
        BufferedImage image = grafico_barras.createBufferedImage(500, 300);
        //Se crea la imagen y se agrega a la etiqueta
        grafico_Product.setIcon(new ImageIcon(image));
        reportesProducto(info);
    }
    
    public void getCodigo() {
        int fila = table1.getSelectedRow();
        String codigo = (String) modelo1.getValueAt(fila, 1);
        graficarProducto(codigo);
    }

    private void reportesProducto(Rventas venta) {
        String[] titulos = {"Codigo", "Producto", "Cantidad/Vendido",
            "Precio/Compra", "Precio/Venta", "Ganancias"};
        modelo2 = new DefaultTableModel(null, titulos);
        if (venta != null) {
            Object[] registros = {
                venta.getCodigo(),
                venta.getDescripcion(),
                venta.getCantidad(),
                venta.getPrecioCompra(),
                venta.getPrecioVenta(),
                venta.getGanancias()
            };
            modelo2.addRow(registros);
        }
        table2.setModel(modelo2);
        table2.setRowHeight(30);

        String[] titulos2 = {"Código", "Producto", "Existencia"};
        modelo3 = new DefaultTableModel(null, titulos2);
        if (venta != null) {
            List<Bodegas> bodega2 = bodegas().stream()
                    .filter(b -> b.getCodigo().equals(venta.getCodigo()))
                    .collect(Collectors.toList());
            if (!bodega2.isEmpty()) {
                Object[] registros2 = {
                    bodega2.get(0).getCodigo(),
                    venta.getDescripcion(),
                    bodega2.get(0).getExistencia()
                };
                modelo3.addRow(registros2);
            }
        }
        table3.setModel(modelo3);
        table3.setRowHeight(30);
    }

}
