
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
@Table(name = "ESTATUS_FOLIO" ,schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstatusFolio.findByEstadoFolio", query = "SELECT e FROM EstatusFolio e WHERE e.estado = :estadoFolio")})
public class EstatusFolio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEstatusFolio")
    @SequenceGenerator(name = "seqEstatusFolio",schema = "SAGACI", sequenceName = "SEQ_ESTATUS_FOLIO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_ESTATUS_FOLIO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ESTADO_FOLIO")
    private String estado;
    @OneToMany(mappedBy = "estatusFolio")
    private List<Archivo> archivoList;
    /** Constructor de la clase sin parametros **/
    public EstatusFolio() {
    
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public EstatusFolio(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param estado nombre del estado de folio
     **/
    public EstatusFolio(Integer id, String estado) {
        this.id = id;
        this.estado = estado;
    }
    /** Obtiene el identificador del estado de folio
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador del estado de folio
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el estado de folio
     * @return estado de folio **/
    public String getEstado() {
        return estado;
    }
    /** Establece el estado de folio
     * @param estado estado folio**/
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
    /** Compara dos estados de folio
     * @param object EstatusFolio
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstatusFolio)) {
            return false;
        }
        EstatusFolio other = (EstatusFolio) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    /** 
     * @return estado folio
     **/
    @Override
    public String toString() {
        return this.estado;
    }
    
}
