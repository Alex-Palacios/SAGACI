
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
@Table(name = "AFIANZADORA", schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Afianzadora.findByNombreAfianzadora", query = "SELECT a FROM Afianzadora a WHERE a.nombre LIKE :nombreAfianzadora")})

public class Afianzadora implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAfianzadora")
    @SequenceGenerator(name = "seqAfianzadora",schema = "SAGACI", sequenceName = "SEQ_AFIANZADORA",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_AFIANZADORA")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE_AFIANZADORA")
    private String nombre;
    @Column(name = "NOTA_AFIANZADORA")
    private String nota;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "afianzadora")
    private List<Garantia> garantiaList;

    /** Constructor de la clase sin parametros **/
    public Afianzadora() {
        
    }
    
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Afianzadora(Integer id) {
        this.id = id;
    }

    /** Constructor de la clase con parametro
     * @param id identificador
     * @param  nombre afianzadora
     * @param nota
     **/
    public Afianzadora(Integer id, String nombre, String nota) {
        this.id = id;
        this.nombre = nombre.toUpperCase();
        this.nota = nota;
    }
    
    
    /** Obtiene el identificador de afianzadora
     * @return identificador de afianzadora
     **/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador de afianzadora 
     * @param id identificador de afianzadora 
     **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el nombre de la afianzadora
     * @return nombre de afianzadora
     **/
    public String getNombre() {
        return nombre;
    }
    /** Establece el nombre de la afianzadora 
     * @param nombre nombre de afianzadora
     **/
    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }
    /** Obtiene nota u observacion de la afianzadora 
     * @return nota u observaciones
     **/
    public String getNota() {
        return nota;
    }
    /** Establece nota u observacion de la afianzadora 
     * @param nota nota u observaciones
     **/
    public void setNota(String nota) {
        this.nota = nota;
    }

    
    /** Obtiene Lista de garantías vinculadas a la afianzadora 
     * @return Lista de garantias
     **/
    @XmlTransient
    public List<Garantia> getGarantiaList() {
        return garantiaList;
    }
    /** Establece Lista de garantías vinculadas a la afianzadora 
     * @param garantiaList Lista de garantias
     **/
    public void setGarantiaList(List<Garantia> garantiaList) {
        this.garantiaList = garantiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos afianzadoras
     * @param object afianzadora
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Afianzadora)) {
            return false;
        }
        Afianzadora other = (Afianzadora) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /** 
     * @return el nombre de afianzadora
     **/
    @Override
    public String toString() {
        return this.nombre;
    }
    
}
