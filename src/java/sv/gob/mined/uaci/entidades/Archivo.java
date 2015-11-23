
package sv.gob.mined.uaci.entidades;

import java.io.Serializable;
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
@Table(name = "ARCHIVO",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Archivo.findByAnioFecha", query = "SELECT a FROM Archivo a WHERE FUNC('TO_CHAR',a.fechaIngreso,'yyyy') = :anio "),
    @NamedQuery(name = "Archivo.findByAnioFechaAndFuente", query = "SELECT a FROM Archivo a WHERE FUNC('TO_CHAR',a.fechaIngreso,'yyyy') = :anio AND a.contrato.fuente = :fuente"),
    @NamedQuery(name = "Archivo.findByCajaArchivo", query = "SELECT a FROM Archivo a WHERE a.caja = :cajaArchivo"),
    @NamedQuery(name = "Archivo.findByAmpoArchivo", query = "SELECT a FROM Archivo a WHERE a.ampo = :ampoArchivo"),
    @NamedQuery(name = "Archivo.findByFentregaArchivo", query = "SELECT a FROM Archivo a WHERE a.fechaEntrega = :fentregaArchivo") })
public class Archivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqArchivo")
    @SequenceGenerator(name = "seqArchivo",schema = "SAGACI", sequenceName = "SEQ_ARCHIVO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_ARCHIVO")
    private Integer id;
    @Column(name = "CAJA_ARCHIVO")
    private String caja;
    @Column(name = "AMPO_ARCHIVO")
    private String ampo;
    @Column(name = "NUMPAG_ARCHIVO")
    private String numpag;
    @Basic(optional = false)
    @Column(name = "FENTREGA_ARCHIVO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "NOTA_ARCHIVO")
    private String nota;
    @Basic(optional = false)
    @Column(name = "REGPOR_ARCHIVO")
    private String registradoPor;
    @Basic(optional = false)
    @Column(name = "FH_ING_ARCHIVO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "MODPOR_ARCHIVO")
    private String modificadoPor;
    @Basic(optional = false)
    @Column(name = "FH_MOD_ARCHIVO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificado;
    @JoinColumn(name = "ID_ESTATUS_FOLIO", referencedColumnName = "ID_ESTATUS_FOLIO")
    @ManyToOne
    private EstatusFolio estatusFolio;
    @JoinColumn(name = "ID_ESTATUS_DIGIT", referencedColumnName = "ID_ESTATUS_DIGIT")
    @ManyToOne
    private EstatusDigit estatusDigit;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID_CONTRATO")
    @ManyToOne(optional = false)
    private Contrato contrato;

    /** Constructor de la clase sin parametros **/
    public Archivo() {
        
    }
    
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Archivo(Integer id) {
        this.id = id;
    }
    
    /** Constructor de la clase con parametro
     * @param id identificador
     * @param  caja numero de caja de archivo
     * @param ampo numero de ampo de archivo
     * @param numpag numero de paginas del expediente
     * @param fechaEntrega fecha de entrega del archivo
     * @param nota nota u observacion
     * @param estatusFolio estado de Folio
     * @param estatusDigit estado de digitalizacion
     * @param contrato contrato del archivo
     * @param registradoPor responsable del ingreso al sistema
     * @param fechaIngreso fecha de ingreso al sistema
     * @param modificadoPor responsable de la ultima modificacion
     * @param fechaModificado fecha de la ultima modificacion
     **/
    public Archivo(Integer id, String caja, String ampo, String numpag, Date fechaEntrega, String nota, String registradoPor, Date fechaIngreso, String modificadoPor, Date fechaModificado, EstatusFolio estatusFolio, EstatusDigit estatusDigit, Contrato contrato) {
        this.id = id;
        this.caja = caja;
        this.ampo = ampo;
        this.numpag = numpag;
        this.fechaEntrega = fechaEntrega;
        this.nota = nota;
        this.estatusFolio = estatusFolio;
        this.estatusDigit = estatusDigit;
        this.contrato = contrato;
        this.registradoPor = registradoPor;
        this.fechaIngreso = fechaIngreso;
        this.modificadoPor = modificadoPor;
        this.fechaModificado = fechaModificado;
    }

    /** Obtiene el identificador de archivo
     * @return identificador **/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador de archivo
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el numero de caja
     * @return numero de caja **/
    public String getCaja() {
        return caja;
    }
    /** Establece numero de caja
     * @param caja numero de caja **/
    public void setCaja(String caja) {
        this.caja = caja;
    }
    /** Obtiene el AMPO del archivo
     * @return identificacion de AMPO **/
    public String getAmpo() {
        return ampo;
    }
    /** Establece el ampo del archivo
     * @param ampo AMPO del archivo**/
    public void setAmpo(String ampo) {
        this.ampo = ampo;
    }
    /** Obtiene el numero de paginas del expediente
     * @return  Numero de paginas del archivo**/
    public String getNumpag() {
        return numpag;
    }
    /** Establece el numero de paginas del expediente
     * @param numpag Nuemro de paginas del archivo **/
    public void setNumpag(String numpag) {
        this.numpag = numpag;
    }
    /** Obtiene la fecha de entrega del archivo
     * @return Fecha de entrega del archivo **/
    public Date getFechaEntrega() {
        return fechaEntrega;
    }
    /** Obtiene texto de la fecha de entrega
     * @return  Fecha de entrega en formato dd/mm/yyyy**/
    public  String getFechaEntregaLabel(){
        return JsfUtil.setFechaFormateada(fechaEntrega,1);
    }
    /** Establece la fecha de entrega del archivo
     * @param fechaEntrega fecha de entrega **/
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
    /** Obtiene la nota u observacion
     * @return  nota u observaciones **/
    public String getNota() {
        return nota;
    }
    /** Establece la nota u observacion
     * @param nota nota u observacion**/
    public void setNota(String nota) {
        this.nota = nota;
    }
    /** Obtiene el responsable del ingreso de la informacion
     * @return responsable de ingreso **/
    public String getRegistradoPor() {
        return registradoPor;
    }
    /** Establece el responsable del ingreso de la informacion
     * @param registradoPor responsable de ingreso **/
    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }
    /** Obtiene la fecha de ingreso al sistema
     * @return  Fecha de ingreso**/
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    /** Obtiene texto de la fecha de ingreso al sistema
     * @return  Fecha de Ingreso en formato dd/mm/yyyy**/
    public  String getFechaIngresoLabel(){
        return JsfUtil.setFechaFormateada(fechaIngreso,1);
    }
    /** Establece la fecha de ingreso al sistema
     * @param fechaIngreso Fecha de ingreso **/
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    /** Obtiene el responsable de la ultima modificacion
     * @return  responsable de modificacion  **/
    public String getModificadoPor() {
        return modificadoPor;
    }
    
    /** Establece el responsable de la ultima modificacion
     * @param modificadoPor responsable de modificacion **/
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    /** Obtiene la fecha de la ultima modificacion
     * @return  Fecha de modificacion **/
    public Date getFechaModificado() {
        return fechaModificado;
    }
    /** Obtiene texto de la fecha de la ultima modificacion
     * @return  Fecha de modificacion en formato dd/mm/yyyy **/
    public  String getFechaModificadoLabel(){
        return JsfUtil.setFechaFormateada(fechaModificado,1);
    }
    /** Establece la fecha de la ultima modificacion
     * @param fechaModificado Fecha de modificacion **/
    public void setFechaModificado(Date fechaModificado) {
        this.fechaModificado = fechaModificado;
    }
    /** Obtiene el estado de Folio
     * @return  Entidad EstatusFolio **/
    public EstatusFolio getEstatusFolio() {
        return estatusFolio;
    }
    /** Establece el estado de Folio
     * @param estatusFolio Entidad EstatusFolio**/
    public void setEstatusFolio(EstatusFolio estatusFolio) {
        this.estatusFolio = estatusFolio;
    }
    /** Obtiene el estado de Digitalizacion
     * @return  Entidad EstatusDigit**/
    public EstatusDigit getEstatusDigit() {
        return estatusDigit;
    }
    /** Obtiene el estado de Digitalizacion
     * @param estatusDigit Entidad EstatusDigit**/
    public void setEstatusDigit(EstatusDigit estatusDigit) {
        this.estatusDigit = estatusDigit;
    }
    /** Obtiene el contrato vinculado
     * @return  Entidad Contrato**/
    public Contrato getContrato() {
        return contrato;
    }
    /** Establece el contrato vinculado
     * @param contrato Entidad Contrato **/
    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
    /** Realiza una copia de todos los parametros de un archivo
     * @param copy Archivo a copiar
     **/
    public void clone(Archivo copy){
        this.setId(copy.getId());
        this.setCaja(copy.getCaja());
        this.setAmpo(copy.getAmpo());
        this.setEstatusDigit(copy.getEstatusDigit());
        this.setEstatusFolio(copy.getEstatusFolio());
        this.setFechaEntrega(copy.getFechaEntrega());
        this.setNota(copy.getNota());
        this.setNumpag(copy.getNumpag());
        this.setContrato(copy.getContrato());
        this.setFechaIngreso(copy.getFechaIngreso());
        this.setFechaModificado(copy.getFechaModificado());
        this.setModificadoPor(copy.getModificadoPor());
        this.setRegistradoPor(copy.getRegistradoPor());
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos archivos
     * @param object archivo
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Archivo)) {
            return false;
        }
        Archivo other = (Archivo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /** 
     * @return el numero de contrato
     **/
    @Override
    public String toString() {
        return this.contrato.toString();
    }
    
}
