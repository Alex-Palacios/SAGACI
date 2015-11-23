
package sv.gob.mined.uaci.entidades;

import java.io.Serializable;
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
@Table(name = "PROCESO",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proceso.findByNumProceso", query = "SELECT p FROM Proceso p WHERE p.numero = :numProceso"),
    @NamedQuery(name = "Proceso.findByNumSiapProceso", query = "SELECT p FROM Proceso p WHERE p.siap = :numSiapProceso"),
    @NamedQuery(name = "Proceso.findByAnioRegistro", query = "SELECT p FROM Proceso p WHERE FUNC('TO_CHAR',p.fechaIngreso,'yyyy') = :anio ") })
public class Proceso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProceso")
    @SequenceGenerator(name = "seqProceso",schema = "SAGACI", sequenceName = "SEQ_PROCESO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_PROCESO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NUM_PROCESO")
    private String numero;
    @Basic(optional = false)
    @Column(name = "DESCRIP_PROCESO")
    private String descripcion;
    @Column(name = "NUM_SIAP_PROCESO")
    private String siap;
    @Column(name = "NOTA_PROCESO")
    private String nota;
    @Basic(optional = false)
    @Column(name = "REGPOR_PROCESO")
    private String registradoPor;
    @Basic(optional = false)
    @Column(name = "FH_ING_PROCESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "MODPOR_PROCESO")
    private String modificadoPor;
    @Basic(optional = false)
    @Column(name = "FH_MOD_PROCESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificado;
    @JoinColumn(name = "ID_TECNICO", referencedColumnName = "ID_TECNICO")
    @ManyToOne(optional = false)
    private Tecnico tecnico;
    @JoinColumn(name = "ID_METODO", referencedColumnName = "ID_METODO")
    @ManyToOne(optional = false)
    private MetodoAdq metodo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proceso")
    private List<Garantia> garantiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proceso")
    private List<Contrato> contratoList;
    /** Constructor de la clase sin parametros **/
    public Proceso() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Proceso(Integer id) {
        this.id = id;
    }
    
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param numero numero de proceso
     * @param descripcion nombre de licitacion o concurso
     * @param siap numero SIAP del proceso
     * @param  nota nota del proceso
     * @param registradoPor responsable del registro
     * @param fechaIngreso Fecha de registro
     * @param modificadoPor responsable de ultima modificacion
     * @param fechaModificado fecha de ultima modificacion
     * @param contratoList Lista de contratos del proceso de licitacion o concurso
     * @param tecnico tecnico GACI responsable del proceso
     * @param metodo metodo de adquisicion del proceso
     **/
    public Proceso(Integer id, String numero, String descripcion, String siap, String nota, String registradoPor, Date fechaIngreso, String modificadoPor, Date fechaModificado, List<Contrato> contratoList, Tecnico tecnico, MetodoAdq metodo) {
        this.id = id;
        this.numero = numero.toUpperCase();
        this.descripcion = descripcion;
        this.siap = siap;
        this.nota = nota;
        this.tecnico = tecnico;
        this.metodo = metodo;
        this.contratoList = contratoList;
        this.registradoPor = registradoPor;
        this.fechaIngreso = fechaIngreso;
        this.modificadoPor = modificadoPor;
        this.fechaModificado = fechaModificado;
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
    /** Obtiene el numero de proceso
     * @return numero de proceso**/
    public String getNumero() {
        return numero;
    }
    /** Obtiene codigo del proceso
     * @return codigo de proceso**/
    public String getCodProceso() {
        return this.metodo.getCodigo()+" "+this.numero;
    }
    /** Establece el numero de proceso
     * @param numero numero de proceso**/
    public void setNumero(String numero) {
        this.numero = numero.toUpperCase();
    }
    /** Obtiene el nombre de licitacion o concurso
     * @return nombre de licitacion o concurso**/
    public String getDescripcion() {
        return descripcion;
    }
    /** Establece el nombre de licitacion o concurso
     * @param descripcion nombre de licitacion o concurso**/
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion.toUpperCase();
    }
    /** Obtiene el numero SIAP
     * @return numero SIAP del proceso**/
    public String getSiap() {
        return siap;
    }
    /** Establece el numero SIAP
     * @param siap numero SIAP del proceso**/
    public void setSiap(String siap) {
        this.siap = siap;
    }
    /** Obtiene la nota del proceso
     * @return observaciones o natas **/
    public String getNota() {
        return nota;
    }
    /** Establece la nota del proceso
     * @param nota observaciones o natas **/
    public void setNota(String nota) {
        this.nota = nota;
    }
    /** Obtiene el responsable de registro
     * @return responsable de transaccion**/
    public String getRegistradoPor() {
        return registradoPor;
    }
    /** Establece el responsable de registro
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
        return JsfUtil.setFechaFormateada(fechaIngreso,3);
    }
    /** Obtiene el responsable de ultima modificacion
     * @return responsable de ultima modificacion**/
    public String getModificadoPor() {
        return modificadoPor;
    }
    /** Establece el responsable de ultima modificacion
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
        return JsfUtil.setFechaFormateada(fechaModificado,3);
    }
    
    /** Obtiene el tecnico responsable del proceso
    * @return Entidad Tecnico**/
    public Tecnico getTecnico() {
        return tecnico;
    }
    /** Establece el tecnico responsable del proceso
    * @param tecnico Entidad Tecnico**/
    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
    /** Obtiene el metodo de adquisicion
    * @return Entidad MetodoAdq**/
    public MetodoAdq getMetodo() {
        return metodo;
    }
    /** Establece el metodo de adquisicion
    * @param metodo Entidad MetodoAdq**/
    public void setMetodo(MetodoAdq metodo) {
        this.metodo = metodo;
    }

    
    
     /** Obtiene la lista de garantias del proceso
     * @return  Lista Entidades Garantia**/
    @XmlTransient
    public List<Garantia> getGarantiaList() {
        return garantiaList;
    }
    /** Establece la lista de garantias del proceso
     * @param garantiaList  Lista Entidades Garantia**/
    public void setGarantiaList(List<Garantia> garantiaList) {
        this.garantiaList = garantiaList;
    }
    /** Obtiene la lista de contratos del proceso
     * @return  Lista Entidades Contrato**/
    @XmlTransient
    public List<Contrato> getContratoList() {
        return contratoList;
    }
    /** Establece la lista de contratos del proceso
     * @param contratoList Lista Entidades Contrato**/
    public void setContratoList(List<Contrato> contratoList) {
        this.contratoList = contratoList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos procesos
     * @param object Proceso
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Proceso)) {
            return false;
        }
        Proceso other = (Proceso) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /** 
     * @return codigo del proceso
     **/
    @Override
    public String toString() {
        return getCodProceso();
    }
    
}
