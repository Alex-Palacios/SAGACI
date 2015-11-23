package sv.gob.mined.uaci.contoladores;

import java.io.IOException;
import java.io.InputStream;
import sv.gob.mined.uaci.entidades.Garantia;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.GarantiaJpaController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.uaci.contoladores.util.Funciones;
import sv.gob.mined.uaci.contoladores.util.GarantiaImport;
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.Proceso;
import sv.gob.mined.uaci.entidades.Proveedor;

@ManagedBean(name = "garantiaController")
@SessionScoped
public class GarantiaController implements Serializable {

    private GarantiaJpaController jpaController;
    private Integer anioRegistro;
    private List<Garantia> items;
    private List<Garantia> filtroGarantias;
    private Garantia selected;
    private List<Garantia> selectedGarantias;
    private Garantia newItem;
    

    public GarantiaController() {
        anioRegistro = JsfUtil.getCalendar().get(Calendar.YEAR);
        fechaVencer = JsfUtil.hoy();
        
    }

    private GarantiaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new GarantiaJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

    
    public Integer getAnioRegistro() {
        return anioRegistro;
    }

    public void setAnioRegistro(Integer anioRegistro) {
        this.anioRegistro = anioRegistro;
    }
    
    public List<Garantia> getItems() {
        if (items == null) {
            items = getJpaController().findGarantiaByAnio(anioRegistro);
        }
        return items;
    }
    
    public Garantia getSelected() {
        if (selected == null) {
            selected = new Garantia();
        }
        return selected;
    }

    public void setSelected(Garantia selected) {
        this.selected = selected;
    }

    public List<Garantia> getSelectedGarantias() {
        return selectedGarantias;
    }

    public void setSelectedGarantias(List<Garantia> selectedGarantias) {
        this.selectedGarantias = selectedGarantias;
    }

    
    public Garantia getNewItem() {
        if (newItem == null) {
            newItem = new Garantia();
        }
        return newItem;
    }

    public void setNewItem(Garantia newItem) {
        this.newItem = newItem;
    }

    

    public List<Garantia> getFiltroGarantias() {
        return filtroGarantias;
    }

    public void setFiltroGarantias(List<Garantia> filtroGarantias) {
        this.filtroGarantias = filtroGarantias;
    }

    public void nextNumGarantia() {
        if(newItem != null){
            newItem.setCorrelativo(jpaController.getNextNumGrantia(newItem.getAnio()));
        }
    }
    
    
    public void recreateModel() {
        items = null;
        if(filtroGarantias != null){
            filtroGarantias = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_garantias"));
    }


    public void prepareCreate() {
        newItem = new Garantia();
        newItem.setAnio(this.getAnioRegistro());
        newItem.setCorrelativo(jpaController.getNextNumGrantia(newItem.getAnio()));
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_garantia_insert"));
    }
    
   
    
    public void create() {
        try {
            getJpaController().create(newItem);
            String clave = newItem.toString();
            newItem = new Garantia();
            newItem.setAnio(this.getAnioRegistro());
            newItem.setCorrelativo(jpaController.getNextNumGrantia(newItem.getAnio()));
            recreateModel();
            JsfUtil.addSuccessMessage("GARANTIA INGRESADA CORRECTAMENTE");
            JsfUtil.addSuccessMessage("CLAVE ASIGNADA: " + clave);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("GARANTIA ACTUALIZADA");
            JsfUtil.cerrarVentana("editarGarantia");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarGarantia");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("GARANTIA ELIMINADA");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    
    public void createProceso() {
        newItem.setProceso(JsfUtil.SessionProcesoController().createProceso());
    }
    
    public void createTecnico() {
        newItem.getProceso().setTecnico(JsfUtil.SessionTecnicoController().createTecnico());
    }
    
    public void createContrato() {
        newItem.setContrato(JsfUtil.SessionContratoController().createContrato());
    }
    
    public void createAfianzadora() {
        newItem.setAfianzadora(JsfUtil.SessionAfianzadoraController().createAfianzadora());
    }
    
    public void createProveedor() {
        newItem.setProveedor(JsfUtil.SessionProveedorController().createProveedor());
    }
    
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findGarantiaEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findGarantiaEntities(), true);
    }
    
    public List<Garantia> getItemsSelectMenu() {
        return getJpaController().findGarantiaEntities();
    }
    
        
    public SelectItem[] getContratosByProcesoList(){
        if(getNewItem().getProceso() != null){
            return JsfUtil.getSelectItems(JsfUtil.SessionContratoController().getContratosByProceso(newItem.getProceso()),true);
        }else{
            return JsfUtil.getSelectItems(new ArrayList<Contrato>(),true);
        }
    }
    
    public SelectItem[] getProcesoContratoList() {
        if(getNewItem().getProceso() != null){
            List<Proceso> procesoList = new ArrayList<Proceso>();
            procesoList.add(newItem.getProceso());
            return JsfUtil.getSelectItems(procesoList,false);
        }else{
            return JsfUtil.getSelectItems(JsfUtil.SessionProcesoController().getItemsSelectMenu(),true);
        }
    }
    
    public SelectItem[] getProveedorGarantiaList() {
        if(getNewItem().getContrato() != null){
            List<Proveedor> proveedorList = new ArrayList<Proveedor>();
            proveedorList.add(newItem.getContrato().getContratista());
            return JsfUtil.getSelectItems(proveedorList,false);
        }else{
            return JsfUtil.getSelectItems(JsfUtil.SessionProveedorController().getItemsSelectMenu(),true);
        }
    }

    @FacesConverter(forClass = Garantia.class)
    public static class GarantiaControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GarantiaController controller = (GarantiaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "garantiaController");
            return controller.getJpaController().findGarantia(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Garantia) {
                Garantia o = (Garantia) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Garantia.class.getName());
            }
        }
    }
    
    
    /* REPORTE DE GARANTIAS VENCIDAS */
    
    private List<Garantia> vencidas;
    private List<Garantia> filtroVencidas;
    
    
    public List<Garantia> getVencidas() {
        if (vencidas == null) {
            vencidas = getJpaController().reporteGarantiasVencidas();
        }
        return vencidas;
    }

    public void setVencidas(List<Garantia> vencidas) {
        this.vencidas = vencidas;
    }

    public List<Garantia> getFiltroVencidas() {
        return filtroVencidas;
    }

    public void setFiltroVencidas(List<Garantia> filtroVencidas) {
        this.filtroVencidas = filtroVencidas;
    }
    
    
    public void resetReporteVencidas(){
        vencidas = null;
        if(filtroVencidas != null){
            filtroVencidas = vencidas;
        }
    }
    
    public void prepareReporteVencidas(){
        resetReporteVencidas();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_garantias_vencidas"));
    }
    
   
    
    
    public void verificarVencidas(){
        try{
            int vencidas = getJpaController().countVencidas();
            if(vencidas > 0){
                JsfUtil.addWarnMessage("HAY " + vencidas +" GARANTIAS VENCIDAS");
            }
        }catch(Exception ex){
            
        }
    }
    
    
    
    public void generarRptVencidas(){
        if(vencidas != null ){
            JsfUtil.removeObjectSession("garantias");
            JsfUtil.putObjectSession("garantias", vencidas);
            JsfUtil.AbrirVentana("rptVencidas");
        }else{
            JsfUtil.addErrorMessage("LISTA DE GARANTIAS VACIA");
        }
        
    }
    
    
    /* REPORTE DE GARANTIAS POR VENCER */
    
    private List<Garantia> porVencer;
    private List<Garantia> filtroXvencer;
    private Date fechaVencer;

    public List<Garantia> getPorVencer() {
        if (porVencer == null) {
            porVencer = getJpaController().reporteGarantiasXVencer(fechaVencer);
        }
        return porVencer;
    }

    public void setPorVencer(List<Garantia> porVencer) {
        this.porVencer = porVencer;
    }

    

    public List<Garantia> getFiltroXvencer() {
        return filtroXvencer;
    }

    public void setFiltroXvencer(List<Garantia> filtroXvencer) {
        this.filtroXvencer = filtroXvencer;
    }

    public Date getFechaVencer() {
        return fechaVencer;
    }

    public void setFechaVencer(Date fechaVencer) {
        this.fechaVencer = fechaVencer;
    }

        
    public void resetReporteXvencer(){
        porVencer = null;
        if(filtroXvencer != null){
            filtroXvencer = porVencer;
        }
    }
        
    public void prepareReportePorVencer(){
        resetReporteXvencer();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_garantias_por_vencer"));
    }
    
   
    
    
    public void generarRptXvencer(){
        if(porVencer != null ){
            JsfUtil.removeObjectSession("garantias");
            JsfUtil.removeObjectSession("fechaV");
            JsfUtil.putObjectSession("garantias", porVencer);
            JsfUtil.putObjectSession("fechaV", fechaVencer);
            JsfUtil.AbrirVentana("rptXvencer");
        }else{
            JsfUtil.addErrorMessage("LISTA DE GARANTIAS VACIA");
        }
        
    }
    
    
    /* IMPORTAR EXCEL */
    private UploadedFile file;
    private List<GarantiaImport> importacion;
    private List<GarantiaImport> errorImport;
    private StreamedContent filePlantilla;
    
    public StreamedContent getFilePlantilla() {
        InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/templates/PlantillaImport.xlsx");
        filePlantilla = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "PlantillaImport.xlsx");
        return filePlantilla;
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public List<GarantiaImport> getImportacion() {
        return importacion;
    }

    public void setImportacion(List<GarantiaImport> importacion) {
        this.importacion = importacion;
    }
 
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
     
    public void prepareImportarExcel(){
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_garantias_import"));
    }
    
    
    
    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        JsfUtil.addSuccessMessage(file.getFileName() + " archivo importado");
        cargarDatos();
    }
    
    public void cargarDatos(){
        if(file != null) {
            try {
                importacion =new  ArrayList<GarantiaImport>();
                Iterator rowIterator = null;
                int filas=0;
                int importadas=0;
                try{
                    XSSFWorkbook workBook = new XSSFWorkbook(file.getInputstream());
                    XSSFSheet hssfSheet = workBook.getSheetAt(0);
                    rowIterator = hssfSheet.rowIterator();
                }catch (IOException ex) {
                    Logger.getLogger(GarantiaController.class.getName()).log(Level.SEVERE, null, ex);
                }catch(POIXMLException ex){
                    try {
                        POIFSFileSystem fsFileSystem = new POIFSFileSystem(file.getInputstream());
                        HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
                        HSSFSheet hssfSheet = workBook.getSheetAt(0);
                        rowIterator = hssfSheet.rowIterator();
                    } catch (IOException e) {
                        Logger.getLogger(GarantiaController.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                rowIterator.next();
                rowIterator.next();
                rowIterator.next();
                while(rowIterator.hasNext()){
                    GarantiaImport row = new GarantiaImport();
                    XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                    filas++;
                    try{
                        row.setAnioClave((int) hssfRow.getCell(0).getNumericCellValue());
                        row.setNumClave((int) hssfRow.getCell(1).getNumericCellValue());
                        row.setFechaRecepcion(hssfRow.getCell(2).getDateCellValue());
                        row.setMetodo(JsfUtil.quitaEspacios(hssfRow.getCell(3).getStringCellValue()));
                        if(row.getMetodo() == null || row.getMetodo().isEmpty() || row.getMetodo().length() > 100){
                            //ERROR
                            JsfUtil.addErrorMessage("Metodo Adquisicion es obligatorio y no debe exceder los 100 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna METODO DE ADQUISICION");
                        }
                        row.setNumProceso(JsfUtil.quitaEspacios(hssfRow.getCell(4).getStringCellValue()));
                        if(row.getNumProceso() == null || row.getNumProceso().isEmpty() || row.getNumProceso().length() > 50){
                            //ERROR
                            JsfUtil.addErrorMessage("Numero de Proceso es obligatorio y no debe exceder los 50 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna NUMERO DE PROCESO");
                        }
                        row.setProceso(hssfRow.getCell(5).getStringCellValue());
                        try{
                            row.setSiap(JsfUtil.quitaEspacios(hssfRow.getCell(6).getStringCellValue()));
                        }catch(Exception e){
                            try{
                                row.setSiap(String.valueOf((int) hssfRow.getCell(6).getNumericCellValue()));
                            }catch(Exception e2){
                                // ERROR
                            }
                        }
                        if(row.getSiap() != null && row.getSiap().length() > 50){
                            //ERROR
                            JsfUtil.addErrorMessage("Numero SIAP no debe exceder los 50 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna NUMERO DE SIAP");
                        }
                        row.setTecnico(JsfUtil.quitaEspacios(hssfRow.getCell(7).getStringCellValue()));
                        if(row.getTecnico()== null || row.getTecnico().isEmpty() || row.getTecnico().length() > 100){
                            //ERROR
                            JsfUtil.addErrorMessage("Nombre de TECNICO es obligatorio y no debe exceder los 100 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna NOMBRE TECNICO");
                        }
                        row.setContrato(JsfUtil.quitaEspacios(hssfRow.getCell(8).getStringCellValue()));
                        if(row.getContrato()!= null && row.getContrato().length() > 50){
                            //ERROR
                            JsfUtil.addErrorMessage("Numero de CONTRATO no debe exceder los 50 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna NUMERO CONTRATO");
                        }
                        row.setModificativa(JsfUtil.quitaEspacios(hssfRow.getCell(9).getStringCellValue()));
                        if(row.getModificativa()!= null && row.getModificativa().length() > 50){
                            //ERROR
                            JsfUtil.addErrorMessage("RESOLUCION MODIFICATIVA no debe exceder los 50 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna RESOLUCION MODIFICATIVA");
                        }
                        try{
                            row.setFechaContrato(hssfRow.getCell(10).getDateCellValue());
                        }catch(Exception e){   }
                        row.setProveedor(JsfUtil.quitaEspacios(hssfRow.getCell(11).getStringCellValue()));
                        if(row.getProveedor()== null || row.getProveedor().isEmpty() || row.getProveedor().length() > 255){
                            //ERROR
                            JsfUtil.addErrorMessage("Nombre de PROVEEDOR es obligatorio y no debe exceder los 255 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna NOMBRE PROVEEDOR");
                        }
                        try{
                            row.setPlazoContrato(JsfUtil.quitaEspacios(hssfRow.getCell(12).getStringCellValue()));
                            if(row.getPlazoContrato() != null && row.getPlazoContrato().length() > 50){
                                //ERROR
                                JsfUtil.addErrorMessage("Plazo de CONTRATO no debe exceder los 50 caracteres");
                                throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna PLAZO CONTRATO");
                            }
                        }catch(Exception e){}
                        try{
                            row.setMontoContrato(hssfRow.getCell(13).getNumericCellValue());
                        }catch(Exception e){}
                        row.setTipoGarantia(JsfUtil.quitaEspacios(hssfRow.getCell(14).getStringCellValue()));
                        if(row.getTipoGarantia()== null || row.getTipoGarantia().isEmpty() || row.getTipoGarantia().length() > 100){
                               //ERROR
                            JsfUtil.addErrorMessage("Tipo de GARANTIA es obligatorio y no debe exceder los 100 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna TIPO GARANTIA");
                        }
                        row.setMontoGarantia(hssfRow.getCell(15).getNumericCellValue());
                        row.setPlazo(hssfRow.getCell(16).getStringCellValue());
                        if(row.getPlazo() != null && row.getPlazo().length() > 50){
                               //ERROR
                            JsfUtil.addErrorMessage("Plazo de GARANTIA no debe exceder los 50 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna PLAZO GARANTIA");
                        }
                        row.setAfianzadora(JsfUtil.quitaEspacios(hssfRow.getCell(17).getStringCellValue()));
                        if(row.getAfianzadora()== null || row.getAfianzadora().isEmpty() || row.getAfianzadora().length() > 100){
                               //ERROR
                            JsfUtil.addErrorMessage("AFIANZADORA es obligatorio y no debe exceder los 100 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna AFIANZADORA");
                        }
                        row.setFechaVencimiento(hssfRow.getCell(18).getDateCellValue());
                        try{
                            row.setNumGarantia(JsfUtil.quitaEspacios(hssfRow.getCell(19).getStringCellValue()));
                        }catch(Exception e){
                            try{
                                row.setNumGarantia(String.valueOf((int) hssfRow.getCell(19).getNumericCellValue()));
                            }catch(Exception e2){}
                        }
                        if(row.getNumGarantia()== null || row.getNumGarantia().isEmpty() || row.getNumGarantia().length() > 50){
                               //ERROR
                            JsfUtil.addErrorMessage("NUMERO de GARANTIA es obligatorio y no debe exceder los 50 caracteres");
                            throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna NUMERO DE GARANTIA");
                        }
                        try{
                            row.setFechaDevolucion(hssfRow.getCell(20).getDateCellValue());
                        }catch(Exception e){}
                        try{
                            row.setPersonaRetira(hssfRow.getCell(21).getStringCellValue());
                            if(row.getPersonaRetira()!= null && row.getPersonaRetira().length() > 100){
                                    //ERROR
                                JsfUtil.addErrorMessage("PERSONA QUE RETIRA no debe exceder los 100 caracteres");
                                throw new Exception("ERROR IMPORTAR GARANTIA "+row.getNumClave() +" Columna PERSONA QUE RETIRA");
                            }
                        }catch(Exception e){}
                        try{
                            row.setEstado(hssfRow.getCell(22).getStringCellValue());
                        }catch(Exception e){
                            
                        }
                        importacion.add(row);
                        importadas++;
                    }catch(Exception exc){
                        JsfUtil.addErrorMessage("Error al importar garantia # "+row.getNumClave());
                        break;
                    }
                }
                JsfUtil.addSuccessMessage(importadas +" IMPORTADAS");
            }catch(Exception ex){
                Logger.getLogger(GarantiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JsfUtil.addErrorMessage(" Datos no cargados");
        }
    }
    
    
       
    public void limpiarCache(){
        file = null;
        if(importacion != null){
            importacion.clear();
        }
    }
    
    public void importarDatos(){
        //Comprobar Datos
        try{
            errorImport = getJpaController().importarGarantias(importacion);
            if(errorImport != null && !errorImport.isEmpty()){
                if(errorImport.size() > 0){
                   JsfUtil.addWarnMessage("ERROR AL GUARDAR GARANTIA # "+errorImport.get(0).getAnioClave() +"/"+errorImport.get(0).getNumClave());
                }
            }
            if(importacion != null || !importacion.isEmpty()){
                importacion = errorImport;
            }else{
                limpiarCache();
            }
        }catch(Exception ex){
            Logger.getLogger(GarantiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* REPORTE DE GARANTIAS VENCIDAS */
    
    private List<Garantia> vigentes;
    private List<Garantia> filtroVigentes;

    public List<Garantia> getVigentes() {
        if (vigentes == null) {
            vigentes = getJpaController().reporteGarantiasVigentes();
        }
        return vigentes;
    }

    public void setVigentes(List<Garantia> vigentes) {
        this.vigentes = vigentes;
    }

    public List<Garantia> getFiltroVigentes() {
        return filtroVigentes;
    }

    public void setFiltroVigentes(List<Garantia> filtroVigentes) {
        this.filtroVigentes = filtroVigentes;
    }
    
    
    public void resetReporteVigentes(){
        vigentes = null;
        if(filtroVigentes != null){
            filtroVigentes = vigentes;
        }
    }
    
    public void prepareReporteVigentes(){
        resetReporteVigentes();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_garantias_vigentes"));
    }
    
    
    public void generarRptVigentes(){
        if(vigentes != null ){
            JsfUtil.removeObjectSession("garantias");
            JsfUtil.putObjectSession("garantias", vigentes);
            JsfUtil.AbrirVentana("rptVigentes");
        }else{
            JsfUtil.addErrorMessage("LISTA DE GARANTIAS VACIA");
        }
        
    }
    
    
    
    
    
    /** FORMULARIO DEVOLUCION **/
    
    
    public void nextNumFormDev() {
        if(selected != null){
            selected.setFormDev(jpaController.getNextFormDev(selected.getAnioDev()));
        }
    }
    
    public void prepareFrmDevolucion(){
        if(selected.getGteFormDev()== null){
            selected.setAnioDev(this.getAnioRegistro());
            selected.setFormDev(jpaController.getNextFormDev(selected.getAnioDev()));
        }
        JsfUtil.AbrirVentana("datosFormDevolucion");
    }
    
    
    public void resetNumFormDev() {
        try {
            selected.setAnioDev(null);
            selected.setFormDev(null);
            selected.setGteFormDev(null);
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("NUMERO DE FORMULARIO DE DEVOLUCION RESETEADO");
            if(selected.getGteFormDev()== null){
                selected.setAnioDev(this.getAnioRegistro());
                selected.setFormDev(jpaController.getNextFormDev(selected.getAnioDev()));
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("ERROR AL DEVOLVER GARANTIA");
            JsfUtil.ventanaErrorEffect("datosFormDevolucion");
        }
    }
    
    public void printFrmDevolucion(){
        try{
            getJpaController().edit(selected);
            
            JsfUtil.removeObjectSession("garantia");
            JsfUtil.removeObjectSession("numFormulario");
            JsfUtil.removeObjectSession("gerente");
            
            JsfUtil.putObjectSession("garantia",selected);
            JsfUtil.putObjectSession("numFormulario", selected.getAnioDev()+ "/"+selected.getFormDev());
            JsfUtil.putObjectSession("gerente", selected.getGteFormDev());
            
            JsfUtil.cerrarVentana("datosFormDevolucion");
            JsfUtil.AbrirVentana("formularioDevolucion");
        }catch(Exception ex){
            JsfUtil.addErrorMessage("ERROR AL GENERAR FORMULARIO DE DEVOLUCION");
        }
    }
    
    
    public void devolver() {
        try {
            selected.setEstado(2);
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("GARANTIA DEVUELTA A PROVEEDOR");
            JsfUtil.cerrarVentana("devolverGarantia");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("ERROR AL DEVOLVER GARANTIA");
            JsfUtil.ventanaErrorEffect("devolverGarantia");
        }
    }
    
    
    public void NODevolver() {
        try {
            if(selected.getEstado() == 2){
                selected.setEstado(1);
                selected.setRetiradoPor(null);
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage("GARANTIA ACTUALIZADA");
                JsfUtil.cerrarVentana("NODevolverGarantia");
            }
            else{
                JsfUtil.addWarnMessage("GARANTIA NO ESTA DEVUELTA");
                JsfUtil.cerrarVentana("NODevolverGarantia");
            }
        }catch (Exception e) {
            JsfUtil.addErrorMessage("ERROR AL ACTUALIZAR GARANTIA");
            JsfUtil.ventanaErrorEffect("NODevolverGarantia");
        }
    }

}
