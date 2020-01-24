/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Clases.Calendario;
import Clases.ExportData;
import Clases.Paginador;
import Interfaces.IClassModels;
import ModelClass.Caja;
import ModelClass.Inventario;
import ModelClass.ListClass;
import ModelClass.Usuario;
import Models.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author AlexJPZ
 */
public class Sistema extends javax.swing.JFrame implements IClassModels {

    private List<Usuarios> listUsuario = new ArrayList<>();
    private List<Cajas> listCaja = new ArrayList<>();
    private List<Proveedores> dataProveedor = new ArrayList<>();
    private List<JLabel> labels = new ArrayList<>();
    private Object[] textFieldObject, labelsObject;
    private DefaultTableModel tablaModeloCLT, tablaModelReportCliente;
    private DefaultTableModel tablaModeloPRO, tablaModelReportProv;
    private DefaultTableModel tablaModeloDpto, tablaModelCat;
    private String accion = "insert", IDProvee, proveedores, saldoProveedor;
    private String pago, deudaActual, role, usuario, precioCompra;
    private int pageSize = 2, tab = 0, idProveeCompra, cantidad, numReg, idProducto;
    private int num_registro = 0, numPagi = 0, idCliente, idRegistro, funcion;
    private int idProveedor, idDpto = 0, idCat = 0, idCompra = 0, idUsuario, cajaUser;
    private Departamentos dpt;
    private Categorias cat;
    private Caja objectcaja;

    private List<JLabel> labelCajas;
    private List<JTextField> textFieldCajas;

    private List<JLabel> labelBodega;
    private List<JTextField> textFieldBodega;
    private Inventario inventario;
    private boolean value = false;

    /**
     * Creates new form Sistema
     */
    public Sistema(List<Usuarios> listUsuario, List<Cajas> listCaja) {
        initComponents();
        //Agregar la imagen al jPanel
        PanelBanner.add(p);
        timer1.start();

        if (null != listUsuario) {
            role = listUsuario.get(0).getRole();
            idUsuario = listUsuario.get(0).getIdUsuario();
            usuario = listUsuario.get(0).getUsuario();
            if (role.equals("Admin")) {
                lblUsuario.setText(listUsuario.get(0).getNombre());
                lblCaja.setText("0");
                this.listUsuario = listUsuario;
                cajaUser = 0;
            } else {
                lblUsuario.setText(listUsuario.get(0).getNombre());
                lblCaja.setText(String.valueOf(listCaja.get(0).getCaja()));
                this.listUsuario = listUsuario;
                this.listCaja = listCaja;
                cajaUser = listCaja.get(0).getCaja();
            }
        }

        //<editor-fold defaultstate="collapsed" desc="CODIGO CLIENTE">
        if (!role.equalsIgnoreCase("Admin")) {
            RadioButton_PagosCliente.setEnabled(false);
        }
        RadioButton_IngresarCliente.setSelected(true);
        RadioButton_IngresarCliente.setForeground(new Color(0, 153, 51));
        TextField_PagosCliente.setEnabled(false);
        cliente.reportesCliente(Table_ReportesCLT, idCliente);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="CODIGO PROVEEDOR">
        if (!role.equalsIgnoreCase("Admin")) {
            RadioButton_PagosPro.setEnabled(false);
        }
        RadioButton_IngresarPro.setSelected(true);
        RadioButton_IngresarPro.setForeground(new Color(0, 153, 51));
        TextField_PagosProv.setEnabled(false);
        proveedor.reportesProveedor(Table_ReportesProveedor, idProveedor);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="CODIGO CAT/DET">
        TextField_Categoria.setEnabled(false);
        RadioButton_Dpt.setSelected(true);
        RadioButton_Cat.setSelected(false);
        RadioButton_Dpt.setForeground(new Color(0, 153, 51));
        departamento.searchDepartamentos(Table_Dpt, "");
        departamento.getCategorias(Table_Cat, idDpto);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO COMPRAS">
        compra.searchProveedores(Table_ComprasProveedor, "");

        if (!role.equalsIgnoreCase("Admin")) {
            Button_Compras.setEnabled(false);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO VENTAS">
        venta.start(cajaUser, idUsuario);
        labels.add(Label_DeudaTotal);
        labels.add(Label_DeudaReciboVentas);
        labels.add(Label_DeudaTotalReciboVentas);
        labels.add(Label_NombreReciboVentas);
        labels.add(Label_DeudaAnteriorReciboVentas);
        labels.add(Label_UltimoPagoReciboVentas);
        labels.add(Label_FechaReciboVentas);
        labels.add(Label_PagoVenta);
        labels.add(LabelMensajeVentasCliente);
        labels.add(Label_SuCambio);

        Button_Ventas.setEnabled(false);
        restablecerVentas();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO CONFIGURACIONES">
        if (!role.equalsIgnoreCase("Admin")) {
            Button_Config.setEnabled(false);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO USUARIOS">
        Object[] TextFieldObject = {
            TextField_NombreUser, TextField_ApellidosUser, TextField_TelefonoUser,
            TextField_DireccionUser, TextField_EmailUser, TextField_UsuarioUser, TextField_PasswordUser
        };
        Object[] LabelsObject = {
            Label_NombreUser, Label_ApellidosUser, Label_TelefonoUser, Label_DireccionUser,
            Label_EmailUser, Label_UsuarioUser, Label_PasswordUser, Label_RolesUser,
            LabelImagenUser, Label_PaginasUsuarios
        };
        this.textFieldObject = TextFieldObject;
        this.labelsObject = LabelsObject;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO CAJAS">
        Object[] objectCajas = {TableCajasIngresos, dateChooserCajas, idUsuario,
            TableCajasCaja, SpinnerNumCaja};
        labelCajas = new ArrayList<>();
        labelCajas.add(Label_CajaIngresos);
        labelCajas.add(Label_CajaRetirarIngreso);
        labelCajas.add(Label_CajaIngresos2);
        labelCajas.add(Label_MaximoCajas); // Mensaje
        labelCajas.add(Label_NumCaja);
        labelCajas.add(Label_CajaNumero);

        textFieldCajas = new ArrayList<>();
        textFieldCajas.add(TextField_CajaRetirar);
        textFieldCajas.add(TextField_CajaIngresoInicial);
        objectcaja = new Caja(objectCajas, labelCajas, textFieldCajas);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CODIGO INVENTARIO"> 
        Object[] objectBodega = {
            TableInventario,
            SpinnerBodega,
            CheckBoxBodega,
            TabbedPaneInventario,
            TableInventarioProductos
        };

        textFieldBodega = new ArrayList<>();
        textFieldBodega.add(TextField_Existencia);
        textFieldBodega.add(TextField_PrecioInventario);
        textFieldBodega.add(TextField_DescuentoInventario);

        labelBodega = new ArrayList<>();
        labelBodega.add(LabelExistenciaInventario);
        labelBodega.add(LabelPaginasInventario);
        labelBodega.add(LabelListaProductosInventario);
        labelBodega.add(LabelPaginasInventarioPro);

        inventario = new Inventario(objectBodega, labelBodega, textFieldBodega);
        ListClass.inventario = inventario;
        //</editor-fold>
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroupProv = new javax.swing.ButtonGroup();
        PanelBanner = new javax.swing.JPanel();
        Button_Config = new javax.swing.JButton();
        Button_Compras = new javax.swing.JButton();
        Button_Cat_Dpt = new javax.swing.JButton();
        Button_Productos = new javax.swing.JButton();
        Button_Cliente = new javax.swing.JButton();
        Button_Ventas = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        TextField_BuscarProductosVentas = new javax.swing.JTextField();
        ButtonBuscarProductoVentas = new javax.swing.JButton();
        lblMensajeVenta = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        Table_VentasTempo =   Table_Clientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_AnteriorVentas = new javax.swing.JButton();
        Button_SiguienteVentas = new javax.swing.JButton();
        Button_PrimeroVentas = new javax.swing.JButton();
        Button_UltimoVentas = new javax.swing.JButton();
        Label_PaginasVentas = new javax.swing.JLabel();
        Button_FacturaVentas = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        Button_GuardarVentas = new javax.swing.JButton();
        Button_CancelarVentas = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel36 = new javax.swing.JPanel();
        CheckBoxVentas = new javax.swing.JCheckBox();
        Label_PagoVenta = new javax.swing.JLabel();
        TextField_PagoConVentas = new javax.swing.JTextField();
        Label_ApellidoCliente1 = new javax.swing.JLabel();
        Label_MontoPagarVentas = new javax.swing.JLabel();
        Label_SuCambio = new javax.swing.JLabel();
        Label_SuCambio2 = new javax.swing.JLabel();
        Label_TelefonoCliente2 = new javax.swing.JLabel();
        Label_DeudaTotal = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        Label_ApellidoCliente2 = new javax.swing.JLabel();
        Label_IngresoIniVentas = new javax.swing.JLabel();
        Label_SuCambio1 = new javax.swing.JLabel();
        Label_IngresoVentasVentas = new javax.swing.JLabel();
        Label_TelefonoCliente3 = new javax.swing.JLabel();
        Label_IngresoTotalVentas = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        Table_VentasClientes =  Table_VentasClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        TextField_BuscarCliente2 = new javax.swing.JTextField();
        LabelMensajeVentasCliente = new javax.swing.JLabel();
        PanelReciboVenta = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        Label_DeudaAnteriorReciboVentas = new javax.swing.JLabel();
        Label_NombreReciboVentas = new javax.swing.JLabel();
        Label_DeudaReciboVentas = new javax.swing.JLabel();
        Label_DeudaTotalReciboVentas = new javax.swing.JLabel();
        Label_UltimoPagoReciboVentas = new javax.swing.JLabel();
        Label_FechaReciboVentas = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        TextField_BuscarCliente = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        RadioButton_IngresarCliente = new javax.swing.JRadioButton();
        RadioButton_PagosCliente = new javax.swing.JRadioButton();
        Label_NombreCliente = new javax.swing.JLabel();
        TextField_NombreCliente = new javax.swing.JTextField();
        Label_ApellidoCliente = new javax.swing.JLabel();
        TextField_ApellidoCliente = new javax.swing.JTextField();
        Label_DireccionCliente = new javax.swing.JLabel();
        TextField_DireccionCliente = new javax.swing.JTextField();
        Label_TelefonoCliente = new javax.swing.JLabel();
        TextField_TelefonoCliente = new javax.swing.JTextField();
        Label_PagoCliente = new javax.swing.JLabel();
        TextField_PagosCliente = new javax.swing.JTextField();
        TextField_IdCliente = new javax.swing.JTextField();
        Label_IdCliente = new javax.swing.JLabel();
        Label_EmailCliente = new javax.swing.JLabel();
        TextField_EmailCliente = new javax.swing.JTextField();
        Label_PagoCliente1 = new javax.swing.JLabel();
        RadioButton_Activo = new javax.swing.JRadioButton();
        RadioButton_Inactivo = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Table_Clientes =   Table_Clientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_AnteriorCLT = new javax.swing.JButton();
        Button_SiguienteCLT = new javax.swing.JButton();
        Button_PrimeroCLT = new javax.swing.JButton();
        Button_UltimoCLT = new javax.swing.JButton();
        Label_PaginasClientes = new javax.swing.JLabel();
        Button_FacturaCliente = new javax.swing.JButton();
        Button_ActDesac = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Table_ReportesCLT = Table_ReportesCLT = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        panelReciboCliente = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        LabelDeudaRecCliente = new javax.swing.JLabel();
        LabelNomRecCliente = new javax.swing.JLabel();
        LabelApeRecCliente = new javax.swing.JLabel();
        LabelUltPagoRecCliente = new javax.swing.JLabel();
        LabelFechaRecCliente = new javax.swing.JLabel();
        Button_GuardarCliente = new javax.swing.JButton();
        Button_CancelarCLT = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        TextField_BuscarProveedor = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        RadioButton_IngresarPro = new javax.swing.JRadioButton();
        RadioButton_PagosPro = new javax.swing.JRadioButton();
        Label_ProveedorProv = new javax.swing.JLabel();
        TextField_ProveedorProv = new javax.swing.JTextField();
        Label_DireccionProv = new javax.swing.JLabel();
        TextField_DireccionProv = new javax.swing.JTextField();
        Label_TelefonoProv = new javax.swing.JLabel();
        TextField_TelefonoProv = new javax.swing.JTextField();
        Label_PagoProv = new javax.swing.JLabel();
        TextField_PagosProv = new javax.swing.JTextField();
        TextField_IdProv = new javax.swing.JTextField();
        Label_IdProv = new javax.swing.JLabel();
        Label_EmailProv = new javax.swing.JLabel();
        TextField_EmailProv = new javax.swing.JTextField();
        Label_EstadoProv = new javax.swing.JLabel();
        RadioButton_ActivoProv = new javax.swing.JRadioButton();
        RadioButton_InactivoProv = new javax.swing.JRadioButton();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        Table_Proveedores =   Table_Proveedores = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_AnteriorPRO = new javax.swing.JButton();
        Button_SiguientePRO = new javax.swing.JButton();
        Button_PrimeroPRO = new javax.swing.JButton();
        Button_UltimoPRO = new javax.swing.JButton();
        Label_PaginasProveedor = new javax.swing.JLabel();
        Button_FacturaProv = new javax.swing.JButton();
        Button_ActDesacProv = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        Table_ReportesProveedor =  Table_ReportesProveedor = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        panelReciboProv = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        LabelDeudaRecProv = new javax.swing.JLabel();
        LabelProvRecProveedor = new javax.swing.JLabel();
        LabelUltPagoRecProv = new javax.swing.JLabel();
        LabelFechaRecProv = new javax.swing.JLabel();
        Button_CancelarProv = new javax.swing.JButton();
        Button_GuardarProv = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        Label_DescripcionPDT = new javax.swing.JLabel();
        TextField_DescripcionPDT = new javax.swing.JTextField();
        Label_PrecioVentaProducto = new javax.swing.JLabel();
        TextField_PrecioVentaPDT = new javax.swing.JTextField();
        Label_DepartamentoPDT = new javax.swing.JLabel();
        Button_GuardarProducto = new javax.swing.JButton();
        Button_CancelarProducto = new javax.swing.JButton();
        Label_CategoriaPDT = new javax.swing.JLabel();
        ComboBox_DepartamentoPro = new javax.swing.JComboBox();
        ComboBox_CategoriaPro = new javax.swing.JComboBox();
        PanelCodeProducto = new javax.swing.JPanel();
        LabelProductoImagenCod = new javax.swing.JLabel();
        LabelProductoCod = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Table_ProductosCompras =   Table_ProductosCompras = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Label1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TextFieldBuscarProductos = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        Table_ProductosProd = Table_ProductosProd = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel14 = new javax.swing.JLabel();
        Label = new javax.swing.JLabel();
        Button_PrimeroProducto = new javax.swing.JButton();
        Button_AnteriorProducto = new javax.swing.JButton();
        Button_SiguienteProducto = new javax.swing.JButton();
        Button_UltimoProducto = new javax.swing.JButton();
        Label_PaginasProductos = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        RadioButton_Dpt = new javax.swing.JRadioButton();
        RadioButton_Cat = new javax.swing.JRadioButton();
        Button_GuardarCatDpt = new javax.swing.JButton();
        Button_EliminarCatDpt = new javax.swing.JButton();
        Button_CancelarCatDpt = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        Label_Cat = new javax.swing.JLabel();
        TextField_Categoria = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        Label_Dpt = new javax.swing.JLabel();
        TextField_Departamento = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        Table_Dpt =   Table_Dpt = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel20 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        TextField_BuscarDpt = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        Table_Cat =   Table_Cat = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        Button_GuardarCompras = new javax.swing.JButton();
        Button_EliminarCompras = new javax.swing.JButton();
        Button_CancelarCompras = new javax.swing.JButton();
        TabbedPaneCompras = new javax.swing.JTabbedPane();
        jPanel33 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        Label_DescripcionCP = new javax.swing.JLabel();
        TextField_DescripcionCP = new javax.swing.JTextField();
        Label_CantidadCP = new javax.swing.JLabel();
        TextField_CantidadCP = new javax.swing.JTextField();
        Label_PrecioCP = new javax.swing.JLabel();
        TextField_PrecioCP = new javax.swing.JTextField();
        Label_ImporteCP = new javax.swing.JLabel();
        Label_ImporteCP1 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        LabelCompraEncaja = new javax.swing.JLabel();
        LabelCompraMontoPagar = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        LabelCompraPago = new javax.swing.JLabel();
        TextField_ComprasPagos = new javax.swing.JTextField();
        CheckBoxCompraCredito = new javax.swing.JCheckBox();
        jLabel51 = new javax.swing.JLabel();
        LabelCompraDeuda = new javax.swing.JLabel();
        PanelReciboCompra = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        LabelCompraProveedorRecibo = new javax.swing.JLabel();
        LabelCompraTotalPagarRecibo = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        LabelCompraDeudaRecibo = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        LabelCompraSaldoRecibo = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        LabelCompraFechaRecibo = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        Table_Compras =   Table_Compras = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_AnteriorCompras = new javax.swing.JButton();
        Button_SiguienteCompras = new javax.swing.JButton();
        Button_PrimeroCompras = new javax.swing.JButton();
        Button_UltimoCompras = new javax.swing.JButton();
        Label_PaginasCompra = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        TextField_BuscarCompras = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        LabelProveedorCP = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        Table_ComprasProveedor =  Table_ComprasProveedor = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        TextField_BuscarProvCompras = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        Label_ImporteCP2 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        ButtonUsuarios = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        ButtonCajas = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        ButtonInventario = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        TextField_BuscarUsuarios = new javax.swing.JTextField();
        jPanel41 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel43 = new javax.swing.JPanel();
        Label_NombreUser = new javax.swing.JLabel();
        TextField_NombreUser = new javax.swing.JTextField();
        Label_ApellidosUser = new javax.swing.JLabel();
        TextField_ApellidosUser = new javax.swing.JTextField();
        Label_TelefonoUser = new javax.swing.JLabel();
        TextField_TelefonoUser = new javax.swing.JTextField();
        TextField_DireccionUser = new javax.swing.JTextField();
        Label_DireccionUser = new javax.swing.JLabel();
        Label_EmailUser = new javax.swing.JLabel();
        TextField_EmailUser = new javax.swing.JTextField();
        TextField_UsuarioUser = new javax.swing.JTextField();
        Label_UsuarioUser = new javax.swing.JLabel();
        Label_PasswordUser = new javax.swing.JLabel();
        TextField_PasswordUser = new javax.swing.JTextField();
        Label_RolesUser = new javax.swing.JLabel();
        ComboBoxRoles = new javax.swing.JComboBox<>();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        LabelImagenUser = new javax.swing.JLabel();
        ButtonImagenUser = new javax.swing.JButton();
        Button_GuardarUsuarios = new javax.swing.JButton();
        Button_CancelarUsuarios = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableUsuarios =  TableUsuarios = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_UltimoUsuarios = new javax.swing.JButton();
        Button_SiguienteUsuarios = new javax.swing.JButton();
        Button_AnteriorUsuarios = new javax.swing.JButton();
        Button_PrimeroUsuarios = new javax.swing.JButton();
        Label_PaginasUsuarios = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        dateChooserCajas = new datechooser.beans.DateChooserCombo();
        jPanel48 = new javax.swing.JPanel();
        TabbedPaneCaja1 = new javax.swing.JTabbedPane();
        jPanel49 = new javax.swing.JPanel();
        Label_CajaRetirarIngreso = new javax.swing.JLabel();
        TextField_CajaRetirar = new javax.swing.JTextField();
        Label_CajaIngresos = new javax.swing.JLabel();
        Label_NombreUser2 = new javax.swing.JLabel();
        Label_NombreUser3 = new javax.swing.JLabel();
        Label_CajaIngresos2 = new javax.swing.JLabel();
        Label_MaximoCajas = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        Label_NombreUser4 = new javax.swing.JLabel();
        Label_NumCaja = new javax.swing.JLabel();
        SpinnerNumCaja = new javax.swing.JSpinner();
        CheckBoxAsignarIngreso = new javax.swing.JCheckBox();
        Label_CajaNumero = new javax.swing.JLabel();
        Label_CajaRetirarIngreso2 = new javax.swing.JLabel();
        TextField_CajaIngresoInicial = new javax.swing.JTextField();
        Label_CajaIngresoInicial = new javax.swing.JLabel();
        Button_GuardarCaja = new javax.swing.JButton();
        Button_CancelarCajas = new javax.swing.JButton();
        jPanel52 = new javax.swing.JPanel();
        TabbedPaneCaja2 = new javax.swing.JTabbedPane();
        jPanel53 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableCajasIngresos =  TableCajasIngresos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel54 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        TableCajasCaja =  TableCajasCaja = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel51 = new javax.swing.JPanel();
        TabbedPaneInventario = new javax.swing.JTabbedPane();
        jPanel55 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        SpinnerBodega = new javax.swing.JSpinner();
        CheckBoxBodega = new javax.swing.JCheckBox();
        LabelExistenciaInventario = new javax.swing.JLabel();
        TextField_Existencia = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        ButtonInventarioExcel = new javax.swing.JButton();
        ButtonInventarioPDF = new javax.swing.JButton();
        jPanel58 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        TableInventario =  TableInventario = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_CancelarInventario = new javax.swing.JButton();
        Button_GuardarInventario = new javax.swing.JButton();
        Button_UltimoInventario = new javax.swing.JButton();
        Button_SiguienteInventario = new javax.swing.JButton();
        Button_AnteriorInventario = new javax.swing.JButton();
        LabelPaginasInventario = new javax.swing.JLabel();
        Button_PrimeroInventario = new javax.swing.JButton();
        jPanel56 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        LabelListaProductosInventario = new javax.swing.JLabel();
        LabelPrecioInventario = new javax.swing.JLabel();
        TextField_PrecioInventario = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        ButtonInventarioProdExcel = new javax.swing.JButton();
        ButtonInventarioProdPDF = new javax.swing.JButton();
        LabelDescuentoInventario = new javax.swing.JLabel();
        TextField_DescuentoInventario = new javax.swing.JTextField();
        jPanel61 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        TableInventarioProductos =  TableInventarioProductos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        Button_UltimoInventarioPro = new javax.swing.JButton();
        Button_SiguienteInventarioPro = new javax.swing.JButton();
        Button_AnteriorInventarioPro = new javax.swing.JButton();
        LabelPaginasInventarioPro = new javax.swing.JLabel();
        Button_PrimeroInventarioPro = new javax.swing.JButton();
        Button_CancelarInventarioProd = new javax.swing.JButton();
        Button_GuardarInventarioProd = new javax.swing.JButton();
        jPanel59 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        TextField_BuscarInventario = new javax.swing.JTextField();
        Button_Proveedores = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lblCaja = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema punto de ventas");
        setPreferredSize(new java.awt.Dimension(996, 745));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        PanelBanner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelBanner.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout PanelBannerLayout = new javax.swing.GroupLayout(PanelBanner);
        PanelBanner.setLayout(PanelBannerLayout);
        PanelBannerLayout.setHorizontalGroup(
            PanelBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PanelBannerLayout.setVerticalGroup(
            PanelBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 66, Short.MAX_VALUE)
        );

        Button_Config.setBackground(new java.awt.Color(0, 153, 153));
        Button_Config.setForeground(new java.awt.Color(255, 255, 255));
        Button_Config.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Process-Accept.png"))); // NOI18N
        Button_Config.setText(" Config");
        Button_Config.setToolTipText("");
        Button_Config.setBorder(null);
        Button_Config.setBorderPainted(false);
        Button_Config.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ConfigActionPerformed(evt);
            }
        });

        Button_Compras.setBackground(new java.awt.Color(0, 153, 153));
        Button_Compras.setForeground(new java.awt.Color(255, 255, 255));
        Button_Compras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Carrito-de-compras.png"))); // NOI18N
        Button_Compras.setText(" Compras");
        Button_Compras.setToolTipText("");
        Button_Compras.setBorder(null);
        Button_Compras.setBorderPainted(false);
        Button_Compras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ComprasActionPerformed(evt);
            }
        });

        Button_Cat_Dpt.setBackground(new java.awt.Color(0, 153, 153));
        Button_Cat_Dpt.setForeground(new java.awt.Color(255, 255, 255));
        Button_Cat_Dpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Etiqueta.png"))); // NOI18N
        Button_Cat_Dpt.setText("Cat Dpt.");
        Button_Cat_Dpt.setToolTipText("");
        Button_Cat_Dpt.setBorder(null);
        Button_Cat_Dpt.setBorderPainted(false);
        Button_Cat_Dpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_Cat_DptActionPerformed(evt);
            }
        });

        Button_Productos.setBackground(new java.awt.Color(0, 153, 153));
        Button_Productos.setForeground(new java.awt.Color(255, 255, 255));
        Button_Productos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-producto-nuevo-28.png"))); // NOI18N
        Button_Productos.setText("Productos");
        Button_Productos.setToolTipText("");
        Button_Productos.setBorder(null);
        Button_Productos.setBorderPainted(false);
        Button_Productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ProductosActionPerformed(evt);
            }
        });

        Button_Cliente.setBackground(new java.awt.Color(0, 153, 153));
        Button_Cliente.setForeground(new java.awt.Color(255, 255, 255));
        Button_Cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Clientes.png"))); // NOI18N
        Button_Cliente.setText("Clientes");
        Button_Cliente.setToolTipText("");
        Button_Cliente.setBorder(null);
        Button_Cliente.setBorderPainted(false);
        Button_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ClienteActionPerformed(evt);
            }
        });

        Button_Ventas.setBackground(new java.awt.Color(0, 153, 153));
        Button_Ventas.setForeground(new java.awt.Color(255, 255, 255));
        Button_Ventas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/compras.png"))); // NOI18N
        Button_Ventas.setText("Ventas");
        Button_Ventas.setToolTipText("");
        Button_Ventas.setBorder(null);
        Button_Ventas.setBorderPainted(false);
        Button_Ventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_VentasActionPerformed(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(70, 106, 124));
        jLabel12.setText("Ventas");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(70, 106, 124));
        jLabel15.setText("Buscar");

        TextField_BuscarProductosVentas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        ButtonBuscarProductoVentas.setBackground(new java.awt.Color(0, 153, 153));
        ButtonBuscarProductoVentas.setForeground(new java.awt.Color(255, 255, 255));
        ButtonBuscarProductoVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-encontrar-y-reemplazar-28.png"))); // NOI18N
        ButtonBuscarProductoVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonBuscarProductoVentasActionPerformed(evt);
            }
        });

        lblMensajeVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(205, 205, 205)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarProductosVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ButtonBuscarProductoVentas)
                .addGap(18, 18, 18)
                .addComponent(lblMensajeVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblMensajeVenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel15)
                            .addComponent(TextField_BuscarProductosVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButtonBuscarProductoVentas))))
                .addGap(17, 17, 17))
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_VentasTempo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_VentasTempo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_VentasTempo.setRowHeight(20);
        Table_VentasTempo.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_VentasTempo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_VentasTempoMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(Table_VentasTempo);

        Button_AnteriorVentas.setBackground(new java.awt.Color(0, 153, 153));
        Button_AnteriorVentas.setForeground(new java.awt.Color(255, 255, 255));
        Button_AnteriorVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        Button_AnteriorVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AnteriorVentasActionPerformed(evt);
            }
        });

        Button_SiguienteVentas.setBackground(new java.awt.Color(0, 153, 153));
        Button_SiguienteVentas.setForeground(new java.awt.Color(255, 255, 255));
        Button_SiguienteVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        Button_SiguienteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SiguienteVentasActionPerformed(evt);
            }
        });

        Button_PrimeroVentas.setBackground(new java.awt.Color(0, 153, 153));
        Button_PrimeroVentas.setForeground(new java.awt.Color(255, 255, 255));
        Button_PrimeroVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        Button_PrimeroVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PrimeroVentasActionPerformed(evt);
            }
        });

        Button_UltimoVentas.setBackground(new java.awt.Color(0, 153, 153));
        Button_UltimoVentas.setForeground(new java.awt.Color(255, 255, 255));
        Button_UltimoVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_UltimoVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UltimoVentasActionPerformed(evt);
            }
        });

        Label_PaginasVentas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_PaginasVentas.setForeground(new java.awt.Color(70, 106, 124));
        Label_PaginasVentas.setText("Page");

        Button_FacturaVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Imprimir.png"))); // NOI18N
        Button_FacturaVentas.setText("Factura");
        Button_FacturaVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_FacturaVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(Button_PrimeroVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Button_AnteriorVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Button_SiguienteVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Button_UltimoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Button_FacturaVentas)
                .addGap(28, 28, 28))
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addContainerGap())
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(291, 291, 291)
                .addComponent(Label_PaginasVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Label_PaginasVentas)
                .addGap(4, 4, 4)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_FacturaVentas, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_AnteriorVentas)
                        .addComponent(Button_PrimeroVentas)
                        .addComponent(Button_SiguienteVentas)
                        .addComponent(Button_UltimoVentas)))
                .addContainerGap())
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(70, 106, 124));
        jLabel16.setText("Configuración de venta");
        jLabel16.setToolTipText("");

        Button_GuardarVentas.setBackground(new java.awt.Color(0, 153, 153));
        Button_GuardarVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarVentas.setBorder(null);
        Button_GuardarVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarVentasActionPerformed(evt);
            }
        });

        Button_CancelarVentas.setBackground(new java.awt.Color(0, 153, 153));
        Button_CancelarVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarVentas.setBorder(null);
        Button_CancelarVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarVentasActionPerformed(evt);
            }
        });

        jTabbedPane2.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        CheckBoxVentas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        CheckBoxVentas.setText("Credito");
        CheckBoxVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxVentasActionPerformed(evt);
            }
        });

        Label_PagoVenta.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_PagoVenta.setForeground(new java.awt.Color(70, 106, 124));
        Label_PagoVenta.setText("Pagó con");
        Label_PagoVenta.setToolTipText("");

        TextField_PagoConVentas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_PagoConVentas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PagoConVentasKeyReleased(evt);
            }
        });

        Label_ApellidoCliente1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_ApellidoCliente1.setForeground(new java.awt.Color(70, 106, 124));
        Label_ApellidoCliente1.setText("Monto a pagar");

        Label_MontoPagarVentas.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_MontoPagarVentas.setForeground(new java.awt.Color(70, 106, 124));
        Label_MontoPagarVentas.setText("0,00€");
        Label_MontoPagarVentas.setToolTipText("");

        Label_SuCambio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_SuCambio.setForeground(new java.awt.Color(70, 106, 124));
        Label_SuCambio.setText("Su cambio");

        Label_SuCambio2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_SuCambio2.setForeground(new java.awt.Color(70, 106, 124));
        Label_SuCambio2.setText("0,00€");

        Label_TelefonoCliente2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_TelefonoCliente2.setForeground(new java.awt.Color(70, 106, 124));
        Label_TelefonoCliente2.setText("Deuda total");

        Label_DeudaTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_DeudaTotal.setForeground(new java.awt.Color(70, 106, 124));
        Label_DeudaTotal.setText("0,00€");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(Label_TelefonoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Label_ApellidoCliente1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_SuCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_MontoPagarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CheckBoxVentas)
                            .addComponent(TextField_PagoConVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_PagoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_SuCambio2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_DeudaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(CheckBoxVentas)
                .addGap(18, 18, 18)
                .addComponent(Label_PagoVenta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_PagoConVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_ApellidoCliente1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_MontoPagarVentas)
                .addGap(23, 23, 23)
                .addComponent(Label_SuCambio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_SuCambio2)
                .addGap(23, 23, 23)
                .addComponent(Label_TelefonoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_DeudaTotal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ventas", jPanel36);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Label_ApellidoCliente2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_ApellidoCliente2.setForeground(new java.awt.Color(70, 106, 124));
        Label_ApellidoCliente2.setText("Ingreso inicial");

        Label_IngresoIniVentas.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_IngresoIniVentas.setForeground(new java.awt.Color(70, 106, 124));
        Label_IngresoIniVentas.setText("0,00€");
        Label_IngresoIniVentas.setToolTipText("");

        Label_SuCambio1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_SuCambio1.setForeground(new java.awt.Color(70, 106, 124));
        Label_SuCambio1.setText("Ingreso de ventas");

        Label_IngresoVentasVentas.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_IngresoVentasVentas.setForeground(new java.awt.Color(70, 106, 124));
        Label_IngresoVentasVentas.setText("0,00€");

        Label_TelefonoCliente3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_TelefonoCliente3.setForeground(new java.awt.Color(70, 106, 124));
        Label_TelefonoCliente3.setText("Ingreso total");

        Label_IngresoTotalVentas.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_IngresoTotalVentas.setForeground(new java.awt.Color(70, 106, 124));
        Label_IngresoTotalVentas.setText("0,00€");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(Label_TelefonoCliente3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Label_ApellidoCliente2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_SuCambio1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_IngresoIniVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_IngresoVentasVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_IngresoTotalVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_ApellidoCliente2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_IngresoIniVentas)
                .addGap(23, 23, 23)
                .addComponent(Label_SuCambio1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_IngresoVentasVentas)
                .addGap(23, 23, 23)
                .addComponent(Label_TelefonoCliente3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_IngresoTotalVentas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ingresos", jPanel37);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(Button_GuardarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Button_CancelarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_GuardarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_VentasClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_VentasClientes.setRowHeight(20);
        Table_VentasClientes.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_VentasClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_VentasClientesMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(Table_VentasClientes);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(70, 106, 124));
        jLabel20.setText("Cliente");

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Clientes.png"))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setText("©PDHN");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(70, 106, 124));
        jLabel25.setText("Buscar");

        TextField_BuscarCliente2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarCliente2KeyReleased(evt);
            }
        });

        LabelMensajeVentasCliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(TextField_BuscarCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(175, 175, 175)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21)
                        .addGap(57, 57, 57)
                        .addComponent(LabelMensajeVentasCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jLabel24)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(TextField_BuscarCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel20)
                    .addComponent(LabelMensajeVentasCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addGap(5, 5, 5))
        );

        PanelReciboVenta.setBackground(new java.awt.Color(255, 255, 255));
        PanelReciboVenta.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel17.setText("Abarrotes punto de venta");

        jLabel26.setText("Nombre:");

        jLabel27.setText("Deuda:");

        jLabel28.setText("Deuda anterior:");

        jLabel29.setText("Deuda actual:");

        jLabel30.setText("Ultimo pago:");

        jLabel31.setText("Fecha:");

        Label_DeudaAnteriorReciboVentas.setText("0,00€");

        Label_NombreReciboVentas.setText("Nombre");

        Label_DeudaReciboVentas.setText("0,00€");

        Label_DeudaTotalReciboVentas.setText("0,00€");

        Label_UltimoPagoReciboVentas.setText("0,00€");

        Label_FechaReciboVentas.setText("Fecha");

        javax.swing.GroupLayout PanelReciboVentaLayout = new javax.swing.GroupLayout(PanelReciboVenta);
        PanelReciboVenta.setLayout(PanelReciboVentaLayout);
        PanelReciboVentaLayout.setHorizontalGroup(
            PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelReciboVentaLayout.createSequentialGroup()
                .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelReciboVentaLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel17))
                    .addGroup(PanelReciboVentaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_NombreReciboVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(Label_DeudaReciboVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Label_DeudaAnteriorReciboVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Label_DeudaTotalReciboVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Label_UltimoPagoReciboVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Label_FechaReciboVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        PanelReciboVentaLayout.setVerticalGroup(
            PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelReciboVentaLayout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(Label_NombreReciboVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(Label_DeudaReciboVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(Label_DeudaAnteriorReciboVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(Label_DeudaTotalReciboVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(Label_UltimoPagoReciboVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelReciboVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(Label_FechaReciboVentas))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(PanelReciboVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(PanelReciboVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ventas", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(70, 106, 124));
        jLabel1.setText("Clientes");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(70, 106, 124));
        jLabel5.setText("Buscar");

        TextField_BuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(204, 204, 204)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(TextField_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        RadioButton_IngresarCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        RadioButton_IngresarCliente.setForeground(new java.awt.Color(0, 153, 51));
        RadioButton_IngresarCliente.setText("Ingresar cliente");
        RadioButton_IngresarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_IngresarClienteActionPerformed(evt);
            }
        });

        RadioButton_PagosCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        RadioButton_PagosCliente.setForeground(new java.awt.Color(70, 106, 124));
        RadioButton_PagosCliente.setText("Pagos de cliente");
        RadioButton_PagosCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_PagosClienteActionPerformed(evt);
            }
        });

        Label_NombreCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_NombreCliente.setForeground(new java.awt.Color(70, 106, 124));
        Label_NombreCliente.setText("Nombre");
        Label_NombreCliente.setToolTipText("");

        TextField_NombreCliente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_NombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_NombreClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_NombreClienteKeyTyped(evt);
            }
        });

        Label_ApellidoCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_ApellidoCliente.setForeground(new java.awt.Color(70, 106, 124));
        Label_ApellidoCliente.setText("Apellidos");

        TextField_ApellidoCliente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_ApellidoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_ApellidoClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_ApellidoClienteKeyTyped(evt);
            }
        });

        Label_DireccionCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_DireccionCliente.setForeground(new java.awt.Color(70, 106, 124));
        Label_DireccionCliente.setText("Dirección");
        Label_DireccionCliente.setToolTipText("");

        TextField_DireccionCliente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_DireccionCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DireccionClienteKeyReleased(evt);
            }
        });

        Label_TelefonoCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_TelefonoCliente.setForeground(new java.awt.Color(70, 106, 124));
        Label_TelefonoCliente.setText("Teléfono");

        TextField_TelefonoCliente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_TelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_TelefonoClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_TelefonoClienteKeyTyped(evt);
            }
        });

        Label_PagoCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_PagoCliente.setForeground(new java.awt.Color(70, 106, 124));
        Label_PagoCliente.setText("Pagos de deudas");

        TextField_PagosCliente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_PagosCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PagosClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_PagosClienteKeyTyped(evt);
            }
        });

        TextField_IdCliente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_IdCliente.setEnabled(false);
        TextField_IdCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_IdClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_IdClienteKeyTyped(evt);
            }
        });

        Label_IdCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_IdCliente.setForeground(new java.awt.Color(70, 106, 124));
        Label_IdCliente.setText("ID");
        Label_IdCliente.setToolTipText("");

        Label_EmailCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_EmailCliente.setForeground(new java.awt.Color(70, 106, 124));
        Label_EmailCliente.setText("Email");
        Label_EmailCliente.setToolTipText("");

        TextField_EmailCliente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_EmailCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_EmailClienteKeyReleased(evt);
            }
        });

        Label_PagoCliente1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_PagoCliente1.setForeground(new java.awt.Color(70, 106, 124));
        Label_PagoCliente1.setText("Estado");

        buttonGroup1.add(RadioButton_Activo);
        RadioButton_Activo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        RadioButton_Activo.setText("Activo");

        buttonGroup1.add(RadioButton_Inactivo);
        RadioButton_Inactivo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        RadioButton_Inactivo.setText("Inactivo");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_PagoCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextField_IdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(Label_TelefonoCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_PagoCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TextField_EmailCliente, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextField_DireccionCliente, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextField_NombreCliente, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextField_ApellidoCliente, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Label_DireccionCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_EmailCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_ApellidoCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_NombreCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(RadioButton_IngresarCliente)
                        .addGap(18, 18, 18)
                        .addComponent(RadioButton_PagosCliente))
                    .addComponent(Label_IdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextField_TelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextField_PagosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(RadioButton_Activo, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RadioButton_Inactivo)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton_IngresarCliente)
                    .addComponent(RadioButton_PagosCliente))
                .addGap(26, 26, 26)
                .addComponent(Label_IdCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_IdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_NombreCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_NombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_ApellidoCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_ApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(Label_DireccionCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_DireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_EmailCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_EmailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_TelefonoCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_TelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_PagoCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_PagosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_PagoCliente1)
                .addGap(11, 11, 11)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton_Activo)
                    .addComponent(RadioButton_Inactivo))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Clientes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Clientes.setRowHeight(20);
        Table_Clientes.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Clientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ClientesMouseClicked(evt);
            }
        });
        Table_Clientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ClientesKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(Table_Clientes);

        Button_AnteriorCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        Button_AnteriorCLT.setText("Anterior ");
        Button_AnteriorCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AnteriorCLTActionPerformed(evt);
            }
        });

        Button_SiguienteCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        Button_SiguienteCLT.setText("Siguiente");
        Button_SiguienteCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SiguienteCLTActionPerformed(evt);
            }
        });

        Button_PrimeroCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        Button_PrimeroCLT.setText("Primero");
        Button_PrimeroCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PrimeroCLTActionPerformed(evt);
            }
        });

        Button_UltimoCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_UltimoCLT.setText("Ultimo");
        Button_UltimoCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UltimoCLTActionPerformed(evt);
            }
        });

        Label_PaginasClientes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_PaginasClientes.setForeground(new java.awt.Color(70, 106, 124));
        Label_PaginasClientes.setText("Page");

        Button_FacturaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Imprimir.png"))); // NOI18N
        Button_FacturaCliente.setText("Factura");
        Button_FacturaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_FacturaClienteActionPerformed(evt);
            }
        });

        Button_ActDesac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_ActDesac.setText("Activar/Desact.");
        Button_ActDesac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ActDesacActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2)
                .addGap(10, 10, 10))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(Button_PrimeroCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Button_AnteriorCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Button_SiguienteCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Button_UltimoCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_ActDesac, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Button_FacturaCliente))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(Label_PaginasClientes)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Label_PaginasClientes)
                .addGap(13, 13, 13)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_FacturaCliente)
                        .addComponent(Button_ActDesac))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_SiguienteCLT)
                        .addComponent(Button_UltimoCLT)
                        .addComponent(Button_AnteriorCLT)
                        .addComponent(Button_PrimeroCLT)))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_ReportesCLT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_ReportesCLT.setRowHeight(20);
        Table_ReportesCLT.setSelectionBackground(new java.awt.Color(102, 204, 255));
        jScrollPane3.setViewportView(Table_ReportesCLT);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(70, 106, 124));
        jLabel7.setText("Reportes");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Clientes.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("©PDHN");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3)))
                .addGap(12, 12, 12))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addContainerGap())
        );

        panelReciboCliente.setBackground(new java.awt.Color(255, 255, 255));
        panelReciboCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(70, 106, 124));
        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel77.setText("Recibo");

        jLabel78.setText("Nombre:");

        jLabel79.setText("Apellido:");

        jLabel80.setText("Deuda actual:");

        jLabel81.setText("Ultimo pago:");

        jLabel83.setText("Fecha:");

        LabelDeudaRecCliente.setText("$0.00");

        LabelNomRecCliente.setText("Nombre");

        LabelApeRecCliente.setText("Apellido");

        LabelUltPagoRecCliente.setText("$0.00");

        LabelFechaRecCliente.setText("Fecha");

        javax.swing.GroupLayout panelReciboClienteLayout = new javax.swing.GroupLayout(panelReciboCliente);
        panelReciboCliente.setLayout(panelReciboClienteLayout);
        panelReciboClienteLayout.setHorizontalGroup(
            panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReciboClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelReciboClienteLayout.createSequentialGroup()
                        .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel81, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel78, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel79, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel80, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelFechaRecCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelUltPagoRecCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelDeudaRecCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelApeRecCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelNomRecCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelReciboClienteLayout.setVerticalGroup(
            panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReciboClienteLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel77)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(LabelNomRecCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(LabelApeRecCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(LabelDeudaRecCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(LabelUltPagoRecCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelReciboClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelFechaRecCliente)
                    .addComponent(jLabel83))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Button_GuardarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarCliente.setText("Guardar");
        Button_GuardarCliente.setBorder(null);
        Button_GuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarClienteActionPerformed(evt);
            }
        });

        Button_CancelarCLT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarCLT.setText("Cancelar");
        Button_CancelarCLT.setBorder(null);
        Button_CancelarCLT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarCLTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(Button_GuardarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(Button_CancelarCLT, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelReciboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelReciboCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Button_CancelarCLT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Button_GuardarCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Clientes", jPanel2);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(70, 106, 124));
        jLabel2.setText("Proveedores");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(70, 106, 124));
        jLabel33.setText("Buscar");

        TextField_BuscarProveedor.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_BuscarProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarProveedorKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(204, 204, 204)
                .addComponent(jLabel33)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel33)
                    .addComponent(TextField_BuscarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        RadioButton_IngresarPro.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        RadioButton_IngresarPro.setForeground(new java.awt.Color(0, 153, 51));
        RadioButton_IngresarPro.setText("Ingresar proveedor");
        RadioButton_IngresarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_IngresarProActionPerformed(evt);
            }
        });

        RadioButton_PagosPro.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        RadioButton_PagosPro.setForeground(new java.awt.Color(70, 106, 124));
        RadioButton_PagosPro.setText("Pagos de proveedores");
        RadioButton_PagosPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_PagosProActionPerformed(evt);
            }
        });

        Label_ProveedorProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_ProveedorProv.setForeground(new java.awt.Color(70, 106, 124));
        Label_ProveedorProv.setText("Proveedor");
        Label_ProveedorProv.setToolTipText("");

        TextField_ProveedorProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_ProveedorProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_ProveedorProvKeyReleased(evt);
            }
        });

        Label_DireccionProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_DireccionProv.setForeground(new java.awt.Color(70, 106, 124));
        Label_DireccionProv.setText("Dirección");
        Label_DireccionProv.setToolTipText("");

        TextField_DireccionProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_DireccionProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DireccionProvKeyReleased(evt);
            }
        });

        Label_TelefonoProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_TelefonoProv.setForeground(new java.awt.Color(70, 106, 124));
        Label_TelefonoProv.setText("Teléfono");

        TextField_TelefonoProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_TelefonoProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_TelefonoProvKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_TelefonoProvKeyTyped(evt);
            }
        });

        Label_PagoProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_PagoProv.setForeground(new java.awt.Color(70, 106, 124));
        Label_PagoProv.setText("Pagos de deudas");

        TextField_PagosProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_PagosProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PagosProvKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_PagosProvKeyTyped(evt);
            }
        });

        TextField_IdProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_IdProv.setEnabled(false);

        Label_IdProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_IdProv.setForeground(new java.awt.Color(70, 106, 124));
        Label_IdProv.setText("ID");
        Label_IdProv.setToolTipText("");

        Label_EmailProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_EmailProv.setForeground(new java.awt.Color(70, 106, 124));
        Label_EmailProv.setText("Email");
        Label_EmailProv.setToolTipText("");

        TextField_EmailProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_EmailProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_EmailProvKeyReleased(evt);
            }
        });

        Label_EstadoProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_EstadoProv.setForeground(new java.awt.Color(70, 106, 124));
        Label_EstadoProv.setText("Estado");

        buttonGroupProv.add(RadioButton_ActivoProv);
        RadioButton_ActivoProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        RadioButton_ActivoProv.setSelected(true);
        RadioButton_ActivoProv.setText("Activo");

        buttonGroupProv.add(RadioButton_InactivoProv);
        RadioButton_InactivoProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        RadioButton_InactivoProv.setText("Inactivo");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_EstadoProv, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextField_IdProv, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(Label_TelefonoProv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_PagoProv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TextField_EmailProv, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextField_DireccionProv, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextField_ProveedorProv, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Label_DireccionProv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_EmailProv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_ProveedorProv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Label_IdProv, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextField_TelefonoProv, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextField_PagosProv, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(RadioButton_ActivoProv, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RadioButton_InactivoProv))
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(RadioButton_PagosPro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RadioButton_IngresarPro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(RadioButton_IngresarPro)
                .addGap(18, 18, 18)
                .addComponent(RadioButton_PagosPro)
                .addGap(18, 18, 18)
                .addComponent(Label_IdProv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_IdProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_ProveedorProv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_ProveedorProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_DireccionProv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_DireccionProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_EmailProv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_EmailProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_TelefonoProv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_TelefonoProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_PagoProv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_PagosProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_EstadoProv)
                .addGap(11, 11, 11)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton_ActivoProv)
                    .addComponent(RadioButton_InactivoProv))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Proveedores.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Proveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Proveedores.setRowHeight(20);
        Table_Proveedores.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Proveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ProveedoresMouseClicked(evt);
            }
        });
        Table_Proveedores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ProveedoresKeyReleased(evt);
            }
        });
        jScrollPane11.setViewportView(Table_Proveedores);

        Button_AnteriorPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        Button_AnteriorPRO.setText("Anterior ");
        Button_AnteriorPRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AnteriorPROActionPerformed(evt);
            }
        });

        Button_SiguientePRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        Button_SiguientePRO.setText("Siguiente");
        Button_SiguientePRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SiguientePROActionPerformed(evt);
            }
        });

        Button_PrimeroPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        Button_PrimeroPRO.setText("Primero");
        Button_PrimeroPRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PrimeroPROActionPerformed(evt);
            }
        });

        Button_UltimoPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_UltimoPRO.setText("Ultimo");
        Button_UltimoPRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UltimoPROActionPerformed(evt);
            }
        });

        Label_PaginasProveedor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_PaginasProveedor.setForeground(new java.awt.Color(70, 106, 124));
        Label_PaginasProveedor.setText("Page");

        Button_FacturaProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Imprimir.png"))); // NOI18N
        Button_FacturaProv.setText("Factura");
        Button_FacturaProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_FacturaProvActionPerformed(evt);
            }
        });

        Button_ActDesacProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_ActDesacProv.setText("Activar/Desact.");
        Button_ActDesacProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ActDesacProvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane11)
                .addGap(10, 10, 10))
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(Button_PrimeroPRO, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Button_AnteriorPRO, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Button_SiguientePRO, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Button_UltimoPRO, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Button_ActDesacProv, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_FacturaProv)
                .addContainerGap())
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(231, 231, 231)
                .addComponent(Label_PaginasProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addGap(13, 13, 13)
                .addComponent(Label_PaginasProveedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_FacturaProv)
                        .addComponent(Button_ActDesacProv))
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_SiguientePRO)
                        .addComponent(Button_UltimoPRO)
                        .addComponent(Button_AnteriorPRO)
                        .addComponent(Button_PrimeroPRO)))
                .addContainerGap())
        );

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_ReportesProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_ReportesProveedor.setRowHeight(20);
        Table_ReportesProveedor.setSelectionBackground(new java.awt.Color(102, 204, 255));
        jScrollPane12.setViewportView(Table_ReportesProveedor);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(70, 106, 124));
        jLabel38.setText("Reportes");

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Clientes.png"))); // NOI18N

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setText("©PDHN");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel39))))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane12)))
                .addGap(12, 12, 12))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel40)
                .addContainerGap())
        );

        panelReciboProv.setBackground(new java.awt.Color(255, 255, 255));
        panelReciboProv.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(70, 106, 124));
        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel82.setText("Recibo");

        jLabel84.setText("Proveedor:");

        jLabel86.setText("Deuda actual:");

        jLabel87.setText("Ultimo pago:");

        jLabel88.setText("Fecha:");

        LabelDeudaRecProv.setText("0,00€");

        LabelProvRecProveedor.setText("Nombre");

        LabelUltPagoRecProv.setText("0,00€");

        LabelFechaRecProv.setText("Fecha");

        javax.swing.GroupLayout panelReciboProvLayout = new javax.swing.GroupLayout(panelReciboProv);
        panelReciboProv.setLayout(panelReciboProvLayout);
        panelReciboProvLayout.setHorizontalGroup(
            panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReciboProvLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelReciboProvLayout.createSequentialGroup()
                        .addGroup(panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel88, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel87, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel84, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel86, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelFechaRecProv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelUltPagoRecProv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelDeudaRecProv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelProvRecProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelReciboProvLayout.setVerticalGroup(
            panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReciboProvLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel82)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(LabelProvRecProveedor))
                .addGap(18, 18, 18)
                .addGroup(panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(LabelDeudaRecProv))
                .addGap(18, 18, 18)
                .addGroup(panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(LabelUltPagoRecProv))
                .addGap(18, 18, 18)
                .addGroup(panelReciboProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelFechaRecProv)
                    .addComponent(jLabel88))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Button_CancelarProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarProv.setText("Cancelar");
        Button_CancelarProv.setBorder(null);
        Button_CancelarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarProvActionPerformed(evt);
            }
        });

        Button_GuardarProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarProv.setText("Guardar");
        Button_GuardarProv.setBorder(null);
        Button_GuardarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarProvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(Button_GuardarProv, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(Button_CancelarProv, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelReciboProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelReciboProv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Button_CancelarProv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Button_GuardarProv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Proveedores", jPanel28);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));
        jPanel11.setPreferredSize(new java.awt.Dimension(310, 352));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(70, 106, 124));
        jLabel11.setText("Llene la información del nuevo producto");
        jLabel11.setToolTipText("");

        Label_DescripcionPDT.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_DescripcionPDT.setForeground(new java.awt.Color(70, 106, 124));
        Label_DescripcionPDT.setText("Descripción");

        TextField_DescripcionPDT.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_DescripcionPDT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DescripcionPDTKeyReleased(evt);
            }
        });

        Label_PrecioVentaProducto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_PrecioVentaProducto.setForeground(new java.awt.Color(70, 106, 124));
        Label_PrecioVentaProducto.setText("Precio venta");
        Label_PrecioVentaProducto.setToolTipText("");

        TextField_PrecioVentaPDT.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_PrecioVentaPDT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PrecioVentaPDTKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_PrecioVentaPDTKeyTyped(evt);
            }
        });

        Label_DepartamentoPDT.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_DepartamentoPDT.setForeground(new java.awt.Color(70, 106, 124));
        Label_DepartamentoPDT.setText("Departamento");

        Button_GuardarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarProducto.setToolTipText("Guardar");
        Button_GuardarProducto.setBorder(null);
        Button_GuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarProductoActionPerformed(evt);
            }
        });

        Button_CancelarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarProducto.setToolTipText("Cancelar");
        Button_CancelarProducto.setBorder(null);
        Button_CancelarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarProductoActionPerformed(evt);
            }
        });

        Label_CategoriaPDT.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_CategoriaPDT.setForeground(new java.awt.Color(70, 106, 124));
        Label_CategoriaPDT.setText("Categoria");

        ComboBox_DepartamentoPro.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        ComboBox_DepartamentoPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBox_DepartamentoProActionPerformed(evt);
            }
        });

        ComboBox_CategoriaPro.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        PanelCodeProducto.setBackground(new java.awt.Color(255, 255, 255));
        PanelCodeProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        LabelProductoImagenCod.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelProductoImagenCod.setToolTipText("");

        LabelProductoCod.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelProductoCod.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelProductoCod.setText("Producto");

        javax.swing.GroupLayout PanelCodeProductoLayout = new javax.swing.GroupLayout(PanelCodeProducto);
        PanelCodeProducto.setLayout(PanelCodeProductoLayout);
        PanelCodeProductoLayout.setHorizontalGroup(
            PanelCodeProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCodeProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCodeProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelProductoCod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LabelProductoImagenCod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelCodeProductoLayout.setVerticalGroup(
            PanelCodeProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCodeProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelProductoCod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelProductoImagenCod, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_DescripcionPDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_PrecioVentaProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelCodeProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TextField_PrecioVentaPDT, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_DescripcionPDT, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(Label_CategoriaPDT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Label_DepartamentoPDT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                .addComponent(ComboBox_DepartamentoPro, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ComboBox_CategoriaPro, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Button_GuardarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Button_CancelarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelCodeProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_DescripcionPDT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_DescripcionPDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(Label_PrecioVentaProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_PrecioVentaPDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_DepartamentoPDT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBox_DepartamentoPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_CategoriaPDT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBox_CategoriaPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_GuardarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_ProductosCompras.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_ProductosCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_ProductosCompras.setRowHeight(20);
        Table_ProductosCompras.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_ProductosCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ProductosComprasMouseClicked(evt);
            }
        });
        Table_ProductosCompras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ProductosComprasKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(Table_ProductosCompras);

        Label1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label1.setForeground(new java.awt.Color(70, 106, 124));
        Label1.setText("Productos comprados");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(70, 106, 124));
        jLabel6.setText("Buscar:");

        TextFieldBuscarProductos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextFieldBuscarProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldBuscarProductosKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1134, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TextFieldBuscarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90)
                .addComponent(Label1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label1)
                    .addComponent(jLabel6)
                    .addComponent(TextFieldBuscarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_ProductosProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_ProductosProd.setRowHeight(20);
        Table_ProductosProd.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_ProductosProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ProductosProdMouseClicked(evt);
            }
        });
        Table_ProductosProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ProductosProdKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(Table_ProductosProd);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(70, 106, 124));
        jLabel14.setText("©PDHN");

        Label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label.setForeground(new java.awt.Color(70, 106, 124));
        Label.setText("Productos");

        Button_PrimeroProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        Button_PrimeroProducto.setText("Primero");
        Button_PrimeroProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PrimeroProductoActionPerformed(evt);
            }
        });

        Button_AnteriorProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        Button_AnteriorProducto.setText("Anterior ");
        Button_AnteriorProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AnteriorProductoActionPerformed(evt);
            }
        });

        Button_SiguienteProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        Button_SiguienteProducto.setText("Siguiente");
        Button_SiguienteProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SiguienteProductoActionPerformed(evt);
            }
        });

        Button_UltimoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_UltimoProducto.setText("Ultimo");
        Button_UltimoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UltimoProductoActionPerformed(evt);
            }
        });

        Label_PaginasProductos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_PaginasProductos.setForeground(new java.awt.Color(70, 106, 124));
        Label_PaginasProductos.setText("Page");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(175, 175, 175)
                                        .addComponent(jLabel14))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(Button_PrimeroProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Button_AnteriorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Button_SiguienteProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Button_UltimoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(208, 208, 208)
                                        .addComponent(Label_PaginasProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(250, 250, 250)
                                .addComponent(Label)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_PaginasProductos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_AnteriorProducto)
                    .addComponent(Button_PrimeroProducto)
                    .addComponent(Button_SiguienteProducto)
                    .addComponent(Button_UltimoProducto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(70, 106, 124));
        jLabel4.setText("Productos");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Productos", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        RadioButton_Dpt.setBackground(new java.awt.Color(255, 255, 255));
        RadioButton_Dpt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RadioButton_Dpt.setForeground(new java.awt.Color(0, 153, 51));
        RadioButton_Dpt.setText("Departamento");
        RadioButton_Dpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_DptActionPerformed(evt);
            }
        });

        RadioButton_Cat.setBackground(new java.awt.Color(255, 255, 255));
        RadioButton_Cat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RadioButton_Cat.setForeground(new java.awt.Color(70, 106, 124));
        RadioButton_Cat.setText("Categoria");
        RadioButton_Cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButton_CatActionPerformed(evt);
            }
        });

        Button_GuardarCatDpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarCatDpt.setToolTipText("Guardar");
        Button_GuardarCatDpt.setBorder(null);
        Button_GuardarCatDpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarCatDptActionPerformed(evt);
            }
        });

        Button_EliminarCatDpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eliminar.png"))); // NOI18N
        Button_EliminarCatDpt.setToolTipText("Eliminar");
        Button_EliminarCatDpt.setBorder(null);
        Button_EliminarCatDpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_EliminarCatDptActionPerformed(evt);
            }
        });

        Button_CancelarCatDpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarCatDpt.setToolTipText("Cancelar");
        Button_CancelarCatDpt.setBorder(null);
        Button_CancelarCatDpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarCatDptActionPerformed(evt);
            }
        });

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Label_Cat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Cat.setForeground(new java.awt.Color(70, 106, 124));
        Label_Cat.setText("Categoria");
        Label_Cat.setToolTipText("");

        TextField_Categoria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TextField_Categoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_CategoriaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(Label_Cat, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(TextField_Categoria))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_Cat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_Categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Label_Dpt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Dpt.setForeground(new java.awt.Color(70, 106, 124));
        Label_Dpt.setText("Departamento");
        Label_Dpt.setToolTipText("");

        TextField_Departamento.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TextField_Departamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DepartamentoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(Label_Dpt, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(TextField_Departamento))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_Dpt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_Departamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(Button_GuardarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_EliminarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_CancelarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(RadioButton_Dpt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(RadioButton_Cat, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton_Dpt)
                    .addComponent(RadioButton_Cat))
                .addGap(18, 18, 18)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_GuardarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_EliminarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarCatDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Dpt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Dpt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Dpt.setRowHeight(20);
        Table_Dpt.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Dpt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_DptMouseClicked(evt);
            }
        });
        Table_Dpt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_DptKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(Table_Dpt);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(70, 106, 124));
        jLabel22.setText("Dpt and Cat");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(70, 106, 124));
        jLabel23.setText("Buscar");

        TextField_BuscarDpt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarDptKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addGap(204, 204, 204)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarDpt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(TextField_BuscarDpt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Cat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Cat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Cat.setRowHeight(20);
        Table_Cat.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_CatMouseClicked(evt);
            }
        });
        Table_Cat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_CatKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(Table_Cat);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        jTabbedPane1.addTab("Dpt/Cat", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")));

        Button_GuardarCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarCompras.setText("Guardar");
        Button_GuardarCompras.setBorder(null);
        Button_GuardarCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_GuardarCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarComprasActionPerformed(evt);
            }
        });

        Button_EliminarCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eliminar.png"))); // NOI18N
        Button_EliminarCompras.setText("Eliminar");
        Button_EliminarCompras.setBorder(null);
        Button_EliminarCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_EliminarCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_EliminarComprasActionPerformed(evt);
            }
        });

        Button_CancelarCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarCompras.setText("Cancelar");
        Button_CancelarCompras.setBorder(null);
        Button_CancelarCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        TabbedPaneCompras.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabbedPaneComprasStateChanged(evt);
            }
        });

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(70, 106, 124));
        jLabel10.setText("Llene la información del producto");
        jLabel10.setToolTipText("");

        Label_DescripcionCP.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_DescripcionCP.setForeground(new java.awt.Color(70, 106, 124));
        Label_DescripcionCP.setText("Descripción");
        Label_DescripcionCP.setToolTipText("");

        TextField_DescripcionCP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TextField_DescripcionCP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DescripcionCPKeyReleased(evt);
            }
        });

        Label_CantidadCP.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_CantidadCP.setForeground(new java.awt.Color(70, 106, 124));
        Label_CantidadCP.setText("Cantidad");
        Label_CantidadCP.setToolTipText("");

        TextField_CantidadCP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TextField_CantidadCP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_CantidadCPKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_CantidadCPKeyTyped(evt);
            }
        });

        Label_PrecioCP.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_PrecioCP.setForeground(new java.awt.Color(70, 106, 124));
        Label_PrecioCP.setText("Precio de compra");

        TextField_PrecioCP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TextField_PrecioCP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PrecioCPKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_PrecioCPKeyTyped(evt);
            }
        });

        Label_ImporteCP.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_ImporteCP.setForeground(new java.awt.Color(70, 106, 124));
        Label_ImporteCP.setText("Importe");

        Label_ImporteCP1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_ImporteCP1.setForeground(new java.awt.Color(70, 106, 124));
        Label_ImporteCP1.setText("0,00€");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_DescripcionCP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TextField_DescripcionCP)
                    .addComponent(Label_CantidadCP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_PrecioCP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_ImporteCP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(TextField_CantidadCP, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_PrecioCP, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_ImporteCP1))
                        .addGap(0, 65, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(Label_DescripcionCP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_DescripcionCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_CantidadCP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_CantidadCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_PrecioCP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_PrecioCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_ImporteCP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Label_ImporteCP1)
                .addContainerGap(225, Short.MAX_VALUE))
        );

        TabbedPaneCompras.addTab("Productos", jPanel33);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(70, 106, 124));
        jLabel13.setText("En caja");

        LabelCompraEncaja.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        LabelCompraEncaja.setForeground(new java.awt.Color(70, 106, 124));
        LabelCompraEncaja.setText("0,00€");

        LabelCompraMontoPagar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        LabelCompraMontoPagar.setForeground(new java.awt.Color(70, 106, 124));
        LabelCompraMontoPagar.setText("0,00€");

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(70, 106, 124));
        jLabel49.setText("Monto a pagar");

        LabelCompraPago.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        LabelCompraPago.setForeground(new java.awt.Color(70, 106, 124));
        LabelCompraPago.setText("Monto a pagar");

        TextField_ComprasPagos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TextField_ComprasPagos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_ComprasPagosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_ComprasPagosKeyTyped(evt);
            }
        });

        CheckBoxCompraCredito.setBackground(new java.awt.Color(255, 255, 255));
        CheckBoxCompraCredito.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        CheckBoxCompraCredito.setForeground(new java.awt.Color(70, 106, 124));
        CheckBoxCompraCredito.setText("Credito");
        CheckBoxCompraCredito.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                CheckBoxCompraCreditoStateChanged(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(70, 106, 124));
        jLabel51.setText("Deuda");

        LabelCompraDeuda.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        LabelCompraDeuda.setForeground(new java.awt.Color(70, 106, 124));
        LabelCompraDeuda.setText("0,00€");

        PanelReciboCompra.setBackground(new java.awt.Color(255, 255, 255));
        PanelReciboCompra.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Punto de Ventas");

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setText("Proveedor:");

        LabelCompraProveedorRecibo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        LabelCompraProveedorRecibo.setText("Nombre");

        LabelCompraTotalPagarRecibo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        LabelCompraTotalPagarRecibo.setText("0,00€");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setText("Total a pagar:");

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setText("Deuda:");

        LabelCompraDeudaRecibo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        LabelCompraDeudaRecibo.setText("0,00€");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setText("Saldo:");

        LabelCompraSaldoRecibo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        LabelCompraSaldoRecibo.setText("0,00€");

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setText("Fecha:");

        LabelCompraFechaRecibo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        LabelCompraFechaRecibo.setText("--/--/----");

        javax.swing.GroupLayout PanelReciboCompraLayout = new javax.swing.GroupLayout(PanelReciboCompra);
        PanelReciboCompra.setLayout(PanelReciboCompraLayout);
        PanelReciboCompraLayout.setHorizontalGroup(
            PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelReciboCompraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelReciboCompraLayout.createSequentialGroup()
                        .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LabelCompraSaldoRecibo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCompraDeudaRecibo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCompraTotalPagarRecibo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCompraProveedorRecibo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCompraFechaRecibo, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))))
                .addContainerGap())
        );
        PanelReciboCompraLayout.setVerticalGroup(
            PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelReciboCompraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addGap(18, 18, 18)
                .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(LabelCompraProveedorRecibo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(LabelCompraTotalPagarRecibo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(LabelCompraDeudaRecibo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCompraSaldoRecibo)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelReciboCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCompraFechaRecibo)
                    .addComponent(jLabel54))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelReciboCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCompraPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelCompraEncaja, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelCompraMontoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 115, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(TextField_ComprasPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CheckBoxCompraCredito)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(LabelCompraDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 127, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCompraEncaja)
                .addGap(18, 18, 18)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCompraMontoPagar)
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(LabelCompraPago)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TextField_ComprasPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(CheckBoxCompraCredito))
                .addGap(18, 18, 18)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelCompraDeuda)
                .addGap(18, 18, 18)
                .addComponent(PanelReciboCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );

        TabbedPaneCompras.addTab("Pagos", jPanel34);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TabbedPaneCompras)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(Button_GuardarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_EliminarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_CancelarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPaneCompras)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_GuardarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_EliminarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_CancelarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_Compras.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Table_Compras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_Compras.setRowHeight(20);
        Table_Compras.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_Compras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ComprasMouseClicked(evt);
            }
        });
        Table_Compras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ComprasKeyReleased(evt);
            }
        });
        jScrollPane9.setViewportView(Table_Compras);

        Button_AnteriorCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        Button_AnteriorCompras.setText("Anterior ");
        Button_AnteriorCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_AnteriorCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AnteriorComprasActionPerformed(evt);
            }
        });

        Button_SiguienteCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        Button_SiguienteCompras.setText("Siguiente");
        Button_SiguienteCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_SiguienteCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SiguienteComprasActionPerformed(evt);
            }
        });

        Button_PrimeroCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        Button_PrimeroCompras.setText("Primero");
        Button_PrimeroCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_PrimeroCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PrimeroComprasActionPerformed(evt);
            }
        });

        Button_UltimoCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_UltimoCompras.setText("Ultimo");
        Button_UltimoCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_UltimoCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UltimoComprasActionPerformed(evt);
            }
        });

        Label_PaginasCompra.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_PaginasCompra.setForeground(new java.awt.Color(70, 106, 124));
        Label_PaginasCompra.setText("Page");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(Button_PrimeroCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_AnteriorCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_SiguienteCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_UltimoCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(302, 302, 302)
                                .addComponent(Label_PaginasCompra)))
                        .addGap(0, 543, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_PaginasCompra)
                .addGap(4, 4, 4)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_UltimoCompras, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Button_SiguienteCompras, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Button_AnteriorCompras, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Button_PrimeroCompras, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(70, 106, 124));
        jLabel18.setText("Compras");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(70, 106, 124));
        jLabel19.setText("Buscar");

        TextField_BuscarCompras.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_BuscarCompras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarComprasKeyReleased(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(70, 106, 124));
        jLabel42.setText("Proveedor:");

        LabelProveedorCP.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        LabelProveedorCP.setForeground(new java.awt.Color(70, 106, 124));
        LabelProveedorCP.setText("proveedor");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(204, 204, 204)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(jLabel42)
                .addGap(18, 18, 18)
                .addComponent(LabelProveedorCP, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(TextField_BuscarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(LabelProveedorCP))
                .addContainerGap())
        );

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Table_ComprasProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Table_ComprasProveedor.setRowHeight(20);
        Table_ComprasProveedor.setSelectionBackground(new java.awt.Color(102, 204, 255));
        Table_ComprasProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_ComprasProveedorMouseClicked(evt);
            }
        });
        Table_ComprasProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_ComprasProveedorKeyReleased(evt);
            }
        });
        jScrollPane13.setViewportView(Table_ComprasProveedor);

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(70, 106, 124));
        jLabel43.setText("Proveedor");

        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-proveedor-28.png"))); // NOI18N

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel45.setText("©anvimol");

        TextField_BuscarProvCompras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarProvComprasKeyReleased(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(70, 106, 124));
        jLabel46.setText("Buscar");

        Label_ImporteCP2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_ImporteCP2.setForeground(new java.awt.Color(70, 106, 124));
        Label_ImporteCP2.setText("0,00€");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13)
                .addGap(12, 12, 12))
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jLabel45))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel46)
                        .addGap(18, 18, 18)
                        .addComponent(TextField_BuscarProvCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel44)
                        .addGap(61, 61, 61)
                        .addComponent(Label_ImporteCP2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel43)
                        .addComponent(jLabel46)
                        .addComponent(TextField_BuscarProvCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label_ImporteCP2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel45)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Compras", jPanel5);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel38.setPreferredSize(new java.awt.Dimension(1038, 56));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(70, 106, 124));
        jLabel32.setText("Opciones");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1335, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        ButtonUsuarios.setBackground(new java.awt.Color(255, 204, 51));
        ButtonUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-usuario-40.png"))); // NOI18N
        ButtonUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUsuariosActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(70, 106, 124));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Usuarios");

        ButtonCajas.setBackground(new java.awt.Color(255, 204, 51));
        ButtonCajas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-caja-registradora-40.png"))); // NOI18N
        ButtonCajas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCajasActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(70, 106, 124));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Cajas");

        ButtonInventario.setBackground(new java.awt.Color(255, 204, 51));
        ButtonInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-análisis-de-stock-40.png"))); // NOI18N
        ButtonInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInventarioActionPerformed(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(70, 106, 124));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("Inventario");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, 1468, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(ButtonUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel34))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButtonInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(jLabel57))))
                .addContainerGap(537, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuraciones", jPanel27);

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(70, 106, 124));
        jLabel35.setText("Usuarios");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(70, 106, 124));
        jLabel36.setText("Buscar");

        TextField_BuscarUsuarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_BuscarUsuariosKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addGap(204, 204, 204)
                .addComponent(jLabel36)
                .addGap(18, 18, 18)
                .addComponent(TextField_BuscarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(TextField_BuscarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTabbedPane3.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTabbedPane3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));

        Label_NombreUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_NombreUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_NombreUser.setText("Nombre");

        TextField_NombreUser.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_NombreUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_NombreUserKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_NombreUserKeyTyped(evt);
            }
        });

        Label_ApellidosUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_ApellidosUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_ApellidosUser.setText("Apellidos");

        TextField_ApellidosUser.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_ApellidosUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_ApellidosUserKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_ApellidosUserKeyTyped(evt);
            }
        });

        Label_TelefonoUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_TelefonoUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_TelefonoUser.setText("Teléfono");

        TextField_TelefonoUser.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_TelefonoUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_TelefonoUserKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextField_TelefonoUserKeyTyped(evt);
            }
        });

        TextField_DireccionUser.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_DireccionUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_DireccionUserKeyReleased(evt);
            }
        });

        Label_DireccionUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_DireccionUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_DireccionUser.setText("Dirección");

        Label_EmailUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_EmailUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_EmailUser.setText("Email");

        TextField_EmailUser.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_EmailUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_EmailUserKeyReleased(evt);
            }
        });

        TextField_UsuarioUser.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_UsuarioUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_UsuarioUserKeyReleased(evt);
            }
        });

        Label_UsuarioUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_UsuarioUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_UsuarioUser.setText("Usuario");

        Label_PasswordUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_PasswordUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_PasswordUser.setText("Contraseña");

        TextField_PasswordUser.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        TextField_PasswordUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextField_PasswordUserKeyReleased(evt);
            }
        });

        Label_RolesUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        Label_RolesUser.setForeground(new java.awt.Color(70, 106, 124));
        Label_RolesUser.setText("Roles");

        ComboBoxRoles.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_NombreUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_ApellidosUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_TelefonoUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_DireccionUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_EmailUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_UsuarioUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_PasswordUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label_RolesUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TextField_TelefonoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_NombreUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_ApellidosUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_DireccionUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_EmailUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_UsuarioUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextField_PasswordUser, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ComboBoxRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 5, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(Label_NombreUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_NombreUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_ApellidosUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_ApellidosUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_TelefonoUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_TelefonoUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_DireccionUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_DireccionUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_EmailUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_EmailUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_UsuarioUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_UsuarioUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_PasswordUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextField_PasswordUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Label_RolesUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBoxRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Información", jPanel43);

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelImagenUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelImagenUser, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                .addContainerGap())
        );

        ButtonImagenUser.setBackground(new java.awt.Color(255, 153, 0));
        ButtonImagenUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-imagen-40.png"))); // NOI18N
        ButtonImagenUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonImagenUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ButtonImagenUser, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ButtonImagenUser, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Foto o Imagen", jPanel44);

        Button_GuardarUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
        Button_GuardarUsuarios.setBorder(null);
        Button_GuardarUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_GuardarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_GuardarUsuariosActionPerformed(evt);
            }
        });

        Button_CancelarUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
        Button_CancelarUsuarios.setBorder(null);
        Button_CancelarUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_CancelarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CancelarUsuariosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(Button_GuardarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(Button_CancelarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_CancelarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_GuardarUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));
        jPanel42.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        TableUsuarios.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TableUsuarios.setSelectionBackground(new java.awt.Color(102, 204, 255));
        TableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableUsuariosMouseClicked(evt);
            }
        });
        TableUsuarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableUsuariosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(TableUsuarios);

        Button_UltimoUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
        Button_UltimoUsuarios.setText("Ultimo");
        Button_UltimoUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_UltimoUsuariosActionPerformed(evt);
            }
        });

        Button_SiguienteUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
        Button_SiguienteUsuarios.setText("Siguiente");
        Button_SiguienteUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SiguienteUsuariosActionPerformed(evt);
            }
        });

        Button_AnteriorUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
        Button_AnteriorUsuarios.setText("Anterior ");
        Button_AnteriorUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AnteriorUsuariosActionPerformed(evt);
            }
        });

        Button_PrimeroUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
        Button_PrimeroUsuarios.setText("Primero");
        Button_PrimeroUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_PrimeroUsuariosActionPerformed(evt);
            }
        });

        Label_PaginasUsuarios.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_PaginasUsuarios.setForeground(new java.awt.Color(70, 106, 124));
        Label_PaginasUsuarios.setText("Page");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1094, Short.MAX_VALUE))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(170, 170, 170)
                                .addComponent(Button_PrimeroUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_AnteriorUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_SiguienteUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_UltimoUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(376, 376, 376)
                                .addComponent(Label_PaginasUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Label_PaginasUsuarios)
                .addGap(13, 13, 13)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_AnteriorUsuarios)
                    .addComponent(Button_PrimeroUsuarios)
                    .addComponent(Button_SiguienteUsuarios)
                    .addComponent(Button_UltimoUsuarios))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8))))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Usuarios", jPanel39);

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(70, 106, 124));
        jLabel55.setText("Cajas");

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(70, 106, 124));
        jLabel56.setText("Buscar");

        dateChooserCajas.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dateChooserCajas.setFormat(2);
    dateChooserCajas.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));
    dateChooserCajas.setLocale(new java.util.Locale("es", "CO", ""));
    dateChooserCajas.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
        public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
            dateChooserCajasOnSelectionChange(evt);
        }
    });

    javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
    jPanel47.setLayout(jPanel47Layout);
    jPanel47Layout.setHorizontalGroup(
        jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel47Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel55)
            .addGap(204, 204, 204)
            .addComponent(jLabel56)
            .addGap(18, 18, 18)
            .addComponent(dateChooserCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel47Layout.setVerticalGroup(
        jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel47Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jLabel56))
                .addComponent(dateChooserCajas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel48.setBackground(new java.awt.Color(255, 255, 255));
    jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TabbedPaneCaja1.setBackground(new java.awt.Color(255, 255, 255));
    TabbedPaneCaja1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    TabbedPaneCaja1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TabbedPaneCaja1.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPaneCaja1StateChanged(evt);
        }
    });

    jPanel49.setBackground(new java.awt.Color(255, 255, 255));

    Label_CajaRetirarIngreso.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    Label_CajaRetirarIngreso.setForeground(new java.awt.Color(70, 106, 124));
    Label_CajaRetirarIngreso.setText("Retirar ingresos");

    TextField_CajaRetirar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TextField_CajaRetirar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextField_CajaRetirarKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextField_CajaRetirarKeyTyped(evt);
        }
    });

    Label_CajaIngresos.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    Label_CajaIngresos.setForeground(new java.awt.Color(70, 106, 124));
    Label_CajaIngresos.setText("0,00€");

    Label_NombreUser2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    Label_NombreUser2.setForeground(new java.awt.Color(70, 106, 124));
    Label_NombreUser2.setText("Ingresos");

    Label_NombreUser3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    Label_NombreUser3.setForeground(new java.awt.Color(70, 106, 124));
    Label_NombreUser3.setText("Ingresos");

    Label_CajaIngresos2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    Label_CajaIngresos2.setForeground(new java.awt.Color(70, 106, 124));
    Label_CajaIngresos2.setText("0,00€");

    Label_MaximoCajas.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    Label_MaximoCajas.setForeground(new java.awt.Color(70, 106, 124));

    javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
    jPanel49.setLayout(jPanel49Layout);
    jPanel49Layout.setHorizontalGroup(
        jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel49Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Label_CajaRetirarIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_NombreUser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_NombreUser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel49Layout.createSequentialGroup()
                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextField_CajaRetirar, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Label_CajaIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Label_CajaIngresos2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 5, Short.MAX_VALUE))
                .addComponent(Label_MaximoCajas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel49Layout.setVerticalGroup(
        jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel49Layout.createSequentialGroup()
            .addGap(26, 26, 26)
            .addComponent(Label_NombreUser2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(Label_CajaIngresos)
            .addGap(18, 18, 18)
            .addComponent(Label_CajaRetirarIngreso)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TextField_CajaRetirar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(Label_NombreUser3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(Label_CajaIngresos2)
            .addGap(34, 34, 34)
            .addComponent(Label_MaximoCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(224, Short.MAX_VALUE))
    );

    TabbedPaneCaja1.addTab("Ingresos", jPanel49);

    jPanel50.setBackground(new java.awt.Color(255, 255, 255));

    Label_NombreUser4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    Label_NombreUser4.setForeground(new java.awt.Color(70, 106, 124));
    Label_NombreUser4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    Label_NombreUser4.setText("Registrar Caja");

    Label_NumCaja.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    Label_NumCaja.setForeground(new java.awt.Color(70, 106, 124));
    Label_NumCaja.setText("Número de cajas");

    SpinnerNumCaja.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    SpinnerNumCaja.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            SpinnerNumCajaStateChanged(evt);
        }
    });

    CheckBoxAsignarIngreso.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    CheckBoxAsignarIngreso.setForeground(new java.awt.Color(70, 106, 124));
    CheckBoxAsignarIngreso.setText("Asignar ingreso");
    CheckBoxAsignarIngreso.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            CheckBoxAsignarIngresoStateChanged(evt);
        }
    });

    Label_CajaNumero.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    Label_CajaNumero.setForeground(new java.awt.Color(70, 106, 124));
    Label_CajaNumero.setText("#0");

    Label_CajaRetirarIngreso2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    Label_CajaRetirarIngreso2.setForeground(new java.awt.Color(70, 106, 124));
    Label_CajaRetirarIngreso2.setText("Número de caja");

    TextField_CajaIngresoInicial.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TextField_CajaIngresoInicial.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextField_CajaIngresoInicialKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextField_CajaIngresoInicialKeyTyped(evt);
        }
    });

    Label_CajaIngresoInicial.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    Label_CajaIngresoInicial.setForeground(new java.awt.Color(70, 106, 124));
    Label_CajaIngresoInicial.setText("Ingreso inicial");

    javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
    jPanel50.setLayout(jPanel50Layout);
    jPanel50Layout.setHorizontalGroup(
        jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel50Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Label_NombreUser4, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addComponent(Label_NumCaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_CajaRetirarIngreso2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_CajaIngresoInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel50Layout.createSequentialGroup()
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(SpinnerNumCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CheckBoxAsignarIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Label_CajaNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TextField_CajaIngresoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel50Layout.setVerticalGroup(
        jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel50Layout.createSequentialGroup()
            .addGap(22, 22, 22)
            .addComponent(Label_NombreUser4)
            .addGap(26, 26, 26)
            .addComponent(Label_NumCaja)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(SpinnerNumCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(CheckBoxAsignarIngreso)
            .addGap(18, 18, 18)
            .addComponent(Label_CajaRetirarIngreso2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(Label_CajaNumero)
            .addGap(18, 18, 18)
            .addComponent(Label_CajaIngresoInicial)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TextField_CajaIngresoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(208, Short.MAX_VALUE))
    );

    TabbedPaneCaja1.addTab("Cajas", jPanel50);

    Button_GuardarCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
    Button_GuardarCaja.setToolTipText("Guardar");
    Button_GuardarCaja.setBorder(null);
    Button_GuardarCaja.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_GuardarCaja.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_GuardarCajaActionPerformed(evt);
        }
    });

    Button_CancelarCajas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
    Button_CancelarCajas.setToolTipText("Cancelar");
    Button_CancelarCajas.setBorder(null);
    Button_CancelarCajas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_CancelarCajas.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_CancelarCajasActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
    jPanel48.setLayout(jPanel48Layout);
    jPanel48Layout.setHorizontalGroup(
        jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel48Layout.createSequentialGroup()
            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel48Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(TabbedPaneCaja1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel48Layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(Button_GuardarCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(55, 55, 55)
                    .addComponent(Button_CancelarCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel48Layout.setVerticalGroup(
        jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel48Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(TabbedPaneCaja1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Button_CancelarCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_GuardarCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(21, 21, 21))
    );

    jPanel52.setBackground(new java.awt.Color(255, 255, 255));
    jPanel52.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TabbedPaneCaja2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TabbedPaneCaja2.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPaneCaja2StateChanged(evt);
        }
    });

    jPanel53.setBackground(new java.awt.Color(255, 255, 255));
    jPanel53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    TableCajasIngresos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    TableCajasIngresos.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableCajasIngresos.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableCajasIngresos.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableCajasIngresosMouseClicked(evt);
        }
    });
    TableCajasIngresos.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableCajasIngresosKeyReleased(evt);
        }
    });
    jScrollPane14.setViewportView(TableCajasIngresos);

    javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
    jPanel53.setLayout(jPanel53Layout);
    jPanel53Layout.setHorizontalGroup(
        jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 1091, Short.MAX_VALUE)
    );
    jPanel53Layout.setVerticalGroup(
        jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
    );

    TabbedPaneCaja2.addTab("Ingresos", jPanel53);

    jPanel54.setBackground(new java.awt.Color(255, 255, 255));
    jPanel54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    TableCajasCaja.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    TableCajasCaja.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableCajasCaja.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableCajasCaja.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableCajasCajaMouseClicked(evt);
        }
    });
    TableCajasCaja.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableCajasCajaKeyReleased(evt);
        }
    });
    jScrollPane15.setViewportView(TableCajasCaja);

    javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
    jPanel54.setLayout(jPanel54Layout);
    jPanel54Layout.setHorizontalGroup(
        jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 1091, Short.MAX_VALUE)
    );
    jPanel54Layout.setVerticalGroup(
        jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
    );

    TabbedPaneCaja2.addTab("Cajas", jPanel54);

    javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
    jPanel52.setLayout(jPanel52Layout);
    jPanel52Layout.setHorizontalGroup(
        jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel52Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(TabbedPaneCaja2)
            .addContainerGap())
    );
    jPanel52Layout.setVerticalGroup(
        jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel52Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(TabbedPaneCaja2)
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
    jPanel46.setLayout(jPanel46Layout);
    jPanel46Layout.setHorizontalGroup(
        jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel46Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(8, 8, 8))))
    );
    jPanel46Layout.setVerticalGroup(
        jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel46Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    jTabbedPane1.addTab("Cajas", jPanel46);

    jPanel51.setBackground(new java.awt.Color(255, 255, 255));
    jPanel51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    TabbedPaneInventario.setBackground(new java.awt.Color(255, 255, 255));
    TabbedPaneInventario.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TabbedPaneInventario.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPaneInventarioStateChanged(evt);
        }
    });

    jPanel55.setBackground(new java.awt.Color(255, 255, 255));
    jPanel55.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jPanel57.setBackground(new java.awt.Color(255, 255, 255));
    jPanel57.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    jPanel57.setPreferredSize(new java.awt.Dimension(312, 4));

    jLabel60.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    jLabel60.setForeground(new java.awt.Color(70, 106, 124));
    jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel60.setText("Productos en bodega");

    jLabel61.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    jLabel61.setForeground(new java.awt.Color(70, 106, 124));
    jLabel61.setText("Limite de existencia");

    SpinnerBodega.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

    CheckBoxBodega.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    CheckBoxBodega.setText("Productos a punto de agotarse");

    LabelExistenciaInventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    LabelExistenciaInventario.setForeground(new java.awt.Color(70, 106, 124));
    LabelExistenciaInventario.setText("Existencia");

    TextField_Existencia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TextField_Existencia.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextField_ExistenciaKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextField_ExistenciaKeyTyped(evt);
        }
    });

    jLabel62.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    jLabel62.setForeground(new java.awt.Color(70, 106, 124));
    jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel62.setText("Opciones para exportar");

    ButtonInventarioExcel.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInventarioExcel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    ButtonInventarioExcel.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInventarioExcel.setText("Excel");
    ButtonInventarioExcel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInventarioExcelActionPerformed(evt);
        }
    });

    ButtonInventarioPDF.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInventarioPDF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    ButtonInventarioPDF.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInventarioPDF.setText("PDF");
    ButtonInventarioPDF.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInventarioPDFActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
    jPanel57.setLayout(jPanel57Layout);
    jPanel57Layout.setHorizontalGroup(
        jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel57Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LabelExistenciaInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel57Layout.createSequentialGroup()
                    .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(SpinnerBodega, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CheckBoxBodega, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TextField_Existencia, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 38, Short.MAX_VALUE))
                .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
        .addGroup(jPanel57Layout.createSequentialGroup()
            .addGap(27, 27, 27)
            .addComponent(ButtonInventarioExcel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ButtonInventarioPDF)
            .addGap(52, 52, 52))
    );
    jPanel57Layout.setVerticalGroup(
        jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel57Layout.createSequentialGroup()
            .addGap(19, 19, 19)
            .addComponent(jLabel60)
            .addGap(18, 18, 18)
            .addComponent(jLabel61)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(SpinnerBodega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(CheckBoxBodega)
            .addGap(18, 18, 18)
            .addComponent(LabelExistenciaInventario)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(TextField_Existencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(30, 30, 30)
            .addComponent(jLabel62)
            .addGap(28, 28, 28)
            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ButtonInventarioExcel)
                .addComponent(ButtonInventarioPDF))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel58.setBackground(new java.awt.Color(255, 255, 255));
    jPanel58.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TableInventario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    TableInventario.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableInventario.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableInventario.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableInventarioMouseClicked(evt);
        }
    });
    TableInventario.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableInventarioKeyReleased(evt);
        }
    });
    jScrollPane16.setViewportView(TableInventario);

    javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
    jPanel58.setLayout(jPanel58Layout);
    jPanel58Layout.setHorizontalGroup(
        jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel58Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane16)
            .addContainerGap())
    );
    jPanel58Layout.setVerticalGroup(
        jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel58Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
            .addContainerGap())
    );

    Button_CancelarInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
    Button_CancelarInventario.setToolTipText("Cancelar");
    Button_CancelarInventario.setBorder(null);
    Button_CancelarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_CancelarInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_CancelarInventarioActionPerformed(evt);
        }
    });

    Button_GuardarInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
    Button_GuardarInventario.setToolTipText("Guardar");
    Button_GuardarInventario.setBorder(null);
    Button_GuardarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_GuardarInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_GuardarInventarioActionPerformed(evt);
        }
    });

    Button_UltimoInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
    Button_UltimoInventario.setText("Ultimo");
    Button_UltimoInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_UltimoInventarioActionPerformed(evt);
        }
    });

    Button_SiguienteInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
    Button_SiguienteInventario.setText("Siguiente");
    Button_SiguienteInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_SiguienteInventarioActionPerformed(evt);
        }
    });

    Button_AnteriorInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
    Button_AnteriorInventario.setText("Anterior ");
    Button_AnteriorInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_AnteriorInventarioActionPerformed(evt);
        }
    });

    LabelPaginasInventario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    LabelPaginasInventario.setForeground(new java.awt.Color(70, 106, 124));
    LabelPaginasInventario.setText("Page");

    Button_PrimeroInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
    Button_PrimeroInventario.setText("Primero");
    Button_PrimeroInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_PrimeroInventarioActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
    jPanel55.setLayout(jPanel55Layout);
    jPanel55Layout.setHorizontalGroup(
        jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel55Layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addComponent(Button_GuardarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(55, 55, 55)
            .addComponent(Button_CancelarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(195, 195, 195)
            .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel55Layout.createSequentialGroup()
                    .addComponent(Button_PrimeroInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Button_AnteriorInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Button_SiguienteInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Button_UltimoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel55Layout.createSequentialGroup()
                    .addGap(208, 208, 208)
                    .addComponent(LabelPaginasInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 513, Short.MAX_VALUE))
        .addGroup(jPanel55Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel55Layout.setVerticalGroup(
        jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel55Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel57, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addComponent(jPanel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_CancelarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_GuardarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel55Layout.createSequentialGroup()
                    .addComponent(LabelPaginasInventario)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_AnteriorInventario)
                        .addComponent(Button_PrimeroInventario)
                        .addComponent(Button_SiguienteInventario)
                        .addComponent(Button_UltimoInventario))))
            .addGap(35, 35, 35))
    );

    TabbedPaneInventario.addTab("Bodega", jPanel55);

    jPanel56.setBackground(new java.awt.Color(255, 255, 255));
    jPanel56.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jPanel60.setBackground(new java.awt.Color(255, 255, 255));
    jPanel60.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    jPanel60.setPreferredSize(new java.awt.Dimension(312, 4));

    LabelListaProductosInventario.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    LabelListaProductosInventario.setForeground(new java.awt.Color(70, 106, 124));
    LabelListaProductosInventario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    LabelListaProductosInventario.setText("Lista de Productos");

    LabelPrecioInventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    LabelPrecioInventario.setForeground(new java.awt.Color(70, 106, 124));
    LabelPrecioInventario.setText("Precio");

    TextField_PrecioInventario.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TextField_PrecioInventario.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextField_PrecioInventarioKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextField_PrecioInventarioKeyTyped(evt);
        }
    });

    jLabel65.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    jLabel65.setForeground(new java.awt.Color(70, 106, 124));
    jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel65.setText("Opciones para exportar");

    ButtonInventarioProdExcel.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInventarioProdExcel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    ButtonInventarioProdExcel.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInventarioProdExcel.setText("Excel");
    ButtonInventarioProdExcel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInventarioProdExcelActionPerformed(evt);
        }
    });

    ButtonInventarioProdPDF.setBackground(new java.awt.Color(0, 102, 153));
    ButtonInventarioProdPDF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    ButtonInventarioProdPDF.setForeground(new java.awt.Color(255, 255, 255));
    ButtonInventarioProdPDF.setText("PDF");
    ButtonInventarioProdPDF.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ButtonInventarioProdPDFActionPerformed(evt);
        }
    });

    LabelDescuentoInventario.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    LabelDescuentoInventario.setForeground(new java.awt.Color(70, 106, 124));
    LabelDescuentoInventario.setText("Descuento");

    TextField_DescuentoInventario.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TextField_DescuentoInventario.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextField_DescuentoInventarioKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            TextField_DescuentoInventarioKeyTyped(evt);
        }
    });

    javax.swing.GroupLayout jPanel60Layout = new javax.swing.GroupLayout(jPanel60);
    jPanel60.setLayout(jPanel60Layout);
    jPanel60Layout.setHorizontalGroup(
        jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel60Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(LabelListaProductosInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LabelPrecioInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel60Layout.createSequentialGroup()
                    .addComponent(TextField_PrecioInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 100, Short.MAX_VALUE))
                .addComponent(jLabel65, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
        .addGroup(jPanel60Layout.createSequentialGroup()
            .addGap(27, 27, 27)
            .addComponent(ButtonInventarioProdExcel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ButtonInventarioProdPDF)
            .addGap(52, 52, 52))
        .addGroup(jPanel60Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(LabelDescuentoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(TextField_DescuentoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel60Layout.setVerticalGroup(
        jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel60Layout.createSequentialGroup()
            .addGap(19, 19, 19)
            .addComponent(LabelListaProductosInventario)
            .addGap(31, 31, 31)
            .addComponent(LabelPrecioInventario)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(TextField_PrecioInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(LabelDescuentoInventario)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(TextField_DescuentoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(60, 60, 60)
            .addComponent(jLabel65)
            .addGap(28, 28, 28)
            .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ButtonInventarioProdExcel)
                .addComponent(ButtonInventarioProdPDF))
            .addContainerGap(184, Short.MAX_VALUE))
    );

    jPanel61.setBackground(new java.awt.Color(255, 255, 255));
    jPanel61.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    TableInventarioProductos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    TableInventarioProductos.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    TableInventarioProductos.setSelectionBackground(new java.awt.Color(102, 204, 255));
    TableInventarioProductos.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            TableInventarioProductosMouseClicked(evt);
        }
    });
    TableInventarioProductos.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TableInventarioProductosKeyReleased(evt);
        }
    });
    jScrollPane17.setViewportView(TableInventarioProductos);

    javax.swing.GroupLayout jPanel61Layout = new javax.swing.GroupLayout(jPanel61);
    jPanel61.setLayout(jPanel61Layout);
    jPanel61Layout.setHorizontalGroup(
        jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel61Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane17)
            .addContainerGap())
    );
    jPanel61Layout.setVerticalGroup(
        jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel61Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
            .addContainerGap())
    );

    Button_UltimoInventarioPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right-12.png"))); // NOI18N
    Button_UltimoInventarioPro.setText("Ultimo");
    Button_UltimoInventarioPro.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_UltimoInventarioProActionPerformed(evt);
        }
    });

    Button_SiguienteInventarioPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Right.png"))); // NOI18N
    Button_SiguienteInventarioPro.setText("Siguiente");
    Button_SiguienteInventarioPro.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_SiguienteInventarioProActionPerformed(evt);
        }
    });

    Button_AnteriorInventarioPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left.png"))); // NOI18N
    Button_AnteriorInventarioPro.setText("Anterior ");
    Button_AnteriorInventarioPro.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_AnteriorInventarioProActionPerformed(evt);
        }
    });

    LabelPaginasInventarioPro.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    LabelPaginasInventarioPro.setForeground(new java.awt.Color(70, 106, 124));
    LabelPaginasInventarioPro.setText("Page");

    Button_PrimeroInventarioPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Left-12.png"))); // NOI18N
    Button_PrimeroInventarioPro.setText("Primero");
    Button_PrimeroInventarioPro.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_PrimeroInventarioProActionPerformed(evt);
        }
    });

    Button_CancelarInventarioProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancelar.png"))); // NOI18N
    Button_CancelarInventarioProd.setToolTipText("Cancelar");
    Button_CancelarInventarioProd.setBorder(null);
    Button_CancelarInventarioProd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_CancelarInventarioProd.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_CancelarInventarioProdActionPerformed(evt);
        }
    });

    Button_GuardarInventarioProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Agregar.png"))); // NOI18N
    Button_GuardarInventarioProd.setToolTipText("Guardar");
    Button_GuardarInventarioProd.setBorder(null);
    Button_GuardarInventarioProd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    Button_GuardarInventarioProd.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_GuardarInventarioProdActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
    jPanel56.setLayout(jPanel56Layout);
    jPanel56Layout.setHorizontalGroup(
        jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel56Layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addComponent(Button_GuardarInventarioProd, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(55, 55, 55)
            .addComponent(Button_CancelarInventarioProd, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(195, 195, 195)
            .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel56Layout.createSequentialGroup()
                    .addComponent(Button_PrimeroInventarioPro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Button_AnteriorInventarioPro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Button_SiguienteInventarioPro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Button_UltimoInventarioPro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel56Layout.createSequentialGroup()
                    .addGap(208, 208, 208)
                    .addComponent(LabelPaginasInventarioPro, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 513, Short.MAX_VALUE))
        .addGroup(jPanel56Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel56Layout.setVerticalGroup(
        jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel56Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel60, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addComponent(jPanel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_CancelarInventarioProd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_GuardarInventarioProd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                    .addComponent(LabelPaginasInventarioPro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_AnteriorInventarioPro)
                        .addComponent(Button_PrimeroInventarioPro)
                        .addComponent(Button_SiguienteInventarioPro)
                        .addComponent(Button_UltimoInventarioPro))))
            .addGap(35, 35, 35))
    );

    TabbedPaneInventario.addTab("Productos", jPanel56);

    jPanel59.setBackground(new java.awt.Color(255, 255, 255));
    jPanel59.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel58.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    jLabel58.setForeground(new java.awt.Color(70, 106, 124));
    jLabel58.setText("Inventario");

    jLabel59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel59.setForeground(new java.awt.Color(70, 106, 124));
    jLabel59.setText("Buscar");

    TextField_BuscarInventario.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
    TextField_BuscarInventario.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TextField_BuscarInventarioKeyReleased(evt);
        }
    });

    javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
    jPanel59.setLayout(jPanel59Layout);
    jPanel59Layout.setHorizontalGroup(
        jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel59Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel58)
            .addGap(199, 199, 199)
            .addComponent(jLabel59)
            .addGap(18, 18, 18)
            .addComponent(TextField_BuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel59Layout.setVerticalGroup(
        jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel59Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel58)
                .addComponent(jLabel59)
                .addComponent(TextField_BuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
    jPanel51.setLayout(jPanel51Layout);
    jPanel51Layout.setHorizontalGroup(
        jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel51Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(TabbedPaneInventario)
                .addComponent(jPanel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel51Layout.setVerticalGroup(
        jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel59, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(TabbedPaneInventario)
            .addContainerGap())
    );

    jTabbedPane1.addTab("Inventario", jPanel51);

    Button_Proveedores.setBackground(new java.awt.Color(0, 153, 153));
    Button_Proveedores.setForeground(new java.awt.Color(255, 255, 255));
    Button_Proveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-proveedor-28.png"))); // NOI18N
    Button_Proveedores.setText("Proveedores");
    Button_Proveedores.setToolTipText("");
    Button_Proveedores.setBorder(null);
    Button_Proveedores.setBorderPainted(false);
    Button_Proveedores.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button_ProveedoresActionPerformed(evt);
        }
    });

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(70, 106, 124));
    jLabel3.setText("Bienvenido:");

    lblUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
    lblUsuario.setForeground(new java.awt.Color(0, 153, 255));
    lblUsuario.setText("Usuario");

    jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel41.setForeground(new java.awt.Color(70, 106, 124));
    jLabel41.setText("Caja Nº");

    lblCaja.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
    lblCaja.setForeground(new java.awt.Color(0, 153, 255));
    lblCaja.setText("0");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addComponent(Button_Ventas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Button_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Button_Proveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Button_Productos, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Button_Cat_Dpt, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Button_Compras, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Button_Config, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel41)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(lblCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
        .addComponent(PanelBanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jTabbedPane1)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(PanelBanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(Button_Ventas, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Productos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Compras, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Cat_Dpt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Config, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Button_Proveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel3)
                .addComponent(lblUsuario)
                .addComponent(jLabel41)
                .addComponent(lblCaja))
            .addGap(17, 17, 17)
            .addComponent(jTabbedPane1))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

// <editor-fold defaultstate="collapsed" desc="CÓDIGO CLIENTE">
    private void Button_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ClienteActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(false);
        Button_Proveedores.setEnabled(true);
        Button_Productos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        cliente.numeros(TextField_IdCliente);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Config.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(false);
        }
        restablecerCliente();

    }//GEN-LAST:event_Button_ClienteActionPerformed

    private void RadioButton_IngresarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_IngresarClienteActionPerformed
        RadioButton_IngresarCliente.setForeground(new Color(0, 153, 51));
        RadioButton_PagosCliente.setForeground(new Color(70, 106, 124));
        TextField_IdCliente.setEnabled(false);
        TextField_NombreCliente.setEnabled(true);
        TextField_ApellidoCliente.setEnabled(true);
        TextField_DireccionCliente.setEnabled(true);
        TextField_EmailCliente.setEnabled(true);
        TextField_TelefonoCliente.setEnabled(true);
        TextField_PagosCliente.setEnabled(false);
        RadioButton_PagosCliente.setSelected(false);
        TextField_NombreCliente.requestFocus();
    }//GEN-LAST:event_RadioButton_IngresarClienteActionPerformed

    private void RadioButton_PagosClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_PagosClienteActionPerformed
        RadioButton_IngresarCliente.setForeground(new Color(70, 106, 124));
        RadioButton_PagosCliente.setForeground(new Color(0, 153, 51));
        TextField_IdCliente.setEnabled(false);
        TextField_NombreCliente.setEnabled(false);
        TextField_ApellidoCliente.setEnabled(false);
        TextField_DireccionCliente.setEnabled(false);
        TextField_EmailCliente.setEnabled(false);
        TextField_TelefonoCliente.setEnabled(false);
        TextField_PagosCliente.setEnabled(true);
        RadioButton_IngresarCliente.setSelected(false);
        TextField_PagosCliente.requestFocus();
    }//GEN-LAST:event_RadioButton_PagosClienteActionPerformed

    private void TextField_IdClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_IdClienteKeyReleased
        if (TextField_IdCliente.getText().isEmpty()) {
            Label_IdCliente.setForeground(new Color(70, 106, 124));
        } else {
            Label_IdCliente.setText("ID");
            Label_IdCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_IdClienteKeyReleased

    private void TextField_IdClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_IdClienteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_IdClienteKeyTyped

    private void TextField_NombreClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_NombreClienteKeyReleased
        if (TextField_NombreCliente.getText().isEmpty()) {
            Label_NombreCliente.setForeground(new Color(70, 106, 124));
        } else {
            Label_NombreCliente.setText("Nombre");
            Label_NombreCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_NombreClienteKeyReleased

    private void TextField_NombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_NombreClienteKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_NombreClienteKeyTyped

    private void TextField_ApellidoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ApellidoClienteKeyReleased
        if (TextField_ApellidoCliente.getText().isEmpty()) {
            Label_ApellidoCliente.setForeground(new Color(70, 106, 124));
        } else {
            Label_ApellidoCliente.setText("Apellidos");
            Label_ApellidoCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_ApellidoClienteKeyReleased

    private void TextField_ApellidoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ApellidoClienteKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_ApellidoClienteKeyTyped

    private void TextField_DireccionClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DireccionClienteKeyReleased
        if (TextField_DireccionCliente.getText().isEmpty()) {
            Label_DireccionCliente.setForeground(new Color(70, 106, 124));
        } else {
            Label_DireccionCliente.setText("Dirección");
            Label_DireccionCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_DireccionClienteKeyReleased

    private void TextField_EmailClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_EmailClienteKeyReleased
        if (TextField_EmailCliente.getText().isEmpty()) {
            Label_EmailCliente.setForeground(new Color(70, 106, 124));
        } else {
            Label_EmailCliente.setText("Email");
            Label_EmailCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_EmailClienteKeyReleased

    private void TextField_TelefonoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoClienteKeyReleased
        if (TextField_TelefonoCliente.getText().isEmpty()) {
            Label_TelefonoCliente.setForeground(new Color(70, 106, 124));
        } else {
            Label_TelefonoCliente.setText("Teléfono");
            Label_TelefonoCliente.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_TelefonoClienteKeyReleased

    private void TextField_TelefonoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoClienteKeyTyped
        evento.numberKeyPress(evt);
    }//GEN-LAST:event_TextField_TelefonoClienteKeyTyped

    private void TextField_PagosClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagosClienteKeyReleased
        if (Table_ReportesCLT.getRowCount() == 0) {
            Label_PagoCliente.setText("Seleccione un cliente");
            Label_PagoCliente.setForeground(Color.RED);
        } else {
            if (!TextField_PagosCliente.getText().isEmpty()) {
                Label_PagoCliente.setText("Pago de deudas");
                Label_PagoCliente.setForeground(new Color(0, 153, 51));
                String deuda1;
                double deuda2, deuda3, deudaTotal;
                deuda1 = (String) tablaModelReportCliente.getValueAt(0, 3);
                deuda2 = formato.reconstruir(deuda1.replace("€", ""));
                deuda3 = Double.parseDouble(TextField_PagosCliente.getText());
                pago = formato.decimal(deuda3);
                deudaTotal = deuda2 - deuda3;
                deudaActual = formato.decimal(deudaTotal);
            }
        }
    }//GEN-LAST:event_TextField_PagosClienteKeyReleased

    private void TextField_PagosClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagosClienteKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_PagosCliente);
    }//GEN-LAST:event_TextField_PagosClienteKeyTyped

    private void guardarCliente() {
        if (validarDatos()) {
            String ID = TextField_IdCliente.getText();
            String nombre = TextField_NombreCliente.getText();
            String apellidos = TextField_ApellidoCliente.getText();
            String direccion = TextField_DireccionCliente.getText();
            String email = TextField_EmailCliente.getText();
            String telefono = TextField_TelefonoCliente.getText();
            String estado = "";
            if (RadioButton_Activo.isSelected()) {
                estado = "Activo";
            } else {
                estado = "Inactivo";
            }
            boolean valor;
            if (accion.equals("insert")) {
                valor = cliente.insertCliente(ID, nombre, apellidos, direccion, email, telefono, estado);
                if (valor) {
                    restablecerCliente();
                } else {
                    Label_IdCliente.setText("La ID ya esta registrada");
                    Label_IdCliente.setForeground(Color.RED);
                    TextField_IdCliente.requestFocus();
                }
            }
            if (role.equalsIgnoreCase("Admin")) {
                if (accion.equals("update")) {
                    valor = cliente.updateCliente(ID, nombre, apellidos, direccion, email,
                            telefono, estado, idCliente);
                    if (valor) {
                        restablecerCliente();
                    } else {
                        Label_IdCliente.setText("La ID ya esta registrada");
                        Label_IdCliente.setForeground(Color.RED);
                        TextField_IdCliente.requestFocus();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
            }
        }
    }

    private void guardarReporte() {
        if (TextField_PagosCliente.getText().isEmpty()) {
            Label_PagoCliente.setText("Ingrese el pago");
            Label_PagoCliente.setForeground(Color.RED);
            TextField_PagosCliente.requestFocus();
        } else {
            cliente.updateReportes(deudaActual, new Calendario().getFecha(), pago, idCliente);
            restablecerCliente();
        }
    }

    private void Button_GuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarClienteActionPerformed
        if (RadioButton_IngresarCliente.isSelected()) {
            guardarCliente();
        } else {
            guardarReporte();
        }

    }//GEN-LAST:event_Button_GuardarClienteActionPerformed

    private void Button_PrimeroCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 1).primero();
        crearTabla();
    }//GEN-LAST:event_Button_PrimeroCLTActionPerformed

    private void Button_AnteriorCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 0).anterior();
        crearTabla();
    }//GEN-LAST:event_Button_AnteriorCLTActionPerformed

    private void Button_SiguienteCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 0).siguiente();
        crearTabla();
    }//GEN-LAST:event_Button_SiguienteCLTActionPerformed

    private void Button_UltimoCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoCLTActionPerformed
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 0).ultimo();
        crearTabla();
    }//GEN-LAST:event_Button_UltimoCLTActionPerformed

    private void Button_CancelarCLTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarCLTActionPerformed
        restablecerCliente();
    }//GEN-LAST:event_Button_CancelarCLTActionPerformed

    private void Table_ClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ClientesKeyReleased
        if (Table_Clientes.getSelectedRows().length > 0) {
            datosClientes();
        }
    }//GEN-LAST:event_Table_ClientesKeyReleased

    private void Table_ClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ClientesMouseClicked
        if (Table_Clientes.getSelectedRows().length > 0) {
            datosClientes();
        }
    }//GEN-LAST:event_Table_ClientesMouseClicked

    private void Button_ActDesacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ActDesacActionPerformed
        cliente.activarCliente(RadioButton_Activo, idCliente);
        restablecerCliente();
    }//GEN-LAST:event_Button_ActDesacActionPerformed

    private void TextField_BuscarClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarClienteKeyReleased
        cliente.searchClientes(Table_Clientes, TextField_BuscarCliente.getText(),
                numPagi, pageSize);
        crearTabla();
    }//GEN-LAST:event_TextField_BuscarClienteKeyReleased

    private void Button_FacturaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_FacturaClienteActionPerformed
        imprimir.imprimirRecibo(panelReciboCliente);
    }//GEN-LAST:event_Button_FacturaClienteActionPerformed

    private void datosClientes() {
        accion = "update";
        tablaModeloCLT = cliente.getModelo();
        int fila = Table_Clientes.getSelectedRow();
        idCliente = Integer.valueOf((String) tablaModeloCLT.getValueAt(fila, 0));
        TextField_IdCliente.setText((String) tablaModeloCLT.getValueAt(fila, 1));
        TextField_NombreCliente.setText((String) tablaModeloCLT.getValueAt(fila, 2));
        TextField_ApellidoCliente.setText((String) tablaModeloCLT.getValueAt(fila, 3));
        TextField_DireccionCliente.setText((String) tablaModeloCLT.getValueAt(fila, 4));
        TextField_EmailCliente.setText((String) tablaModeloCLT.getValueAt(fila, 5));
        TextField_TelefonoCliente.setText((String) tablaModeloCLT.getValueAt(fila, 6));
        if ((Boolean) tablaModeloCLT.getValueAt(fila, 7).equals("Activo")) {
            RadioButton_Activo.setSelected(true);
        } else {
            RadioButton_Inactivo.setSelected(true);
        }

        tablaModelReportCliente = cliente.reportesCliente(Table_ReportesCLT, idCliente);
        idRegistro = Integer.valueOf((String) tablaModelReportCliente.getValueAt(0, 0));
        LabelNomRecCliente.setText((String) tablaModelReportCliente.getValueAt(0, 1));
        LabelApeRecCliente.setText((String) tablaModelReportCliente.getValueAt(0, 2));
        LabelDeudaRecCliente.setText((String) tablaModelReportCliente.getValueAt(0, 3));
        LabelUltPagoRecCliente.setText((String) tablaModelReportCliente.getValueAt(0, 5));
        LabelFechaRecCliente.setText((String) tablaModelReportCliente.getValueAt(0, 6));
    }

    private void restablecerCliente() {
        accion = "insert";
        tab = 1;
        idCliente = 0;
        idRegistro = 0;
        TextField_NombreCliente.setText("");
        TextField_ApellidoCliente.setText("");
        TextField_DireccionCliente.setText("");
        TextField_EmailCliente.setText("");
        TextField_TelefonoCliente.setText("");
        TextField_PagosCliente.setText("");
        TextField_NombreCliente.setEnabled(true);
        TextField_ApellidoCliente.setEnabled(true);
        TextField_DireccionCliente.setEnabled(true);
        TextField_EmailCliente.setEnabled(true);
        TextField_TelefonoCliente.setEnabled(true);
        TextField_PagosCliente.setEnabled(false);
        Label_IdCliente.setForeground(new Color(70, 106, 124));
        Label_IdCliente.setText("ID");
        Label_NombreCliente.setForeground(new Color(70, 106, 124));
        Label_NombreCliente.setText("Nombre");
        Label_ApellidoCliente.setForeground(new Color(70, 106, 124));
        Label_ApellidoCliente.setText("Apellidos");
        Label_DireccionCliente.setForeground(new Color(70, 106, 124));
        Label_DireccionCliente.setText("Dirección");
        Label_EmailCliente.setForeground(new Color(70, 106, 124));
        Label_EmailCliente.setText("Email");
        Label_TelefonoCliente.setForeground(new Color(70, 106, 124));
        Label_TelefonoCliente.setText("Teléfono");
        Label_PagoCliente.setForeground(new Color(70, 106, 124));
        Label_PagoCliente.setText("Pago de deudas");
        RadioButton_IngresarCliente.setSelected(true);
        RadioButton_PagosCliente.setSelected(false);
        TextField_NombreCliente.requestFocus();
        RadioButton_IngresarCliente.setForeground(new Color(0, 153, 51));
        RadioButton_PagosCliente.setForeground(new Color(70, 106, 124));
        new Paginador(tab, Table_Clientes, Label_PaginasClientes, 1);
        cliente.numeros(TextField_IdCliente);
        cliente.reportesCliente(Table_ReportesCLT, idCliente);
        LabelNomRecCliente.setText("Nombre");
        LabelApeRecCliente.setText("Apellidos");
        LabelDeudaRecCliente.setText("0,00€");
        LabelUltPagoRecCliente.setText("0,00€");
        LabelFechaRecCliente.setText("Fecha");
        crearTabla();
    }

    private boolean validarDatos() {
        if (TextField_NombreCliente.getText().isEmpty()) {
            Label_NombreCliente.setText("Ingrese el Nombre");
            Label_NombreCliente.setForeground(Color.RED);
            TextField_NombreCliente.requestFocus();
            return false;
        } else if (TextField_ApellidoCliente.getText().isEmpty()) {
            Label_ApellidoCliente.setText("Ingrese los apellidos");
            Label_ApellidoCliente.setForeground(Color.RED);
            TextField_ApellidoCliente.requestFocus();
            return false;
        } else if (TextField_DireccionCliente.getText().isEmpty()) {
            Label_DireccionCliente.setText("Ingrese la Dirección");
            Label_DireccionCliente.setForeground(Color.RED);
            TextField_DireccionCliente.requestFocus();
            return false;
        } else if (TextField_EmailCliente.getText().isEmpty()) {
            Label_EmailCliente.setText("Ingrese el Email");
            Label_EmailCliente.setForeground(Color.RED);
            TextField_EmailCliente.requestFocus();
            return false;
        } else if (!evento.isEmail(TextField_EmailCliente.getText())) {
            Label_EmailCliente.setText("Ingrese un Email valido");
            Label_EmailCliente.setForeground(Color.RED);
            TextField_EmailCliente.requestFocus();
            return false;
        } else if (TextField_TelefonoCliente.getText().isEmpty()) {
            Label_TelefonoCliente.setText("Ingrese el Teléfono");
            Label_TelefonoCliente.setForeground(Color.RED);
            TextField_TelefonoCliente.requestFocus();
            return false;
        }
        return true;
    }

    private void crearTabla() {
        //--------------------PRESENTACION DE JTABLE----------------------

        TableCellRenderer render = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //Determinar Alineaciones   
                if (column == 1 || column == 6) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    l.setHorizontalAlignment(SwingConstants.LEFT);
                }

                //Colores en Jtable        
                if (isSelected) {
                    //l.setBackground(new Color(203, 159, 41));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setBackground(new Color(153, 204, 255));
                    l.setForeground(Color.WHITE);
                    if (column == 7) {
                        String valor = (String) value;
                        if (valor.equals("Activo")) {
                            l.setBackground(Color.GREEN);
                            l.setForeground(Color.WHITE);
                        } else {
                            l.setBackground(Color.RED);
                            l.setForeground(Color.WHITE);
                        }
                    }
                } else {
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        l.setBackground(new Color(232, 232, 232));
                        //l.setBackground(new Color(254, 227, 152));
                    }
                    if (column == 7) {
                        String valor = (String) value;
                        if (valor.equals("Activo")) {
                            l.setBackground(Color.GREEN);
                            l.setForeground(Color.WHITE);
                        } else {
                            l.setBackground(Color.RED);
                            l.setForeground(Color.WHITE);
                        }
                    }
                }
                return l;
            }
        };
        //Agregar Render
        for (int i = 0; i < Table_Clientes.getColumnCount(); i++) {
            Table_Clientes.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        Table_Clientes.setAutoResizeMode(Table_Clientes.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {10, 150, 250, 250, 300, 300, 150, 150};
        for (int i = 0; i < Table_Clientes.getColumnCount(); i++) {
            Table_Clientes.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //Altos de cada fila
        Table_Clientes.setRowHeight(30);

        ocultarColumnas();
    }

    private void ocultarColumnas() {
        Table_Clientes.getColumnModel().getColumn(0).setMaxWidth(0);
        Table_Clientes.getColumnModel().getColumn(0).setMinWidth(0);
        Table_Clientes.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="CÓDIGO PROVEEDOR">    
    private void Button_ProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ProveedoresActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Proveedores.setEnabled(false);
        Button_Productos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        proveedor.numeros(TextField_IdProv);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Config.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(false);
        }
        restablecerProve();

    }//GEN-LAST:event_Button_ProveedoresActionPerformed

    private void TextField_ProveedorProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ProveedorProvKeyReleased
        if (TextField_ProveedorProv.getText().isEmpty()) {
            Label_ProveedorProv.setForeground(new Color(70, 106, 124));
        } else {
            Label_ProveedorProv.setText("Proveedor");
            Label_ProveedorProv.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_ProveedorProvKeyReleased

    private void TextField_DireccionProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DireccionProvKeyReleased
        if (TextField_DireccionProv.getText().isEmpty()) {
            Label_DireccionProv.setForeground(new Color(70, 106, 124));
        } else {
            Label_DireccionProv.setText("Dirección");
            Label_DireccionProv.setForeground(new Color(0, 153, 51));

        }
    }//GEN-LAST:event_TextField_DireccionProvKeyReleased

    private void TextField_EmailProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_EmailProvKeyReleased
        if (TextField_EmailProv.getText().isEmpty()) {
            Label_EmailProv.setForeground(new Color(70, 106, 124));
        } else {
            Label_EmailProv.setText("Email");
            Label_EmailProv.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_EmailProvKeyReleased

    private void TextField_TelefonoProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoProvKeyReleased
        if (TextField_TelefonoProv.getText().isEmpty()) {
            Label_TelefonoProv.setForeground(new Color(70, 106, 124));
        } else {
            Label_TelefonoProv.setText("Teléfono");
            Label_TelefonoProv.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_TelefonoProvKeyReleased

    private void TextField_TelefonoProvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoProvKeyTyped
        evento.numberKeyPress(evt);
    }//GEN-LAST:event_TextField_TelefonoProvKeyTyped

    private void TextField_PagosProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagosProvKeyReleased
        if (Table_ReportesProveedor.getRowCount() == 0) {
            Label_PagoProv.setText("Seleccione un proveedor");
            Label_PagoProv.setForeground(Color.RED);
        } else {
            if (!TextField_PagosProv.getText().isEmpty()) {
                Label_PagoProv.setText("Pago de deudas");
                Label_PagoProv.setForeground(new Color(0, 153, 51));
                String deuda1;
                double deuda2, deuda3, deudaTotal;
                deuda1 = (String) tablaModelReportProv.getValueAt(0, 2);
                deuda2 = formato.reconstruir(deuda1.replace("€", ""));
                deuda3 = formato.reconstruir(TextField_PagosProv.getText());
                pago = formato.decimal(deuda3);
                deudaTotal = deuda2 - deuda3;
                deudaActual = formato.decimal(deudaTotal);
            }
        }
    }//GEN-LAST:event_TextField_PagosProvKeyReleased

    private void TextField_PagosProvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagosProvKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_PagosProv);
    }//GEN-LAST:event_TextField_PagosProvKeyTyped

    private void datosProveedores() {
        accion = "update";
        tablaModeloPRO = proveedor.getModelo();
        int fila = Table_Proveedores.getSelectedRow();
        idProveedor = Integer.valueOf((String) tablaModeloPRO.getValueAt(fila, 0));
        TextField_IdProv.setText((String) tablaModeloPRO.getValueAt(fila, 1));
        TextField_ProveedorProv.setText((String) tablaModeloPRO.getValueAt(fila, 2));
        TextField_DireccionProv.setText((String) tablaModeloPRO.getValueAt(fila, 3));
        TextField_EmailProv.setText((String) tablaModeloPRO.getValueAt(fila, 4));
        TextField_TelefonoProv.setText((String) tablaModeloPRO.getValueAt(fila, 5));
        if ((Boolean) tablaModeloPRO.getValueAt(fila, 6).equals("Activo")) {
            RadioButton_ActivoProv.setSelected(true);
        } else {
            RadioButton_InactivoProv.setSelected(true);
        }

        tablaModelReportProv = proveedor.reportesProveedor(Table_ReportesProveedor, idProveedor);
        idRegistro = Integer.valueOf((String) tablaModelReportProv.getValueAt(0, 0));
        LabelProvRecProveedor.setText((String) tablaModelReportProv.getValueAt(0, 1));
        LabelDeudaRecProv.setText((String) tablaModelReportProv.getValueAt(0, 2));
        LabelUltPagoRecProv.setText((String) tablaModelReportProv.getValueAt(0, 4));
        LabelFechaRecProv.setText((String) tablaModelReportProv.getValueAt(0, 5));

        Label_IdProv.setForeground(new Color(0, 153, 51));
        Label_ProveedorProv.setForeground(new Color(0, 153, 51));
        Label_DireccionProv.setForeground(new Color(0, 153, 51));
        Label_EmailProv.setForeground(new Color(0, 153, 51));
        Label_TelefonoProv.setForeground(new Color(0, 153, 51));
        Label_EstadoProv.setForeground(new Color(0, 153, 51));
    }

    private void guardarProveedor() {
        if (validarDatosPro()) {
            String ID = TextField_IdProv.getText();
            String provee = TextField_ProveedorProv.getText();
            String direccion = TextField_DireccionProv.getText();
            String email = TextField_EmailProv.getText();
            String telefono = TextField_TelefonoProv.getText();
            String estado = "";
            if (RadioButton_ActivoProv.isSelected()) {
                estado = "Activo";
            } else {
                estado = "Inactivo";
            }
            boolean valor;
            switch (accion) {
                case "insert":
                    dataProveedor = proveedor.insertProveedor(ID, provee, direccion,
                            email, telefono, estado);
                    if (dataProveedor.isEmpty()) {
                        restablecerProve();
                    } else {
                        if (dataProveedor.get(0).getTelefono().equals(telefono)) {
                            Label_TelefonoProv.setText("El Teléfono ya esta registrado");
                            Label_TelefonoProv.setForeground(Color.RED);
                            TextField_TelefonoProv.requestFocus();
                        }
                        if (dataProveedor.get(0).getEmail().equals(email)) {
                            Label_EmailProv.setText("Este Email ya esta registrado");
                            Label_EmailProv.setForeground(Color.RED);
                            TextField_EmailProv.requestFocus();
                        }
                    }
                    break;
                case "update":
                    if (role.equalsIgnoreCase("Admin")) {
                        dataProveedor = proveedor.updateProveedor(idProveedor, ID,
                                provee, direccion, email, telefono, estado);
                        if (dataProveedor.isEmpty()) {
                            restablecerProve();
                        } else {
                            if (idProveedor != dataProveedor.get(0).getIdProveedor()) {
                                Label_TelefonoProv.setText("El Teléfono ya esta registrado");
                                Label_TelefonoProv.setForeground(Color.RED);
                                TextField_TelefonoProv.requestFocus();
                            }
                            if (2 == dataProveedor.size()
                                    && idProveedor != dataProveedor.get(1).getIdProveedor()) {
                                Label_EmailProv.setText("Este Email ya esta registrado");
                                Label_EmailProv.setForeground(Color.RED);
                                TextField_EmailProv.requestFocus();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
                    }
                    break;
            }
        }
    }

    private void guardarReportProve() {
        if (TextField_PagosProv.getText().isEmpty()) {
            Label_PagoProv.setText("Ingrese el Pago");
            Label_PagoProv.setForeground(Color.RED);
            TextField_PagosProv.requestFocus();
        } else {
            proveedor.updateReporProve(deudaActual, new Calendario().getFecha(), pago, idProveedor);
            restablecerProve();
        }
    }

    private boolean validarDatosPro() {
        if (TextField_ProveedorProv.getText().isEmpty()) {
            Label_ProveedorProv.setText("Ingrese el Proveedor");
            Label_ProveedorProv.setForeground(Color.RED);
            TextField_ProveedorProv.requestFocus();
            return false;
        } else if (TextField_DireccionProv.getText().isEmpty()) {
            Label_DireccionProv.setText("Ingrese la Dirección");
            Label_DireccionProv.setForeground(Color.RED);
            TextField_DireccionProv.requestFocus();
            return false;
        } else if (TextField_EmailProv.getText().isEmpty()) {
            Label_EmailProv.setText("Ingrese el Email");
            Label_EmailProv.setForeground(Color.RED);
            TextField_EmailProv.requestFocus();
            return false;
        } else if (!evento.isEmail(TextField_EmailProv.getText())) {
            Label_EmailProv.setText("Ingrese un Email valido");
            Label_EmailProv.setForeground(Color.RED);
            TextField_EmailProv.requestFocus();
            return false;
        } else if (TextField_TelefonoProv.getText().isEmpty()) {
            Label_TelefonoProv.setText("Ingrese el Teléfono");
            Label_TelefonoProv.setForeground(Color.RED);
            TextField_TelefonoProv.requestFocus();
            return false;
        }
        return true;
    }

    private void Button_GuardarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarProvActionPerformed
        if (RadioButton_IngresarPro.isSelected()) {
            guardarProveedor();
        } else {
            guardarReportProve();
        }

    }//GEN-LAST:event_Button_GuardarProvActionPerformed

    private void RadioButton_IngresarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_IngresarProActionPerformed
        RadioButton_IngresarPro.setForeground(new Color(0, 153, 51));
        RadioButton_PagosPro.setForeground(new Color(70, 106, 124));
        TextField_ProveedorProv.setEnabled(true);
        TextField_DireccionProv.setEnabled(true);
        TextField_EmailProv.setEnabled(true);
        TextField_TelefonoProv.setEnabled(true);
        TextField_PagosProv.setEnabled(false);
        RadioButton_PagosPro.setSelected(false);
        TextField_ProveedorProv.requestFocus();
    }//GEN-LAST:event_RadioButton_IngresarProActionPerformed

    private void RadioButton_PagosProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_PagosProActionPerformed
        RadioButton_IngresarPro.setForeground(new Color(70, 106, 124));
        RadioButton_PagosPro.setForeground(new Color(0, 153, 51));
        TextField_ProveedorProv.setEnabled(false);
        TextField_DireccionProv.setEnabled(false);
        TextField_EmailProv.setEnabled(false);
        TextField_TelefonoProv.setEnabled(false);
        TextField_PagosProv.setEnabled(true);
        RadioButton_IngresarPro.setSelected(false);
        TextField_PagosProv.requestFocus();
    }//GEN-LAST:event_RadioButton_PagosProActionPerformed

    private void restablecerProve() {
        tab = 2;
        accion = "insert";
        idProveedor = 0;
        idRegistro = 0;
        TextField_ProveedorProv.setText("");
        TextField_DireccionProv.setText("");
        TextField_EmailProv.setText("");
        TextField_TelefonoProv.setText("");
        TextField_PagosProv.setText("");
        TextField_ProveedorProv.requestFocus();
        TextField_ProveedorProv.setEnabled(true);
        TextField_DireccionCliente.setEnabled(true);
        TextField_EmailProv.setEnabled(true);
        TextField_TelefonoProv.setEnabled(true);
        TextField_PagosProv.setEnabled(false);
        Label_IdProv.setForeground(new Color(70, 106, 124));
        Label_IdProv.setText("ID");
        Label_ProveedorProv.setForeground(new Color(70, 106, 124));
        Label_ProveedorProv.setText("Proveedor");
        Label_DireccionProv.setForeground(new Color(70, 106, 124));
        Label_DireccionProv.setText("Dirección");
        Label_EmailProv.setForeground(new Color(70, 106, 124));
        Label_EmailProv.setText("Email");
        Label_TelefonoProv.setForeground(new Color(70, 106, 124));
        Label_TelefonoProv.setText("Teléfono");
        Label_PagoProv.setForeground(new Color(70, 106, 124));
        Label_PagoProv.setText("Pago de deudas");
        RadioButton_IngresarPro.setSelected(true);
        RadioButton_IngresarPro.setForeground(new Color(0, 153, 51));
        RadioButton_PagosPro.setSelected(false);
        RadioButton_PagosPro.setForeground(new Color(70, 106, 124));
        LabelProvRecProveedor.setText("Proveedor");
        LabelDeudaRecProv.setText("0,00€");
        LabelUltPagoRecProv.setText("0,00€");
        LabelFechaRecProv.setText("Fecha");
        new Paginador(tab, Table_Proveedores, Label_PaginasProveedor, 1);
        proveedor.numeros(TextField_IdProv);
        crearTablaProv();
        proveedor.reportesProveedor(Table_ReportesProveedor, idProveedor);
    }

    private void Table_ProveedoresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ProveedoresKeyReleased
        if (Table_Proveedores.getSelectedRows().length > 0) {
            datosProveedores();
        }
    }//GEN-LAST:event_Table_ProveedoresKeyReleased

    private void Table_ProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ProveedoresMouseClicked
        if (Table_Proveedores.getSelectedRows().length > 0) {
            datosProveedores();
        }
    }//GEN-LAST:event_Table_ProveedoresMouseClicked

    private void Button_CancelarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarProvActionPerformed
        restablecerProve();
    }//GEN-LAST:event_Button_CancelarProvActionPerformed

    private void Button_PrimeroPROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroPROActionPerformed
        new Paginador(tab, Table_Proveedores, Label_PaginasProveedor, 1).primero();
        crearTablaProv();
    }//GEN-LAST:event_Button_PrimeroPROActionPerformed

    private void Button_AnteriorPROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorPROActionPerformed
        new Paginador(tab, Table_Proveedores, Label_PaginasProveedor, 0).anterior();
        crearTablaProv();
    }//GEN-LAST:event_Button_AnteriorPROActionPerformed

    private void Button_SiguientePROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguientePROActionPerformed
        new Paginador(tab, Table_Proveedores, Label_PaginasProveedor, 0).siguiente();
        crearTablaProv();
    }//GEN-LAST:event_Button_SiguientePROActionPerformed

    private void Button_UltimoPROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoPROActionPerformed
        new Paginador(tab, Table_Proveedores, Label_PaginasProveedor, 0).ultimo();
        crearTablaProv();
    }//GEN-LAST:event_Button_UltimoPROActionPerformed

    private void Button_ActDesacProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ActDesacProvActionPerformed
        proveedor.activarProveedor(RadioButton_ActivoProv, idProveedor);
        restablecerProve();
    }//GEN-LAST:event_Button_ActDesacProvActionPerformed

    private void TextField_BuscarProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarProveedorKeyReleased
        proveedor.searchProveedores(Table_Proveedores, TextField_BuscarProveedor.getText(),
                num_registro, pageSize);
    }//GEN-LAST:event_TextField_BuscarProveedorKeyReleased

    private void Button_FacturaProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_FacturaProvActionPerformed
        imprimir.imprimirRecibo(panelReciboProv);
    }//GEN-LAST:event_Button_FacturaProvActionPerformed

    private void crearTablaProv() {
        //--------------------PRESENTACION DE JTABLE----------------------

        TableCellRenderer render = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //Determinar Alineaciones   
                if (column == 1 || column == 5) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    l.setHorizontalAlignment(SwingConstants.LEFT);
                }

                //Colores en Jtable        
                if (isSelected) {
                    //l.setBackground(new Color(203, 159, 41));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setBackground(new Color(153, 204, 255));
                    l.setForeground(Color.WHITE);
                    if (column == 6) {
                        String valor = (String) value;
                        if (valor.equals("Activo")) {
                            l.setBackground(Color.GREEN);
                            l.setForeground(Color.WHITE);
                        } else {
                            l.setBackground(Color.RED);
                            l.setForeground(Color.WHITE);
                        }
                    }
                } else {
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        l.setBackground(new Color(232, 232, 232));
                        //l.setBackground(new Color(254, 227, 152));
                    }
                    if (column == 6) {
                        String valor = (String) value;
                        if (valor.equals("Activo")) {
                            l.setBackground(Color.GREEN);
                            l.setForeground(Color.WHITE);
                        } else {
                            l.setBackground(Color.RED);
                            l.setForeground(Color.WHITE);
                        }
                    }
                }
                return l;
            }
        };
        //Agregar Render
        for (int i = 0; i < Table_Proveedores.getColumnCount(); i++) {
            Table_Proveedores.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        Table_Proveedores.setAutoResizeMode(Table_Proveedores.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {10, 150, 350, 350, 350, 150, 200};
        for (int i = 0; i < Table_Proveedores.getColumnCount(); i++) {
            Table_Proveedores.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //Altos de cada fila
        Table_Proveedores.setRowHeight(30);

        ocultarColumnasProv();
    }

    private void ocultarColumnasProv() {
        Table_Proveedores.getColumnModel().getColumn(0).setMaxWidth(0);
        Table_Proveedores.getColumnModel().getColumn(0).setMinWidth(0);
        Table_Proveedores.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
// </editor-fold> 

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                "Estas seguro de salir del sistema? " + "'", "Cerrar sesión ",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            new Login().setVisible(true);
            if (0 < listUsuario.size()) {
                int idUsuario = listUsuario.get(0).getIdUsuario();
                String nombre = listUsuario.get(0).getNombre();
                String apellido = listUsuario.get(0).getApellidos();
                String user = listUsuario.get(0).getUsuario();

                if (role.equals("Admin")) {
                    caja.insertCajaRegistro(idUsuario, nombre, apellido,
                            user, role, 0, 0, false, new Calendario().getHora(),
                            new Calendario().getFecha());
                } else {
                    int idCaja = listCaja.get(0).getIdCaja();
                    int cajas = listCaja.get(0).getCaja();
                    caja.updateCaja(idCaja, true);
                    caja.insertCajaRegistro(idUsuario, nombre, apellido,
                            user, role, idCaja, cajas, false,
                            new Calendario().getHora(), new Calendario().getFecha());
                }
            }
            dispose();
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }//GEN-LAST:event_formWindowClosing

    Timer timer1 = new Timer(3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (tab) {
                case 0:
                    venta.ingresosCaja(Label_IngresoIniVentas, Label_IngresoVentasVentas,
                            Label_IngresoTotalVentas);
                    break;
                case 8:
                    objectcaja.getCaja();
                    break;
                case 9:
                    switch (TabbedPaneInventario.getSelectedIndex()) {
                        case 0:
                            inventario.getBodegas("", num_registro, pageSize);
                            break;
                            case 1:
                            inventario.getProductos("", num_registro, pageSize);
                            break;
                    }

                    break;
            }
        }
    });

    //<editor-fold defaultstate="collapsed" desc="CODIGO DPT/CAT">
    private void Button_Cat_DptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_Cat_DptActionPerformed
        jTabbedPane1.setSelectedIndex(4);
        accion = "insert";
        tab = 4;
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Proveedores.setEnabled(true);
        Button_Productos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(false);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Config.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(false);
        }
//        cliente.numeros(TextField_IdCliente);
//        cargarDatos();
    }//GEN-LAST:event_Button_Cat_DptActionPerformed

    private void RadioButton_DptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_DptActionPerformed
        TextField_Categoria.setEnabled(false);
        TextField_Departamento.setEnabled(true);
        RadioButton_Dpt.setForeground(new Color(0, 153, 51));
        RadioButton_Cat.setForeground(new Color(70, 106, 124));
        RadioButton_Cat.setSelected(false);
        TextField_Categoria.setText("");
        TextField_Departamento.requestFocus();
    }//GEN-LAST:event_RadioButton_DptActionPerformed

    private void RadioButton_CatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButton_CatActionPerformed
        TextField_Categoria.setEnabled(true);
        TextField_Departamento.setEnabled(false);
        RadioButton_Dpt.setForeground(new Color(70, 106, 124));
        RadioButton_Cat.setForeground(new Color(0, 153, 51));
        RadioButton_Dpt.setSelected(false);
        TextField_Departamento.setText("");
        TextField_Categoria.requestFocus();
    }//GEN-LAST:event_RadioButton_CatActionPerformed

    private void TextField_DepartamentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DepartamentoKeyReleased
        if (TextField_Departamento.getText().isEmpty()) {
            Label_Dpt.setText("Departamento");
            Label_Dpt.setForeground(new Color(70, 106, 124));
        } else {
            Label_Dpt.setText("Departamento");
            Label_Dpt.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_DepartamentoKeyReleased

    private void TextField_CategoriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CategoriaKeyReleased
        if (TextField_Categoria.getText().isEmpty()) {
            Label_Cat.setText("Categoria");
            Label_Cat.setForeground(new Color(70, 106, 124));
        } else {
            Label_Cat.setText("Categoria");
            Label_Cat.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_CategoriaKeyReleased

    private void guardarCatDpt() {
        boolean valor = false;
        if (RadioButton_Dpt.isSelected()) {
            if (TextField_Departamento.getText().isEmpty()) {
                Label_Dpt.setText("Ingrese el departamento");
                Label_Dpt.setForeground(Color.RED);
                TextField_Departamento.requestFocus();
            } else {
                switch (accion) {
                    case "insert":
                        valor = departamento.insertDptoCat(TextField_Departamento.getText(), 0, "dpto");
                        break;
                    case "update":
                        valor = departamento.updateDptoCat(TextField_Departamento.getText(),
                                idDpto, 0, "dpto");
                        break;
                }
                if (valor == false) {
                    Label_Dpt.setText("El Departamento ya esta registrado");
                    Label_Dpt.setForeground(Color.RED);
                } else {
                    restablecerDptoCat();
                }

            }
        }
        if (RadioButton_Cat.isSelected()) {
            if (TextField_Categoria.getText().isEmpty()) {
                Label_Cat.setText("Ingrese la categoria");
                Label_Cat.setForeground(Color.RED);
                TextField_Categoria.requestFocus();
            } else {
                if (idDpto != 0) {
                    switch (accion) {
                        case "insert":
                            valor = departamento.insertDptoCat(TextField_Categoria.getText(), idDpto, "cat");
                            break;
                        case "update":
                            valor = departamento.updateDptoCat(TextField_Categoria.getText(),
                                    idDpto, idCat, "cat");
                            break;
                    }
                    if (valor == false) {
                        Label_Cat.setText("La Categoria ya esta registrada");
                        Label_Cat.setForeground(Color.RED);
                    } else {
                        restablecerDptoCat();
                    }
                } else {
                    Label_Dpt.setText("Seleccione el departamento");
                    Label_Dpt.setForeground(Color.RED);
                }
            }
        }
    }

    private void restablecerDptoCat() {
        accion = "insert";
        idDpto = 0;
        idCat = 0;
        TextField_Departamento.setEnabled(true);
        TextField_Categoria.setEnabled(false);
        TextField_Departamento.setText("");
        TextField_Categoria.setText("");
        TextField_BuscarDpt.setText("");
        TextField_Departamento.requestFocus();
        RadioButton_Dpt.setForeground(new Color(0, 153, 51));
        RadioButton_Cat.setForeground(new Color(70, 106, 124));
        RadioButton_Dpt.setSelected(true);
        RadioButton_Cat.setSelected(false);
        Label_Dpt.setForeground(new Color(70, 106, 124));
        Label_Cat.setForeground(new Color(70, 106, 124));
        departamento.searchDepartamentos(Table_Dpt, "");
        departamento.getCategorias(Table_Cat, idDpto);
    }

    private void datosDpto() {
        if (RadioButton_Dpt.isSelected()) {
            accion = "update";
        }

        tablaModeloDpto = departamento.getModelo();
        int fila = Table_Dpt.getSelectedRow();
        idDpto = Integer.valueOf((String) tablaModeloDpto.getValueAt(fila, 0));
        TextField_Departamento.setText((String) tablaModeloDpto.getValueAt(fila, 1));

        if (RadioButton_Cat.isSelected()) {
            accion = "insert";
            Label_Dpt.setText("Departamento");
            Label_Dpt.setForeground(new Color(0, 153, 51));
        }
        Label_Dpt.setText("Departamento");
        Label_Dpt.setForeground(new Color(0, 153, 51));
        departamento.getCategorias(Table_Cat, idDpto);
    }

    private void datosCat() {
        accion = "update";
        tablaModelCat = departamento.getModeloCat();
        int fila = Table_Cat.getSelectedRow();
        idCat = Integer.valueOf((String) tablaModelCat.getValueAt(fila, 0));
        TextField_Categoria.setText((String) tablaModelCat.getValueAt(fila, 1));

        Label_Cat.setText("Categoria");
        Label_Cat.setForeground(new Color(0, 153, 51));
    }

    private void eliminarDptoCat() {
        if (RadioButton_Dpt.isSelected()) {
            if (idDpto > 0) {
                if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                        "Estas seguro de eliminar este Departamento? " + "'", "Eliminar Departamento ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    departamento.deleteDptoCat(idDpto, idCat, "dpto");
                    restablecerDptoCat();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un departamento");
            }
        } else {
            if (idCat > 0) {
                if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                        "Estas seguro de eliminar esta Categoria? " + "'", "Eliminar Categoria ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    departamento.deleteDptoCat(idDpto, idCat, "cat");
                    restablecerDptoCat();
                }
            }
        }
    }

    private void Button_GuardarCatDptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarCatDptActionPerformed
        if (role.equals("Admin")) {
            guardarCatDpt();
        } else {
            JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
        }
    }//GEN-LAST:event_Button_GuardarCatDptActionPerformed

    private void Table_DptKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_DptKeyReleased
        if (Table_Dpt.getSelectedRows().length > 0) {
            datosDpto();
        }
    }//GEN-LAST:event_Table_DptKeyReleased

    private void Table_DptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_DptMouseClicked
        if (Table_Dpt.getSelectedRows().length > 0) {
            datosDpto();
        }
    }//GEN-LAST:event_Table_DptMouseClicked

    private void Table_CatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_CatKeyReleased
        if (Table_Cat.getSelectedRows().length > 0) {
            datosCat();
        }
    }//GEN-LAST:event_Table_CatKeyReleased

    private void Table_CatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_CatMouseClicked
        if (Table_Cat.getSelectedRows().length > 0) {
            datosCat();
        }
    }//GEN-LAST:event_Table_CatMouseClicked

    private void Button_EliminarCatDptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_EliminarCatDptActionPerformed
        if (role.equals("Admin")) {
            eliminarDptoCat();
        } else {
            JOptionPane.showMessageDialog(null, "No cuenta con el permiso requerido");
        }
    }//GEN-LAST:event_Button_EliminarCatDptActionPerformed

    private void Button_CancelarCatDptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarCatDptActionPerformed
        restablecerDptoCat();
    }//GEN-LAST:event_Button_CancelarCatDptActionPerformed

    private void TextField_BuscarDptKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarDptKeyReleased
        departamento.searchDepartamentos(Table_Dpt, TextField_BuscarDpt.getText());
    }//GEN-LAST:event_TextField_BuscarDptKeyReleased

    // </editor-fold> 
    // <editor-fold defaultstate="collapsed" desc="CÓDIGO COMPRAS">
    private void Button_ComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ComprasActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Proveedores.setEnabled(true);
        Button_Productos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(false);
        }
        restablecerCompras();
    }//GEN-LAST:event_Button_ComprasActionPerformed

    private void TextField_DescripcionCPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DescripcionCPKeyReleased
        if (TextField_DescripcionCP.getText().isEmpty()) {
            Label_DescripcionCP.setForeground(new Color(70, 106, 124));
        } else {
            Label_DescripcionCP.setText("Descripción");
            Label_DescripcionCP.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_DescripcionCPKeyReleased

    private void TextField_CantidadCPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CantidadCPKeyReleased
        if (TextField_CantidadCP.getText().isEmpty()) {
            Label_CantidadCP.setForeground(new Color(70, 106, 124));
        } else {
            Label_CantidadCP.setText("Cantidad");
            Label_CantidadCP.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_CantidadCPKeyReleased

    private void TextField_CantidadCPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CantidadCPKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_CantidadCP);
    }//GEN-LAST:event_TextField_CantidadCPKeyTyped

    private void TextField_PrecioCPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PrecioCPKeyReleased
        if (TextField_PrecioCP.getText().isEmpty()) {
            Label_PrecioCP.setForeground(new Color(70, 106, 124));
        } else {
            Label_PrecioCP.setText("Precio de compra");
            Label_PrecioCP.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_PrecioCPKeyReleased

    private void guardarTempoCompras() {
        if (validarDatosCompras()) {
            String des = TextField_DescripcionCP.getText();
            int cant = Integer.valueOf(TextField_CantidadCP.getText());
            String precio = TextField_PrecioCP.getText();
            switch (accion) {
                case "insert":
                    if (TabbedPaneCompras.getSelectedIndex() == 0) {
                        if (idProveeCompra != 0) {
                            compra.guardarTempoCompra(des, cant, precio);
                            restablecerCompras();
                        } else {
                            JOptionPane.showMessageDialog(null, "Seleccione un proveedor",
                                    "Información", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    break;
                case "update":
                    if (TabbedPaneCompras.getSelectedIndex() == 0) {
                        if (idProveeCompra != 0) {
                            compra.updateTempoCompra(idCompra, des, cant, precio);
                            restablecerCompras();
                        } else {
                            JOptionPane.showMessageDialog(null, "Seleccione un proveedor",
                                    "Información", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    break;
            }
        }
    }

    private void guardarCompras() {
        boolean valor;
        if (TextField_ComprasPagos.getText().isEmpty()) {
            LabelCompraPago.setText("Ingrese el pago");
            LabelCompraPago.setForeground(Color.RED);
            TextField_ComprasPagos.requestFocus();
        } else {
            if (idProveeCompra != 0) {
                valor = compra.verificarPago(TextField_ComprasPagos, LabelCompraPago,
                        CheckBoxCompraCredito, LabelCompraDeuda, LabelCompraDeudaRecibo,
                        LabelCompraSaldoRecibo, idProveeCompra);
                if (valor) {
                    compra.saveCompras(proveedores, idProveeCompra, usuario, idUsuario, role);
                    compra.deleteCompras(0);
                    restablecerProveedorCompras(1);
                } else {
                    LabelCompraPago.setText("Seleccione un proveeedor");
                    LabelCompraPago.setForeground(Color.RED);
                }
            }
        }
    }

    private boolean validarDatosCompras() {
        if (TextField_DescripcionCP.getText().isEmpty()) {
            Label_DescripcionCP.setText("Ingrese la descripción");
            Label_DescripcionCP.setForeground(Color.RED);
            TextField_DescripcionCP.requestFocus();
            return false;
        } else if (TextField_CantidadCP.getText().isEmpty()) {
            Label_CantidadCP.setText("Ingrese la cantidad");
            Label_CantidadCP.setForeground(Color.RED);
            TextField_CantidadCP.requestFocus();
            return false;
        } else if (TextField_PrecioCP.getText().isEmpty()) {
            Label_PrecioCP.setText("Ingrese el precio de la compra");
            Label_PrecioCP.setForeground(Color.RED);
            TextField_PrecioCP.requestFocus();
            return false;
        }
        return true;
    }

    private void restablecerCompras() {
        tab = 5;
        TabbedPaneCompras.setSelectedIndex(0);
        TextField_DescripcionCP.setText("");
        TextField_CantidadCP.setText("");
        TextField_PrecioCP.setText("");
        TextField_BuscarCompras.setText("");
        TextField_BuscarProvCompras.setText("");
        TextField_DescripcionCP.requestFocus();
        Label_DescripcionCP.setText("Descripción");
        Label_DescripcionCP.setForeground(new Color(70, 106, 124));
        Label_CantidadCP.setText("Cantidad");
        Label_CantidadCP.setForeground(new Color(70, 106, 124));
        Label_PrecioCP.setText("Precio de compra");
        Label_PrecioCP.setForeground(new Color(70, 106, 124));
        new Paginador(tab, Table_Compras, Label_PaginasCompra, 1);
        compra.searchCompras(Table_Compras, "", num_registro, pageSize);
        compra.importesTempo(Label_ImporteCP1, Label_ImporteCP2, LabelCompraTotalPagarRecibo);

        TextField_BuscarCompras.setText("");
        TextField_ComprasPagos.setText("");
        LabelCompraDeuda.setText("0,00€");
        LabelCompraPago.setText("Monto a pagar");
        LabelCompraPago.setForeground(new Color(70, 106, 124));
        CheckBoxCompraCredito.setSelected(false);
        LabelCompraProveedorRecibo.setText("Nombre");
        LabelCompraTotalPagarRecibo.setText("0,00€");
        LabelCompraDeudaRecibo.setText("0,00€");
        LabelCompraSaldoRecibo.setText("0,00€");
        LabelCompraFechaRecibo.setText("--/--/----");

        LabelProveedorCP.setText("Proveedor");
        idCompra = 0;
    }

    private void datosCompras() {
        accion = "update";
        //tablaModeloCompra = compra.getModelo();
        int fila = Table_Compras.getSelectedRow();
        idCompra = Integer.valueOf((String) compra.getModelo().getValueAt(fila, 0));
        TextField_DescripcionCP.setText((String) compra.getModelo().getValueAt(fila, 1));
        TextField_CantidadCP.setText((String) compra.getModelo().getValueAt(fila, 2));
        String precios = (String) compra.getModelo().getValueAt(fila, 3);
        TextField_PrecioCP.setText(precios.replace("€", ""));
        Label_DescripcionCP.setText("Descripción");
        Label_DescripcionCP.setForeground(new Color(0, 153, 51));
        Label_CantidadCP.setText("Cantidad");
        Label_CantidadCP.setForeground(new Color(0, 153, 51));
        Label_PrecioCP.setText("Precio de compra");
        Label_PrecioCP.setForeground(new Color(0, 153, 51));
    }

    private void TextField_PrecioCPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PrecioCPKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_PrecioCP);
    }//GEN-LAST:event_TextField_PrecioCPKeyTyped

    private void Button_GuardarComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarComprasActionPerformed
        if (TabbedPaneCompras.getSelectedIndex() == 0) {
            guardarTempoCompras();
        } else {
            guardarCompras();
        }

    }//GEN-LAST:event_Button_GuardarComprasActionPerformed

    private void Table_ComprasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ComprasKeyReleased
        if (Table_Compras.getSelectedRows().length > 0) {
            datosCompras();
        }
    }//GEN-LAST:event_Table_ComprasKeyReleased

    private void Table_ComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ComprasMouseClicked
        if (Table_Compras.getSelectedRows().length > 0) {
            datosCompras();
        }
    }//GEN-LAST:event_Table_ComprasMouseClicked

    private void Button_PrimeroComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroComprasActionPerformed
        new Paginador(tab, Table_Compras, Label_PaginasCompra, 1).primero();
    }//GEN-LAST:event_Button_PrimeroComprasActionPerformed

    private void Button_AnteriorComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorComprasActionPerformed
        new Paginador(tab, Table_Compras, Label_PaginasCompra, 0).anterior();
    }//GEN-LAST:event_Button_AnteriorComprasActionPerformed

    private void Button_SiguienteComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteComprasActionPerformed
        new Paginador(tab, Table_Compras, Label_PaginasCompra, 0).siguiente();
    }//GEN-LAST:event_Button_SiguienteComprasActionPerformed

    private void Button_UltimoComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoComprasActionPerformed
        new Paginador(tab, Table_Compras, Label_PaginasCompra, 0).ultimo();
    }//GEN-LAST:event_Button_UltimoComprasActionPerformed

    private void TextField_BuscarComprasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarComprasKeyReleased
        compra.searchCompras(Table_Compras, TextField_BuscarCompras.getText(),
                num_registro, pageSize);
    }//GEN-LAST:event_TextField_BuscarComprasKeyReleased

    private void TextField_BuscarProvComprasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarProvComprasKeyReleased
        compra.searchProveedores(Table_ComprasProveedor, TextField_BuscarProvCompras.getText());
    }//GEN-LAST:event_TextField_BuscarProvComprasKeyReleased

    private void Table_ComprasProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ComprasProveedorKeyReleased
        if (Table_ComprasProveedor.getSelectedRows().length > 0) {
            datosComprasProvee();
        }
    }//GEN-LAST:event_Table_ComprasProveedorKeyReleased

    private void Table_ComprasProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ComprasProveedorMouseClicked
        if (Table_ComprasProveedor.getSelectedRows().length > 0) {
            datosComprasProvee();
        }
    }//GEN-LAST:event_Table_ComprasProveedorMouseClicked

    private void datosComprasProvee() {
        int fila = Table_ComprasProveedor.getSelectedRow();
        idProveeCompra = Integer.valueOf((String) compra.getModelo2().getValueAt(fila, 0));
        IDProvee = (String) compra.getModelo2().getValueAt(fila, 1);
        proveedores = (String) compra.getModelo2().getValueAt(fila, 2);
        saldoProveedor = (String) compra.getModelo2().getValueAt(fila, 5);
        LabelProveedorCP.setText(proveedores);
        LabelCompraProveedorRecibo.setText(proveedores);
        LabelCompraFechaRecibo.setText(new Calendario().getFecha());
    }

    private void Button_EliminarComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_EliminarComprasActionPerformed
        if (0 < idCompra) {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                    "Estas seguro de eliminar esta compra? " + "'", "Eliminar compra ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                compra.deleteCompras(idCompra);
                restablecerCompras();
            }
        } else {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                    "Estas seguro de eliminar estas compras? " + "'", "Eliminar compras ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                compra.deleteCompras(0);
                restablecerProveedorCompras(0);
            }
        }
    }//GEN-LAST:event_Button_EliminarComprasActionPerformed

    private void TabbedPaneComprasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneComprasStateChanged
        compra.getIngresos(LabelCompraEncaja);
        compra.importesTempo(LabelCompraMontoPagar, Label_ImporteCP2, LabelCompraTotalPagarRecibo);
    }//GEN-LAST:event_TabbedPaneComprasStateChanged

    private void TextField_ComprasPagosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ComprasPagosKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_ComprasPagos);
    }//GEN-LAST:event_TextField_ComprasPagosKeyTyped

    private void TextField_ComprasPagosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ComprasPagosKeyReleased
        compra.verificarPago(TextField_ComprasPagos, LabelCompraPago, CheckBoxCompraCredito,
                LabelCompraDeuda, LabelCompraDeudaRecibo, LabelCompraSaldoRecibo, idProveeCompra);
    }//GEN-LAST:event_TextField_ComprasPagosKeyReleased

    private void CheckBoxCompraCreditoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CheckBoxCompraCreditoStateChanged
        compra.verificarPago(TextField_ComprasPagos, LabelCompraPago, CheckBoxCompraCredito,
                LabelCompraDeuda, LabelCompraDeudaRecibo, LabelCompraSaldoRecibo, idProveeCompra);
    }//GEN-LAST:event_CheckBoxCompraCreditoStateChanged

    private void restablecerProveedorCompras(int compras) {
        if (0 < compras) {
            imprimir.imprimirRecibo(PanelReciboCompra);
        }
        idProveeCompra = 0;
        TextField_BuscarProvCompras.setText("");
        compra.searchProveedores(Table_ComprasProveedor, "");
        restablecerCompras();
    }

    // </editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="CODIGO PRODUCTOS"> 
    private void Button_ProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ProductosActionPerformed
        jTabbedPane1.setSelectedIndex(3);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Proveedores.setEnabled(true);
        Button_Productos.setEnabled(false);
        Button_Cat_Dpt.setEnabled(true);
        cliente.numeros(TextField_IdCliente);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Config.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(false);
        }
        restablecerProducto();
    }//GEN-LAST:event_Button_ProductosActionPerformed

    private void restablecerProducto() {
        tab = 4;
        idCompra = 0;
        funcion = 0;
        idProducto = 0;
        accion = "insert";
        TextField_DescripcionPDT.setText("");
        TextField_PrecioVentaPDT.setText("");
        Label_DescripcionPDT.setForeground(new Color(70, 106, 124));
        Label_PrecioVentaProducto.setForeground(new Color(70, 106, 124));
        producto.getProductos(Table_ProductosCompras, "");
        producto.getDepartamento(ComboBox_DepartamentoPro, "");
        dpt = (Departamentos) ComboBox_DepartamentoPro.getSelectedItem();
        producto.getCategorias(ComboBox_DepartamentoPro, ComboBox_CategoriaPro,
                dpt.getIdDpto(), "");
        producto.codeBarra(LabelProductoImagenCod, "0000000000000",
                TextField_DescripcionPDT.getText(), TextField_PrecioVentaPDT.getText());
        new Paginador(tab, Table_ProductosProd, Label_PaginasProductos, 1);
        producto.searchProductos(Table_ProductosProd, TextField_DescripcionPDT.getText(),
                num_registro, pageSize);
    }

    private void datosTempoProductos() {
        String product;
        funcion = 1;
        accion = "insert";
        int fila = Table_ProductosCompras.getSelectedRow();
        idCompra = Integer.valueOf((String) producto.getModelo().getValueAt(fila, 0));
        product = (String) producto.getModelo().getValueAt(fila, 1);
        cantidad = Integer.valueOf((String) producto.getModelo().getValueAt(fila, 2));
        precioCompra = (String) producto.getModelo().getValueAt(fila, 3);
        LabelProductoCod.setText(product);
        TextField_DescripcionPDT.setText(product);
        Label_DescripcionPDT.setText("Descripción");
        Label_DescripcionPDT.setForeground(new Color(0, 153, 51));
        producto.codeBarra(LabelProductoImagenCod, "0", product, TextField_PrecioVentaPDT.getText());
        producto.searchProductos(Table_ProductosProd, product, num_registro, pageSize);
    }

    private void Table_ProductosComprasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ProductosComprasKeyReleased
        if (Table_ProductosCompras.getSelectedRows().length > 0) {
            datosTempoProductos();
        }
    }//GEN-LAST:event_Table_ProductosComprasKeyReleased

    private void Table_ProductosComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ProductosComprasMouseClicked
        if (Table_ProductosCompras.getSelectedRows().length > 0) {
            datosTempoProductos();
        }
    }//GEN-LAST:event_Table_ProductosComprasMouseClicked

    private void TextField_DescripcionPDTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DescripcionPDTKeyReleased
        if (TextField_DescripcionPDT.getText().isEmpty()) {
            Label_DescripcionPDT.setText("Descripcion");
            Label_DescripcionPDT.setForeground(new Color(70, 106, 124));
        } else {
            Label_DescripcionPDT.setText("Descripcion");
            Label_DescripcionPDT.setForeground(new Color(0, 153, 51));
            if (funcion == 1) {
                producto.codeBarra(LabelProductoImagenCod, "0", TextField_DescripcionPDT.getText(),
                        TextField_PrecioVentaPDT.getText());
            }
        }
        producto.searchProductos(Table_ProductosProd, TextField_DescripcionPDT.getText(),
                num_registro, pageSize);
    }//GEN-LAST:event_TextField_DescripcionPDTKeyReleased

    private void TextField_PrecioVentaPDTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PrecioVentaPDTKeyReleased
        if (TextField_PrecioVentaPDT.getText().isEmpty()) {
            Label_PrecioVentaProducto.setText("Precio venta");
            Label_PrecioVentaProducto.setForeground(new Color(70, 106, 124));
        } else {
            Label_PrecioVentaProducto.setText("Precio venta");
            Label_PrecioVentaProducto.setForeground(new Color(0, 153, 51));
            if (funcion == 1 && precioCompra != null) {
                producto.codeBarra(LabelProductoImagenCod, "0", TextField_DescripcionPDT.getText(),
                        TextField_PrecioVentaPDT.getText());
                producto.verificarPrecioVenta(Label_PrecioVentaProducto,
                        TextField_PrecioVentaPDT.getText(), precioCompra, funcion);
            }
        }
    }//GEN-LAST:event_TextField_PrecioVentaPDTKeyReleased

    private void guardarProducto() {
        if (validarDatosProductos()) {
            String product = TextField_DescripcionPDT.getText();
            String precio = TextField_PrecioVentaPDT.getText();
            dpt = (Departamentos) ComboBox_DepartamentoPro.getSelectedItem();
            String departa = dpt.getDepartamento();
            cat = (Categorias) ComboBox_CategoriaPro.getSelectedItem();
            String categ = cat.getCategoria();
            boolean verificar = producto.verificarPrecioVenta(Label_PrecioVentaProducto,
                    TextField_PrecioVentaPDT.getText(), precioCompra, funcion);

            switch (accion) {
                case "insert":
                    if (funcion == 1) {
                        if (verificar) {
                            producto.saveProducto(product, cantidad, precio,
                                    departa, categ, accion, idCompra);
                            imprimir.imprimirRecibo(PanelCodeProducto);
                            restablecerProducto();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione un producto");
                    }
                    break;
                case "update":
                    if (funcion == 2) {
                        producto.saveProducto(product, cantidad, precio,
                                departa, categ, accion, idProducto);
                        restablecerProducto();
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione un producto");
                    }
                    break;
            }
        }
    }

    private boolean validarDatosProductos() {
        if (TextField_DescripcionPDT.getText().isEmpty()) {
            Label_DescripcionPDT.setText("Ingrese la Descripción");
            Label_DescripcionPDT.setForeground(Color.RED);
            TextField_DescripcionPDT.requestFocus();
            return false;
        } else if (TextField_PrecioVentaPDT.getText().isEmpty()) {
            Label_PrecioVentaProducto.setText("Ingrese el Precio de venta");
            Label_PrecioVentaProducto.setForeground(Color.RED);
            TextField_PrecioVentaPDT.requestFocus();
            return false;
        }
        return true;
    }

    private void datosProductos() {
        String product, codigo, precio, departamento, categoria;
        funcion = 2;
        accion = "update";
        int fila = Table_ProductosProd.getSelectedRow();
        idProducto = Integer.valueOf((String) producto.getModelo2().getValueAt(fila, 0));
        codigo = (String) producto.getModelo2().getValueAt(fila, 1);
        product = (String) producto.getModelo2().getValueAt(fila, 2);
        precio = (String) producto.getModelo2().getValueAt(fila, 3);
        departamento = (String) producto.getModelo2().getValueAt(fila, 5);
        categoria = (String) producto.getModelo2().getValueAt(fila, 6);
        TextField_DescripcionPDT.setText(product);
        TextField_PrecioVentaPDT.setText(precio.replace("€", ""));
        Label_DescripcionPDT.setForeground(new Color(0, 153, 51));
        Label_PrecioVentaProducto.setForeground(new Color(0, 153, 51));
        dpt = producto.getDepartamento(ComboBox_DepartamentoPro, departamento);
        ComboBox_DepartamentoPro.setSelectedItem(dpt);
        cat = producto.getCategorias(ComboBox_DepartamentoPro, ComboBox_CategoriaPro,
                dpt.getIdDpto(), categoria);
        ComboBox_CategoriaPro.setSelectedItem(cat);
        producto.codeBarra(LabelProductoImagenCod, codigo, product,
                TextField_PrecioVentaPDT.getText());
    }

    private void TextField_PrecioVentaPDTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PrecioVentaPDTKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_PrecioVentaPDT);
    }//GEN-LAST:event_TextField_PrecioVentaPDTKeyTyped

    private void ComboBox_DepartamentoProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBox_DepartamentoProActionPerformed
        dpt = (Departamentos) ComboBox_DepartamentoPro.getSelectedItem();
        producto.getCategorias(ComboBox_DepartamentoPro, ComboBox_CategoriaPro,
                dpt.getIdDpto(), "");
    }//GEN-LAST:event_ComboBox_DepartamentoProActionPerformed

    private void Button_GuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarProductoActionPerformed
        guardarProducto();
    }//GEN-LAST:event_Button_GuardarProductoActionPerformed

    private void TextFieldBuscarProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldBuscarProductosKeyReleased
        producto.getProductos(Table_ProductosCompras, TextFieldBuscarProductos.getText());
    }//GEN-LAST:event_TextFieldBuscarProductosKeyReleased

    private void Table_ProductosProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_ProductosProdKeyReleased
        if (Table_ProductosProd.getSelectedRows().length > 0) {
            datosProductos();
        }
    }//GEN-LAST:event_Table_ProductosProdKeyReleased

    private void Table_ProductosProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_ProductosProdMouseClicked
        if (Table_ProductosProd.getSelectedRows().length > 0) {
            datosProductos();
        }
    }//GEN-LAST:event_Table_ProductosProdMouseClicked

    private void Button_CancelarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarProductoActionPerformed
        restablecerProducto();
    }//GEN-LAST:event_Button_CancelarProductoActionPerformed

    private void Button_PrimeroProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroProductoActionPerformed
        new Paginador(tab, Table_ProductosProd, Label_PaginasProductos, 0).primero();
    }//GEN-LAST:event_Button_PrimeroProductoActionPerformed

    private void Button_AnteriorProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorProductoActionPerformed
        new Paginador(tab, Table_ProductosProd, Label_PaginasProductos, 1).anterior();
    }//GEN-LAST:event_Button_AnteriorProductoActionPerformed

    private void Button_SiguienteProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteProductoActionPerformed
        new Paginador(tab, Table_ProductosProd, Label_PaginasProductos, 1).siguiente();
    }//GEN-LAST:event_Button_SiguienteProductoActionPerformed

    private void Button_UltimoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoProductoActionPerformed
        new Paginador(tab, Table_ProductosProd, Label_PaginasProductos, 1).ultimo();
    }//GEN-LAST:event_Button_UltimoProductoActionPerformed
    // </editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="CODIGO VENTAS"> 
    private void Button_VentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_VentasActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        Button_Ventas.setEnabled(false);
        Button_Cliente.setEnabled(true);
        Button_Proveedores.setEnabled(true);
        Button_Productos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Config.setEnabled(true);
        } else {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(false);
        }
        restablecerVentas();
    }//GEN-LAST:event_Button_VentasActionPerformed

    public void restablecerVentas() {
        tab = 0;
        accion = "insert";
        TextField_PagoConVentas.setText("");
        TextField_BuscarCliente2.setText("");
        Label_DeudaTotal.setText("0,00€");
        Label_SuCambio.setText("Su cambio");
        Label_SuCambio2.setText("0,00€");
        Label_SuCambio.setForeground(new Color(70, 106, 124));
        Label_PagoVenta.setForeground(new Color(70, 106, 124));
        if (CheckBoxVentas.isSelected() == false) {
            Label_NombreReciboVentas.setText("Nombre");
            Label_DeudaReciboVentas.setText("0,00€");
            Label_DeudaAnteriorReciboVentas.setText("0,00€");
            Label_DeudaTotalReciboVentas.setText("0,00€");
            Label_UltimoPagoReciboVentas.setText("0,00€");
            Label_FechaReciboVentas.setText("-/--/----");
        }
        lblMensajeVenta.setText("");
        CheckBoxVentas.setSelected(false);
        venta.searchVentaTempo(Table_VentasTempo, num_registro, pageSize);
        venta.importes(Label_MontoPagarVentas, cajaUser, idUsuario);
        venta.reportesClientes(Table_VentasClientes, TextField_BuscarCliente2.getText());
        new Paginador(tab, Table_VentasTempo, Label_PaginasVentas, 1);
    }

    private void ButtonBuscarProductoVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonBuscarProductoVentasActionPerformed
        if (TextField_BuscarProductosVentas.getText().equals("")) {
            lblMensajeVenta.setText("Ingrese el código del producto");
            lblMensajeVenta.setForeground(Color.RED);
            TextField_BuscarProductosVentas.requestFocus();
        } else {
            List<Bodegas> bodega = venta.searchBodega(TextField_BuscarProductosVentas.getText());
            if (0 < bodega.size()) {
                lblMensajeVenta.setText("");
                venta.saveVentasTempo(TextField_BuscarProductosVentas.getText(), 0);
                venta.searchVentaTempo(Table_VentasTempo, num_registro, pageSize);
                venta.importes(Label_MontoPagarVentas, cajaUser, idUsuario);
            } else {
                lblMensajeVenta.setText("El código del producto no existe");
                lblMensajeVenta.setForeground(Color.RED);
            }
        }
    }//GEN-LAST:event_ButtonBuscarProductoVentasActionPerformed

    private void Table_VentasTempoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_VentasTempoMouseClicked
        if (Table_VentasTempo.getSelectedRows().length > 0) {
            if (evt.getClickCount() == 2) {
                int fila = Table_VentasTempo.getSelectedRow();
                String codigo = (String) venta.getModelo().getValueAt(fila, 1);
                int cantidad = Integer.valueOf((String) venta.getModelo().getValueAt(fila, 4));
                venta.deleteVentaTempo(codigo, cantidad, cajaUser, idUsuario);
                venta.searchVentaTempo(Table_VentasTempo, num_registro, pageSize);
                venta.importes(Label_MontoPagarVentas, cajaUser, idUsuario);
            }
        }
    }//GEN-LAST:event_Table_VentasTempoMouseClicked

    private void TextField_PagoConVentasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PagoConVentasKeyReleased
        venta.pagosCliente(TextField_PagoConVentas, Label_SuCambio,
                Label_SuCambio2, Label_PagoVenta, CheckBoxVentas);
    }//GEN-LAST:event_TextField_PagoConVentasKeyReleased

    private void TextField_BuscarCliente2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarCliente2KeyReleased
        venta.reportesClientes(Table_VentasClientes, TextField_BuscarCliente2.getText());
    }//GEN-LAST:event_TextField_BuscarCliente2KeyReleased

    private void CheckBoxVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxVentasActionPerformed
        venta.dataCliente(CheckBoxVentas, TextField_PagoConVentas,
                TextField_BuscarCliente2, Table_VentasClientes, labels);
        venta.reportesClientes(Table_VentasClientes, TextField_BuscarCliente2.getText());
        venta.cobrar(CheckBoxVentas, TextField_PagoConVentas, Table_VentasClientes,
                labels);
        venta.pagosCliente(TextField_PagoConVentas, Label_SuCambio, Label_SuCambio2,
                Label_PagoVenta, CheckBoxVentas);
        lblMensajeVenta.setText("");
    }//GEN-LAST:event_CheckBoxVentasActionPerformed

    private void Table_VentasClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_VentasClientesMouseClicked
        if (Table_VentasClientes.getSelectedRows().length > 0) {
            LabelMensajeVentasCliente.setText("");
            if (CheckBoxVentas.isSelected()) {
                if (!TextField_PagoConVentas.getText().isEmpty()) {
                    venta.dataCliente(CheckBoxVentas, TextField_PagoConVentas,
                            TextField_BuscarCliente2, Table_VentasClientes, labels);
                } else {
                    LabelMensajeVentasCliente.setText("Ingrese el pago");
                    LabelMensajeVentasCliente.setForeground(Color.RED);
                    TextField_PagoConVentas.requestFocus();
                }
            } else {
                LabelMensajeVentasCliente.setText("Seleccione la opción crédito");
                LabelMensajeVentasCliente.setForeground(Color.RED);
            }
        }
    }//GEN-LAST:event_Table_VentasClientesMouseClicked

    private void Button_GuardarVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarVentasActionPerformed
        boolean valor = venta.cobrar(CheckBoxVentas, TextField_PagoConVentas,
                Table_VentasClientes, labels);
        if (valor) {
            imprimir.imprimirRecibo(null);
            restablecerVentas();
        }
    }//GEN-LAST:event_Button_GuardarVentasActionPerformed

    private void Button_PrimeroVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroVentasActionPerformed
        new Paginador(tab, Table_VentasTempo, Label_PaginasVentas, 1).primero();
    }//GEN-LAST:event_Button_PrimeroVentasActionPerformed

    private void Button_AnteriorVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorVentasActionPerformed
        new Paginador(tab, Table_VentasTempo, Label_PaginasVentas, 0).anterior();
    }//GEN-LAST:event_Button_AnteriorVentasActionPerformed

    private void Button_SiguienteVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteVentasActionPerformed
        new Paginador(tab, Table_VentasTempo, Label_PaginasVentas, 0).siguiente();
    }//GEN-LAST:event_Button_SiguienteVentasActionPerformed

    private void Button_UltimoVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoVentasActionPerformed
        new Paginador(tab, Table_VentasTempo, Label_PaginasVentas, 0).ultimo();
    }//GEN-LAST:event_Button_UltimoVentasActionPerformed

    private void Button_FacturaVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_FacturaVentasActionPerformed
        if (CheckBoxVentas.isSelected()) {
            imprimir.imprimirRecibo(PanelReciboVenta);
            CheckBoxVentas.setSelected(false);
            restablecerVentas();
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar el CheckBox credito");
        }
    }//GEN-LAST:event_Button_FacturaVentasActionPerformed

    private void Button_CancelarVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarVentasActionPerformed
        restablecerVentas();
    }//GEN-LAST:event_Button_CancelarVentasActionPerformed
// </editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="CODIGO CONFIGURACIONES"> 
    private void Button_ConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ConfigActionPerformed
        jTabbedPane1.setSelectedIndex(6);
        Button_Ventas.setEnabled(true);
        Button_Cliente.setEnabled(true);
        Button_Proveedores.setEnabled(true);
        Button_Productos.setEnabled(true);
        Button_Cat_Dpt.setEnabled(true);
        if (role.equals("Admin")) {
            Button_Compras.setEnabled(true);
            Button_Config.setEnabled(false);
        } else {
            Button_Compras.setEnabled(false);
            Button_Config.setEnabled(false);
        }
    }//GEN-LAST:event_Button_ConfigActionPerformed
// </editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="CODIGO USUARIOS"> 
    private Usuario user;

    private void ButtonUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUsuariosActionPerformed
        tab = 8;
        jTabbedPane1.setSelectedIndex(7);
        Button_Config.setEnabled(true);
        user = new Usuario(textFieldObject, labelsObject, ComboBoxRoles, TableUsuarios);
        user.restablecerUsuarios();
    }//GEN-LAST:event_ButtonUsuariosActionPerformed

    private void Button_GuardarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarUsuariosActionPerformed
        if (user.registrarUsuario()) {
            user.restablecerUsuarios();
        }

    }//GEN-LAST:event_Button_GuardarUsuariosActionPerformed

    private void TextField_NombreUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_NombreUserKeyReleased
        if (TextField_NombreUser.getText().isEmpty()) {
            Label_NombreUser.setForeground(new Color(70, 106, 124));
        } else {
            Label_NombreUser.setText("Nombre");
            Label_NombreUser.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_NombreUserKeyReleased

    private void TextField_NombreUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_NombreUserKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_NombreUserKeyTyped

    private void TextField_ApellidosUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ApellidosUserKeyReleased
        if (TextField_ApellidosUser.getText().isEmpty()) {
            Label_ApellidosUser.setForeground(new Color(70, 106, 124));
        } else {
            Label_ApellidosUser.setText("Apellidos");
            Label_ApellidosUser.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_ApellidosUserKeyReleased

    private void TextField_ApellidosUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ApellidosUserKeyTyped
        evento.textKeyPress(evt);
    }//GEN-LAST:event_TextField_ApellidosUserKeyTyped

    private void TextField_TelefonoUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoUserKeyReleased
        if (TextField_TelefonoUser.getText().isEmpty()) {
            Label_TelefonoUser.setForeground(new Color(70, 106, 124));
        } else {
            Label_TelefonoUser.setText("Teléfono");
            Label_TelefonoUser.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_TelefonoUserKeyReleased

    private void TextField_TelefonoUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_TelefonoUserKeyTyped
        evento.numberKeyPress(evt);
    }//GEN-LAST:event_TextField_TelefonoUserKeyTyped

    private void TextField_DireccionUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DireccionUserKeyReleased
        if (TextField_DireccionUser.getText().isEmpty()) {
            Label_DireccionUser.setForeground(new Color(70, 106, 124));
        } else {
            Label_DireccionUser.setText("Dirección");
            Label_DireccionUser.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_DireccionUserKeyReleased

    private void TextField_EmailUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_EmailUserKeyReleased
        if (TextField_EmailUser.getText().isEmpty()) {
            Label_EmailUser.setForeground(new Color(70, 106, 124));
        } else {
            Label_EmailUser.setText("Email");
            Label_EmailUser.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_EmailUserKeyReleased

    private void TextField_UsuarioUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_UsuarioUserKeyReleased
        if (TextField_UsuarioUser.getText().isEmpty()) {
            Label_UsuarioUser.setForeground(new Color(70, 106, 124));
        } else {
            Label_UsuarioUser.setText("Usuario");
            Label_UsuarioUser.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_UsuarioUserKeyReleased

    private void TextField_PasswordUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PasswordUserKeyReleased
        if (TextField_PasswordUser.getText().isEmpty()) {
            Label_PasswordUser.setForeground(new Color(70, 106, 124));
        } else {
            Label_PasswordUser.setText("Contraseña");
            Label_PasswordUser.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_PasswordUserKeyReleased

    private void TableUsuariosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableUsuariosKeyReleased
        if (TableUsuarios.getSelectedRows().length > 0) {
            user.dataTableUsuarios();
        }
    }//GEN-LAST:event_TableUsuariosKeyReleased

    private void TableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableUsuariosMouseClicked
        if (TableUsuarios.getSelectedRows().length > 0) {
            user.dataTableUsuarios();
        }
    }//GEN-LAST:event_TableUsuariosMouseClicked

    private void ButtonImagenUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonImagenUserActionPerformed
        usuarios.cargarImagen(LabelImagenUser, true, null);
    }//GEN-LAST:event_ButtonImagenUserActionPerformed

    private void Button_UltimoUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoUsuariosActionPerformed
        new Paginador(tab, TableUsuarios, Label_PaginasUsuarios, 0).ultimo();
    }//GEN-LAST:event_Button_UltimoUsuariosActionPerformed

    private void Button_SiguienteUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteUsuariosActionPerformed
        new Paginador(tab, TableUsuarios, Label_PaginasUsuarios, 0).siguiente();
    }//GEN-LAST:event_Button_SiguienteUsuariosActionPerformed

    private void Button_AnteriorUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorUsuariosActionPerformed
        new Paginador(tab, TableUsuarios, Label_PaginasUsuarios, 0).anterior();
    }//GEN-LAST:event_Button_AnteriorUsuariosActionPerformed

    private void Button_PrimeroUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroUsuariosActionPerformed
        new Paginador(tab, TableUsuarios, Label_PaginasUsuarios, 1).primero();
    }//GEN-LAST:event_Button_PrimeroUsuariosActionPerformed

    private void TextField_BuscarUsuariosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarUsuariosKeyReleased
        user.searchUsuario(TableUsuarios, TextField_BuscarUsuarios.getText(),
                num_registro, pageSize);
    }//GEN-LAST:event_TextField_BuscarUsuariosKeyReleased

    private void Button_CancelarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarUsuariosActionPerformed
        user.restablecerUsuarios();
    }//GEN-LAST:event_Button_CancelarUsuariosActionPerformed
// </editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="CODIGO CAJAS"> 

    private void ButtonCajasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCajasActionPerformed
        tab = 8;
        jTabbedPane1.setSelectedIndex(8);
        Button_Config.setEnabled(true);
        objectcaja.restablecerCajas();
    }//GEN-LAST:event_ButtonCajasActionPerformed

    private void TextField_CajaRetirarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CajaRetirarKeyReleased
        objectcaja.cajaIngresos();
    }//GEN-LAST:event_TextField_CajaRetirarKeyReleased

    private void TextField_CajaRetirarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CajaRetirarKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_CajaRetirar);
    }//GEN-LAST:event_TextField_CajaRetirarKeyTyped

    private void Button_GuardarCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarCajaActionPerformed
        if (TabbedPaneCaja1.getSelectedIndex() == 0) {
            if (objectcaja.cajaIngresos()) {
                objectcaja.guardarIngresos();
                objectcaja.restablecerCajas();
            }
        } else {
            if (CheckBoxAsignarIngreso.isSelected()) {
                if (TextField_CajaIngresoInicial.getText().isEmpty()) {
                    Label_CajaIngresoInicial.setText("Introduzca el ingreso inicial");
                    Label_CajaIngresoInicial.setForeground(Color.RED);
                } else {
                    objectcaja.insertarIngresoInicial();
                    CheckBoxAsignarIngreso.setSelected(false);
                }
            } else {
                objectcaja.registrarCajas();
            }
        }
    }//GEN-LAST:event_Button_GuardarCajaActionPerformed

    private void Button_CancelarCajasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarCajasActionPerformed
        objectcaja.restablecerCajas();
        CheckBoxAsignarIngreso.setSelected(false);
    }//GEN-LAST:event_Button_CancelarCajasActionPerformed

    private void dateChooserCajasOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCajasOnSelectionChange
        objectcaja.getIngresos();
    }//GEN-LAST:event_dateChooserCajasOnSelectionChange

    private void TableCajasIngresosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableCajasIngresosKeyReleased
        if (TableCajasIngresos.getSelectedRows().length > 0) {
            objectcaja.dataTableCajas();
        }
    }//GEN-LAST:event_TableCajasIngresosKeyReleased

    private void TableCajasIngresosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableCajasIngresosMouseClicked
        if (TableCajasIngresos.getSelectedRows().length > 0) {
            objectcaja.dataTableCajas();
        }
    }//GEN-LAST:event_TableCajasIngresosMouseClicked

    private void TabbedPaneCaja1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneCaja1StateChanged
        if (TabbedPaneCaja1.getSelectedIndex() == 0) {
            if (TabbedPaneCaja2.getSelectedIndex() > 0) {
                TabbedPaneCaja2.setSelectedIndex(0);
            }
        } else {
            TabbedPaneCaja2.setSelectedIndex(1);
        }

    }//GEN-LAST:event_TabbedPaneCaja1StateChanged

    private void TabbedPaneCaja2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneCaja2StateChanged
        if (TabbedPaneCaja2.getSelectedIndex() == 0) {
            if (TabbedPaneCaja1.getSelectedIndex() > 0) {
                TabbedPaneCaja1.setSelectedIndex(0);
            }
        } else {
            TabbedPaneCaja1.setSelectedIndex(1);
        }
    }//GEN-LAST:event_TabbedPaneCaja2StateChanged

    private void TextField_CajaIngresoInicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CajaIngresoInicialKeyReleased
        if (TextField_CajaIngresoInicial.getText().isEmpty()) {
            Label_CajaIngresoInicial.setText("Ingreso inicial");
            Label_CajaIngresoInicial.setForeground(new Color(70, 106, 124));
        } else {
            Label_CajaIngresoInicial.setText("Ingreso inicial");
            Label_CajaIngresoInicial.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_CajaIngresoInicialKeyReleased

    private void TextField_CajaIngresoInicialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_CajaIngresoInicialKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_CajaIngresoInicial);
    }//GEN-LAST:event_TextField_CajaIngresoInicialKeyTyped

    private void SpinnerNumCajaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SpinnerNumCajaStateChanged
        Label_NumCaja.setText("Número de cajas");
        Label_NumCaja.setForeground(new Color(70, 106, 124));

    }//GEN-LAST:event_SpinnerNumCajaStateChanged

    private void TableCajasCajaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableCajasCajaKeyReleased
        if (TableCajasCaja.getSelectedRows().length > 0) {
            objectcaja.dataCajaIngresos();
            CheckBoxAsignarIngreso.setSelected(true);
        }
    }//GEN-LAST:event_TableCajasCajaKeyReleased

    private void TableCajasCajaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableCajasCajaMouseClicked
        if (TableCajasCaja.getSelectedRows().length > 0) {
            objectcaja.dataCajaIngresos();
            CheckBoxAsignarIngreso.setSelected(true);
        }
    }//GEN-LAST:event_TableCajasCajaMouseClicked

    private void CheckBoxAsignarIngresoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CheckBoxAsignarIngresoStateChanged
        if (CheckBoxAsignarIngreso.isSelected()) {
            timer1.stop();
        } else {
            timer1.start();
            objectcaja.restablecerCajas();
        }
        Label_CajaIngresoInicial.setText("Ingreso inicial");
        Label_CajaIngresoInicial.setForeground(new Color(70, 106, 124));
    }//GEN-LAST:event_CheckBoxAsignarIngresoStateChanged

    // </editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="CODIGO INVENTARIO"> 
    private void ButtonInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInventarioActionPerformed
        tab = 9;
        value = false;
        jTabbedPane1.setSelectedIndex(9);
        Button_Config.setEnabled(true);
        inventario.restablecerBodega();
    }//GEN-LAST:event_ButtonInventarioActionPerformed

    private void TextField_BuscarInventarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_BuscarInventarioKeyReleased
        if (TextField_BuscarInventario.getText().isEmpty()) {
            timer1.start();
        } else {
            timer1.stop();
        }
        switch (TabbedPaneInventario.getSelectedIndex()) {
            case 0:
                inventario.getBodegas(TextField_BuscarInventario.getText(),
                        num_registro, pageSize);
                break;
            case 1:
                inventario.getProductos(TextField_BuscarInventario.getText(),
                        num_registro, pageSize);
                break;
        }
    }//GEN-LAST:event_TextField_BuscarInventarioKeyReleased

    private void TextField_ExistenciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ExistenciaKeyReleased
        if (TextField_Existencia.getText().isEmpty()) {
            LabelExistenciaInventario.setForeground(new Color(70, 106, 124));
        } else {
            LabelExistenciaInventario.setText("Existencia");
            LabelExistenciaInventario.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_ExistenciaKeyReleased

    private void TableInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableInventarioMouseClicked
        if (TableInventario.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dataTableBodega();
        }
    }//GEN-LAST:event_TableInventarioMouseClicked

    private void TableInventarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableInventarioKeyReleased
        if (TableInventario.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dataTableBodega();
        }
    }//GEN-LAST:event_TableInventarioKeyReleased

    private void Button_CancelarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarInventarioActionPerformed
        timer1.start();
        inventario.restablecerBodega();
    }//GEN-LAST:event_Button_CancelarInventarioActionPerformed

    private void Button_GuardarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarInventarioActionPerformed
        timer1.start();
        inventario.updateExistencia();
        inventario.restablecerBodega();
    }//GEN-LAST:event_Button_GuardarInventarioActionPerformed

    private void TextField_ExistenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_ExistenciaKeyTyped
        evento.numberKeyPress(evt);
    }//GEN-LAST:event_TextField_ExistenciaKeyTyped

    private void ButtonInventarioExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInventarioExcelActionPerformed
        String[] title = {"Código", "Producto", "Existencia"};
        int[] colum = {0, 4}; // 0, 4 Columnas de las que no queremos obtener ningun valor
        new ExportData().exportarDataExcel(TableInventario, title, colum, "Bodega");
    }//GEN-LAST:event_ButtonInventarioExcelActionPerformed

    private void ButtonInventarioPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInventarioPDFActionPerformed
        String[] title = {"Código", "Producto", "Existencia"};
        int[] colum = {0, 4}; // 0, 4 Columnas de las que no queremos obtener ningun valor
        new ExportData().exportarDataPDF(TableInventario, title, colum, "Productos en inventario");
    }//GEN-LAST:event_ButtonInventarioPDFActionPerformed

    private void Button_UltimoInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoInventarioActionPerformed
        timer1.stop();
        new Paginador(tab, TableInventario, LabelPaginasInventario, 0).ultimo();
    }//GEN-LAST:event_Button_UltimoInventarioActionPerformed

    private void Button_SiguienteInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteInventarioActionPerformed
        timer1.stop();
        new Paginador(tab, TableInventario, LabelPaginasInventario, 0).siguiente();
    }//GEN-LAST:event_Button_SiguienteInventarioActionPerformed

    private void Button_AnteriorInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorInventarioActionPerformed
        timer1.stop();
        new Paginador(tab, TableInventario, LabelPaginasInventario, 0).anterior();
    }//GEN-LAST:event_Button_AnteriorInventarioActionPerformed

    private void Button_PrimeroInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroInventarioActionPerformed
        timer1.start();
        new Paginador(tab, TableInventario, LabelPaginasInventario, 1).primero();
    }//GEN-LAST:event_Button_PrimeroInventarioActionPerformed

    private void TextField_PrecioInventarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PrecioInventarioKeyReleased
        if (TextField_PrecioInventario.getText().isEmpty()) {
            LabelPrecioInventario.setForeground(new Color(70, 106, 124));
        } else {
            LabelPrecioInventario.setText("Precio");
            LabelPrecioInventario.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_PrecioInventarioKeyReleased

    private void TextField_PrecioInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_PrecioInventarioKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_PrecioInventario);
    }//GEN-LAST:event_TextField_PrecioInventarioKeyTyped

    private void ButtonInventarioProdExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInventarioProdExcelActionPerformed
        if (TableInventarioProductos.getSelectedRows().length > 0) {
            timer1.stop();
            String[] title = {"Código", "Producto", "Precio", "Descuento", 
                "Departamento", "Categoria"};
            int[] colum = {0};
            new ExportData().exportarDataExcel(TableInventarioProductos, title, 
                    colum, "Lista de productos");
        }
    }//GEN-LAST:event_ButtonInventarioProdExcelActionPerformed

    private void ButtonInventarioProdPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInventarioProdPDFActionPerformed
        if (TableInventarioProductos.getSelectedRows().length > 0) {
            timer1.stop();
            String[] title = {"Código", "Producto", "Precio", "Descuento", 
                "Departamento", "Categoria"};
            int[] colum = {0};
            new ExportData().exportarDataPDF(TableInventarioProductos, title, 
                    colum, "Lista de productos");
        }
    }//GEN-LAST:event_ButtonInventarioProdPDFActionPerformed

    private void TableInventarioProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableInventarioProductosMouseClicked
        if (TableInventarioProductos.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dateTableProductos();
        }
    }//GEN-LAST:event_TableInventarioProductosMouseClicked

    private void TableInventarioProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableInventarioProductosKeyReleased
        if (TableInventarioProductos.getSelectedRows().length > 0) {
            timer1.stop();
            inventario.dateTableProductos();
        }
    }//GEN-LAST:event_TableInventarioProductosKeyReleased

    private void Button_UltimoInventarioProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_UltimoInventarioProActionPerformed
        timer1.stop();
        new Paginador(10, TableInventarioProductos, LabelPaginasInventarioPro, 0).ultimo();
    }//GEN-LAST:event_Button_UltimoInventarioProActionPerformed

    private void Button_SiguienteInventarioProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SiguienteInventarioProActionPerformed
        timer1.stop();
        new Paginador(10, TableInventarioProductos, LabelPaginasInventarioPro, 0).siguiente();
    }//GEN-LAST:event_Button_SiguienteInventarioProActionPerformed

    private void Button_AnteriorInventarioProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AnteriorInventarioProActionPerformed
        timer1.stop();
        new Paginador(10, TableInventarioProductos, LabelPaginasInventarioPro, 0).anterior();
    }//GEN-LAST:event_Button_AnteriorInventarioProActionPerformed

    private void Button_PrimeroInventarioProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_PrimeroInventarioProActionPerformed
        timer1.start();
        new Paginador(10, TableInventarioProductos, LabelPaginasInventarioPro, 1).primero();
    }//GEN-LAST:event_Button_PrimeroInventarioProActionPerformed

    private void Button_CancelarInventarioProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CancelarInventarioProdActionPerformed
        timer1.start();
        inventario.restablecerBodega();
    }//GEN-LAST:event_Button_CancelarInventarioProdActionPerformed

    private void Button_GuardarInventarioProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_GuardarInventarioProdActionPerformed
        timer1.start();
        inventario.updateProductos();
    }//GEN-LAST:event_Button_GuardarInventarioProdActionPerformed

    private void TextField_DescuentoInventarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DescuentoInventarioKeyReleased
        if (TextField_DescuentoInventario.getText().isEmpty()) {
            LabelDescuentoInventario.setForeground(new Color(70, 106, 124));
        } else {
            LabelDescuentoInventario.setText("Descuento");
            LabelDescuentoInventario.setForeground(new Color(0, 153, 51));
        }
    }//GEN-LAST:event_TextField_DescuentoInventarioKeyReleased

    private void TextField_DescuentoInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextField_DescuentoInventarioKeyTyped
        evento.numberDecimalKeyPress(evt, TextField_DescuentoInventario);
    }//GEN-LAST:event_TextField_DescuentoInventarioKeyTyped

    private void TabbedPaneInventarioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneInventarioStateChanged
        if (TabbedPaneInventario.getSelectedIndex() == 0) {
            if (value) {
                inventario.restablecerBodega();
            }
        } else {
            inventario.restablecerBodega();
            value = true;
        }
    }//GEN-LAST:event_TabbedPaneInventarioStateChanged

    // </editor-fold> 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema(null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonBuscarProductoVentas;
    private javax.swing.JButton ButtonCajas;
    private javax.swing.JButton ButtonImagenUser;
    private javax.swing.JButton ButtonInventario;
    private javax.swing.JButton ButtonInventarioExcel;
    private javax.swing.JButton ButtonInventarioPDF;
    private javax.swing.JButton ButtonInventarioProdExcel;
    private javax.swing.JButton ButtonInventarioProdPDF;
    private javax.swing.JButton ButtonUsuarios;
    private javax.swing.JButton Button_ActDesac;
    private javax.swing.JButton Button_ActDesacProv;
    private javax.swing.JButton Button_AnteriorCLT;
    private javax.swing.JButton Button_AnteriorCompras;
    private javax.swing.JButton Button_AnteriorInventario;
    private javax.swing.JButton Button_AnteriorInventarioPro;
    private javax.swing.JButton Button_AnteriorPRO;
    private javax.swing.JButton Button_AnteriorProducto;
    private javax.swing.JButton Button_AnteriorUsuarios;
    private javax.swing.JButton Button_AnteriorVentas;
    private javax.swing.JButton Button_CancelarCLT;
    private javax.swing.JButton Button_CancelarCajas;
    private javax.swing.JButton Button_CancelarCatDpt;
    private javax.swing.JButton Button_CancelarCompras;
    private javax.swing.JButton Button_CancelarInventario;
    private javax.swing.JButton Button_CancelarInventarioProd;
    private javax.swing.JButton Button_CancelarProducto;
    private javax.swing.JButton Button_CancelarProv;
    private javax.swing.JButton Button_CancelarUsuarios;
    private javax.swing.JButton Button_CancelarVentas;
    private javax.swing.JButton Button_Cat_Dpt;
    private javax.swing.JButton Button_Cliente;
    private javax.swing.JButton Button_Compras;
    private javax.swing.JButton Button_Config;
    private javax.swing.JButton Button_EliminarCatDpt;
    private javax.swing.JButton Button_EliminarCompras;
    private javax.swing.JButton Button_FacturaCliente;
    private javax.swing.JButton Button_FacturaProv;
    private javax.swing.JButton Button_FacturaVentas;
    private javax.swing.JButton Button_GuardarCaja;
    private javax.swing.JButton Button_GuardarCatDpt;
    private javax.swing.JButton Button_GuardarCliente;
    private javax.swing.JButton Button_GuardarCompras;
    private javax.swing.JButton Button_GuardarInventario;
    private javax.swing.JButton Button_GuardarInventarioProd;
    private javax.swing.JButton Button_GuardarProducto;
    private javax.swing.JButton Button_GuardarProv;
    private javax.swing.JButton Button_GuardarUsuarios;
    private javax.swing.JButton Button_GuardarVentas;
    private javax.swing.JButton Button_PrimeroCLT;
    private javax.swing.JButton Button_PrimeroCompras;
    private javax.swing.JButton Button_PrimeroInventario;
    private javax.swing.JButton Button_PrimeroInventarioPro;
    private javax.swing.JButton Button_PrimeroPRO;
    private javax.swing.JButton Button_PrimeroProducto;
    private javax.swing.JButton Button_PrimeroUsuarios;
    private javax.swing.JButton Button_PrimeroVentas;
    private javax.swing.JButton Button_Productos;
    private javax.swing.JButton Button_Proveedores;
    private javax.swing.JButton Button_SiguienteCLT;
    private javax.swing.JButton Button_SiguienteCompras;
    private javax.swing.JButton Button_SiguienteInventario;
    private javax.swing.JButton Button_SiguienteInventarioPro;
    private javax.swing.JButton Button_SiguientePRO;
    private javax.swing.JButton Button_SiguienteProducto;
    private javax.swing.JButton Button_SiguienteUsuarios;
    private javax.swing.JButton Button_SiguienteVentas;
    private javax.swing.JButton Button_UltimoCLT;
    private javax.swing.JButton Button_UltimoCompras;
    private javax.swing.JButton Button_UltimoInventario;
    private javax.swing.JButton Button_UltimoInventarioPro;
    private javax.swing.JButton Button_UltimoPRO;
    private javax.swing.JButton Button_UltimoProducto;
    private javax.swing.JButton Button_UltimoUsuarios;
    private javax.swing.JButton Button_UltimoVentas;
    private javax.swing.JButton Button_Ventas;
    private javax.swing.JCheckBox CheckBoxAsignarIngreso;
    private javax.swing.JCheckBox CheckBoxBodega;
    private javax.swing.JCheckBox CheckBoxCompraCredito;
    private javax.swing.JCheckBox CheckBoxVentas;
    private javax.swing.JComboBox<String> ComboBoxRoles;
    private javax.swing.JComboBox ComboBox_CategoriaPro;
    private javax.swing.JComboBox ComboBox_DepartamentoPro;
    private javax.swing.JLabel Label;
    private javax.swing.JLabel Label1;
    private javax.swing.JLabel LabelApeRecCliente;
    private javax.swing.JLabel LabelCompraDeuda;
    private javax.swing.JLabel LabelCompraDeudaRecibo;
    private javax.swing.JLabel LabelCompraEncaja;
    private javax.swing.JLabel LabelCompraFechaRecibo;
    private javax.swing.JLabel LabelCompraMontoPagar;
    private javax.swing.JLabel LabelCompraPago;
    private javax.swing.JLabel LabelCompraProveedorRecibo;
    private javax.swing.JLabel LabelCompraSaldoRecibo;
    private javax.swing.JLabel LabelCompraTotalPagarRecibo;
    private javax.swing.JLabel LabelDescuentoInventario;
    private javax.swing.JLabel LabelDeudaRecCliente;
    private javax.swing.JLabel LabelDeudaRecProv;
    private javax.swing.JLabel LabelExistenciaInventario;
    private javax.swing.JLabel LabelFechaRecCliente;
    private javax.swing.JLabel LabelFechaRecProv;
    private javax.swing.JLabel LabelImagenUser;
    private javax.swing.JLabel LabelListaProductosInventario;
    private javax.swing.JLabel LabelMensajeVentasCliente;
    private javax.swing.JLabel LabelNomRecCliente;
    private javax.swing.JLabel LabelPaginasInventario;
    private javax.swing.JLabel LabelPaginasInventarioPro;
    private javax.swing.JLabel LabelPrecioInventario;
    private javax.swing.JLabel LabelProductoCod;
    private javax.swing.JLabel LabelProductoImagenCod;
    private javax.swing.JLabel LabelProvRecProveedor;
    private javax.swing.JLabel LabelProveedorCP;
    private javax.swing.JLabel LabelUltPagoRecCliente;
    private javax.swing.JLabel LabelUltPagoRecProv;
    private javax.swing.JLabel Label_ApellidoCliente;
    private javax.swing.JLabel Label_ApellidoCliente1;
    private javax.swing.JLabel Label_ApellidoCliente2;
    private javax.swing.JLabel Label_ApellidosUser;
    private javax.swing.JLabel Label_CajaIngresoInicial;
    private javax.swing.JLabel Label_CajaIngresos;
    private javax.swing.JLabel Label_CajaIngresos2;
    private javax.swing.JLabel Label_CajaNumero;
    private javax.swing.JLabel Label_CajaRetirarIngreso;
    private javax.swing.JLabel Label_CajaRetirarIngreso2;
    private javax.swing.JLabel Label_CantidadCP;
    private javax.swing.JLabel Label_Cat;
    private javax.swing.JLabel Label_CategoriaPDT;
    private javax.swing.JLabel Label_DepartamentoPDT;
    private javax.swing.JLabel Label_DescripcionCP;
    private javax.swing.JLabel Label_DescripcionPDT;
    private javax.swing.JLabel Label_DeudaAnteriorReciboVentas;
    private javax.swing.JLabel Label_DeudaReciboVentas;
    private javax.swing.JLabel Label_DeudaTotal;
    private javax.swing.JLabel Label_DeudaTotalReciboVentas;
    private javax.swing.JLabel Label_DireccionCliente;
    private javax.swing.JLabel Label_DireccionProv;
    private javax.swing.JLabel Label_DireccionUser;
    private javax.swing.JLabel Label_Dpt;
    private javax.swing.JLabel Label_EmailCliente;
    private javax.swing.JLabel Label_EmailProv;
    private javax.swing.JLabel Label_EmailUser;
    private javax.swing.JLabel Label_EstadoProv;
    private javax.swing.JLabel Label_FechaReciboVentas;
    private javax.swing.JLabel Label_IdCliente;
    private javax.swing.JLabel Label_IdProv;
    private javax.swing.JLabel Label_ImporteCP;
    private javax.swing.JLabel Label_ImporteCP1;
    private javax.swing.JLabel Label_ImporteCP2;
    private javax.swing.JLabel Label_IngresoIniVentas;
    private javax.swing.JLabel Label_IngresoTotalVentas;
    private javax.swing.JLabel Label_IngresoVentasVentas;
    private javax.swing.JLabel Label_MaximoCajas;
    private javax.swing.JLabel Label_MontoPagarVentas;
    private javax.swing.JLabel Label_NombreCliente;
    private javax.swing.JLabel Label_NombreReciboVentas;
    private javax.swing.JLabel Label_NombreUser;
    private javax.swing.JLabel Label_NombreUser2;
    private javax.swing.JLabel Label_NombreUser3;
    private javax.swing.JLabel Label_NombreUser4;
    private javax.swing.JLabel Label_NumCaja;
    private javax.swing.JLabel Label_PaginasClientes;
    private javax.swing.JLabel Label_PaginasCompra;
    private javax.swing.JLabel Label_PaginasProductos;
    private javax.swing.JLabel Label_PaginasProveedor;
    private javax.swing.JLabel Label_PaginasUsuarios;
    private javax.swing.JLabel Label_PaginasVentas;
    private javax.swing.JLabel Label_PagoCliente;
    private javax.swing.JLabel Label_PagoCliente1;
    private javax.swing.JLabel Label_PagoProv;
    private javax.swing.JLabel Label_PagoVenta;
    private javax.swing.JLabel Label_PasswordUser;
    private javax.swing.JLabel Label_PrecioCP;
    private javax.swing.JLabel Label_PrecioVentaProducto;
    private javax.swing.JLabel Label_ProveedorProv;
    private javax.swing.JLabel Label_RolesUser;
    private javax.swing.JLabel Label_SuCambio;
    private javax.swing.JLabel Label_SuCambio1;
    private javax.swing.JLabel Label_SuCambio2;
    private javax.swing.JLabel Label_TelefonoCliente;
    private javax.swing.JLabel Label_TelefonoCliente2;
    private javax.swing.JLabel Label_TelefonoCliente3;
    private javax.swing.JLabel Label_TelefonoProv;
    private javax.swing.JLabel Label_TelefonoUser;
    private javax.swing.JLabel Label_UltimoPagoReciboVentas;
    private javax.swing.JLabel Label_UsuarioUser;
    private javax.swing.JPanel PanelBanner;
    private javax.swing.JPanel PanelCodeProducto;
    private javax.swing.JPanel PanelReciboCompra;
    private javax.swing.JPanel PanelReciboVenta;
    private javax.swing.JRadioButton RadioButton_Activo;
    private javax.swing.JRadioButton RadioButton_ActivoProv;
    private javax.swing.JRadioButton RadioButton_Cat;
    private javax.swing.JRadioButton RadioButton_Dpt;
    private javax.swing.JRadioButton RadioButton_Inactivo;
    private javax.swing.JRadioButton RadioButton_InactivoProv;
    private javax.swing.JRadioButton RadioButton_IngresarCliente;
    private javax.swing.JRadioButton RadioButton_IngresarPro;
    private javax.swing.JRadioButton RadioButton_PagosCliente;
    private javax.swing.JRadioButton RadioButton_PagosPro;
    private javax.swing.JSpinner SpinnerBodega;
    private javax.swing.JSpinner SpinnerNumCaja;
    private javax.swing.JTabbedPane TabbedPaneCaja1;
    private javax.swing.JTabbedPane TabbedPaneCaja2;
    private javax.swing.JTabbedPane TabbedPaneCompras;
    private javax.swing.JTabbedPane TabbedPaneInventario;
    private javax.swing.JTable TableCajasCaja;
    private javax.swing.JTable TableCajasIngresos;
    private javax.swing.JTable TableInventario;
    private javax.swing.JTable TableInventarioProductos;
    private javax.swing.JTable TableUsuarios;
    private javax.swing.JTable Table_Cat;
    private javax.swing.JTable Table_Clientes;
    private javax.swing.JTable Table_Compras;
    private javax.swing.JTable Table_ComprasProveedor;
    private javax.swing.JTable Table_Dpt;
    private javax.swing.JTable Table_ProductosCompras;
    private javax.swing.JTable Table_ProductosProd;
    private javax.swing.JTable Table_Proveedores;
    private javax.swing.JTable Table_ReportesCLT;
    private javax.swing.JTable Table_ReportesProveedor;
    private javax.swing.JTable Table_VentasClientes;
    private javax.swing.JTable Table_VentasTempo;
    private javax.swing.JTextField TextFieldBuscarProductos;
    private javax.swing.JTextField TextField_ApellidoCliente;
    private javax.swing.JTextField TextField_ApellidosUser;
    private javax.swing.JTextField TextField_BuscarCliente;
    private javax.swing.JTextField TextField_BuscarCliente2;
    private javax.swing.JTextField TextField_BuscarCompras;
    private javax.swing.JTextField TextField_BuscarDpt;
    private javax.swing.JTextField TextField_BuscarInventario;
    private javax.swing.JTextField TextField_BuscarProductosVentas;
    private javax.swing.JTextField TextField_BuscarProvCompras;
    private javax.swing.JTextField TextField_BuscarProveedor;
    private javax.swing.JTextField TextField_BuscarUsuarios;
    private javax.swing.JTextField TextField_CajaIngresoInicial;
    private javax.swing.JTextField TextField_CajaRetirar;
    private javax.swing.JTextField TextField_CantidadCP;
    private javax.swing.JTextField TextField_Categoria;
    private javax.swing.JTextField TextField_ComprasPagos;
    private javax.swing.JTextField TextField_Departamento;
    private javax.swing.JTextField TextField_DescripcionCP;
    private javax.swing.JTextField TextField_DescripcionPDT;
    private javax.swing.JTextField TextField_DescuentoInventario;
    private javax.swing.JTextField TextField_DireccionCliente;
    private javax.swing.JTextField TextField_DireccionProv;
    private javax.swing.JTextField TextField_DireccionUser;
    private javax.swing.JTextField TextField_EmailCliente;
    private javax.swing.JTextField TextField_EmailProv;
    private javax.swing.JTextField TextField_EmailUser;
    private javax.swing.JTextField TextField_Existencia;
    private javax.swing.JTextField TextField_IdCliente;
    private javax.swing.JTextField TextField_IdProv;
    private javax.swing.JTextField TextField_NombreCliente;
    private javax.swing.JTextField TextField_NombreUser;
    private javax.swing.JTextField TextField_PagoConVentas;
    private javax.swing.JTextField TextField_PagosCliente;
    private javax.swing.JTextField TextField_PagosProv;
    private javax.swing.JTextField TextField_PasswordUser;
    private javax.swing.JTextField TextField_PrecioCP;
    private javax.swing.JTextField TextField_PrecioInventario;
    private javax.swing.JTextField TextField_PrecioVentaPDT;
    private javax.swing.JTextField TextField_ProveedorProv;
    private javax.swing.JTextField TextField_TelefonoCliente;
    private javax.swing.JTextField TextField_TelefonoProv;
    private javax.swing.JTextField TextField_TelefonoUser;
    private javax.swing.JTextField TextField_UsuarioUser;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroupProv;
    private datechooser.beans.DateChooserCombo dateChooserCajas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel lblCaja;
    private javax.swing.JLabel lblMensajeVenta;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel panelReciboCliente;
    private javax.swing.JPanel panelReciboProv;
    // End of variables declaration//GEN-END:variables
}
