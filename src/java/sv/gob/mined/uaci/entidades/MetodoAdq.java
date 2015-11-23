
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
@Table(name = "METODO_ADQ",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetodoAdq.findByCodMetodo", query = "SELECT m FROM MetodoAdq m WHERE m.codigo = :codigo"),
    @NamedQuery(name = "MetodoAdq.findByNombreMetodo", query = "SELECT m FROM MetodoAdq m WHERE m.nombre = :nombreMetodo")})
public class MetodoAdq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqMetodoAdq")
    @SequenceGenerator(name = "seqMetodoAdq",schema = "SAGACI", sequenceName = "SEQ_METODO_ADQ",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_METODO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "COD_METODO")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "NOMBRE_METODO")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "metodo")
    private List<Proceso> procesoList;
    /** Constructor de la clase sin parametros **/
    public MetodoAdq() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public MetodoAdq(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param codigo abreviatura del metodo de adquisicion
     * @param nombre nombre del metodo de adquisicion
     **/
    public MetodoAdq(Integer id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo.toUpperCase();
        this.nombre = nombre.toUpperCase();
    }
    /** Obtiene el identificador del metodo de adquisicion
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador del metodo de adquisicion
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el codigo o abreviatura del metodo
     * @return  codigo o abreviatura**/
    public String getCodigo() {
        return codigo;
    }
    /** Establece el codigo del metodo de adquisicion
     * @param codigo abreviatura o codigo **/
    public void setCodigo(String codigo) {
        this.codigo = codigo.toUpperCase();
    }
    /** Obtiene el nombre del metodo
     * @return el nombre del metodo de adquisicion**/
    public String getNombre() {
        return nombre;
    }
    /** Establece el nombre del metodo
     * @param nombre el nombre del metodo de adquisicion**/
    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    
    /** Obtiene la lista de procesos con el mismo metodo de adquisicion
     * @return  Lista Entidades Proceso**/
    @XmlTransient
    public List<Proceso> getProcesoList() {
        return procesoList;
    }
    /** Estable la lista de procesos con el mismo metodo de adquisicion
     * @param procesoList Lista Entidades Proceso**/
    public void setProcesoList(List<Proceso> procesoList) {
        this.procesoList = procesoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /** Compara dos Metodos de adquisicion
     * @param object Metodo
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MetodoAdq)) {
            return false;
        }
        MetodoAdq other = (MetodoAdq) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    /** 
     * @return nombre del metodo de adquisicion
     **/
    @Override
    public String toString() {
        return this.nombre;
    }
    
        
}
