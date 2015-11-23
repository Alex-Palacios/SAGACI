package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.TipoPago;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.TipoPagoJpaController;

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

@ManagedBean(name = "tipoPagoController")
@SessionScoped
public class TipoPagoController implements Serializable {

    private TipoPagoJpaController jpaController;
    private List<TipoPago> items;
    private List<TipoPago> filtroEstatus;
    private TipoPago selected;
    private TipoPago newItem;

    public TipoPagoController() {
    }

    public TipoPagoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new TipoPagoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }


    public List<TipoPago> getItems() {
        if (items == null) {
            items = getJpaController().findTipoPagoEntities();
        }
        return items;
    }

    public void setItems(List<TipoPago> items) {
        this.items = items;
    }

    public List<TipoPago> getFiltroEstatus() {
        return filtroEstatus;
    }

    public void setFiltroEstatus(List<TipoPago> filtroEstatus) {
        this.filtroEstatus = filtroEstatus;
    }

    public TipoPago getSelected() {
        if (selected == null) {
            selected = new TipoPago();
        }
        return selected;
    }

    public void setSelected(TipoPago selected) {
        this.selected = selected;
    }

    public TipoPago getNewItem() {
        if (newItem == null) {
            newItem = new TipoPago();
        }
        return newItem;
    }

    
    public void setNewItem(TipoPago newItem) {
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
        newItem = new TipoPago();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("TIPO DE PAGO AGREGADO");
            JsfUtil.cerrarVentana("crearTipoPago");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearTipoPago");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("TIPO PAGO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarTipoPago");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarTipoPago");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("TIPO DE PAGO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findTipoPagoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTipoPagoEntities(), true);
    }
    
    public List<TipoPago> getItemsSelectMenu() {
        return getJpaController().findTipoPagoEntities();
    }
    
    
    

    @FacesConverter(forClass = TipoPago.class)
    public static class TipoPagoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoPagoController controller = (TipoPagoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoPagoController");
            return controller.getJpaController().findTipoPago(getKey(value));
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
            if (object instanceof TipoPago) {
                TipoPago o = (TipoPago) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoPago.class.getName());
            }
        }
    }
}
