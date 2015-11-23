/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.contoladores.util;

import java.util.Date;

/**
 *
 * @author ALEX
 */
public class GarantiaImport {
    private Integer anioClave;
    private Integer numClave;
    private String metodo;
    private String numProceso;
    private String proceso;
    private String siap;
    private String tecnico;
    private String contrato;
    private String modificativa;
    private Date fechaContrato;
    private String proveedor;
    private String plazoContrato;
    private Double montoContrato;
    private String tipoGarantia;
    private Date fechaRecepcion;
    private Double montoGarantia;
    private String plazo;
    private String afianzadora;
    private Date fechaVencimiento;
    private String numGarantia;
    private Date fechaDevolucion;
    private String personaRetira;
    private String estado;
    

    public GarantiaImport() {
    }

    public Integer getAnioClave() {
        return anioClave;
    }

    public void setAnioClave(Integer anioClave) {
        this.anioClave = anioClave;
    }

    public Integer getNumClave() {
        return numClave;
    }

    public void setNumClave(Integer numClave) {
        this.numClave = numClave;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getNumProceso() {
        return numProceso;
    }

    public void setNumProceso(String numProceso) {
        this.numProceso = numProceso;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getSiap() {
        return siap;
    }

    public void setSiap(String siap) {
        this.siap = siap;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getModificativa() {
        return modificativa;
    }

    public void setModificativa(String modificativa) {
        this.modificativa = modificativa;
    }

    public Date getFechaContrato() {
        return fechaContrato;
    }
    
    public String getFechaContratoString() {
        return JsfUtil.setFechaFormateada(fechaContrato, 1);
    }

    public void setFechaContrato(Date fechaContrato) {
        this.fechaContrato = fechaContrato;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getPlazoContrato() {
        return plazoContrato;
    }

    public void setPlazoContrato(String plazoContrato) {
        this.plazoContrato = plazoContrato;
    }

    public Double getMontoContrato() {
        return montoContrato;
    }

    public void setMontoContrato(Double montoContrato) {
        this.montoContrato = montoContrato;
    }

    public String getTipoGarantia() {
        return tipoGarantia;
    }

    public void setTipoGarantia(String tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }
    
    public String getFechaRecepcionString() {
        return JsfUtil.setFechaFormateada(fechaRecepcion, 1);
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public Double getMontoGarantia() {
        return montoGarantia;
    }

    public void setMontoGarantia(Double montoGarantia) {
        this.montoGarantia = montoGarantia;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public String getAfianzadora() {
        return afianzadora;
    }

    public void setAfianzadora(String afianzadora) {
        this.afianzadora = afianzadora;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public String getFechaVencimientoString() {
        return JsfUtil.setFechaFormateada(fechaVencimiento, 1);
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumGarantia() {
        return numGarantia;
    }

    public void setNumGarantia(String numGarantia) {
        this.numGarantia = numGarantia;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }
    
    public String getFechaDevolucionString() {
        return JsfUtil.setFechaFormateada(fechaDevolucion, 1);
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getPersonaRetira() {
        return personaRetira;
    }

    public void setPersonaRetira(String personaRetira) {
        this.personaRetira = personaRetira;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
    
}
