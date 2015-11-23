
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
@Table(name = "TECNICO",schema = "SAGACI")
@XmlRootElement
@NamedQueries({ 
    @NamedQuery(name = "Tecnico.findByNombre", query = "SELECT t FROM Tecnico t WHERE t.nombre LIKE :nombreTecnico")
 })

public class Tecnico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTecnico")
    @SequenceGenerator(name = "seqTecnico",schema = "SAGACI", sequenceName = "SEQ_TECNICO",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_TECNICO")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE_TECNICO")
    private String nombre;
    @Column(name = "TEL_TECNICO")
    private String tel;
    @Column(name = "CORREO_TECNICO")
    private String correo;
    @Column(name = "NOTA_TECNICO")
    private String nota;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tecnico")
    private List<Proceso> procesoList;
    /** Constructor de la clase sin parametros **/
    public Tecnico() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Tecnico(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param nombre nombre del tecnico GACI
     * @param tel telefono tecnico
     * @param correo email del tecnico
     * @param nota nota u observaciones
     **/
    public Tecnico(Integer id, String nombre, String tel, String correo, String nota) {
        this.id = id;
        this.nombre = nombre.toUpperCase();
        this.tel = tel;
        this.correo = correo;
        this.nota = nota;
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
    /** Obtiene el nombre del tecnico
     * @return nombre del tecnico**/
    public String getNombre() {
        return nombre;
    }
    /** Obtiene el nombre del tecnico
     * @return nombre del tecnico**/
    public String getNombreCompleto() {
        return nombre;
    }
    /** Establece el nombre del tecnico
     * @param nombre nombre del tecnico**/
    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }
    
    /** Obtiene el telefono del tecnico
     * @return telefono del tecnico**/
    public String getTel() {
        return tel;
    }
    /** Establece el telefono del tecnico
     * @param tel telefono del tecnico**/
    public void setTel(String tel) {
        this.tel = tel;
    }
    /** Obtiene el correo electronico del tecnico
     * @return correo del tecnico**/
    public String getCorreo() {
        return correo;
    }
    /** Establece el correo electronico del tecnico
     * @param correo email del tecnico**/
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    /** Obtiene nota u observaciones
     * @return notas u observaciones**/
    public String getNota() {
        return nota;
    }
    /** Establece notas u observaciones
     * @param nota observaciones**/
    public void setNota(String nota) {
        this.nota = nota;
    }

   
    /** Obtiene la lista de procesos del tecnico
     * @return  Lista Entidades Proceso**/
    @XmlTransient
    public List<Proceso> getProcesoList() {
        return procesoList;
    }
    /** Establece la lista de procesos del tecnico
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
    
    /** Compara dos tecnicos
     * @param object Tecnico
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tecnico)) {
            return false;
        }
        Tecnico other = (Tecnico) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /** 
     * @return nombre del tecnico
     **/
    @Override
    public String toString() {
        return getNombreCompleto();
    }
    
}
