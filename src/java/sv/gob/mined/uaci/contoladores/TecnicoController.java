package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.Tecnico;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.TecnicoJpaController;

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

@ManagedBean(name = "tecnicoController")
@SessionScoped
public class TecnicoController implements Serializable {

    
    private TecnicoJpaController jpaController;
    private List<Tecnico> items;
    private List<Tecnico> filtroTecnicos;
    private Tecnico selected;
    private Tecnico newItem;

    public TecnicoController() {
        
    }

    public TecnicoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new TecnicoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

    
    public List<Tecnico> getItems() {
        if (items == null) {
            items = getJpaController().findTecnicoEntities();
        }
        return items;
    }
    
    public Tecnico getSelected() {
        if (selected == null) {
            selected = new Tecnico();
        }
        return selected;
    }

    public void setSelected(Tecnico selected) {
        this.selected = selected;
    }

    
    public Tecnico getNewItem() {
        if (newItem == null) {
            newItem = new Tecnico();
        }
        return newItem;
    }

    public void setNewItem(Tecnico newItem) {
        this.newItem = newItem;
    }

    public List<Tecnico> getFiltroTecnicos() {
        return filtroTecnicos;
    }

    public void setFiltroTecnicos(List<Tecnico> filtroTecnicos) {
        this.filtroTecnicos = filtroTecnicos;
    }
    
    

    


    public void recreateModel() {
        items = null;
        if(filtroTecnicos != null){
            filtroTecnicos = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_tecnicos"));
    }

    

    public void prepareCreate() {
        newItem = new Tecnico();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("TECNICO LISTADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearTecnico");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearTecnico");
        }
    }

    public Tecnico createTecnico() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("TECNICO LISTADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearTecnico");
            return newItem;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearTecnico");
            return null;
        }
    }

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("INFORMACION DE TECNICO ACTUALIZADA");
            JsfUtil.cerrarVentana("editarTecnico");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarTecnico");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("TECNICO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findTecnicoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTecnicoEntities(), true);
    }
    
    public List<Tecnico> getItemsSelectMenu() {
        return getJpaController().findTecnicoEntities();
    }
    
    @FacesConverter(forClass = Tecnico.class)
    public static class TecnicoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TecnicoController controller = (TecnicoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tecnicoController");
            return controller.getJpaController().findTecnico(getKey(value));
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
            if (object instanceof Tecnico) {
                Tecnico o = (Tecnico) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tecnico.class.getName());
            }
        }
    }
}
