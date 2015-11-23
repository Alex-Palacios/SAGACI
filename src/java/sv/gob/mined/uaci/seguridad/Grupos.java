/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.seguridad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "GRUPOS",schema = "SEGURIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupos.findAll", query = "SELECT g FROM Grupos g WHERE g.idAplicacion = :APP"),
    @NamedQuery(name = "Grupos.findByCodigoGrupo", query = "SELECT g FROM Grupos g WHERE g.idAplicacion = :APP AND g.idGrupo = :grupo") })
public class Grupos implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_GRUPO")
    private Integer idGrupo;
    @Column(name = "CODIGO_GRUPO")
    private String codigoGrupo;
    @Column(name = "ID_APLICACION")
    private String idAplicacion;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(mappedBy = "idGrupo")
    private List<OpcionesMenuGrupo> opcionesMenuGrupoList;
    @OneToMany(mappedBy = "idGrupo")
    private List<UsuariosAplicacion> usuariosAplicacionList;

    public Grupos() {
    }

    public Grupos(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(String idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<OpcionesMenuGrupo> getOpcionesMenuGrupoList() {
        return opcionesMenuGrupoList;
    }

    public void setOpcionesMenuGrupoList(List<OpcionesMenuGrupo> opcionesMenuGrupoList) {
        this.opcionesMenuGrupoList = opcionesMenuGrupoList;
    }

    @XmlTransient
    public List<UsuariosAplicacion> getUsuariosAplicacionList() {
        return usuariosAplicacionList;
    }

    public void setUsuariosAplicacionList(List<UsuariosAplicacion> usuariosAplicacionList) {
        this.usuariosAplicacionList = usuariosAplicacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupo != null ? idGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupos)) {
            return false;
        }
        Grupos other = (Grupos) object;
        if ((this.idGrupo == null && other.idGrupo != null) || (this.idGrupo != null && !this.idGrupo.equals(other.idGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.descripcion;
    }
    
}
