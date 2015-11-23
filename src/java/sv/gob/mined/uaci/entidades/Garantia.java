
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
@Table(name = "GARANTIA",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Garantia.findByClaveGarantia", query = "SELECT g FROM Garantia g WHERE g.anio = :anioGarantia AND g.correlativo = :correlativo"),
    @NamedQuery(name = "Garantia.findByNumGarantia", query = "SELECT g FROM Garantia g WHERE g.numGarantia = :numGarantia"),
    @NamedQuery(name = "Garantia.findByEstadoGarantia", query = "SELECT g FROM Garantia g WHERE g.estado = :estadoGarantia"),
    @NamedQuery(name = "Garantia.findByFechaDevolucion", query = "SELECT g FROM Garantia g WHERE g.fechaDevolucion = :fechaDevolucion"),
    @NamedQuery(name = "Garantia.findByAnio", query = "SELECT g FROM Garantia g WHERE g.anio = :anio "),
    @NamedQuery(name = "Garantia.findByClave", query = "SELECT g FROM Garantia g WHERE g.anio = :anio AND g.correlativo = :correlativo"),
    @NamedQuery(name = "Garantia.vencidas", query = "SELECT g FROM Garantia g WHERE g.estado = 1 AND g.fechaVencimiento < :fecha ORDER BY g.fechaVencimiento ASC "),
    @NamedQuery(name = "Garantia.Xvencer", query = "SELECT g FROM Garantia g WHERE g.estado = 1 AND g.fechaVencimiento <= :fecha ORDER BY g.fechaVencimiento ASC "),
    @NamedQuery(name = "Garantia.vigentes", query = "SELECT g FROM Garantia g WHERE g.estado = 1 AND g.fechaVencimiento >= :fecha ORDER BY g.fechaVencimiento ASC "),
    @NamedQuery(name = "Garantia.countVencidas", query = "SELECT COUNT(g) FROM Garantia g WHERE g.estado = 1 AND g.fechaVencimiento < :fecha ") })
public class Garantia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGarantia")
    @SequenceGenerator(name = "seqGarantia",schema = "SAGACI", sequenceName = "SEQ_GARANTIA",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_GARANTIA")
    private Integer id;
    @Column(name = "ANIO_GARANTIA")
    private Integer anio;
    @Column(name = "CORR_GARANTIA")
    private Integer correlativo;
    @Basic(optional = false)
    @Column(name = "NUM_GARANTIA")
    private String numGarantia;
    @Column(name = "FINICIO_GARANTIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "PLAZO_GARANTIA")
    private String plazo;
    @Basic(optional = false)
    @Column(name = "FVENC_GARANTIA")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Basic(optional = false)
    @Column(name = "MONTO_GARANTIA")
    private BigDecimal monto;
    @Basic(optional = false)
    @Column(name = "ESTADO_GARANTIA")
    private Integer estado;
    @Column(name = "RETIRA_GARANTIA")
    private String retiradoPor;
    @Column(name = "FDEVOL_GARANTIA")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    @Column(name = "ANIODEV_GARANTIA")
    private Integer anioDev;
    @Column(name = "FORMDEV_GARANTIA")
    private Integer formDev;
    @Column(name = "GTEFORMDEV_GARANTIA")
    private String gteFormDev;
    @Column(name = "NOTA_GARANTIA")
    private String nota;
    @Basic(optional = false)
    @Column(name = "REGPOR_GARANTIA")
    private String registradoPor;
    @Basic(optional = false)
    @Column(name = "FH_ING_GARANTIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "MODPOR_GARANTIA")
    private String modificadoPor;
    @Basic(optional = false)
    @Column(name = "FH_MOD_GARANTIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificado;
    @JoinColumn(name = "ID_TIPO_GARANTIA", referencedColumnName = "ID_TIPO_GARANTIA")
    @ManyToOne(optional = false)
    private TipoGarantia tipo;
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID_PROVEEDOR")
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @JoinColumn(name = "ID_PROCESO", referencedColumnName = "ID_PROCESO")
    @ManyToOne(optional = false)
    private Proceso proceso;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID_CONTRATO")
    @ManyToOne
    private Contrato contrato;
    @JoinColumn(name = "ID_AFIANZADORA", referencedColumnName = "ID_AFIANZADORA")
    @ManyToOne(optional = false)
    private Afianzadora afianzadora;
    /** Constructor de la clase sin parametros **/
    public Garantia() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Garantia(Integer id) {
        this.id = id;
    }
    /** Obtiene el identificador de la garantia
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador de la garantia
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el año de recepcion de la garantia
     * @return  año de recepcion**/
    public Integer getAnio() {
        return anio;
    }
    /** Establece el año de recepcion de la garantia
     * @param  anio año de recepcion**/
    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    /** Obtiene el correlativo de recepcion de la garantia
     * @return  correlativo de recepcion**/
    public Integer getCorrelativo() {
        return correlativo;
    }
    /** Obtiene el la clave de identificacion de la garantia
     * @return  Anio/correlativo de recepcion de garantia**/
    public String getClave(){
        return anio+"/"+correlativo;
    }
    /** Establece el correlativo de recepcion de la garantia
     * @param  correlativo numero correlativo de recepcion**/
    public void setCorrelativo(Integer correlativo) {
        this.correlativo = correlativo;
    }
    /** Obtiene el numero del documento de la garantia
     * @return  numero de la garantia**/
    public String getNumGarantia() {
        return numGarantia;
    }
    /** Establece el numero del documento de la garantia
     * @param numGarantia   numero de la garantia**/
    public void setNumGarantia(String numGarantia) {
        this.numGarantia = numGarantia;
    }
    /** Obtiene la fecha de Inicio de la garantia
     * @return  la fecha de inicio **/
    public Date getFechaInicio() {
        return fechaInicio;
    }
    /** Establece la fecha de Inicio de la garantia
     * @param fechaInicio  la fecha de inicio **/
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    /** Obtiene la fecha de Inicio en formato de texto
     * @return  la fecha de inicio en formato dd/mm/yyyy **/
    public String getFechaInicioLabel(){
        return JsfUtil.setFechaFormateada(fechaInicio,1);
    }
    /** Obtiene el plazo de la garantia
     * @return  plazo de la garantia **/
    public String getPlazo() {
        return plazo;
    }
    /** Establece el plazo de la garantia
     * @param plazo  plazo de la garantia **/
    public void setPlazo(String plazo) {
        this.plazo = plazo.toUpperCase();
    }
    /** Obtiene la fecha de vencimiento de la garantia
     * @return  fecha de vencimiento **/
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }
    /** Establece la fecha de vencimiento de la garantia
     * @param fechaVencimiento   fecha de vencimiento **/
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    /** Obtiene la fecha de vencimiento en formato de texto
     * @return  la fecha de vencimiento en formato dd/mm/yyyy **/
    public String getFechaVencimientoLabel(){
        return JsfUtil.setFechaFormateada(fechaVencimiento,1);
    }
    /** Obtiene el monto en dinero de la garantia
     * @return  monto de la garantia **/
    public BigDecimal getMonto() {
        return monto;
    }
    /** Establece el monto de la garantia
     * @param monto  monto de la garantia **/
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    /** Obtiene el estado de la garantia
     * @return  estado de la garantia **/
    public Integer getEstado() {
        return estado;
    }
    /** Obtiene el estado de la garantia en texto
     * @return  estado de la garantia en texto **/
    public String getEstadoLabel() {
        if(estado != null){
            if(fechaVencimiento.after(JsfUtil.hoy()) && estado != 2){
                return estadoGarantiaLabel(1);
            }else if(fechaVencimiento.before(JsfUtil.hoy()) && estado != 2){
                return estadoGarantiaLabel(3);
            }else if(estado == 2){
                return estadoGarantiaLabel(2);
            }else{
                return "";
            }
        }else{
            return "";
        }
    }
    
    /** Convierte el estado de la garantia en texto
     * @param value estado de la garantia en entero
     * @return  estado de la garantia en texto**/
    public String estadoGarantiaLabel(int value){
        String r = "";
        switch(value){
            case 1: r = "VIGENTE";break;
            case 2: r = "DEVUELTA";break;
            case 3: r = "VENCIDA";break;
        }
        return r;
    }
    /** Establece el estado de la garantia
     * @param estado  estado de la garantia **/
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
    /** Obtiene el nombre de la persona que retira la garantia
     * @return persona que retira la garantia **/
    public String getRetiradoPor() {
        return retiradoPor;
    }
    /** Establece el nombre de la persona que retira la garantia
     * @param retiradaPor persona que retira la garantia **/
    public void setRetiradoPor(String retiradaPor) {
        if(retiradaPor != null){
            retiradaPor = retiradaPor.toUpperCase();
        }
        this.retiradoPor = retiradaPor;
    }
    /** Obtiene la fecha de devolucion de la garantia
     * @return  la fecha de devolucion **/
    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }
    /** Establece la fecha de devolucion de la garantia
     * @param fechaDevolucion  la fecha de devolucion **/
    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    /** Obtiene la fecha de devolucion de la garantia en formato texto
     * @return  la fecha de devolucion en formato dd/mm/yyyy **/
    public String getFechaDevolucionLabel(){
        return JsfUtil.setFechaFormateada(fechaDevolucion,1);
    }
    /** Obtiene el año de formulario de devolucion
     * @return  anio de forumalario de devolucion **/
    public Integer getAnioDev() {
        return anioDev;
    }
    /** Establece la el anio de formulario de devolucion
     * @param anioDev  anio de formulario **/
    public void setAnioDev(Integer anioDev) {
        this.anioDev = anioDev;
    }
    
    
    /** Obtiene el correlativo de formulario de devolucion
     * @return  correlativo de forumalario de devolucion **/
    public Integer getFormDev() {
        return formDev;
    }
    /** Establece la el correlativo de formulario de devolucion
     * @param formDev  correlativo de formulario **/
    public void setFormDev(Integer formDev) {
        this.formDev = formDev;
    }

    public String getGteFormDev() {
        return gteFormDev;
    }

    public void setGteFormDev(String gteFormDev) {
        this.gteFormDev = gteFormDev;
    }
    
    
    /** Obtiene la nota de la garantia
     * @return  observacion u nota **/
    public String getNota() {
        return nota;
    }
    /** Establece la nota de la garantia
     * @param nota  observacion u nota **/
    public void setNota(String nota) {
        this.nota = nota;
    }
    /** Obtiene el responsable del registro de la garantia
     * @return  responsable del registro **/
    public String getRegistradoPor() {
        return registradoPor;
    }
    /** Establece el responsable del registro de la garantia
     * @param registradaPor responsable del registro **/
    public void setRegistradoPor(String registradaPor) {
        this.registradoPor = registradaPor;
    }
    /** Obtiene la fecha de registro de la garantia
     * @return fecha de registro **/
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    /** Establece la fecha de registro de la garantia
     * @param fechaIngreso  fecha de registro **/
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    /** Obtiene la fecha de registro de la garantia en formato texto
     * @return  la fecha de registro en formato dd/mm/yyyy **/
    public String getFechaIngresoLabel(){
        return JsfUtil.setFechaFormateada(fechaIngreso,1);
    }
    /** Obtiene el responsable de la ultima modificacion
     * @return  responsable de la ultima modificacion **/
    public String getModificadoPor() {
        return modificadoPor;
    }
    /** Establece el responsable de la ultima modificacion
     * @param modificadoPor responsable de la ultima modificacion **/
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    /** Obtiene la fecha de la ultima modificacion
     * @return  la fecha de la ultima modificacion **/
    public Date getFechaModificado() {
        return fechaModificado;
    }
    /** Establece la fecha de la ultima modificacion
     * @param fechaModificado la fecha de la ultima modificacion **/
    public void setFechaModificado(Date fechaModificado) {
        this.fechaModificado = fechaModificado;
    }
    
    /** Obtiene la fecha de modificacion en formato texto
     * @return  la fecha de modificacion en formato dd/mm/yyyy **/
    public String getFechaModificadoLabel(){
        return JsfUtil.setFechaFormateada(fechaModificado,1);
    }
    /** Obtiene el tipo de la garantia
     * @return  Entidad TipoGarantia **/
    public TipoGarantia getTipo() {
        return tipo;
    }
    /** Establece el tipo de la garantia
     * @param tipo  Entidad TipoGarantia **/
    public void setTipo(TipoGarantia tipo) {
        this.tipo = tipo;
    }
    /** Obtiene el proveedor de la garantia
     * @return  Entidad Proveedor **/
    public Proveedor getProveedor() {
        return proveedor;
    }
    /** Establece el proveedor de la garantia
     * @param proveedor  Entidad TipoGarantia **/
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    /** Obtiene el proceso de la garantia
     * @return  Entidad Proceso **/
    public Proceso getProceso() {
        return proceso;
    }
    /** Establece el proceso de la garantia
     * @param proceso  Entidad Proceso **/
    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }
    /** Obtiene el contrato de la garantia
     * @return  Entidad Contrato **/
    public Contrato getContrato() {
        return contrato;
    }
    /** Establece el contrato de la garantia
     * @param contrato  Entidad Contrato **/
    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
    /** Obtiene la afianzadora de la garantia
     * @return  Entidad Afianzadora **/
    public Afianzadora getAfianzadora() {
        return afianzadora;
    }
    /** Establece la afianzadora de la garantia
     * @param afianzadora  Entidad Afianzadora **/
    public void setAfianzadora(Afianzadora afianzadora) {
        this.afianzadora = afianzadora;
    }
    /** Indica si la garantia esta vencida
     * @return  true si la varantia esta vencidad de lo contrario retorna false **/
    public boolean isVencida(){
        return this.fechaVencimiento.compareTo(JsfUtil.hoy()) == -1;
    }
    
    /** Indica si la garantia esta vigente
     * @return  true si la varantia esta vigente de lo contrario retorna false **/
    public boolean isVigente(){
        return fechaVencimiento.after(JsfUtil.hoy());
    }
    
    
    /** Indica si la garantia esta vigente
     * @return  true si la varantia esta vigente de lo contrario retorna false **/
    public boolean isDevuelta(){
        return this.estado == 2;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos Garantias
     * @param object Garantia
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Garantia)) {
            return false;
        }
        Garantia other = (Garantia) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    /** 
     * @return clave de la garantia anio/correlativo
     **/
    @Override
    public String toString() {
        return getClave();
    }
    
    
    
    
}
