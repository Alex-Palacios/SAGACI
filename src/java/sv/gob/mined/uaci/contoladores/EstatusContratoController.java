package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.EstatusContrato;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.EstatusContratoJpaController;

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

@ManagedBean(name = "estatusContratoController")
@SessionScoped
public class EstatusContratoController implements Serializable {

    private EstatusContratoJpaController jpaController;
    private List<EstatusContrato> items;
    private List<EstatusContrato> filtroEstatus;
    private EstatusContrato selected;
    private EstatusContrato newItem;

    public EstatusContratoController() {
    }

    public EstatusContratoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new EstatusContratoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

    public void setJpaController(EstatusContratoJpaController jpaController) {
        this.jpaController = jpaController;
    }

    public List<EstatusContrato> getItems() {
        if (items == null) {
            items = getJpaController().findEstatusContratoEntities();
        }
        return items;
    }

    public void setItems(List<EstatusContrato> items) {
        this.items = items;
    }

    public List<EstatusContrato> getFiltroEstatus() {
        return filtroEstatus;
    }

    public void setFiltroEstatus(List<EstatusContrato> filtroEstatus) {
        this.filtroEstatus = filtroEstatus;
    }

    public EstatusContrato getSelected() {
        if (selected == null) {
            selected = new EstatusContrato();
        }
        return selected;
    }

    public void setSelected(EstatusContrato selected) {
        this.selected = selected;
    }

    public EstatusContrato getNewItem() {
        if (newItem == null) {
            newItem = new EstatusContrato();
        }
        return newItem;
    }

    public void setNewItem(EstatusContrato newItem) {
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
        newItem = new EstatusContrato();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("ESTADO DE CONTRATO AGREGADO");
            JsfUtil.cerrarVentana("crearEstatus");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearEstatus");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("ESTADO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarEstatus");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarEstatus");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("ESTADO DE CONTRATO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findEstatusContratoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findEstatusContratoEntities(), true);
    }
    
    public List<EstatusContrato> getItemsSelectMenu() {
        return getJpaController().findEstatusContratoEntities();
    }
    

    @FacesConverter(forClass = EstatusContrato.class)
    public static class EstatusContratoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EstatusContratoController controller = (EstatusContratoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "estatusContratoController");
            return controller.getJpaController().findEstatusContrato(getKey(value));
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
            if (object instanceof EstatusContrato) {
                EstatusContrato o = (EstatusContrato) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EstatusContrato.class.getName());
            }
        }
    }
}
