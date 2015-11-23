/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.contoladores.util;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Descripcion: En esta clase se encuentran funciones comunes usadas por digerentes clases de este proyecto
 *              Algunas funciones son para el control de otros componentes de la vista, desplegar mensajes 
 *              flotantes, acceder a los controladores de vistas de una sesion, etc.
 *              Esta clase es un objeto de session y se puede acceder desde la vista.
 */
@ManagedBean(name = "funciones")
@SessionScoped
public class Funciones implements Serializable {

    private List<Integer> ultimos5Anios = new ArrayList<Integer>();
    
    public Funciones() {
    }
    
    public String getformatFecha(Date fecha){
        String f = JsfUtil.setFechaFormateada(fecha, 1);
        return f;
    }
    
    public String formatFechaCompleta(Date fecha){
        return JsfUtil.setFechaFormateada(fecha,3);
    }
    
    public List<Integer> anios(int rango){
        return JsfUtil.getAnios(rango);
    }
    
    
    public List<Integer> getUltimos5Anios(){
        return JsfUtil.getAnios(5);
    }

    public List<Integer> getUltimos5AniosNULL(){
        List<Integer> anios = new ArrayList<Integer>();
        anios.add(0);
        for(Integer i : JsfUtil.getAnios(5)){
            anios.add(i);
        }
        return anios;
    }
    
    public String nombreMes(int mes){
        String nombre = "";
        switch(mes){
            case 1: nombre = "ENERO";break;
            case 2: nombre = "FEBRERO";break;
            case 3: nombre = "MARZO";break;
            case 4: nombre = "ABRIL";break;
            case 5: nombre = "MAYO";break;
            case 6: nombre = "JUNIO";break;
            case 7: nombre = "JULIO";break;
            case 8: nombre = "AGOSTO";break;
            case 9: nombre = "SEPTIEMBRE";break;
            case 10: nombre = "OCTUBRE";break;
            case 11: nombre = "NOVIEMBRE";break;
            case 12: nombre = "DICIEMBRE";break;
        }
        return nombre;
    }
    
    
    
    
     /*
    * FUNCIONES PARA DATA-EXPORTER
    */
        
   public void postProcessXLS_TECNICO(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Tecnicos"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 12;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(3);
            anchor.setRow1(2);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(0);
            anchor2.setRow1(2);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m);
                    sheetD.addMergedRegion(cellRangeAddress);  
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
       
        }catch(Exception exc){ }
        
   }
   
   
   public void postProcessXLS_PROCESOS(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Procesos"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 11;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(5);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(0);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
            HSSFCell anio = sheetD.getRow(8).getCell(2);
            anio.setCellValue(JsfUtil.SessionProcesoController().getAnioRegistro());
        }catch(Exception exc){  }
   }
   
   
   
   public void postProcessXLS_PROVEEDORES(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Proveedores"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 10;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(6);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(0);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
        }catch(Exception exc){  }
   }
   
     
   
   
   public void postProcessXLS_CONTRATOS(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Contratos"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 11;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(9);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(1);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
            Integer a = JsfUtil.SessionContratoController().getAnioRegistro();
            HSSFCell anio = sheetD.getRow(8).getCell(2);
            if(a == 0){
                anio.setCellValue("NO ESPECIFICADO (N/E)");
            }else{
                anio.setCellValue(a.toString());
            }
        }catch(Exception exc){  }
   }
   
   
   
   public void postProcessXLS_AFIANZADORAS(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Afianzadoras"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 11;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(1);
            anchor.setRow1(2);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            anchor.setDx1(550);
            anchor.setDx2(850);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(0);
            anchor2.setRow1(2);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m);
                    sheetD.addMergedRegion(cellRangeAddress);  
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
       
        }catch(Exception exc){ }
        
   }
   
   
   public void postProcessXLS_GARANTIAS(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Garantias"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 11;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(3);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.30);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(15);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.50);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
            Integer a = JsfUtil.SessionGarantiaController().getAnioRegistro();
            HSSFCell anio = sheetD.getRow(8).getCell(2);
            anio.setCellValue(a);
        }catch(Exception exc){  }
   }
   
   
   
   
   public void postProcessXLS_VIGENTES(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Garantias_Vigentes"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 10;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(10);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(3);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
        }catch(Exception exc){  }
   }
   
   
   public void postProcessXLS_VENCIDAS(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Garantias_Vencidas"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 10;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(10);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(3);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
        }catch(Exception exc){  }
   }
   
   
   
   public void postProcessXLS_XVENCER(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Garantias_Xvencer"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 11;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(10);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(3);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
            Date d = JsfUtil.SessionGarantiaController().getFechaVencer();
            HSSFCell date = sheetD.getRow(8).getCell(4);
            date.setCellValue(JsfUtil.dateFormat(d,"dd/MM/yyyy" ));
        }catch(Exception exc){  }
   }
   
   
   
   
   public void postProcessXLS_ARCHIVOS(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Archivos"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 12;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(20);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.50);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(8);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.30);
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m).copy();
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
            Integer a = JsfUtil.SessionArchivoController().getAnioRegistro();
            HSSFCell anio = sheetD.getRow(8).getCell(2);
            anio.setCellValue(a);
            if(JsfUtil.SessionArchivoController().getFuente() != null){
                String f = JsfUtil.SessionArchivoController().getFuente().getNombre();
                HSSFCell fuente = sheetD.getRow(8).getCell(8);
                fuente.setCellValue(f);
            }else{
                HSSFCell fuente = sheetD.getRow(8).getCell(8);
                fuente.setCellValue("TODAS LAS FUENTES");
            }
            
        }catch(Exception exc){  }
   }
   
   
   
   
   
   public void postProcessXLS_PAGOS(Object document) throws FileNotFoundException, IOException, URISyntaxException { 
        try{
            //LEER PLANTILLA
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(ResourceBundle.getBundle("/Bundle").getString("plantillaXLS_Pagos"));
            StreamedContent plantillaXLS = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "Plantilla.xls");
            HSSFWorkbook plantilla = new HSSFWorkbook(plantillaXLS.getStream());
            HSSFSheet sheetP = plantilla.getSheetAt(0); 
            //LEER DATOS
            HSSFWorkbook datos = (HSSFWorkbook) document; 
            HSSFSheet sheetD = datos.getSheetAt(0);
            //Filas que ocupa el encabezado de plantilla
            int encabezado = 11;
            //Quitar encabezado y desplazar Datos
            sheetD.removeRow(sheetD.getRow(0));
            sheetD.shiftRows(1, sheetD.getLastRowNum(), encabezado -1);
            //Copiar contenido de plantilla a la hoja del reporte
            int inicio = 0;
            for (int f = 0; f < encabezado; f++) {
                JsfUtil.copyRow(sheetP, sheetD, f, inicio);
                inicio++;
            }
            //Imagen de PLantilla
            HSSFPictureData imagen = plantilla.getAllPictures().get(0);
            int pictureIdx = datos.addPicture(imagen.getData(), imagen.getFormat());
            CreationHelper helper = datos.getCreationHelper();
            Drawing drawing = sheetD.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(6);
            anchor.setRow1(1);
            anchor.setAnchorType(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.45);
            //IMAGEN 2
            HSSFPictureData imagen2 = plantilla.getAllPictures().get(1);
            int pictureIdx2 = datos.addPicture(imagen2.getData(), imagen2.getFormat());
            CreationHelper helper2 = datos.getCreationHelper();
            Drawing drawing2 = sheetD.createDrawingPatriarch();
            ClientAnchor anchor2 = helper2.createClientAnchor();
            anchor2.setCol1(0);
            anchor2.setRow1(1);
            anchor2.setAnchorType(1);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);
            pict2.resize(0.25);
            sheetD.getRow(1).setHeight((short)1);
            
            //Combinar las columnas al igual que la plantilla
            for (int m = 0; m < sheetP.getNumMergedRegions(); m++) {
                    CellRangeAddress cellRangeAddress = sheetP.getMergedRegion(m);
                    sheetD.addMergedRegion(cellRangeAddress);
            }
            //Ajustar Columna a texto
            for(int f=encabezado; f < sheetD.getPhysicalNumberOfRows();f++){
                for(int c=1; c <= sheetD.getRow(f).getPhysicalNumberOfCells();c++ ){
                    try{
                        sheetD.getRow(f).getCell(c).getCellStyle().setWrapText(true);
                    }catch(Exception ex){}
                }
            }
            
            Integer a = JsfUtil.SessionPagoController().getAnio();
            HSSFCell anio = sheetD.getRow(8).getCell(2);
            anio.setCellValue(a);
        }catch(Exception exc){  }
   }
   
   
   
   
   public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {  
        Document pdf = (Document) document;  
        pdf.open();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();  
        String logoURL = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "logo3.jpg";  
        Image logo = Image.getInstance(logoURL);
        logo.scalePercent(25);
        pdf.right();
        pdf.add(logo);
        pdf.addCreationDate();
   }
   
   
 
   public String getPathServletRptVencidas() {
        String pathServletVencidas = JsfUtil.getPathContext() + "/RptGarantiasVencidas";
        return pathServletVencidas;
    }
   
   public String getPathServletRptVigentes() {
        String pathServletVigentes = JsfUtil.getPathContext() + "/RptGarantiasVigentes";
        return pathServletVigentes;
    }
   
    public String getPathServletFrmDevolucion() {
        String pathServletFrmDevolucion = JsfUtil.getPathContext() + "/FrmDevolucion";
        return pathServletFrmDevolucion;
    }
    
    public String getPathServletRptArchivos() {
        String pathServletVigentes = JsfUtil.getPathContext() + "/RptArchivos";
        return pathServletVigentes;
    }
    
    public String getPathServletRptXvencer() {
        String pathServletXvencer = JsfUtil.getPathContext() + "/RptGarantiasXvencer";
        return pathServletXvencer;
    }
    
}
