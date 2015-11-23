package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.Archivo;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.ArchivoJpaController;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.FuenteFinanc;
import sv.gob.mined.uaci.jpa.ContratoJpaController;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Descripcion: Esta clase es la encargada de comunicar las capas de vista y la capa
 *              de controlador y se encarga de los operaciones con la entidad Archivo, y de
 *              devolver el resultado de las consultas sobre esta entidad.
 */
@ManagedBean(name = "archivoController")
@SessionScoped
public class ArchivoController implements Serializable {

    private ArchivoJpaController jpaController;
    private ContratoJpaController jpaContratoController;
    private Integer anioRegistro;
    private List<Contrato> itemsContratos;
    private List<Contrato> filtroContratos;
    private List<Archivo> items;
    private List<Archivo> filtroArchivos;
    private Archivo selected;
    private Archivo newItem;
    private Contrato selectedContrato;
    private List<Contrato> archivarList;
    private int numContratosArchivar;
    private FuenteFinanc fuente;
    
    /* Constructor de clase */
    public ArchivoController() {
        anioRegistro = JsfUtil.getCalendar().get(Calendar.YEAR);
    }

    public Integer getAnioRegistro() {
        return anioRegistro;
    }

    public void setAnioRegistro(Integer anioRegistro) {
        this.anioRegistro = anioRegistro;
    }

    
    public ArchivoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ArchivoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

    public ContratoJpaController getJpaContratoController() {
        if (jpaContratoController == null) {
            jpaContratoController = new ContratoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaContratoController;
    }

    public List<Contrato> getArchivarList() {
        return archivarList;
    }

    public void setArchivarList(List<Contrato> archivarList) {
        this.archivarList = archivarList;
    }

    public FuenteFinanc getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanc fuente) {
        this.fuente = fuente;
    }

    
    
    public List<Archivo> getItems() {
        if (items == null) {
            if(fuente == null){
                items = getJpaController().findArchivoByAnio(anioRegistro);
            }else{
                items = getJpaController().findArchivoByAnio(anioRegistro, fuente);
            }
        }
        return items;
    }
    
    public Archivo getSelected() {
        if (selected == null) {
            selected = new Archivo();
        }
        return selected;
    }

    public void setSelected(Archivo selected) {
        this.selected = selected;
    }

    
    public Archivo getNewItem() {
        if (newItem == null) {
            newItem = new Archivo();
        }
        return newItem;
    }

    public void setNewItem(Archivo newItem) {
        this.newItem = newItem;
    }

    public List<Archivo> getFiltroArchivos() {
        return filtroArchivos;
    }

    public void setFiltroArchivos(List<Archivo> filtroArchivos) {
        this.filtroArchivos = filtroArchivos;
    }

    public List<Contrato> getItemsContratos() {
        if (itemsContratos == null) {
            itemsContratos = getJpaContratoController().findContratoParaArchivar();
        }
        return itemsContratos;
    }

    public void setItemsContratos(List<Contrato> itemsContratos) {
        this.itemsContratos = itemsContratos;
    }

    public List<Contrato> getFiltroContratos() {
        return filtroContratos;
    }

    public void setFiltroContratos(List<Contrato> filtroContratos) {
        this.filtroContratos = filtroContratos;
    }

    public Contrato getSelectedContrato() {
        if(selectedContrato == null){
            selectedContrato = new Contrato();
        }
        return selectedContrato;
    }

    public void setSelectedContrato(Contrato selectedContrato) {
        this.selectedContrato = selectedContrato;
    }

    public int getNumContratosArchivar() {
        if(archivarList == null){
            numContratosArchivar = 0;
        }else{
            numContratosArchivar = archivarList.size();
        }
        return numContratosArchivar;
    }

    public void setNumContratosArchivar(int numContratosArchivar) {
        this.numContratosArchivar = numContratosArchivar;
    }
    
    
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////// CONTRATOS //////////////////////////////////////////////////////////////////////

    public void recreateModelContratos() {
        itemsContratos = null;
        if(filtroContratos != null){
            filtroContratos = itemsContratos;
        }
    }
    
    public void prepareListArchivar() {
        recreateModelContratos();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_archivar_contratos"));
    }
    
    public void prepareArchivarContratos() {
        if(archivarList != null && !archivarList.isEmpty()){
            prepareCreate();
            JsfUtil.AbrirVentana("archivarContrato");
        }else{
            JsfUtil.addWarnMessage("DEBE SELECCIONAR AL MENOS UN CONTRATO");
        }
    }
    
    public void archivar(){
        int archivados=0;
        int errores=0;
        for(Contrato c : archivarList){
            Archivo archivo = new Archivo();
            archivo.clone(newItem);
            archivo.setContrato(c);
            if(createArchivo(archivo)){
                archivados++;
                recreateModelContratos();
            }else{
                errores++;
            }
        }
        if(archivados > 0){
            JsfUtil.addSuccessMessage(archivados + " CONTRATOS ARCHIVADOS CORRECTAMENTE");
        }
        if(errores > 0){
            JsfUtil.addErrorMessage("NO SE PUDIERON ARCHIVAR " +errores + " CONTRATOS");
        }
        JsfUtil.cerrarVentana("archivarContrato");
    }

    public boolean createArchivo(Archivo archivoNuevo) {
        boolean r = false;
        try {
            getJpaController().create(archivoNuevo);
            r=true;
        } catch (Exception e) {   }finally{return r;}
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////// ARCHIVO //////////////////////////////////////////////////////////////////////

    
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_archivos"));
    }
    
    
    public void recreateModel() {
        items = null;
        if(filtroArchivos != null){
            filtroArchivos = items;
        }
    }
    
    

    public void prepareCreate() {
        newItem = new Archivo();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("ARCHIVO REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearArchivo");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearArchivo");
        }
    }

    
    public Archivo createArchivo() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("ARCHIVO REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearArchivo");
            return newItem;
        } catch (Exception e) {
            JsfUtil.ventanaErrorEffect("crearArchivo");
            return  null;
        }
    }
    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("ARCHIVO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarArchivo");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarArchivo");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("ARCHIVO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public void prepareUpdateContrato() {
        selectedContrato = selected.getContrato();
    }
    
    public void updateContrato() {
        try {
            JsfUtil.SessionContratoController().getJpaController().edit(selectedContrato);
            JsfUtil.addSuccessMessage("CONTRATO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarContrato");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarContrato");
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findArchivoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findArchivoEntities(), true);
    }
    
    public List<Archivo> getItemsSelectMenu() {
        return getJpaController().findArchivoEntities();
    }
    

    public void generarRptArchivos(){
        if(items != null ){
            JsfUtil.putObjectSession("archivos", items);
            JsfUtil.AbrirVentana("rptArchivos");
        }else{
            JsfUtil.addErrorMessage("LISTA DE ARCHIVOS VACIA");
        }
        
    }
    
    
    @FacesConverter(forClass = Archivo.class)
    public static class ArchivoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ArchivoController controller = (ArchivoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "archivoController");
            return controller.getJpaController().findArchivo(getKey(value));
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
            if (object instanceof Archivo) {
                Archivo o = (Archivo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Archivo.class.getName());
            }
        }
    }
}
