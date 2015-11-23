package sv.gob.mined.uaci.contoladores.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.primefaces.context.RequestContext;
import sv.gob.mined.uaci.contoladores.AfianzadoraController;
import sv.gob.mined.uaci.contoladores.ArchivoController;
import sv.gob.mined.uaci.contoladores.ContratoController;
import sv.gob.mined.uaci.contoladores.GarantiaController;
import sv.gob.mined.uaci.contoladores.MetodoAdqController;
import sv.gob.mined.uaci.contoladores.PagoController;
import sv.gob.mined.uaci.contoladores.ProcesoController;
import sv.gob.mined.uaci.contoladores.ProveedorController;
import sv.gob.mined.uaci.contoladores.TecnicoController;
import sv.gob.mined.uaci.contoladores.TipoGarantiaController;
import sv.gob.mined.uaci.entidades.Archivo;
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.Garantia;
import sv.gob.mined.uaci.entidades.Pago;
import sv.gob.mined.uaci.entidades.Proceso;
import sv.gob.mined.uaci.seguridad.UsuariosAplicacion;
import sv.gob.mined.uaci.seguridad.SeguridadController;


/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Descripcion: En esta clase se encuentran funciones comunes usadas por digerentes clases de este proyecto
 *              Algunas funciones son para el control de otros componentes de la vista, desplegar mensajes 
 *              flotantes, acceder a los controladores de vistas de una sesion, etc.
 * 
 *              Esta clase no tiene constructor todos sus metodos son estaticos.
 * 
 * Ultima Actualizacion: 05/09/2015
 */

public class JsfUtil {

   
    
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    /**
     * FUNCION QUE CONSTRUYE EL MENSAJE QUE SE MUESTRA AL USUARIO
     * Esta funcion detecta si la base mando un mensaje de error por violacion de indices unicos 
     * y traduce el mensaje para que sea comprendido por el usuario.
     */
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            if(msg.contains("ORA-00001: unique constraint")){
                if(msg.contains("UIX_AFIAZADORA")){
                    addErrorMessage("AFIANZADORA YA EXISTE");
                }else if(msg.contains("UIX_CLAVE_GARANTIA")){
                    addErrorMessage("CLAVE DE LA GARANTIA YA EXISTE");
                }else if(msg.contains("UIX_COD_FUENTE")){
                    addErrorMessage("CODIGO DE FUENTE DE FINANCIAMIENTO YA EXISTE");
                }else if(msg.contains("UIX_COD_METODO")){
                    addErrorMessage("CODIGO DE METODO DE ADQUISION YA EXISTE");
                }else if(msg.contains("UIX_ESTADO_CONTRATO")){
                    addErrorMessage("ESTADO DE CONTRATO YA EXISTE");
                }else if(msg.contains("UIX_ESTADO_DIGIT")){
                    addErrorMessage("ESTADO DE DIGITALIZACION YA EXISTE");
                }else if(msg.contains("UIX_ESTADO_FOLIO")){
                    addErrorMessage("ESTADO DE FOLIO YA EXISTE");
                }else if(msg.contains("UIX_NOMBRE_PROVEEDOR")){
                    addErrorMessage("PROVEEDOR YA EXISTE");
                }else if(msg.contains("UIX_NOMBRE_TECNICO")){
                    addErrorMessage("TECNICO YA EXISTE");
                }else if(msg.contains("UIX_NOMBRE_UNIDAD")){
                    addErrorMessage("UNIDAD TECNICA YA EXISTE");
                }else if(msg.contains("UIX_NUM_CONTRATO")){
                    addErrorMessage("NUMERO DE CONTRATO YA EXISTE");
                }else if(msg.contains("UIX_NUM_PROCESO")){
                    addErrorMessage("NUMERO DE PROCESO YA EXISTE");
                }else if(msg.contains("UIX_TIPO_GARANTIA")){
                    addErrorMessage("TIPO DE GARANTIA YA EXISTE");
                }else if(msg.contains("UIX_TIPO_PAGO")){
                    addErrorMessage("TIPO DE PAGO YA EXISTE");
                }else{
                    addErrorMessage("VIOLACION DE INDICE: DATOS YA EXISTEN \nCOMUNIQUESE CON EL ADMINISTRADOR");
                }
            }else{
                addErrorMessage(msg);
            }
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addWarnMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
    
    
    public static void putObjectSession(String nombre, Object value){
        FacesContext context = FacesContext.getCurrentInstance();  
        context.getExternalContext().getSessionMap().put(nombre,value);
    }
    
    public static Object removeObjectSession(String nombre){
        FacesContext context = FacesContext.getCurrentInstance();  
        return context.getExternalContext().getSessionMap().remove(nombre);
    }
    
    public static String getPathContext(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return servletContext.getContextPath();
    }
    
    public static String getRealPath(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return servletContext.getRealPath("");
    }
    
    public static void ventanaErrorEffect(String widgetVar){
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("var jqDialog = jQuery('#'+ " + widgetVar +".id); jqDialog.effect('shake', { times:3 }, 100);");
    }
    
    public static void AbrirVentana(String widgetVar){
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('"+ widgetVar + "').show();");
    }
    
    public static void cerrarVentana(String widgetVar){
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('"+ widgetVar + "').hide();");
    }
    
    
    public static void cerrarSessionUsuario(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
    }
    
    /*
     * FUNCION QUE ENCRIPTA EL PASSWORD A MD5
     */
    public static String getMD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(text.getBytes("utf-8"));
        final StringBuilder hexStringBuilder = new StringBuilder();
        final byte[] digest = algorithm.digest();
        for (byte digestItem : digest) {
            String hex = Integer.toHexString(0xFF & digestItem);
            if (hex.length() == 1) {
                hexStringBuilder.append('0');
            }
            hexStringBuilder.append(hex.toUpperCase());
        }
        return hexStringBuilder.toString();
    }
    
    
    /*Funcion que redirecciona a otra pagina existente en la aplicacion
     * recibe como parametro la url relativa a partir del nodo raiz
     * de la pagina que se quiere servir
     */ 
    public static void irA(String urlRelat) {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();  
            contex.getExternalContext().redirect("/SAGACIv1.0" + urlRelat);
        } catch (Exception e) {
            
        }
    }
  
   public static SeguridadController SessionSeguridadController() {
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        SeguridadController seguridadController = (SeguridadController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "seguridadController");
        return seguridadController;
    }
     
   public static TecnicoController SessionTecnicoController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        TecnicoController tecnicoController = (TecnicoController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "tecnicoController");
        return tecnicoController;
   }
   
   public static MetodoAdqController SessionMetodoAdqController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        MetodoAdqController metodoController = (MetodoAdqController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "metodoAdqController");
        return metodoController;
   }
   
   
   public static UsuariosAplicacion UsuarioActual() throws Exception{
       return SessionSeguridadController().getUsuario();
   }
        
   public static ProcesoController SessionProcesoController() {
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        ProcesoController procesoController = (ProcesoController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "procesoController");
        return procesoController;
   }
   
   public static ProveedorController SessionProveedorController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        ProveedorController proveedorController = (ProveedorController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "proveedorController");
        return proveedorController;
    }
   
   public static TipoGarantiaController SessionTipoGarantiaController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        TipoGarantiaController tipoController = (TipoGarantiaController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "tipoGarantiaController");
        return tipoController;
   }
   
   public static AfianzadoraController SessionAfianzadoraController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        AfianzadoraController afianzadoraController = (AfianzadoraController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "afianzadoraController");
        return afianzadoraController;
   }
   
   public static GarantiaController SessionGarantiaController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        GarantiaController garantiaController = (GarantiaController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "garantiaController");
        return garantiaController;
   }
   
   public static ContratoController SessionContratoController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        ContratoController contratoController = (ContratoController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "contratoController");
        return contratoController;
    }
   
   public static ArchivoController SessionArchivoController(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        ArchivoController archivoController = (ArchivoController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "archivoController");
        return archivoController;
    }
   
   public static PagoController SessionPagoController() {
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        PagoController pagoController = (PagoController) context.getApplication().getELResolver().
                getValue(context.getELContext(), null, "pagoController");
        return pagoController;
   }
   
    public static TimeZone getTimeZone() {
        return TimeZone.getTimeZone("America/El_Salvador");
    }
    
    
    public static Date hoy() {
        return getCalendar().getTime();
    }
    
    public static Calendar getCalendar() {
        TimeZone tz = getTimeZone();
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeZone(tz);
        return calendario;
    }
    
    
    /**
     * 
     * @param date
     * @param format
     * @return fechaFormateada
     * dd 'de' MMMM 'de' yyyy
     * Formatos
     *  dd: dia en digitos
     *  MM: mes en digitos
     *  MMMM: mes en letras 
     *  yyyy: año en digitos
     *  
     */ 
    public static String dateFormat(Date date, String format) {
        if (date != null) {
                SimpleDateFormat formato = new SimpleDateFormat(format,new Locale("es"));
                String fechaFormateada = formato.format(date);
                return fechaFormateada;
            } else {
                return null;
            }
    }
    
    //Formatea la fecha a dd/MM/yyyy para poder visualizarse
    public static String setFechaFormateada(Date fecha,int format) {
        if (fecha != null) {
            SimpleDateFormat formato=new SimpleDateFormat("dd/mm/yyyy");
            switch(format){
                case 1:
                    formato = new SimpleDateFormat("dd/MM/yyyy");
                    break;
                case 2:
                    formato = new SimpleDateFormat("yyyy-MM-dd"); //PARA LA BD
                    break;
                case 3:
                    formato = new SimpleDateFormat("dd/MM/yyyy HH:mm a"); //PARA LA BD
                    break; 
            }
            String fechaFormateada = formato.format(fecha);
            return fechaFormateada;
        } else {
            return "";
        }
    }
    
    //Formatea la fecha a dd/MM/yyyy para poder visualizarse
    public static Date convertToFecha(String fecha) {
        Date F = new Date();
        try {
            //Convierte el String en tipo Date
            //Cambia el Formato de dd/MM/yyyy a yyyy-MM-dd para la BD
            DateFormat dfMysql = new SimpleDateFormat("yyyy-MM-dd");
            F = dfMysql.parse(fecha);
        } catch (ParseException ex) { }
        
        return F;
    }
    
    public static BigDecimal redondearMas(BigDecimal valor,int precision) {
        BigDecimal big = new BigDecimal(valor.floatValue());
        big = big.setScale(precision, RoundingMode.HALF_UP);
        return big;
    }
   
    
    public static List<Integer> getAnios(int range) {
        Integer actual = getCalendar().get(Calendar.YEAR);
        List<Integer> anios = new ArrayList<Integer>();
        if(range >=0){
            for(int i=-1; i < range; i++){
                anios.add(actual - i);
            }
        }
        return anios;
    }
    
    
    
    
    public static Proceso ultimaModificacionT(Proceso proceso) throws Exception {
        UsuariosAplicacion usuarioActual = UsuarioActual();
        proceso.setModificadoPor(usuarioActual.getUserId().toString());
        proceso.setFechaModificado(hoy());
        return proceso;
    }
    
    public static Contrato ultimaModificacionT(Contrato contrato) throws Exception {
        UsuariosAplicacion usuarioActual = UsuarioActual();
        contrato.setModificadoPor(usuarioActual.getUserId().toString());
        contrato.setFechaModificado(hoy());
        return contrato;
    }
    
    public static Pago ultimaModificacionT(Pago pago) throws Exception {
        UsuariosAplicacion usuarioActual = UsuarioActual();
        pago.setModificadoPor(usuarioActual.getUserId().toString());
        pago.setFechaModificado(hoy());
        return pago;
    }
    
    public static Garantia ultimaModificacionT(Garantia garantia) throws Exception {
        UsuariosAplicacion usuarioActual = UsuarioActual();
        garantia.setModificadoPor(usuarioActual.getUserId().toString());
        garantia.setFechaModificado(hoy());
        return garantia;
    }
    
    public static Archivo ultimaModificacionT(Archivo archivo) throws Exception {
        UsuariosAplicacion usuarioActual = UsuarioActual();
        archivo.setModificadoPor(usuarioActual.getUserId().toString());
        archivo.setFechaModificado(hoy());
        return archivo;
    }
    
    public static void postProcessXLS(Object document) {  
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            header.getCell(i).setCellStyle(cellStyle);
        }
   }
    
    
    
    public static void preProcessXLS(Object document) {  
        HSSFWorkbook wb = (HSSFWorkbook) document;  
        HSSFSheet sheet = wb.getSheetAt(0);  
        HSSFRow header = sheet.getRow(7); 
        HSSFCellStyle cellStyle = wb.createCellStyle();    
        cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index); 
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font=wb.createFont();
        /* set the weight of the font */
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        /* attach the font to the style created earlier */
        cellStyle.setFont(font);
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {  
            HSSFCell cell = header.getCell(i);  
            cell.setCellStyle(cellStyle); 
        }  
   }
    
       
    public static void copyRow(HSSFSheet worksheetSource, HSSFSheet worksheetDestination, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        HSSFRow origen = worksheetSource.getRow(sourceRowNum);
        HSSFRow destino = worksheetDestination.createRow(destinationRowNum);
        
        // Loop through source columns to add to new row
        for (int i = 0; i < origen.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = origen.getCell(i);
            HSSFCell newCell = destino.createCell(i);
            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }
            
            //Ajustar tamaños columnas
            worksheetDestination.setColumnWidth(i, worksheetSource.getColumnWidth(i));
                       
            // Copy style from old cell and apply to new cell
            HSSFCellStyle newCellStyle = newCell.getSheet().getWorkbook().createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }
             

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());
            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
            
            
            
        }
    
    }
    
    
    
    
     public static String quitaEspacios(String texto) {
         if(texto != null){
            java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
            StringBuilder buff = new StringBuilder();
            while (tokens.hasMoreTokens()) {
                buff.append(" ").append(tokens.nextToken());
            }
            return buff.toString().trim();
         }else{
             return null;
         }
        
    }
    
}