
package sv.gob.mined.uaci.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 */
@Entity
@Table(name = "PAGO",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findByFechaPago", query = "SELECT p FROM Pago p WHERE p.fecha = :fechaPago"),
    @NamedQuery(name = "Pago.findByAnio", query = "SELECT p FROM Pago p WHERE FUNC('TO_CHAR',p.fecha,'yyyy') = :anio "),
    @NamedQuery(name = "Pago.findByDocPago", query = "SELECT p FROM Pago p WHERE p.documento = :docPago")})
public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPago")
    @SequenceGenerator(name = "seqPago",schema = "SAGACI", sequenceName = "SEQ_PAGO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_PAGO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "DOC_PAGO")
    private String documento;
    @Basic(optional = false)
    @Column(name = "MONTO_PAGO")
    private BigDecimal monto;
    @Column(name = "NOTA_PAGO")
    private String nota;
    
    @Basic(optional = false)
    @Column(name = "REGPOR_PAGO")
    private String registradoPor;
    
    @Basic(optional = false)
    @Column(name = "FH_ING_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    
    @Basic(optional = false)
    @Column(name = "MODPOR_PAGO")
    private String modificadoPor;
    
    @Basic(optional = false)
    @Column(name = "FH_MOD_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificado;
    @JoinColumn(name = "ID_TIPO_PAGO", referencedColumnName = "ID_TIPO_PAGO")
    @ManyToOne(optional = false)
    private TipoPago tipo;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID_CONTRATO")
    @ManyToOne(optional = false)
    private Contrato contrato;
    /** Constructor de la clase sin parametros **/
    public Pago() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Pago(Integer id) {
        this.id = id;
    }
    /** Obtiene el identificador del pago
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador del pago
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene la fecha de pago
     * @return fecha de pago**/
    public Date getFecha() {
        return fecha;
    }
    /** Establece la fecha de pago
     * @param fecha fecha de pago**/
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    /** Obtiene la fecha de pago en formato texto
     * @return fecha de pago en formato dd/mm/yyyy**/
    public  String getFechaLabel(){
        return JsfUtil.setFechaFormateada(fecha,1);
    }
    
    /** Obtiene el numero de documento de pago
     * @return documento de pago**/
    public String getDocumento() {
        return documento;
    }
    /** Establece el numero de documento de pago
     * @param documento documento de pago**/
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    /** Obtiene el monto del pago
     * @return monto de pago**/
    public BigDecimal getMonto() {
        return monto;
    }
    /** Establece el monto del pago
     * @param monto monto de pago**/
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    /** Obtiene la nota del pago
     * @return nota de pago**/
    public String getNota() {
        return nota;
    }
    /** Establece la nota del pago
     * @param nota nota de pago**/
    public void setNota(String nota) {
        this.nota = nota;
    }
    /** Obtiene el responsable de registrar el pago
     * @return responsable de transaccion**/
    public String getRegistradoPor() {
        return registradoPor;
    }
    /** Estable el responsable del registro
     * @param registradoPor responsable de transaccion**/
    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }
    /** Obtiene la fecha de registro
     * @return fecha de registro**/
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    
    /** Establece la fecha de registro
     * @param fechaIngreso fecha de registro**/
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    /** Obtiene la fecha de registro en formato texto
     * @return fecha de registro en formato dd/mm/yyyy**/
    public  String getFechaIngresoLabel(){
        return JsfUtil.setFechaFormateada(fechaIngreso,1);
    }
    
    /** Obtiene el responsable de ultima modificacion del pago
     * @return responsable de ultima modificacion**/
    public String getModificadoPor() {
        return modificadoPor;
    }
    /** Establece el responsable de ultima modificacion del pago
     * @param modificadoPor responsable de ultima modificacion**/
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    /** Obtiene la fecha de modificacion
     * @return fecha de modificacion**/
    public Date getFechaModificado() {
        return fechaModificado;
    }
    /** Establece la fecha de modificacion
     * @param fechaModificado  fecha de modificacion**/
    public void setFechaModificado(Date fechaModificado) {
        this.fechaModificado = fechaModificado;
    }
    /** Obtiene la fecha de modificacion en formato texto
     * @return fecha de modificacion en formato dd/mm/yyyy**/
    public  String getFechaModificadoLabel(){
        return JsfUtil.setFechaFormateada(fechaModificado,1);
    }
    
    /** Obtiene el tipo de pago
     * @return Entidad TipoPago**/
    public TipoPago getTipo() {
        return tipo;
    }
    /** Establece el tipo de pago
     * @param tipo Entidad TipoPago**/
    public void setTipo(TipoPago tipo) {
        this.tipo = tipo;
    }
    /** Obtiene el contrato de pago
     * @return Entidad Contrato**/
    public Contrato getContrato() {
        return contrato;
    }
    /** Establece el contrato de pago
     * @param contrato Entidad Contrato**/
    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    /** Compara dos pagos
     * @param object Pago
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /** 
     * @return identificador en texto
     **/
    @Override
    public String toString() {
        return this.id.toString();
    }
    
}
