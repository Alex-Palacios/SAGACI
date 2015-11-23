package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.FuenteFinanc;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.contoladores.util.PaginationHelper;
import sv.gob.mined.uaci.jpa.FuenteFinancJpaController;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;

@ManagedBean(name = "fuenteFinancController")
@SessionScoped
public class FuenteFinancController implements Serializable {

    private FuenteFinancJpaController jpaController;
    private List<FuenteFinanc> items;
    private List<FuenteFinanc> filtroFuentes;
    private FuenteFinanc selected;
    private FuenteFinanc newItem;

    public FuenteFinancController() {
    }

    public FuenteFinancJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new FuenteFinancJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }


    public List<FuenteFinanc> getItems() {
        if (items == null) {
            items = getJpaController().findFuenteFinancEntities();
        }
        return items;
    }

    public void setItems(List<FuenteFinanc> items) {
        this.items = items;
    }

    public List<FuenteFinanc> getFiltroFuentes() {
        return filtroFuentes;
    }

    public void setFiltroFuentes(List<FuenteFinanc> filtroFuentes) {
        this.filtroFuentes = filtroFuentes;
    }

    public FuenteFinanc getSelected() {
        if (selected == null) {
            selected = new FuenteFinanc();
        }
        return selected;
    }

    public void setSelected(FuenteFinanc selected) {
        this.selected = selected;
    }

    public FuenteFinanc getNewItem() {
        if (newItem == null) {
            newItem = new FuenteFinanc();
        }
        return newItem;
    }

    public void setNewItem(FuenteFinanc newItem) {
        this.newItem = newItem;
    }

    
    public void recreateModel() {
        items = null;
        if(filtroFuentes != null){
            filtroFuentes = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_fuentes"));
    }

    

    public void prepareCreate() {
        newItem = new FuenteFinanc();
        
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("FUENTE AGREGADA");
            JsfUtil.cerrarVentana("crearFuente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearFuente");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("FUENTE ACTUALIZADA");
            JsfUtil.cerrarVentana("editarFuente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarFuente");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("FUENTE ELIMINADA");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findFuenteFinancEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findFuenteFinancEntities(), true);
    }
    
    public List<FuenteFinanc> getItemsSelectMenu() {
        return getJpaController().findFuenteFinancEntities();
    }


    @FacesConverter(forClass = FuenteFinanc.class)
    public static class FuenteFinancControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FuenteFinancController controller = (FuenteFinancController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fuenteFinancController");
            return controller.getJpaController().findFuenteFinanc(getKey(value));
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
            if (object instanceof FuenteFinanc) {
                FuenteFinanc o = (FuenteFinanc) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + FuenteFinanc.class.getName());
            }
        }
    }
}
