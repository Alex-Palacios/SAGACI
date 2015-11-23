package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.UnidadTecnica;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.UnidadTecnicaJpaController;

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

@ManagedBean(name = "unidadTecnicaController")
@SessionScoped
public class UnidadTecnicaController implements Serializable {

    private UnidadTecnicaJpaController jpaController;
    private List<UnidadTecnica> items;
    private List<UnidadTecnica> filtroEstatus;
    private UnidadTecnica selected;
    private UnidadTecnica newItem;

    public UnidadTecnicaController() {
    }

    public UnidadTecnicaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new UnidadTecnicaJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }


    public List<UnidadTecnica> getItems() {
        if (items == null) {
            items = getJpaController().findUnidadTecnicaEntities();
        }
        return items;
    }

    public void setItems(List<UnidadTecnica> items) {
        this.items = items;
    }

    public List<UnidadTecnica> getFiltroEstatus() {
        return filtroEstatus;
    }

    public void setFiltroEstatus(List<UnidadTecnica> filtroEstatus) {
        this.filtroEstatus = filtroEstatus;
    }

    public UnidadTecnica getSelected() {
        if (selected == null) {
            selected = new UnidadTecnica();
        }
        return selected;
    }

    public void setSelected(UnidadTecnica selected) {
        this.selected = selected;
    }

    public UnidadTecnica getNewItem() {
        if (newItem == null) {
            newItem = new UnidadTecnica();
        }
        return newItem;
    }

    public void setNewItem(UnidadTecnica newItem) {
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
        newItem = new UnidadTecnica();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("UNIDAD TECNICA AGREGADA");
            JsfUtil.cerrarVentana("crearUnidad");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearUnidad");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("UNIDAD TECNICA ACTUALIZADA");
            JsfUtil.cerrarVentana("editarUnidad");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarUnidad");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("UNIDAD TECNICA ELIMINADA");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findUnidadTecnicaEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findUnidadTecnicaEntities(), true);
    }
    
    public List<UnidadTecnica> getItemsSelectMenu() {
        return getJpaController().findUnidadTecnicaEntities();
    }

    @FacesConverter(forClass = UnidadTecnica.class)
    public static class UnidadTecnicaControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UnidadTecnicaController controller = (UnidadTecnicaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "unidadTecnicaController");
            return controller.getJpaController().findUnidadTecnica(getKey(value));
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
            if (object instanceof UnidadTecnica) {
                UnidadTecnica o = (UnidadTecnica) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UnidadTecnica.class.getName());
            }
        }
    }
}
