
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
@Table(name = "TIPO_PAGO" ,schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPago.findByNombreTipoPago", query = "SELECT t FROM TipoPago t WHERE t.nombreTipo = :nombreTipo")})
public class TipoPago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTipoPago")
    @SequenceGenerator(name = "seqTipoPago",schema = "SAGACI", sequenceName = "SEQ_TIPO_PAGO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_TIPO_PAGO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE_TIPO_PAGO")
    private String nombreTipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<Pago> pagoList;
    /** Constructor de la clase sin parametros **/
    public TipoPago() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public TipoPago(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param nombreTipo tipo de pago
     **/
    public TipoPago(Integer id, String nombreTipo) {
        this.id = id;
        this.nombreTipo = nombreTipo;
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
    /** Obtiene el nombre del tipo de pago
     * @return el tipo de pago**/
    public String getNombreTipo() {
        return nombreTipo;
    }
    /** Establece el nombre del tipo de pago
     * @param nombreTipo el tipo de pago**/
    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

   
    /** Obtiene la lista de Pagos del mismo tipo
     * @return  Lista Entidades Pago**/
    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }
    /** Establece la lista de Pagos del mismo tipo
     * @param pagoList Lista Entidades Pago**/
    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** Compara dos tipos de pago
     * @param object Tipo
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoPago)) {
            return false;
        }
        TipoPago other = (TipoPago) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    /** 
     * @return nombre del tipo de pago
     **/
    @Override
    public String toString() {
        return this.nombreTipo;
    }
    
}
