
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
@Table(name = "ESTATUS_CONTRATO", schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstatusContrato.findByEstadoContrato", query = "SELECT e FROM EstatusContrato e WHERE e.estado = :estadoContrato")})

public class EstatusContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEstatusContrato")
    @SequenceGenerator(name = "seqEstatusContrato",schema = "SAGACI", sequenceName = "SEQ_ESTATUS_CONTRATO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_ESTATUS_CONTRATO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ESTADO_CONTRATO")
    private String estado;
    @OneToMany(mappedBy = "estatusContrato")
    private List<Contrato> contratoList;

    /** Constructor de la clase sin parametros **/
    public EstatusContrato() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public EstatusContrato(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param estado nombre del estado del contrato
     **/
    public EstatusContrato(Integer id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    /** Obtiene el identificador del estado de contrato
     * @return  identificador**/
    public Integer getId() {
        return id;
    }
    /** Establece el identificador del estado de contrato
     * @param id identificador **/
    public void setId(Integer id) {
        this.id = id;
    }
    /** Obtiene el estado de contrato
     * @return estado contrato **/
    public String getEstado() {
        return estado;
    }
    /** Establece el estado de contrato
     * @param estado estado contrato**/
    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    /** Obtiene la lista de contratos con el mismo estado
     * @return  Lista Entidades Contrato**/
    @XmlTransient
    public List<Contrato> getContratoList() {
        return contratoList;
    }
    /** Establece la lista contratos con el mismo estado
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

    /** Compara dos estados de contratos
     * @param object EstatusContrato
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstatusContrato)) {
            return false;
        }
        EstatusContrato other = (EstatusContrato) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /** 
     * @return nombre del estado del contrato
     **/
    @Override
    public String toString() {
        return this.estado;
    }
    
}
