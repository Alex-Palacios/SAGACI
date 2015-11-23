/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.seguridad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ResourceBundle;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "USUARIOS_APLICACION", schema = "SEGURIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosAplicacion.findAll", query = "SELECT u FROM UsuariosAplicacion u WHERE u.idAplicacion = :APP"),
    @NamedQuery(name = "UsuariosAplicacion.findByLogin", query = "SELECT u FROM UsuariosAplicacion u WHERE u.idAplicacion = :APP AND u.login = :login AND u.password = :password"),
    @NamedQuery(name = "UsuariosAplicacion.countUsername", query = "SELECT COUNT(u) FROM UsuariosAplicacion u WHERE u.idAplicacion = :APP AND u.login = :login ") })
public class UsuariosAplicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USR_APLICACION")
    private BigDecimal idUsrAplicacion;
    @Column(name = "ID_APLICACION")
    private String idAplicacion;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "CAMBIAR_PASSWD")
    private Character cambiarPasswd;
    @Column(name = "CODIGO_GRUPO")
    private String codigoGrupo;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private Usuarios userId;
    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID_GRUPO")
    @ManyToOne
    private Grupos idGrupo;

    public UsuariosAplicacion() {
    }

    public UsuariosAplicacion(BigDecimal idUsrAplicacion) {
        this.idUsrAplicacion = idUsrAplicacion;
    }

    public BigDecimal getIdUsrAplicacion() {
        return idUsrAplicacion;
    }

    public void setIdUsrAplicacion(BigDecimal idUsrAplicacion) {
        this.idUsrAplicacion = idUsrAplicacion;
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(String idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Character getCambiarPasswd() {
        return cambiarPasswd;
    }

    public void setCambiarPasswd(Character cambiarPasswd) {
        this.cambiarPasswd = cambiarPasswd;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public Usuarios getUserId() {
        return userId;
    }

    public void setUserId(Usuarios userId) {
        this.userId = userId;
    }

    public Grupos getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupos idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsrAplicacion != null ? idUsrAplicacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosAplicacion)) {
            return false;
        }
        UsuariosAplicacion other = (UsuariosAplicacion) object;
        if ((this.idUsrAplicacion == null && other.idUsrAplicacion != null) || (this.idUsrAplicacion != null && !this.idUsrAplicacion.equals(other.idUsrAplicacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.login;
    }
    
}
