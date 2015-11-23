/** Entidades de BD **/

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

/** ENTIDAD UNIDAD TECNICA
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 */
@Entity
@Table(name = "UNIDAD_TECNICA",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadTecnica.findByNombreUnidad", query = "SELECT u FROM UnidadTecnica u WHERE u.nombreUnidad = :nombreUnidad")})
public class UnidadTecnica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUnidadTecnica")
    @SequenceGenerator(name = "seqUnidadTecnica",schema = "SAGACI", sequenceName = "SEQ_UNIDAD_TECNICA",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_UNIDAD")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE_UNIDAD")
    private String nombreUnidad;
    @OneToMany(mappedBy = "unidad")
    private List<Contrato> contratoList;
    /** Constructor de la clase sin parametros **/
    public UnidadTecnica() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public UnidadTecnica(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param nombreUnidad nombre de la unidad tecnica encargado de un contrato
     **/
    public UnidadTecnica(Integer id, String nombreUnidad) {
        this.id = id;
        this.nombreUnidad = nombreUnidad;
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
    /** Obtiene el nombre de la unidad tecnica
     * @return unidad tecnica**/
    public String getNombreUnidad() {
        return nombreUnidad;
    }
    /** Establece el nombre de la unidad tecnica
     * @param nombreUnidad unidad tecnica**/
    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

   
    /** Obtiene la lista de Contrato de una unidad tecnica responsable
     * @return  Lista Entidades Contrato**/
    @XmlTransient
    public List<Contrato> getContratoList() {
        return contratoList;
    }
    /** Establece la lista de Contrato de una unidad tecnica responsable
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

    /** Compara dos unidades tecnicas
     * @param object Unidad tecnica
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UnidadTecnica)) {
            return false;
        }
        UnidadTecnica other = (UnidadTecnica) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    /** 
     * @return nombre de la unidad tecnica
     **/
    @Override
    public String toString() {
        return this.nombreUnidad;
    }
    
}
