
package sv.gob.mined.uaci.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "ESTATUS_DIGIT",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstatusDigit.findByEstadoDigitalizacion", query = "SELECT e FROM EstatusDigit e WHERE e.estado = :estado")})
public class EstatusDigit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEstatusDigit")
    @SequenceGenerator(name = "seqEstatusDigit",schema = "SAGACI", sequenceName = "SEQ_ESTATUS_DIGIT",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_ESTATUS_DIGIT")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ESTADO_DIGITALIZACION")
    private String estado;
    @OneToMany(mappedBy = "estatusDigit")
    private List<Archivo> archivoList;

    /** Constructor de la clase sin parametros **/
    public EstatusDigit() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public EstatusDigit(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param estado nombre del estado de digitalizacion
     **/
    public EstatusDigit(Integer id, String estado) {
        this.id = id;
        this.estado = estado;
    }
    /** Obtiene el identificador del estado de digitalizacion
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador del estado de digitalizacion
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el estado de digitalizacion
     * @return estado de digitalizacion **/
    public String getEstado() {
        return estado;
    }
    /** Establece el estado de digitalizacion
     * @param estado estado digitalizacion**/
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Obtiene la lista de archivos con el mismo estado
     * @return  Lista Entidades Archivo**/
    @XmlTransient
    public List<Archivo> getArchivoList() {
        return archivoList;
    }
    /** Establece la lista archivos con el mismo estado
     * @param archivoList Lista Entidades Archivo **/
    public void setArchivoList(List<Archivo> archivoList) {
        this.archivoList = archivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    /** Compara dos estados de digitalizacion
     * @param object EstatusDigit
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstatusDigit)) {
            return false;
        }
        EstatusDigit other = (EstatusDigit) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    /** 
     * @return estado digitalizacion
     **/
    @Override
    public String toString() {
        return this.estado;
    }
    
}
