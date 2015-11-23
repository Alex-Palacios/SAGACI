package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.Afianzadora;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.AfianzadoraJpaController;

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

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Descripcion: Esta clase es la encargada de comunicar las capas de vista y la capa
 *              de controlador y se encarga de los operaciones con la entidad Afianzadora, y de
 *              devolver el resultado de las consultas sobre esta entidad.
 */

@ManagedBean(name = "afianzadoraController")
@SessionScoped
public class AfianzadoraController implements Serializable {

    private AfianzadoraJpaController jpaController; // Controlador de persistencia
    private List<Afianzadora> items;//Lista de afianzadoras
    private List<Afianzadora> filtroAfianzadoras; // filtro para tablas en la vista
    private Afianzadora selected; // Afianzadora Selecccionada
    private Afianzadora newItem; // Nueva Afianzadora

    /* Constructor de clase */
    public AfianzadoraController() {
        
    }
    /* Controlador JPA para la persistencia*/
    public AfianzadoraJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AfianzadoraJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

    /** GETTERS y SETTERS **/
    public List<Afianzadora> getItems() {
        if (items == null) {
            items = getJpaController().findAfianzadoraEntities();
        }
        return items;
    }
    
    public Afianzadora getSelected() {
        if (selected == null) {
            selected = new Afianzadora();
        }
        return selected;
    }

    public void setSelected(Afianzadora selected) {
        this.selected = selected;
    }

    
    public Afianzadora getNewItem() {
        if (newItem == null) {
            newItem = new Afianzadora();
        }
        return newItem;
    }

    public void setNewItem(Afianzadora newItem) {
        this.newItem = newItem;
    }

    public List<Afianzadora> getFiltroAfianzadoras() {
        return filtroAfianzadoras;
    }

    public void setFiltroAfianzadoras(List<Afianzadora> filtroAfianzadoras) {
        this.filtroAfianzadoras = filtroAfianzadoras;
    }
    
    /** Limpia la lista de afiazadora y filtro **/
    public void recreateModel() {
        items = null;
        if(filtroAfianzadoras != null){
            filtroAfianzadoras = items;
        }
    }
    
    /** Redirige a la url de lista de afianzadora **/
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_afianzadoras"));
    }

    
    // Prepara nueva afiazadora
    public void prepareCreate() {
        newItem = new Afianzadora();
    }
    
    /** **
     * Operaciones CRUD para la entidad afianzadora
     **/
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("AFIANZADORA LISTADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearAfianzadora");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearAfianzadora");
        }
    }

    public Afianzadora createAfianzadora() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("AFIANZADORA LISTADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearAfianzadora");
            return newItem;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearAfianzadora");
            return null;
        }
    }

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("INFORMACION DE AFIANZADORA ACTUALIZADA");
            JsfUtil.cerrarVentana("editarAfianzadora");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarAfianzadora");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            JsfUtil.addSuccessMessage("AFIANZADORA ELIMINADA");
            recreateModel();
            JsfUtil.cerrarVentana("eliminarAfianzadora");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /** **
     * Listas de entidades Afianzadoras
     **/
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findAfianzadoraEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findAfianzadoraEntities(), true);
    }
    
    public List<Afianzadora> getItemsSelectMenu() {
        return getJpaController().findAfianzadoraEntities();
    }

    /** 
     * Descripcion: Esta es una clase convertidora y se encarga de transformar los items de un combobox en la vista
     *              en la clase de la entidad Afianzadora a partir de su Primary Key.
     **/
    @FacesConverter(forClass = Afianzadora.class)
    public static class AfianzadoraControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AfianzadoraController controller = (AfianzadoraController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "afianzadoraController");
            return controller.getJpaController().findAfianzadora(getKey(value));
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
            if (object instanceof Afianzadora) {
                Afianzadora o = (Afianzadora) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Afianzadora.class.getName());
            }
        }
    }
}
