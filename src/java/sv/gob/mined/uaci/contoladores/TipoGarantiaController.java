package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.TipoGarantia;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.TipoGarantiaJpaController;

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

@ManagedBean(name = "tipoGarantiaController")
@SessionScoped
public class TipoGarantiaController implements Serializable {

    
    
    private TipoGarantiaJpaController jpaController;
    private List<TipoGarantia> items;
    private List<TipoGarantia> filtroTipoGarantias;
    private TipoGarantia selected;
    private TipoGarantia newItem;
    
    public TipoGarantiaController() {
    }

    public TipoGarantiaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new TipoGarantiaJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

       
    public List<TipoGarantia> getItems() {
        if (items == null) {
            items = getJpaController().findTipoGarantiaEntities();
        }
        return items;
    }
    
    public TipoGarantia getSelected() {
        if (selected == null) {
            selected = new TipoGarantia();
        }
        return selected;
    }

    public void setSelected(TipoGarantia selected) {
        this.selected = selected;
    }

    
    public TipoGarantia getNewItem() {
        if (newItem == null) {
            newItem = new TipoGarantia();
        }
        return newItem;
    }

    public void setNewItem(TipoGarantia newItem) {
        this.newItem = newItem;
    }

    
    
    

    public List<TipoGarantia> getFiltroTipoGarantias() {
        return filtroTipoGarantias;
    }

    public void setFiltroTipoGarantias(List<TipoGarantia> filtroTipoGarantias) {
        this.filtroTipoGarantias = filtroTipoGarantias;
    }


    public void recreateModel() {
        items = null;
        if(filtroTipoGarantias != null){
            filtroTipoGarantias = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
      //  JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_procesos"));
    }

    

    public void prepareCreate() {
        newItem = new TipoGarantia();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("TIPO DE GARANTIA AGREGADO");
            JsfUtil.cerrarVentana("crearTipoGarantia");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearTipoGarantia");
        }
    }

    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("TIPO DE GARANTIA ACTUALIZADO");
            JsfUtil.cerrarVentana("editarTipoGarantia");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarTipoGarantia");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("TIPO DE GARANTIA ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findTipoGarantiaEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTipoGarantiaEntities(), true);
    }
    
    public List<TipoGarantia> getItemsSelectMenu() {
        return getJpaController().findTipoGarantiaEntities();
    }
    
    public SelectItem[] getEstadosG() {
        SelectItem[] items = new SelectItem[4];
        for (int i=0;i<4;i++) {
            if(i==0){
                items[i] = new SelectItem("", "---");
            }else if(i==1){
                items[i] = new SelectItem("VIGENTE","VIGENTE" );
            }else if(i==2){
                items[i] = new SelectItem("DEVUELTA","DEVUELTA" );
            }else if(i==3){
                items[i] = new SelectItem("VENCIDA","VENCIDA" );
            }
        }
        return items;
    }
    
    public SelectItem[] getEstadosXVencer() {
        SelectItem[] items = new SelectItem[3];
        for (int i=0;i<3;i++) {
            if(i==0){
                items[i] = new SelectItem("", "---");
            }else if(i==1){
                items[i] = new SelectItem("true","VENCIDA" );
            }else if(i==2){
                items[i] = new SelectItem("false","POR VENCER" );
            }
        }
        return items;
    }
    
    @FacesConverter(forClass = TipoGarantia.class)
    public static class TipoGarantiaControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoGarantiaController controller = (TipoGarantiaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoGarantiaController");
            return controller.getJpaController().findTipoGarantia(getKey(value));
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
            if (object instanceof TipoGarantia) {
                TipoGarantia o = (TipoGarantia) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoGarantia.class.getName());
            }
        }
    }
}
