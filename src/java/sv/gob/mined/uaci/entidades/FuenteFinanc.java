
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
@Table(name = "FUENTE_FINANC",schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FuenteFinanc.findByCodFuente", query = "SELECT f FROM FuenteFinanc f WHERE f.codigo = :codFuente"),
    @NamedQuery(name = "FuenteFinanc.findByNombreFuente", query = "SELECT f FROM FuenteFinanc f WHERE f.nombre = :nombreFuente")})
public class FuenteFinanc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqFuenteFinanc")
    @SequenceGenerator(name = "seqFuenteFinanc",schema = "SAGACI", sequenceName = "SEQ_FUENTE_FINANC",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_FUENTE")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "COD_FUENTE")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "NOMBRE_FUENTE")
    private String nombre;
    @Column(name = "NOTA_FUENTE")
    private String nota;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fuente")
    private List<Contrato> contratoList;
    
    /** Constructor de la clase sin parametros **/
    public FuenteFinanc() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public FuenteFinanc(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param codigo codigo o abreviatura de fuente de financiamiento
     * @param nombre nombre de fuente de financiamiento
     * @param nota nota u observacion
     **/
    public FuenteFinanc(Integer id, String codigo, String nombre, String nota) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.nota = nota;
    }
    /** Obtiene el identificador de la fuente
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador de la fuente
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el codigo o abreviatura de la fuente
     * @return  codigo o abreviatura**/
    public String getCodigo() {
        return codigo;
    }
    /** Establece el codigo de la fuente
     * @param codigo abreviatura o codigo **/
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    /** Obtiene el nombre de la fuente
     * @return  fuente de financiamiento**/
    public String getNombre() {
        return nombre;
    }
    /** Establece el nombre de la fuente
     * @param nombre fuente de financiamiento **/
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /** Obtiene la nota u observaciones
     * @return  notas**/
    public String getNota() {
        return nota;
    }
    /** Establece la nota
     * @param nota observacion o nota **/
    public void setNota(String nota) {
        this.nota = nota;
    }

    /** Obtiene la lista de contratos de la misma fuente
     * @return  Lista Entidades Contrato**/
    @XmlTransient
    public List<Contrato> getContratoList() {
        return contratoList;
    }
    /** Establece la lista contratos con la misma fuente
     * @param contratoList Lista Entidades Contrato **/
    public void setContratoList(List<Contrato> contratoList) {
        this.contratoList = contratoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos Fuentes de financiamiento
     * @param object FuenteFinanc
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FuenteFinanc)) {
            return false;
        }
        FuenteFinanc other = (FuenteFinanc) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    /** 
     * @return abreviatura o codigo
     **/
    @Override
    public String toString() {
        return this.codigo;
    }
    
}
