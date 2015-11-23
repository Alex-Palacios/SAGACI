
package sv.gob.mined.uaci.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 */

@Entity
@Table(name = "CONTRATO",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contrato.findByNumContrato", query = "SELECT c FROM Contrato c WHERE c.numero = :numContrato"),
    @NamedQuery(name = "Contrato.findByModificativaContrato", query = "SELECT c FROM Contrato c WHERE c.modificativa = :modificativa"),
    @NamedQuery(name = "Contrato.findByAnioFecha", query = "SELECT c FROM Contrato c WHERE FUNC('TO_CHAR',c.fecha,'yyyy') = :anio "),
    @NamedQuery(name = "Contrato.findByAnioFechaNULL", query = "SELECT c FROM Contrato c WHERE c.fecha IS NULL "),
    @NamedQuery(name = "Contrato.findByProceso", query = "SELECT c FROM Contrato c WHERE c.proceso = :proceso "),
    @NamedQuery(name = "Contrato.findByArchivar", query = "SELECT c FROM Contrato c WHERE c.id NOT IN (SELECT a.contrato.id FROM Archivo a) "),
    @NamedQuery(name = "Contrato.findByArchivadosByAnio", query = "SELECT c FROM Contrato c WHERE c.id IN (SELECT a.contrato.id FROM Archivo a) AND FUNC('TO_CHAR',c.fecha,'yyyy') = :anio  "),
    @NamedQuery(name = "Contrato.findByArchivadosByAnioNULL", query = "SELECT c FROM Contrato c WHERE c.id IN (SELECT a.contrato.id FROM Archivo a) AND c.fecha IS NULL")})

public class Contrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqContrato")
    @SequenceGenerator(name = "seqContrato",schema = "SAGACI", sequenceName = "SEQ_CONTRATO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_CONTRATO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NUM_CONTRATO")
    private String numero;
    @Column(name = "FECHA_CONTRATO")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "PLAZO_CONTRATO")
    private String plazo;
    @Column(name = "FINICIO_CONTRATO")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "FFIN_CONTRATO")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "FORDEN_CONTRATO")
    @Temporal(TemporalType.DATE)
    private Date fechaOrden;
    @Column(name = "MONTO_CONTRATO")
    private BigDecimal monto;
    @Column(name = "ADMIN_CONTRATO")
    private String admin;
    @Column(name = "FORMPAGO_CONTRATO")
    private String formaPago;
    @Column(name = "NUMADJ_CONTRATO")
    private String adjudicacion;
    @Column(name = "COMPRESU_CONTRATO")
    private String compromisoPresup;
    @Column(name = "NOTA_CONTRATO")
    private String nota;
    @Column(name = "RESOLMOD_CONTRATO")
    private String modificativa;
    @Basic(optional = false)
    @Column(name = "REGPOR_CONTRATO")
    private String registradoPor;
    @Basic(optional = false)
    @Column(name = "FH_ING_CONTRATO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "MODPOR_CONTRATO")
    private String modificadoPor;
    @Basic(optional = false)
    @Column(name = "FH_MOD_CONTRATO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificado;
    @JoinColumn(name = "ID_UNIDAD", referencedColumnName = "ID_UNIDAD")
    @ManyToOne
    private UnidadTecnica unidad;
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID_PROVEEDOR")
    @ManyToOne(optional = false)
    private Proveedor contratista;
    @JoinColumn(name = "ID_PROCESO", referencedColumnName = "ID_PROCESO")
    @ManyToOne(optional = false)
    private Proceso proceso;
    @JoinColumn(name = "ID_FUENTE", referencedColumnName = "ID_FUENTE")
    @ManyToOne
    private FuenteFinanc fuente;
    @JoinColumn(name = "ID_ESTATUS_CONTRATO", referencedColumnName = "ID_ESTATUS_CONTRATO")
    @ManyToOne
    private EstatusContrato estatusContrato;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contrato")
    private List<Pago> pagoList;
    @OneToMany(mappedBy = "contrato")
    private List<Garantia> garantiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contrato")
    private List<Archivo> archivoList;
    
    /** Constructor de la clase sin parametros **/
    public Contrato() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Contrato(Integer id) {
        this.id = id;
    }
    /** Obtiene el identificador de contrato
     * @return identificador **/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador de contrato
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el numero de contrato
     * @return numero de contrato **/
    public String getNumero() {
        return numero;
    }
    /** Establece el numero de contrato
     * @param numero numero de contrato **/
    public void setNumero(String numero) {
        this.numero = numero.toUpperCase();
    }
    /** Obtiene la fecha de contrato
     * @return Fecha de contrato **/
    public Date getFecha() {
        return fecha;
    }
    /** Establece la fecha de contrato
     * @param fecha Fecha de contrato**/
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    /** Obtiene texto de la fecha de contrato
     * @return Fecha de contrato en formato dd/mm/yyyy  **/
    public  String getFechaLabel(){
        return JsfUtil.setFechaFormateada(fecha,1);
    }
    /** Obtiene plazo de contrato
     * @return  Plazo del contrato**/
    public String getPlazo() {
        return plazo;
    }
    /** Establece plazo de contrato
     * @param plazo Plazo del contrato **/
    public void setPlazo(String plazo) {
        this.plazo = plazo.toUpperCase();
    }
    /** Obtiene fecha de inicio de contrato
     * @return  Fecha de Inicio**/
    public Date getFechaInicio() {
        return fechaInicio;
    }
    /** Establece fecha de inicio de contrato
     * @param fechaInicio Fecha de Inicio**/
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    /** Obtiene fecha de finalizacion de contrato
     * @return  Fecha de finalizacion**/
    public Date getFechaFin() {
        return fechaFin;
    }
    /** Establece fecha de finalizacion de contrato
     * @param fechaFin Fecha de Finalizacion **/
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    /** Obtiene fecha de Orden de inicio de contrato
     * @return Fecha de Orden de Inicio **/
    public Date getFechaOrden() {
        return fechaOrden;
    }
    /** Establece fecha de Orden de inicio de contrato
     * @param fechaOrden Fecha de Orden de Inicio **/
    public void setFechaOrden(Date fechaOrden) {
        this.fechaOrden = fechaOrden;
    }
    /** Obtiene fecha de Inicio de contrato
     * @return Fecha de Inicio en formato dd/mm/yyyy **/
    public  String getFechaInicioLabel(){
        return JsfUtil.setFechaFormateada(fechaInicio,1);
    }
    /** Obtiene fecha de Inicio de finalizacion
     * @return  Fecha de finalizacion **/
    public  String getFechaFinLabel(){
        return JsfUtil.setFechaFormateada(fechaFin,1);
    }
    /** Obtiene fecha de Orden de Inicio de contrato
     * @return  Fecha de finalizacion **/
    public  String getFechaOrdenLabel(){
        return JsfUtil.setFechaFormateada(fechaOrden,1);
    }
    /** Obtiene el monto del contrato
     * @return  Monto de contrato **/
    public BigDecimal getMonto() {
        return monto;
    }
    /** Obtiene el monto del contrato
     * @return  Monto redondeado a 2 decimales **/
    public BigDecimal getMontoRedondeado() {
        if(monto != null){
            return JsfUtil.redondearMas(monto, 2);
        }else{
            return BigDecimal.ZERO;
        }
    }
    /** Establece el monto del contrato
     * @param monto Monto de contrato**/
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    /** Obtiene el administrador del contrato
     * @return Nombre del Administrador **/
    public String getAdmin() {
        return admin;
    }
    /** Establece el administrador del contrato
     * @param admin Nombre del administrador **/
    public void setAdmin(String admin) {
        if(admin != null){
            admin = admin.toUpperCase();
        }
        this.admin = admin;
    }
    /** Obtiene la forma de pago del contrato
     * @return  Forma de pago **/
    public String getFormaPago() {
        return formaPago;
    }
    /** Establece la forma de pago del contrato
     * @param formaPago Forma de pago**/
    public void setFormaPago(String formaPago) {
        if(formaPago != null){
            formaPago = formaPago.toUpperCase();
        }
        this.formaPago = formaPago;
    }
    /** Obtiene el numero de adjudicacion del contrato
     * @return Numero de adjudicacion **/
    public String getAdjudicacion() {
        return adjudicacion;
    }
    /** Establece el numero de adjudicacion del contrato
     * @param adjudicacion Numero de adjudicacion **/
    public void setAdjudicacion(String adjudicacion) {
        this.adjudicacion = adjudicacion;
    }
    /** Obtiene el numero de compromiso presupuestario del contrato
     * @return Numero del compromiso presupuestario **/
    public String getCompromisoPresup() {
        return compromisoPresup;
    }
    /** Establece el numero de compromiso presupuestario del contrato
     * @param compromisoPresup Numero del compromiso presupuestario **/
    public void setCompromisoPresup(String compromisoPresup) {
        this.compromisoPresup = compromisoPresup;
    }
    /** Obtiene nota u observaciones del contrato
     * @return  Observaciones **/
    public String getNota() {
        return nota;
    }
    /** Establece nota u observaciones del contrato
     * @param nota Observaciones **/
    public void setNota(String nota) {
        this.nota = nota;
    }
    /** Obtiene el numero de la resolucion modificativa del contrato
     * @return Numero de resolucion modificativa **/
    public String getModificativa() {
        return modificativa;
    }
    /** Establece el numero de la resolucion modificativa del contrato
     * @param modificativa Numero de resolucion modificativa **/
    public void setModificativa(String modificativa) {
        if(modificativa != null){
            modificativa = modificativa.toUpperCase();
        }
        this.modificativa = modificativa;
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
    /** Obtiene la Unidad tecnica encargada del contrato
     * @return  Entidad UnidadTecnica */
    public UnidadTecnica getUnidad() {
        return unidad;
    }
    /** Establece la Unidad tecnica encargada del contrato
     * @param unidad Entidad UnidadTecnica  */
    public void setUnidad(UnidadTecnica unidad) {
        this.unidad = unidad;
    }
    /** Obtiene el contratista del contrato
     * @return  Entidad Proveedor */
    public Proveedor getContratista() {
        return contratista;
    }
    /** Establece el contratista del contrato
     * @param contratista Entidad Proveedor */
    public void setContratista(Proveedor contratista) {
        this.contratista = contratista;
    }
    /** Obtiene el proceso del contrato
     * @return  Entidad Proceso */
    public Proceso getProceso() {
        return proceso;
    }
    /** Establece el proceso del contrato
     * @param proceso Entidad Proceso */
    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }
    /** Obtiene la fuente de financiamiento del contrato
     * @return  Entidad FuenteFinanc */
    public FuenteFinanc getFuente() {
        return fuente;
    }
    /** Establece la fuente de financiamiento del contrato
     * @param fuente Entidad FuenteFinanc */
    public void setFuente(FuenteFinanc fuente) {
        this.fuente = fuente;
    }
    /** Obtiene el estado del contrato
     * @return  Entidad EstatusContrato */
    public EstatusContrato getEstatusContrato() {
        return estatusContrato;
    }
    /** Establece el estado del contrato
     * @param estatusContrato Entidad EstatusContrato */
    public void setEstatusContrato(EstatusContrato estatusContrato) {
        this.estatusContrato = estatusContrato;
    }


    
    /** Obtiene el historial de pagos del contrato
     * @return Lista de pagos */

    @XmlTransient
    public List<Pago> getPagoList() {
        if(pagoList == null){
            pagoList = new ArrayList<Pago>();
        }
        return pagoList;
    }
    /** Establece el historial de pagos del contrato
     * @param pagoList Lista de pagos */
    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }
    /** Obtiene las garantias vinculadas al contrato
     * @return  Lista de garantias*/
    @XmlTransient
    public List<Garantia> getGarantiaList() {
        return garantiaList;
    }
    /** Establece las garantias vinculadas al contrato
     * @param garantiaList Lista de garantias */
    public void setGarantiaList(List<Garantia> garantiaList) {
        this.garantiaList = garantiaList;
    }
    /** Obtiene los archivos vinculados al contrato
     * @return Lista de Archivos */
    @XmlTransient
    public List<Archivo> getArchivoList() {
        return archivoList;
    }
    /** Establece los archivos vinculados al contrato
     * @param archivoList Lista de Archivos */
    public void setArchivoList(List<Archivo> archivoList) {
        this.archivoList = archivoList;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos contratos
     * @param object contrato
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Contrato)) {
            return false;
        }
        Contrato other = (Contrato) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    /** 
     * @return el numero de contrato
     **/
    @Override
    public String toString() {
        return this.numero;
    }
    /** Calcula suma de pagos
     * @return sumatoria de los pagos realizados al contrato
     **/
    public BigDecimal sumaPagos(){
        BigDecimal total = BigDecimal.ZERO;
        for(Pago p: this.getPagoList()){
            total = total.add(p.getMonto());
        }
        total.setScale(2, RoundingMode.UP);
        return total;
    }
    
    public BigDecimal sumaPagos(Integer idpago){
        BigDecimal total = BigDecimal.ZERO;
        for(Pago p: this.getPagoList()){
            if(p.getId() != idpago){
                total = total.add(p.getMonto());
            }
        }
        total.setScale(2, RoundingMode.UP);
        return total;
    }
    
    public BigDecimal getSumaPagos(){
        return sumaPagos();
    }
    
}
