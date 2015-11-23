
package sv.gob.mined.uaci.entidades;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
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
@Table(name = "PROVEEDOR" , schema = "SAGACI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedor.findByNombreProveedor", query = "SELECT p FROM Proveedor p WHERE p.nombre LIKE :nombreProveedor"),
    @NamedQuery(name = "Proveedor.findByNaturalezaProveedor", query = "SELECT p FROM Proveedor p WHERE p.naturaleza = :naturalezaProveedor"),
    @NamedQuery(name = "Proveedor.findByNitProveedor", query = "SELECT p FROM Proveedor p WHERE p.nit = :nitProveedor") })
public class Proveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProveedor")
    @SequenceGenerator(name = "seqProveedor",schema = "SAGACI", sequenceName = "SEQ_PROVEEDOR",allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "ID_PROVEEDOR")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE_PROVEEDOR")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "NATURALEZA_PROVEEDOR")
    private Integer naturaleza;
    @Column(name = "NIT_PROVEEDOR")
    private String nit;
    @Column(name = "TEL_PROVEEDOR")
    private String tel;
    @Column(name = "CORREO_PROVEEDOR")
    private String correo;
    @Column(name = "CONTACTO_PROVEEDOR")
    private String contacto;
    @Column(name = "NOTA_PROVEEDOR")
    private String nota;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor")
    private List<Garantia> garantiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contratista")
    private List<Contrato> contratoList;
    /** Constructor de la clase sin parametros **/
    public Proveedor() {
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     **/
    public Proveedor(Integer id) {
        this.id = id;
    }
    /** Constructor de la clase con parametros
     * @param id identificador
     * @param nombre nombre del proveedor
     * @param naturaleza naturaleza juridica del proveedor
     * @param nit numero de NIT del proveedor
     * @param  tel telefono de contacto
     * @param correo correo del proveedor
     * @param nota nota u observaciones
     **/
    public Proveedor(Integer id, String nombre, Integer naturaleza, String nit, String tel, String correo, String nota) {
        this.id = id;
        this.nombre = nombre.toUpperCase();
        this.naturaleza = naturaleza;
        this.nit = nit;
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
    /** Obtiene el nombre del proveedor
     * @return nombre del provedor**/
    public String getNombre() {
        return nombre;
    }
    /** Establece el nombre del proveedor
     * @param nombre nombre del provedor**/
    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }
    /** Obtiene la naturaleza del proveedor
     * @return naturaleza del provedor**/
    public Integer getNaturaleza() {
        return naturaleza;
    }
    /** Obtiene la naturaleza del proveedor en formato texto
     * @return naturaleza del provedor en texto**/
    public String getNaturalezaLabel() {
        if(naturaleza != null){
            return naturalezaLabel(naturaleza);
        }else{
            return "";
        }
    }
    /** Establece la naturaleza del proveedor
     * @param naturaleza  naturaleza del provedor**/
    public void setNaturaleza(Integer naturaleza) {
        this.naturaleza = naturaleza;
    }
    /** Convierte naturaleza de proveedor a partir de su numero entero
     * @return naturaleza en formato string **/
    public static String naturalezaLabel(int value){
        String r = "";
        switch(value){
            //Valores segun BD
            case 1: r = ResourceBundle.getBundle("/Bundle").getString("personaNaturalLabel");break;
            case 2: r = ResourceBundle.getBundle("/Bundle").getString("personaJuridicaLabel");break;
        }
        return r;
    }
    /** Obtiene el NIT del proveedor
     * @return NIT del provedor **/
    public String getNit() {
        return nit;
    }
    /** Establece el NIT del proveedor
     * @param nit NIT del provedor **/
    public void setNit(String nit) {
        this.nit = nit;
    }
    /** Obtiene el TEL del proveedor
     * @return TEL del provedor **/
    public String getTel() {
        return tel;
    }
    /** Establece el TEL del proveedor
     * @param tel TEL del provedor **/
    public void setTel(String tel) {
        this.tel = tel;
    }
    /** Obtiene el correo del proveedor
     * @return correo del provedor **/
    public String getCorreo() {
        return correo;
    }
    /** Establece el correo del proveedor
     * @param correo correo del provedor **/
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    /** Obtiene el contacto del proveedor
     * @return contacto del provedor **/
    public String getContacto() {
        return contacto;
    }
    /** Establece el contacto del proveedor
     * @param contacto contacto del provedor **/
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    /** Obtiene nota del proveedor
     * @return nota del provedor **/
    public String getNota() {
        return nota;
    }
    /** Establece nota del proveedor
     * @param nota  nota del provedor **/
    public void setNota(String nota) {
        this.nota = nota;
    }

    
    /** Obtiene la lista de garantias del proveedor
     * @return  Lista Entidades Garantia**/
    @XmlTransient
    public List<Garantia> getGarantiaList() {
        return garantiaList;
    }
    /** Establece la lista de garantias del proveedor
     * @param garantiaList  Lista Entidades Garantia**/
    public void setGarantiaList(List<Garantia> garantiaList) {
        this.garantiaList = garantiaList;
    }
    /** Obtiene la lista de contratos del proveedor
     * @return  Lista Entidades Contrato**/
    @XmlTransient
    public List<Contrato> getContratoList() {
        return contratoList;
    }
    /** Establece la lista de contratos del proveedor
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

    /** Compara dos proveedores
     * @param object Proveedor
     * @return resultado de la comparacion
     **/
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /** 
     * @return nombre del proveedor
     **/
    @Override
    public String toString() {
        return this.nombre;
    }
    
    
    
}
