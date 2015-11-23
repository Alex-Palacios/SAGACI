/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.servlet.reportes;

import java.math.BigDecimal;

/**
 *
 * @author Alex
 */
public class GarantiaVencida {
    private String clave;
    private String tipo;
    private String fRecepcion;
    private String proceso;
    private String descripcion;
    private String contrato;
    private String proveedor;
    private String afianzadora;
    private String tecnico;
    private String numGarantia;
    private String fVenc;
    private String plazo;
    private BigDecimal monto;

    public GarantiaVencida() {
    }

    public GarantiaVencida(String clave, String tipo, String fRecepcion, String proceso, String descripcion, String contrato, String proveedor, String afianzadora, String tecnico, String numGarantia, String fVenc, String plazo, BigDecimal monto) {
        this.clave = clave;
        this.tipo = tipo;
        this.fRecepcion = fRecepcion;
        this.proceso = proceso;
        this.descripcion = descripcion;
        this.contrato = contrato;
        this.proveedor = proveedor;
        this.afianzadora = afianzadora;
        this.tecnico = tecnico;
        this.numGarantia = numGarantia;
        this.fVenc = fVenc;
        this.plazo = plazo;
        this.monto = monto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getfRecepcion() {
        return fRecepcion;
    }

    public void setfRecepcion(String fRecepcion) {
        this.fRecepcion = fRecepcion;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getAfianzadora() {
        return afianzadora;
    }

    public void setAfianzadora(String afianzadora) {
        this.afianzadora = afianzadora;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getNumGarantia() {
        return numGarantia;
    }

    public void setNumGarantia(String numGarantia) {
        this.numGarantia = numGarantia;
    }

    public String getfVenc() {
        return fVenc;
    }

    public void setfVenc(String fVenc) {
        this.fVenc = fVenc;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
    
}
