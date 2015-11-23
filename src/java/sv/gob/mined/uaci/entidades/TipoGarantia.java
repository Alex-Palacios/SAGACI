
package sv.gob.mined.uaci.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 */
@Entity
@Table(name = "TIPO_GARANTIA" ,schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoGarantia.findByNombreTipoGarantia", query = "SELECT t FROM TipoGarantia t WHERE t.nombreTipo = :nombreTipo"),
    @NamedQuery(name = "TipoGarantia.findByTipo", query = "SELECT t FROM TipoGarantia t WHERE t.nombreTipo LIKE :nombreTipo") })
public class TipoGarantia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTipoGarantia")
    @SequenceGenerator(name = "seqTipoGarantia",schema = "SAGACI", sequenceName = "SEQ_TIPO_GARANTIA",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_TIPO_GARANTIA")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE_TIPO_GARANTIA")
    private String nombreTipo;
    @Basic(optional = false)
    @Column(name = "LIGADO_CONTRATO")
    private Boolean ligadoAContrato;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<Garantia> garantiaList;
    /** Constructor de la clase sin parametros **/
    public TipoGarantia() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public TipoGarantia(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param nombreTipo tipo de garantia
     * @param ligadoAContrato indica si necesita un contrato vinculado
     **/
    public TipoGarantia(Integer id, String nombreTipo, Boolean ligadoAContrato) {
        this.id = id;
        this.nombreTipo = nombreTipo.toUpperCase();
        this.ligadoAContrato = ligadoAContrato;
    }
    /** Obtiene el identificador
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el tipo de garantia
     * @return el tipo de garantia**/
    public String getNombreTipo() {
        return nombreTipo;
    }
    
    /** Establece el tipo de garantia
     * @param nombreTipo el tipo de garantia**/
    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo.toUpperCase();
    }
     /** Indica si el tipo de garantia se vincula a un contrato o no
     * @return estado de ligadoAcontrato de la garantia **/
    public Boolean getLigadoAContrato() {
        return ligadoAContrato;
    }
    /** Obtiene SI o NO la garantia se vincula a contrato
     * @return texto que indica la viculacion de la garantia a contrato**/
    public String getLigadoAContratoLabel() {
        if(ligadoAContrato){
            return "SI";
        }else{
            return "NO";
        }
    }
    /** Establece si la garantia se vincula a un contrato
     * @param ligadoAContrato indica si la garantia se vincula a contrato**/
    public void setLigadoAContrato(Boolean ligadoAContrato) {
        this.ligadoAContrato = ligadoAContrato;
    }

   
    /** Obtiene la lista de Garantias del mismo tipo
     * @return  Lista Entidades Garantia**/
    @XmlTransient
    public List<Garantia> getGarantiaList() {
        return garantiaList;
    }
    /** Establece la lista de Garantias del mismo tipo
     * @param garantiaList Lista Entidades Garantia**/
    public void setGarantiaList(List<Garantia> garantiaList) {
        this.garantiaList = garantiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos tipos de garantia
     * @param object Tipo
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoGarantia)) {
            return false;
        }
        TipoGarantia other = (TipoGarantia) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /** 
     * @return nombre del tipo de garantia
     **/
    @Override
    public String toString() {
        return this.nombreTipo;
    }
    
}
