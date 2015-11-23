package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.Proceso;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.ProcesoJpaController;

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

@ManagedBean(name = "procesoController")
@SessionScoped
public class ProcesoController implements Serializable {

    private ProcesoJpaController jpaController;
    private Integer anioRegistro;
    private List<Proceso> items;
    private List<Proceso> filtroProcesos;
    private Proceso selected;
    private Proceso newItem;

    public ProcesoController() {
        anioRegistro = JsfUtil.getCalendar().get(Calendar.YEAR);
    }

    public ProcesoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ProcesoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

    public Integer getAnioRegistro() {
        return anioRegistro;
    }

    public void setAnioRegistro(Integer anioRegistro) {
        this.anioRegistro = anioRegistro;
    }
    
    public List<Proceso> getItems() {
        if (items == null) {
            items = getJpaController().findProcesoByAnioRegistro(anioRegistro);
        }
        return items;
    }
    
    public Proceso getSelected() {
        if (selected == null) {
            selected = new Proceso();
        }
        return selected;
    }

    public void setSelected(Proceso selected) {
        this.selected = selected;
    }

    
    public Proceso getNewItem() {
        if (newItem == null) {
            newItem = new Proceso();
        }
        return newItem;
    }

    public void setNewItem(Proceso newItem) {
        this.newItem = newItem;
    }
    
    

    public List<Proceso> getFiltroProcesos() {
        return filtroProcesos;
    }

    public void setFiltroProcesos(List<Proceso> filtroProcesos) {
        this.filtroProcesos = filtroProcesos;
    }


    public void recreateModel() {
        items = null;
        if(filtroProcesos != null){
            filtroProcesos = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_procesos"));
    }

    

    public void prepareCreate() {
        newItem = new Proceso();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("PROCESO REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearProceso");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearProceso");
        }
    }
    
    public Proceso createProceso() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("PROCESO REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearProceso");
            return newItem;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearProceso");
            return null;
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("PROCESO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarProceso");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarProceso");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("PROCESO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findProcesoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findProcesoEntities(), true);
    }
    
    public List<Proceso> getItemsSelectMenu() {
        return getJpaController().findProcesoEntities();
    }
    
    
    @FacesConverter(forClass = Proceso.class)
    public static class ProcesoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProcesoController controller = (ProcesoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "procesoController");
            return controller.getJpaController().findProceso(getKey(value));
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
            if (object instanceof Proceso) {
                Proceso o = (Proceso) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Proceso.class.getName());
            }
        }
    }
}
