package sv.gob.mined.uaci.contoladores;

import sv.gob.mined.uaci.entidades.Pago;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;

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
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.jpa.ContratoJpaController;
import sv.gob.mined.uaci.jpa.PagoJpaController;

@ManagedBean(name = "pagoController")
@SessionScoped
public class PagoController implements Serializable {

    private PagoJpaController jpaController;
    private List<Pago> items;
    private List<Pago> filtroPagos;
    private Pago selected;
    private Pago newItem;
    private String listarPor;
    private boolean verPorContratos;
    private boolean verPorPagos;
    private Integer anio;
    
    public PagoController() {
        listarPor = "CONTRATOS";
        anio = JsfUtil.getCalendar().get(Calendar.YEAR);
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    
    public PagoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new PagoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }


    public List<Pago> getItems() {
        if (items == null) {
            items = getJpaController().findPagosByAnio(anio);
        }
        return items;
    }

    public void setItems(List<Pago> items) {
        this.items = items;
    }

    public String getListarPor() {
        return listarPor;
    }

    public void setListarPor(String listarPor) {
        this.listarPor = listarPor;
    }

    
    public List<Pago> getFiltroPagos() {
        return filtroPagos;
    }

    public void setFiltroPagos(List<Pago> filtroPagos) {
        this.filtroPagos = filtroPagos;
    }

    public Pago getSelected() {
        if (selected == null) {
            selected = new Pago();
        }
        return selected;
    }

    public void setSelected(Pago selected) {
        this.selected = selected;
    }

    public Pago getNewItem() {
        if (newItem == null) {
            newItem = new Pago();
        }
        return newItem;
    }

    public void setNewItem(Pago newItem) {
        this.newItem = newItem;
    }

    public boolean isVerPorContratos() {
        if(listarPor != null && listarPor.equals("CONTRATOS")){
            verPorContratos = true;
        }else{
            verPorContratos = false;
        }
        return verPorContratos;
    }

    public void setVerPorContratos(boolean verPorContratos) {
        this.verPorContratos = verPorContratos;
    }

    public boolean isVerPorPagos() {
        if(listarPor != null && listarPor.equals("PAGOS")){
            verPorPagos = true;
        }else{
            verPorPagos = false;
        }
        return verPorPagos;
    }

    public void setVerPorPagos(boolean verPorPagos) {
        this.verPorPagos = verPorPagos;
    }

    
    
    
    
    public void prepareList() {
        recreateModel();
        recreateModelContratos();
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_pagos"));
    }

    
    public void recreateModel() {
        items = null;
        if(filtroPagos != null){
            filtroPagos = items;
        }
    }
    

    public void prepareCreate() {
        newItem = new Pago();
    }
    
    public void create() {
        try {
            getJpaController().create(newItem);
            prepareCreate();
            recreateModel();
            JsfUtil.addSuccessMessage("PAGO AGREGADO");
            JsfUtil.cerrarVentana("crearPago");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("crearPago");
        }
    }

    public void create(Pago pago) throws Exception {
        try {
            getJpaController().create(pago);
        } catch (Exception e) {
            throw e;
        }
    }
    

    public void update() {
        try {
            getJpaController().edit(selected);
            JsfUtil.addSuccessMessage("PAGO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarPago");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarPago");
        }
    }
   

    public void delete() {
        try {
            getJpaController().destroy(selected.getId());
            recreateModel();
            JsfUtil.addSuccessMessage("PAGO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findPagoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findPagoEntities(), true);
    }
    
    public List<Pago> getItemsSelectMenu() {
        return getJpaController().findPagoEntities();
    }
    
    /// POR CONTRATOS /////
    private ContratoJpaController jpaContratoController;
    private List<Contrato> contratos;
    private List<Contrato> filtroContratos;
    private Contrato selectedContrato;

    public ContratoJpaController getJpaContratoController() {
        if (jpaContratoController == null) {
            jpaContratoController = new ContratoJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaContratoController;
    }

    
    public List<Contrato> getContratos() {
        if (contratos == null) {
            contratos = getJpaContratoController().findContratosArchivados(anio);
        }
        return contratos;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
    }

    public List<Contrato> getFiltroContratos() {
        return filtroContratos;
    }

    public void setFiltroContratos(List<Contrato> filtroContratos) {
        this.filtroContratos = filtroContratos;
    }

    public Contrato getSelectedContrato() {
        return selectedContrato;
    }

    public void setSelectedContrato(Contrato selectedContrato) {
        this.selectedContrato = selectedContrato;
    }
    
    
    
    public void recreateModelContratos() {
        contratos = null;
        if(filtroContratos != null){
            filtroContratos = contratos;
        }
    }
    
    public void prepareRegistrarPagoContrato(){
        selected = new Pago();
        selected.setContrato(selectedContrato);
    }
    
    public void registrarPagoContrato(){
        try {
            BigDecimal validarMonto = selected.getContrato().sumaPagos();
            validarMonto = validarMonto.add(selected.getMonto());
            if(validarMonto.compareTo(selected.getContrato().getMonto()) <= 0){
                create(selected);
                recreateModelContratos();
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
    
    
    
    public void eliminarPagoContrato(){
        try {
            delete();
            recreateModelContratos();
            JsfUtil.cerrarVentana("eliminarPago");
        } catch (Exception e) {
            JsfUtil.ventanaErrorEffect("eliminarPago");
        }
    }
    
    public void updateConsulta(){
        recreateModel();
        recreateModelContratos();
    }
    
    
    @FacesConverter(forClass = Pago.class)
    public static class PagoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PagoController controller = (PagoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pagoController");
            return controller.getJpaController().findPago(getKey(value));
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
            if (object instanceof Pago) {
                Pago o = (Pago) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Pago.class.getName());
            }
        }
    }
}
