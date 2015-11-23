package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.Proveedor;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.ProveedorJpaController;

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

@ManagedBean(name = "proveedorController")
@SessionScoped
public class ProveedorController implements Serializable {

    
    private ProveedorJpaController jpaController;
    private List<Proveedor> items;
    private List<Proveedor> filtroProveedores;
    private Proveedor selected;
    private Proveedor newItem;

    public ProveedorController() {
    }

    public ProveedorJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ProveedorJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

        
    public List<Proveedor> getItems() {
        if (items == null) {
            items = getJpaController().findProveedorEntities();
        }
        return items;
    }
    
    public Proveedor getSelected() {
        if (selected == null) {
            selected = new Proveedor();
        }
        return selected;
    }

    public void setSelected(Proveedor selected) {
        this.selected = selected;
    }

    
    public Proveedor getNewItem() {
        if (newItem == null) {
            newItem = new Proveedor();
        }
        return newItem;
    }

    public void setNewItem(Proveedor newItem) {
        this.newItem = newItem;
    }
    
    

    public List<Proveedor> getFiltroProveedores() {
        return filtroProveedores;
    }

    public void setFiltroProveedores(List<Proveedor> filtroProveedores) {
        this.filtroProveedores = filtroProveedores;
    }


    public void recreateModel() {
        items = null;
        if(filtroProveedores != null){
            filtroProveedores = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_proveedores"));
    }

    

    public void prepareCreate() {
        newItem = new Proveedor();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("PROVEEDOR REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearProveedor");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearProveedor");
        }
    }

    public Proveedor createProveedor() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("PROVEEDOR REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearProveedor");
            return newItem;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearProveedor");
            return null;
        }
    }

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("PROVEEDOR ACTUALIZADO");
            JsfUtil.cerrarVentana("editarProveedor");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarProveedor");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("PROVEEDOR ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findProveedorEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findProveedorEntities(), true);
    }
    
    public List<Proveedor> getItemsSelectMenu() {
        return getJpaController().findProveedorEntities();
    }
    
    public SelectItem[] getNaturalezaProveedorItems() {
        SelectItem[] options = new SelectItem[3];
        options[0] = new SelectItem("", "--------");
        options[1] = new SelectItem("1", ResourceBundle.getBundle("/Bundle").getString("personaNaturalLabel"));  
        options[2] = new SelectItem("2", ResourceBundle.getBundle("/Bundle").getString("personaJuridicaLabel"));
        return options;
    }
  

    @FacesConverter(forClass = Proveedor.class)
    public static class ProveedorControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProveedorController controller = (ProveedorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proveedorController");
            return controller.getJpaController().findProveedor(getKey(value));
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
            if (object instanceof Proveedor) {
                Proveedor o = (Proveedor) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Proveedor.class.getName());
            }
        }
    }
}
