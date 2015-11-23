package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.EstatusFolio;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.EstatusFolioJpaController;

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

@ManagedBean(name = "estatusFolioController")
@SessionScoped
public class EstatusFolioController implements Serializable {

    private EstatusFolioJpaController jpaController;
    private List<EstatusFolio> items;
    private List<EstatusFolio> filtroEstatus;
    private EstatusFolio selected;
    private EstatusFolio newItem;

    public EstatusFolioController() {
    }

    public EstatusFolioJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new EstatusFolioJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }


    public List<EstatusFolio> getItems() {
        if (items == null) {
            items = getJpaController().findEstatusFolioEntities();
        }
        return items;
    }

    public void setItems(List<EstatusFolio> items) {
        this.items = items;
    }

    public List<EstatusFolio> getFiltroEstatus() {
        return filtroEstatus;
    }

    public void setFiltroEstatus(List<EstatusFolio> filtroEstatus) {
        this.filtroEstatus = filtroEstatus;
    }

    public EstatusFolio getSelected() {
        if (selected == null) {
            selected = new EstatusFolio();
        }
        return selected;
    }

    public void setSelected(EstatusFolio selected) {
        this.selected = selected;
    }

    public EstatusFolio getNewItem() {
        if (newItem == null) {
            newItem = new EstatusFolio();
        }
        return newItem;
    }

    
    public void setNewItem(EstatusFolio newItem) {
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
        newItem = new EstatusFolio();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("ESTADO DE FOLIO AGREGADO");
            JsfUtil.cerrarVentana("crearEstatusFolio");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearEstatusFolio");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("ESTADO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarEstatusFolio");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarEstatusFolio");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("ESTADO DE FOLIO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findEstatusFolioEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findEstatusFolioEntities(), true);
    }
    
    public List<EstatusFolio> getItemsSelectMenu() {
        return getJpaController().findEstatusFolioEntities();
    }
    

    @FacesConverter(forClass = EstatusFolio.class)
    public static class EstatusFolioControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EstatusFolioController controller = (EstatusFolioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "estatusFolioController");
            return controller.getJpaController().findEstatusFolio(getKey(value));
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
            if (object instanceof EstatusFolio) {
                EstatusFolio o = (EstatusFolio) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EstatusFolio.class.getName());
            }
        }
    }
}
