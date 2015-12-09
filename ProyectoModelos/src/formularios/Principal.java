/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import java.io.*;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import jxl.*;
import jxl.read.biff.BiffException;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Erick
 */
public class Principal extends javax.swing.JFrame {
    int n,it;//n: numero de datos, it:numero de iteraciones(numero de numeros aleatorios)
    DefaultTableModel modelo,weiner,caminata;
    DefaultListModel lista;    
    double z[]= new double [n];    
    public static XYSeriesCollection collection =new XYSeriesCollection();   
    
    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();
        jPanel1.setVisible(false);
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel4.setVisible(false);
        setExtendedState(MAXIMIZED_BOTH);
    }
    public void quitarFocoDatos(){
        if(tblDatos.isEditing())
            tblDatos.getCellEditor().stopCellEditing();
    }
    public void verificar(){
       try{
            n=Integer.valueOf(txtNumeroDatos.getText());            
            if(n==0){
                JOptionPane.showMessageDialog(null,"INGRESE UN VALOR DIFERENTE DE CERO","ADVERTENCIA",JOptionPane.WARNING_MESSAGE);
                txtNumeroDatos.setText("");
                txtNumeroDatos.requestFocus();
            }else{
                inicio();
                jDialog1.dispose();
                txtNumeroDatos.setText("");
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null,"EL CAMPO: NÚMERO DE DATOS ESTÁ VACÍO O "
                    + "\nEL VALOR INTRODUCIDO NO ES UN NÚMERO VÁLIDO","ERROR",JOptionPane.ERROR_MESSAGE);
            txtNumeroDatos.setText("");
            txtNumeroDatos.requestFocus();
        }       
    }
    public void verificarIteraciones(){
       try{
            it=Integer.valueOf(txtNumeroIteraciones.getText());
            if(it==0){
                JOptionPane.showMessageDialog(null,"INGRESE UN VALOR DIFERENTE DE CERO","ADVERTENCIA",JOptionPane.WARNING_MESSAGE);
                txtNumeroIteraciones.setText("");
                txtNumeroIteraciones.requestFocus();
            }else{
                habilitarPaneles();
                caminataAleatoria();
                procesosWiener();
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null,"EL CAMPO: NÚMERO DE ITERACIONES ESTÁ VACÍO O "
                    + "\nEL VALOR INTRODUCIDO NO ES UN NÚMERO VÁLIDO","ERROR",JOptionPane.ERROR_MESSAGE);
            txtNumeroIteraciones.setText("");
            txtNumeroIteraciones.requestFocus();            
        }
    }
    public void verificarIteracionesImportados(){
       try{
            it=Integer.valueOf(txtNumeroIteraciones.getText());
            if(it==0){
                JOptionPane.showMessageDialog(null,"INGRESE UN VALOR DIFERENTE DE CERO","ADVERTENCIA",JOptionPane.WARNING_MESSAGE);
                txtNumeroIteraciones.setText("");
                txtNumeroIteraciones.requestFocus();
            }else{
                habilitarPaneles();
                caminataAleatoriaImportados();
                procesosWienerImportados();
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null,"EL CAMPO: NÚMERO DE ITERACIONES ESTÁ VACÍO O "
                    + "\nEL VALOR INTRODUCIDO NO ES UN NÚMERO VÁLIDO","ERROR",JOptionPane.ERROR_MESSAGE);
            txtNumeroIteraciones.setText("");
            txtNumeroIteraciones.requestFocus();            
        }
    }
    public void inicio(){
        String [] titulos={"Nº","Dato"};
        modelo=new DefaultTableModel(null,titulos);
        tblDatos.setModel(modelo);
        crearFila();
        jPanel1.setVisible(true);
    }
    public void crearFila(){
        Object[]fila=new Object[n];        
        for(int i=0;i<n;i++){            
            for(int j=0;j<2;j++){
                if(j==0)
                    fila[j]=i+1;
                else
                    fila[j]="";
            }
            modelo.addRow(fila);
        }
    }
    public void importar(){        
        JFileChooser examinar=new JFileChooser();
        FileNameExtensionFilter filtro= new FileNameExtensionFilter("Archivos de Excel","xls");
        examinar.setFileFilter(filtro);
        examinar.setDialogTitle("Abrir Archivo");        
        examinar.setMultiSelectionEnabled(false);
        examinar.setAcceptAllFileFilterUsed(false);
        File archivo;
        if(examinar.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
            archivo=examinar.getSelectedFile().getAbsoluteFile();
            modelo=new DefaultTableModel();
        tblDatos.setModel(modelo);
            try {
                Workbook leer=Workbook.getWorkbook(archivo);
                for(int i=0;i<leer.getNumberOfSheets();i++){
                    Sheet hoja=leer.getSheet(i);
                    int col=hoja.getColumns();
                    int fil=hoja.getRows();
                    Object data[]=new Object[col];
                    for(int f=0;f<fil;f++){
                        for(int c=0;c<col;c++){
                            if(f==0)
                                modelo.addColumn(hoja.getCell(c, f).getContents().toUpperCase());                            
                            if(f>=1){
                                data[c]=hoja.getCell(c,f).getContents();                                
                            }
                        }
                        modelo.addRow(data);
                    }                    
                }
                modelo.removeRow(0);
                n=tblDatos.getRowCount();
                jPanel1.setVisible(true);                
            } catch (IOException | BiffException | IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null,"Ocurrió un error al importar","ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void habilitarPaneles(){
        jPanel2.setVisible(true);
        jPanel3.setVisible(true);
        jPanel4.setVisible(true);
    }

    public double[] obtenerDatos(){
//        int n=Integer.valueOf(txtNumeroDatos.getText());
        double []reg=new double[n];
        for(int i=0;i<n;i++){
            reg[i]=Double.parseDouble((String) tblDatos.getValueAt(i,1));
        }
        return reg;   
    }    
    public double[] obtenerDatosImportados(){
        double []reg=new double[tblDatos.getRowCount()];
        for(int i=0;i<tblDatos.getRowCount();i++){
            reg[i]=Double.parseDouble((String) tblDatos.getValueAt(i,0));
        }
        return reg;   
    } 
    public void Mediana(double vector[]) {
        double valor;
        double Mediana;
        double datos;
        for(int i=1;i<obtenerDatos().length;i++) {
            for(int j=0;j<(obtenerDatos().length)-1;j++) {
                if (vector[j]>vector[j+1]) {
                    double aux;
                    aux=vector[j];
                    vector[j]=vector[j+1];
                    vector[j+1]=aux;
                }
            }
        }
        int numDatos=n-1;
        if(n%2==0){
            valor=(vector[(vector.length/2)-1]+vector[(vector.length/2)])/2;
        }
        else{
            valor=vector[(vector.length-1)/2];
        }
        Mediana=valor;
        txtMediana.setText(String.valueOf(Mediana));
    }
    public void MedianaImportados(double vector[]) {
        double valor;
        double Mediana;
        double datos;
        datos=obtenerDatosImportados().length;
        for(int i=1;i<obtenerDatosImportados().length;i++) {
            for(int j=0;j<(obtenerDatosImportados().length)-1;j++) {
                if (vector[j]>vector[j+1]) {
                    double aux;
                    aux=vector[j];
                    vector[j]=vector[j+1];
                    vector[j+1]=aux;
                }
            }
        }
        if(datos%2==0){
            valor=(vector[(vector.length/2)-1]+vector[(vector.length/2)])/2;
        }
        else{
            valor=vector[(vector.length-1)/2];
        }
        Mediana=valor;
        txtMediana.setText(String.valueOf(Mediana));
    }
    public void Moda(double vector[]) {
    int [] numRepetidos=new int [n];  
    int contador=0;
    int mayor=0;
    int posicion = 0;
    double Moda=-1;
    int frecuenciaTemp, frecuenciaModa = 0; 
        
        for (int i=0; i < vector.length-1; i++){
            frecuenciaTemp = 1;
            for(int j = i+1 ; j< vector.length; j++){
                if(vector[i] == vector[j])
                    frecuenciaTemp ++;                
            }
            if(frecuenciaTemp > frecuenciaModa){
                frecuenciaModa = frecuenciaTemp;
                Moda = vector[i];
            }
        }
        txtModa.setText(String.valueOf(Moda));

    }
    public void ModaImportados(double vector[]) {
    int [] numRepetidos=new int [obtenerDatosImportados().length];  
    int contador=0;
    int mayor=0;
    int posicion = 0;
    double Moda=-1;
    int frecuenciaTemp, frecuenciaModa = 0; 
        
        for (int i=0; i < vector.length-1; i++){
            frecuenciaTemp = 1;
            for(int j = i+1 ; j< vector.length; j++){
                if(vector[i] == vector[j])
                    frecuenciaTemp ++;                
            }
            if(frecuenciaTemp > frecuenciaModa){
                frecuenciaModa = frecuenciaTemp;
                Moda = vector[i];
            }
        }
        txtModa.setText(String.valueOf(Moda));

    }
    public void EstadisticaDescriptiva(){
        MedianaImportados(obtenerDatos());
        VarianzaImportados();
        ModaImportados(obtenerDatos());
        rango();
    }
    public void EstadisticaDescriptivaImportados(){
        MedianaImportados(obtenerDatosImportados());
        VarianzaImportados();
        ModaImportados(obtenerDatosImportados());
        rangoImportados();
    }
    public void Varianza(){
       double suma=0;
       double Media;
       double sumaVarianza=0;
       double sumaCurtosis=0;
       double sumaSesgo=0;
       double Sesgo=0;
       double Curtosis=0;
       double varianza=0;
       double desviacionEstandar;
       double errorTipico=0;
       double numero=n;
       double JB;
       double vector[]= new double [n];
       vector=obtenerDatos();
       for(int i=0;i<obtenerDatos().length;i++){
            suma=suma+obtenerDatos()[i];
        }
       Media=suma/n;
       for(int i=0;i<obtenerDatos().length;i++){
           sumaVarianza=sumaVarianza+Math.pow(obtenerDatos()[i]-Media,2);
       }
       varianza=sumaVarianza/(n-1);
       desviacionEstandar=Math.sqrt(varianza);
       for(int i=0;i<obtenerDatos().length;i++){
           sumaSesgo=sumaSesgo +Math.pow((obtenerDatos()[i]-Media)/desviacionEstandar,3);
       }
       Sesgo=(numero/((numero-1)*(numero-2)))*sumaSesgo;
       for(int i=0;i<obtenerDatos().length;i++){
           sumaCurtosis=sumaCurtosis+Math.pow((obtenerDatos()[i]-Media)/desviacionEstandar,4);
       }
       Curtosis=(((numero*(numero+1))/((numero-1)*(numero-2)*(numero-3)))*sumaCurtosis)-((3*Math.pow(numero-1,2))/((numero-2)*(numero-3)));
       errorTipico=desviacionEstandar/Math.sqrt(n);
       JB=numero*((Math.pow(Sesgo, 2)/6)+((Math.pow(Curtosis, 2))/24));
       txtError.setText(String.valueOf(errorTipico));
       txtMedia.setText(String.valueOf(Media));
       txtCurtosis.setText(String.valueOf(Curtosis));
       txtSesgo.setText(String.valueOf(Sesgo));
       txtVarianza.setText(String.valueOf(varianza));
       txtDEsviacion.setText(String.valueOf(desviacionEstandar));
       txtSuma.setText(String.valueOf(suma));
       txtCuenta.setText(String.valueOf(numero));
       txtPruebJB.setText(String.valueOf(JB));
    }
public void VarianzaImportados(){
       double suma=0;
       double Media;
       double sumaVarianza=0;
       double sumaCurtosis=0;
       double sumaSesgo=0;
       double Sesgo=0;
       double Curtosis=0;
       double varianza=0;
       double desviacionEstandar;
       double errorTipico=0;
       double numero=obtenerDatosImportados().length;
       double JB;
       double vector[]= new double [obtenerDatosImportados().length];
       vector=obtenerDatosImportados();
       for(int i=0;i<obtenerDatosImportados().length;i++){
            suma=suma+obtenerDatosImportados()[i];
        }
       Media=suma/numero;
       for(int i=0;i<obtenerDatosImportados().length;i++){
           sumaVarianza=sumaVarianza+Math.pow(obtenerDatosImportados()[i]-Media,2);
       }
       varianza=sumaVarianza/(numero-1);
       desviacionEstandar=Math.sqrt(varianza);
       for(int i=0;i<obtenerDatosImportados().length;i++){
           sumaSesgo=sumaSesgo +Math.pow((obtenerDatosImportados()[i]-Media)/desviacionEstandar,3);
       }
       Sesgo=(numero/((numero-1)*(numero-2)))*sumaSesgo;
       for(int i=0;i<obtenerDatosImportados().length;i++){
           sumaCurtosis=sumaCurtosis+Math.pow((obtenerDatosImportados()[i]-Media)/desviacionEstandar,4);
       }
       Curtosis=(((numero*(numero+1))/((numero-1)*(numero-2)*(numero-3)))*sumaCurtosis)-((3*Math.pow(numero-1,2))/((numero-2)*(numero-3)));
       errorTipico=desviacionEstandar/Math.sqrt(numero);
       JB=numero*((Math.pow(Sesgo, 2)/6)+((Math.pow(Curtosis, 2))/24));
       txtError.setText(String.valueOf(errorTipico));
       txtMedia.setText(String.valueOf(Media));
       txtCurtosis.setText(String.valueOf(Curtosis));
       txtSesgo.setText(String.valueOf(Sesgo));
       txtVarianza.setText(String.valueOf(varianza));
       txtDEsviacion.setText(String.valueOf(desviacionEstandar));
       txtSuma.setText(String.valueOf(suma));
       txtCuenta.setText(String.valueOf(numero));
       txtPruebJB.setText(String.valueOf(JB));
    }
    public void rango(){
    double mayor=0;
    double menor=99999;
    double rango=0;
    double maximo;
    double minimo;
    double vector[]= new double [n];
    vector=obtenerDatos();    
            for(int j=0;j<vector.length;j++) {
                    if (vector[j]>mayor) {
                        mayor=vector[j];
                    }
                    if (vector[j]<menor) {
                        menor=vector[j];
                    }
                          
        }  
        rango=mayor-menor;
        maximo=mayor;
        minimo=menor;
        txtRango.setText(String.valueOf(rango));
        txtMin.setText(String.valueOf(minimo));
        txtMax.setText(String.valueOf(maximo));
    }
    public void rangoImportados(){
    double mayor=0;
    double menor=99999;
    double rango=0;
    double maximo;
    double minimo;
    double vector[]= new double [obtenerDatosImportados().length];
    vector=obtenerDatosImportados();    
            for(int j=0;j<vector.length;j++) {
                    if (vector[j]>mayor) {
                        mayor=vector[j];
                    }
                    if (vector[j]<menor) {
                        menor=vector[j];
                    }
                          
        }  
        rango=mayor-menor;
        maximo=mayor;
        minimo=menor;
        txtRango.setText(String.valueOf(rango));
        txtMin.setText(String.valueOf(minimo));
        txtMax.setText(String.valueOf(maximo));
    }
    public void caminataAleatoria(){
        double r1[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];
        double r2[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];        
        double x[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];
        double z[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];
        int fila=0;
        for(int i=0;i<Integer.valueOf(txtNumeroIteraciones.getText());i++){
            r1[i] = (double) Math.rint((Math.random()*(1-0+0)+0)*10000)/10000;
            r2[i] = (double) Math.rint((Math.random()*(1-0+0)+0)*10000)/10000;
            z[i]=(double) Math.rint(((Math.sqrt(-2*Math.log(r1[i])))*(Math.sin((2*Math.PI)*r2[i])))*10000)/10000;
            x[i]=(double) Math.rint((10+112*z[i])*10000)/10000;
        }
        
        String []titulos={"#","R1","R2","Z","X"};
        String [] registros=new String [5];
        caminata = new DefaultTableModel(null,titulos);
        while(fila<Integer.valueOf(txtNumeroIteraciones.getText())){
                registros[0]=String.valueOf(fila+1);
                registros[1]=String.valueOf(r1[fila]);
                registros[2]=String.valueOf(r2[fila]);
                registros[3]=String.valueOf(z[fila]);
                registros[4]=String.valueOf(x[fila]);
                caminata.addRow(registros);
                fila++;
            }
            jtbCaminata.setModel(caminata);
    }
    public void caminataAleatoriaImportados(){
        double r1[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];
        double r2[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];        
        double x[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];
        double z[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];
        int fila=0;
        for(int i=0;i<Integer.valueOf(txtNumeroIteraciones.getText());i++){
            r1[i] = (double) Math.rint((Math.random()*(1-0+0)+0)*10000)/10000;
            r2[i] = (double) Math.rint((Math.random()*(1-0+0)+0)*10000)/10000;
            z[i]=(double) Math.rint(((Math.sqrt(-2*Math.log(r1[i])))*(Math.sin((2*Math.PI)*r2[i])))*10000)/10000;
            x[i]=(double) Math.rint((10+112*z[i])*10000)/10000;
        }
        
        String []titulos={"#","R1","R2","Z","X"};
        String [] registros=new String [5];
        caminata = new DefaultTableModel(null,titulos);
        while(fila<Integer.valueOf(txtNumeroIteraciones.getText())){
                registros[0]=String.valueOf(fila+1);
                registros[1]=String.valueOf(r1[fila]);
                registros[2]=String.valueOf(r2[fila]);
                registros[3]=String.valueOf(z[fila]);
                registros[4]=String.valueOf(x[fila]);
                caminata.addRow(registros);
                fila++;
            }
            jtbCaminata.setModel(caminata);
    }
    public void procesosWiener(){
        int fila=0;
        float paso,ten,vol,precio,cambio,nvalor;
        double prec[]=new double[Integer.valueOf(txtNumeroIteraciones.getText())];
        double camb[]=new double[Integer.valueOf(txtNumeroIteraciones.getText())];
        double nuevalor[]=new double[Integer.valueOf(txtNumeroIteraciones.getText())];
        paso=1/Float.valueOf(txtNumeroIteraciones.getText());
        precio=Float.valueOf(String.valueOf(tblDatos.getValueAt(obtenerDatosImportados().length-1,1)));
        ten=Float.valueOf(String.valueOf(txtMedia.getText()));
        vol=Float.valueOf(String.valueOf(txtDEsviacion.getText()));
        txtTendencia.setText(String.valueOf(ten));
        txtVolatilidad.setText(String.valueOf(vol));
        txtIteraciones.setText(String.valueOf(it));
        txtPaso.setText(String.valueOf(paso));
        for(int i=0;i<Integer.valueOf(txtNumeroIteraciones.getText());i++){
            if(i==0){
                prec[i]=precio;                
            }else{
                prec[i]=nuevalor[i-1];                
            }
            camb[i]=(prec[i]*ten*paso)+(prec[i]*vol*Math.sqrt(paso)*z[i]);
            nuevalor[i]=prec[i]+camb[i];            
        }
        String []titulos={"PRECIO","CAMBIO","NUEVO VALOR"};
        String [] registros=new String [3];
        weiner = new DefaultTableModel(null,titulos);
        while(fila<Integer.valueOf(txtNumeroIteraciones.getText())){
                registros[0]=String.valueOf(prec[fila]);
                registros[1]=String.valueOf(camb[fila]);
                registros[2]=String.valueOf(nuevalor[fila]);                
                weiner.addRow(registros);
                fila++;
            }
            tblTrayectorias.setModel(weiner);       
    }
    public void procesosWienerImportados(){
        int fila=0;
        float paso,ten,vol,precio,cambio,nvalor;
        double prec[]=new double[Integer.valueOf(txtNumeroIteraciones.getText())];
        double camb[]=new double[Integer.valueOf(txtNumeroIteraciones.getText())];
        double nuevalor[]=new double[Integer.valueOf(txtNumeroIteraciones.getText())];
        double z1[]= new double [Integer.valueOf(txtNumeroIteraciones.getText())];
        paso=1/Float.valueOf(txtNumeroIteraciones.getText());
        double paso1=1/Double.valueOf(txtNumeroIteraciones.getText());
        System.out.println("paso "+ paso + " paso1 "+paso1 + " n "+txtNumeroIteraciones.getText());
        precio=Float.valueOf(String.valueOf(tblDatos.getValueAt(obtenerDatosImportados().length-1, fila)));
        ten=Float.valueOf(String.valueOf(txtMedia.getText()));
        vol=Float.valueOf(String.valueOf(txtDEsviacion.getText()));
        txtTendencia.setText(String.valueOf(ten));
        txtVolatilidad.setText(String.valueOf(vol));
        txtIteraciones.setText(String.valueOf(it));
        txtPaso.setText(String.valueOf(paso));
        for(int i=0;i<Integer.valueOf(txtNumeroIteraciones.getText());i++){
            if(i==0){
                prec[i]=precio;                
            }else{
                prec[i]=nuevalor[i-1];                
            }
            camb[i]=(prec[i]*ten*paso)+(prec[i]*vol*Math.sqrt(paso)*z1[i]);
            nuevalor[i]=prec[i]+camb[i];            
        }
        String []titulos={"PRECIO","CAMBIO","NUEVO VALOR"};
        String [] registros=new String [3];
        weiner = new DefaultTableModel(null,titulos);
        while(fila<Integer.valueOf(txtNumeroIteraciones.getText())){
                registros[0]=String.valueOf(prec[fila]);
                registros[1]=String.valueOf(camb[fila]);
                registros[2]=String.valueOf(nuevalor[fila]);                
                weiner.addRow(registros);
                fila++;
            }
            tblTrayectorias.setModel(weiner);
            GraficoWiener gw=new GraficoWiener();
            gw.setVisible(true);
    }    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroDatos = new javax.swing.JTextField();
        btnAceptar = new javax.swing.JButton();
        txtCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtNumeroIteraciones = new javax.swing.JTextField();
        btnCalcular = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtMedia = new javax.swing.JTextField();
        txtError = new javax.swing.JTextField();
        txtMediana = new javax.swing.JTextField();
        txtModa = new javax.swing.JTextField();
        txtDEsviacion = new javax.swing.JTextField();
        txtVarianza = new javax.swing.JTextField();
        txtCurtosis = new javax.swing.JTextField();
        txtSesgo = new javax.swing.JTextField();
        txtRango = new javax.swing.JTextField();
        txtMin = new javax.swing.JTextField();
        txtMax = new javax.swing.JTextField();
        txtSuma = new javax.swing.JTextField();
        txtCuenta = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtPruebJB = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtbCaminata = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTendencia = new javax.swing.JTextField();
        txtVolatilidad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIteraciones = new javax.swing.JTextField();
        txtPaso = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTrayectorias = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        jLabel1.setText("Número de Datos:");

        txtNumeroDatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroDatosKeyTyped(evt);
            }
        });

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        txtCancelar.setText("Cancelar");
        txtCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCancelar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNumeroDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar)
                    .addComponent(txtCancelar))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "DATOS"));

        tblDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDatos);

        jLabel2.setText("Iteraciones:");

        txtNumeroIteraciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroIteracionesKeyTyped(evt);
            }
        });

        btnCalcular.setText("Calcular");
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCalcular)
                    .addComponent(jLabel2)
                    .addComponent(txtNumeroIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCalcular)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ESTADISTICA DESCRIPTIVA"));

        txtError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtErrorActionPerformed(evt);
            }
        });

        jLabel7.setText("Media");

        jLabel8.setText("Error Tipico");

        jLabel9.setText("Mediana");

        jLabel10.setText("Moda");

        jLabel11.setText("Desviacion Estandar");

        jLabel12.setText("Varianza de la Muestra");

        jLabel13.setText("Curtosis");

        jLabel14.setText("Coeficiente de Asimetria");

        jLabel15.setText("Rango");

        jLabel16.setText("Mínimo");

        jLabel17.setText("Máximo");

        jLabel18.setText("Suma");

        jLabel19.setText("Cuenta");

        jLabel20.setText("JB");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtModa)
                    .addComponent(txtError)
                    .addComponent(txtMedia, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMediana)
                    .addComponent(txtDEsviacion)
                    .addComponent(txtVarianza)
                    .addComponent(txtCurtosis)
                    .addComponent(txtSesgo)
                    .addComponent(txtRango)
                    .addComponent(txtMin, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(txtMax)
                    .addComponent(txtSuma)
                    .addComponent(txtCuenta)
                    .addComponent(txtPruebJB))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMedia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtError)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMediana)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtModa)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDEsviacion)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVarianza)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCurtosis)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSesgo)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRango)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMin)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMax)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSuma)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCuenta)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtPruebJB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(108, 108, 108))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "CAMINATA ALEATORIA"));

        jtbCaminata.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jtbCaminata);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "TRAYECTORIAS WIENER"));

        jLabel3.setText("Tendencia:");

        txtTendencia.setEditable(false);

        txtVolatilidad.setEditable(false);

        jLabel4.setText("Volatilidad:");

        jLabel5.setText("Iteraciones:");

        txtIteraciones.setEditable(false);

        txtPaso.setEditable(false);

        jLabel6.setText("Paso:");

        tblTrayectorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblTrayectorias);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTendencia, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(txtVolatilidad)
                            .addComponent(txtIteraciones)
                            .addComponent(txtPaso))
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTendencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtVolatilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPaso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Importar Datos");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Salir");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumeroDatosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDatosKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(c<'0'||c>'9'){            
            evt.consume();
        }
    }//GEN-LAST:event_txtNumeroDatosKeyTyped

    private void txtCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_txtCancelarActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        // TODO add your handling code here:
        verificar();
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jDialog1.setBounds(100, 100, 230, 180);
        jDialog1.setLocationRelativeTo(null);
        jDialog1.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        importar();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void txtNumeroIteracionesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroIteracionesKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(c<'0'||c>'9'){            
            evt.consume();
        }
    }//GEN-LAST:event_txtNumeroIteracionesKeyTyped

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        // TODO add your handling code here:
        quitarFocoDatos();        
//        verificarIteraciones();
//        EstadisticaDescriptiva();
        EstadisticaDescriptivaImportados();
        verificarIteracionesImportados();
       
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void txtErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtErrorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtErrorActionPerformed

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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCalcular;
    private javax.swing.JDialog jDialog1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jtbCaminata;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTable tblTrayectorias;
    private javax.swing.JButton txtCancelar;
    private javax.swing.JTextField txtCuenta;
    private javax.swing.JTextField txtCurtosis;
    private javax.swing.JTextField txtDEsviacion;
    private javax.swing.JTextField txtError;
    private javax.swing.JTextField txtIteraciones;
    private javax.swing.JTextField txtMax;
    private javax.swing.JTextField txtMedia;
    private javax.swing.JTextField txtMediana;
    private javax.swing.JTextField txtMin;
    private javax.swing.JTextField txtModa;
    private static javax.swing.JTextField txtNumeroDatos;
    private javax.swing.JTextField txtNumeroIteraciones;
    private javax.swing.JTextField txtPaso;
    private javax.swing.JTextField txtPruebJB;
    private javax.swing.JTextField txtRango;
    private javax.swing.JTextField txtSesgo;
    private javax.swing.JTextField txtSuma;
    private javax.swing.JTextField txtTendencia;
    private javax.swing.JTextField txtVarianza;
    private javax.swing.JTextField txtVolatilidad;
    // End of variables declaration//GEN-END:variables
}
