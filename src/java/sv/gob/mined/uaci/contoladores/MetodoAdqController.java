package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.MetodoAdq;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.MetodoAdqJpaController;

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

@ManagedBean(name = "metodoAdqController")
@SessionScoped
public class MetodoAdqController implements Serializable {

    
    private MetodoAdqJpaController jpaController;
    private List<MetodoAdq> items;
    private List<MetodoAdq> filtroMetodoAdqs;
    private MetodoAdq selected;
    private MetodoAdq newItem;

    public MetodoAdqController() {
    }

    public MetodoAdqJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new MetodoAdqJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

       
    public List<MetodoAdq> getItems() {
        if (items == null) {
            items = getJpaController().findMetodoAdqEntities();
        }
        return items;
    }
    
    public MetodoAdq getSelected() {
        if (selected == null) {
            selected = new MetodoAdq();
        }
        return selected;
    }

    public void setSelected(MetodoAdq selected) {
        this.selected = selected;
    }

    
    public MetodoAdq getNewItem() {
        if (newItem == null) {
            newItem = new MetodoAdq();
        }
        return newItem;
    }

    public void setNewItem(MetodoAdq newItem) {
        this.newItem = newItem;
    }
    
    

    public List<MetodoAdq> getFiltroMetodoAdqs() {
        return filtroMetodoAdqs;
    }

    public void setFiltroMetodoAdqs(List<MetodoAdq> filtroMetodoAdqs) {
        this.filtroMetodoAdqs = filtroMetodoAdqs;
    }


    public void recreateModel() {
        items = null;
        if(filtroMetodoAdqs != null){
            filtroMetodoAdqs = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
      //  JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_procesos"));
    }

    

    public void prepareCreate() {
        newItem = new MetodoAdq();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("METODO DE ADQUISICION AGREGADO");
            JsfUtil.cerrarVentana("crearMetodoAdq");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearMetodoAdq");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("METODO ADQUISICION ACTUALIZADO");
            JsfUtil.cerrarVentana("editarMetodoAdq");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarMetodoAdq");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("METODO DE ADQUISCION ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findMetodoAdqEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findMetodoAdqEntities(), true);
    }
    
    public List<MetodoAdq> getItemsSelectMenu() {
        return getJpaController().findMetodoAdqEntities();
    }
    
    @FacesConverter(forClass = MetodoAdq.class)
    public static class MetodoAdqControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MetodoAdqController controller = (MetodoAdqController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "metodoAdqController");
            return controller.getJpaController().findMetodoAdq(getKey(value));
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
            if (object instanceof MetodoAdq) {
                MetodoAdq o = (MetodoAdq) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + MetodoAdq.class.getName());
            }
        }
    }
}
