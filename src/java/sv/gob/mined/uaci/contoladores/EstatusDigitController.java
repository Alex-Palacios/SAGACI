package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.EstatusDigit;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.EstatusDigitJpaController;

import java.io.Serializable;
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

@ManagedBean(name = "estatusDigitController")
@SessionScoped
public class EstatusDigitController implements Serializable {

    private EstatusDigitJpaController jpaController;
    private List<EstatusDigit> items;
    private List<EstatusDigit> filtroEstatus;
    private EstatusDigit selected;
    private EstatusDigit newItem;

    public EstatusDigitController() {
    }

    public EstatusDigitJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new EstatusDigitJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }


    public List<EstatusDigit> getItems() {
        if (items == null) {
            items = getJpaController().findEstatusDigitEntities();
        }
        return items;
    }

    public void setItems(List<EstatusDigit> items) {
        this.items = items;
    }

    public List<EstatusDigit> getFiltroEstatus() {
        return filtroEstatus;
    }

    public void setFiltroEstatus(List<EstatusDigit> filtroEstatus) {
        this.filtroEstatus = filtroEstatus;
    }

    public EstatusDigit getSelected() {
        if (selected == null) {
            selected = new EstatusDigit();
        }
        return selected;
    }

    public void setSelected(EstatusDigit selected) {
        this.selected = selected;
    }

    public EstatusDigit getNewItem() {
        if (newItem == null) {
            newItem = new EstatusDigit();
        }
        return newItem;
    }

    
    public void setNewItem(EstatusDigit newItem) {
        this.newItem = newItem;
    }

    
    public void recreateModel() {
        items = null;
        if(filtroEstatus != null){
            filtroEstatus = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
    }

    

    public void prepareCreate() {
        newItem = new EstatusDigit();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("ESTADO DE DIGITALIZACION AGREGADO");
            JsfUtil.cerrarVentana("crearEstatusDigit");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearEstatusDigit");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("ESTADO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarEstatusDigit");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarEstatusDigit");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("ESTADO DE DIGITALIZACION ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findEstatusDigitEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findEstatusDigitEntities(), true);
    }
    
    public List<EstatusDigit> getItemsSelectMenu() {
        return getJpaController().findEstatusDigitEntities();
    }

    @FacesConverter(forClass = EstatusDigit.class)
    public static class EstatusDigitControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EstatusDigitController controller = (EstatusDigitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "estatusDigitController");
            return controller.getJpaController().findEstatusDigit(getKey(value));
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
            if (object instanceof EstatusDigit) {
                EstatusDigit o = (EstatusDigit) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EstatusDigit.class.getName());
            }
        }
    }
}
