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
public class ArchivoExp {
    private String registradoPor;
    private String caja;
    private String fRecepcion;
    private String proceso;
    private String descripcion;
    private String contrato;
    private String proveedor;
    private String ampo;
    private String tecnico;
    private String folio;
    private String fEntrega;
    private String plazo;
    private BigDecimal monto;
    private String digit;
    private String numPag;
    
    public ArchivoExp() {
    }

    public ArchivoExp(String registradoPor, String caja, String fRecepcion, String proceso, String descripcion, String contrato, String proveedor, String ampo, String tecnico, String folio, String fEntrega, String plazo, BigDecimal monto, String digit, String numPag) {
        this.registradoPor = registradoPor;
        this.caja = caja;
        this.fRecepcion = fRecepcion;
        this.proceso = proceso;
        this.descripcion = descripcion;
        this.contrato = contrato;
        this.proveedor = proveedor;
        this.ampo = ampo;
        this.tecnico = tecnico;
        this.folio = folio;
        this.fEntrega = fEntrega;
        this.plazo = plazo;
        this.monto = monto;
        this.digit = digit;
        this.numPag = numPag;
    }

    public String getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getAmpo() {
        return ampo;
    }

    public void setAmpo(String ampo) {
        this.ampo = ampo;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getfEntrega() {
        return fEntrega;
    }

    public void setfEntrega(String fEntrega) {
        this.fEntrega = fEntrega;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getNumPag() {
        return numPag;
    }

    public void setNumPag(String numPag) {
        this.numPag = numPag;
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


    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
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
