/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.seguridad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "USUARIOS", schema = "SEGURIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findByUser", query = "SELECT u FROM Usuarios u WHERE u.nombres = :nombre AND u.apellidos = :apellido")  })
public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private BigDecimal userId;
    @Column(name = "NOMBRES")
    private String nombres;
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Column(name = "FECHA_EXPIRACION")
    @Temporal(TemporalType.DATE)
    private Date fechaExpiracion;
    @Column(name = "ES_ADMIN_SYS")
    private Character esAdminSys;
    @Column(name = "ACTIVO")
    private Character activo;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;
    @OneToMany(mappedBy = "userId")
    private List<UsuariosAplicacion> usuariosAplicacionList;

    public Usuarios() {
    }

    public Usuarios(BigDecimal userId) {
        this.userId = userId;
    }

    public BigDecimal getUserId() {
        return userId;
    }

    public void setUserId(BigDecimal userId) {
        this.userId = userId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }
    
    public String getFechaExpiracionLabel(){
        return JsfUtil.setFechaFormateada(fechaExpiracion,1);
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Character getEsAdminSys() {
        return esAdminSys;
    }

    public void setEsAdminSys(Character esAdminSys) {
        this.esAdminSys = esAdminSys;
    }

    public Character getActivo() {
        return activo;
    }

    public String getLbActivo(){
        if(activo.equals('Y')){
            return "SI";
        }else{
            return "NO";
        }
    }
    
    public String getStyleClassEstado(){
        if(activo.equals('Y')){
            return "color:green";
        }else{
            return "color:red";
        }
    }
    
    public void setActivo(Character activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
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
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombres + " " + this.apellidos;
    }
    
}
