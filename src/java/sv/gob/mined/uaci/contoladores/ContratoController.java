package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.ContratoJpaController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
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
import sv.gob.mined.uaci.entidades.Pago;
import sv.gob.mined.uaci.entidades.Proceso;

@ManagedBean(name = "contratoController")
@SessionScoped
public class ContratoController implements Serializable {

    private ContratoJpaController jpaController;
    private Integer anioRegistro;
    private List<Contrato> items;
    private List<Contrato> filtroContratos;
    private Contrato selected;
    private Contrato newItem;

    public ContratoController() {
        anioRegistro = JsfUtil.getCalendar().get(Calendar.YEAR);
    }

    public ContratoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ContratoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }

    public Integer getAnioRegistro() {
        return anioRegistro;
    }

    public void setAnioRegistro(Integer anioRegistro) {
        this.anioRegistro = anioRegistro;
    }
    
    public List<Contrato> getItems() {
        if (items == null) {
            items = getJpaController().findContratoByAnioFechaDoc(anioRegistro);
        }
        return items;
    }
    
    public Contrato getSelected() {
        if (selected == null) {
            selected = new Contrato();
        }
        return selected;
    }

    public void setSelected(Contrato selected) {
        this.selected = selected;
    }

    
    public Contrato getNewItem() {
        if (newItem == null) {
            newItem = new Contrato();
        }
        return newItem;
    }

    public void setNewItem(Contrato newItem) {
        this.newItem = newItem;
    }
    
    

    public List<Contrato> getFiltroContratos() {
        return filtroContratos;
    }

    public void setFiltroContratos(List<Contrato> filtroContratos) {
        this.filtroContratos = filtroContratos;
    }


    public void recreateModel() {
        items = null;
        if(filtroContratos != null){
            filtroContratos = items;
        }
    }
    
    
    public void prepareList() {
        recreateModel();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_contratos"));
    }

    

    public void prepareCreate() {
        newItem = new Contrato();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("CONTRATO REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearContrato");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearContrato");
        }
    }

    
    public Contrato createContrato() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("CONTRATO REGISTRADO CORRECTAMENTE");
            JsfUtil.cerrarVentana("crearContrato");
            return newItem;
        } catch (Exception e) {
            JsfUtil.ventanaErrorEffect("crearContrato");
            return  null;
        }
    }
    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("CONTRATO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarContrato");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarContrato");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("CONTRATO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findContratoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findContratoEntities(), true);
    }
    
    public List<Contrato> getItemsSelectMenu() {
        return getJpaController().findContratoEntities();
    }
    
    public List<Contrato> getContratosByProceso(Proceso proceso) {
        return getJpaController().findContratoByProceso(proceso);
    }
   

    private Pago selectedPago;

    public Pago getSelectedPago() {
        if(selectedPago == null){
            selectedPago = new Pago();
        }
        return selectedPago;
    }

    public void setSelectedPago(Pago selectedPago) {
        this.selectedPago = selectedPago;
    }
    
    public void prepareRegistrarPago(){
        selectedPago = new Pago();
        selectedPago.setContrato(selected);
    }
    
    public void registrarPago(){
        try {
            BigDecimal validarMonto = selectedPago.getContrato().sumaPagos();
            validarMonto = validarMonto.add(selectedPago.getMonto());
            if(validarMonto.compareTo(selectedPago.getContrato().getMonto()) <= 0){
                JsfUtil.SessionPagoController().create(selectedPago);
                recreateModel();
                JsfUtil.addSuccessMessage("PAGO REGISTRADO");
                JsfUtil.cerrarVentana("registrarPago");
            }else{
                JsfUtil.addErrorMessage("MONTO EXCEDE MONTO DE CONTRATO");
            }
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("registrarPago");
        }
    }
    
    
    
    
    
    @FacesConverter(forClass = Contrato.class)
    public static class ContratoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ContratoController controller = (ContratoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "contratoController");
            return controller.getJpaController().findContrato(getKey(value));
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
            if (object instanceof Contrato) {
                Contrato o = (Contrato) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Contrato.class.getName());
            }
        }
    }
    
}
