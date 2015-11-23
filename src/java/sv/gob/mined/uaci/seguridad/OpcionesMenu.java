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
@Table(name = "OPCIONES_MENU",schema = "SEGURIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpcionesMenu.findAll", query = "SELECT o FROM OpcionesMenu o WHERE o.idAplicacion = :APP ")  })
public class OpcionesMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_OPCION")
    private BigDecimal idOpcion;
    @Column(name = "ID_APLICACION")
    private String idAplicacion;
    @Column(name = "CODIGO_OPC_MENU")
    private String codigoOpcMenu;
    @Column(name = "NOMBRE_OPCION")
    private String nombreOpcion;
    @OneToMany(mappedBy = "idOpcion")
    private List<OpcionesMenuGrupo> opcionesMenuGrupoList;

    public OpcionesMenu() {
    }

    public OpcionesMenu(BigDecimal idOpcion) {
        this.idOpcion = idOpcion;
    }

    public BigDecimal getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(BigDecimal idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(String idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    
    public String getCodigoOpcMenu() {
        return codigoOpcMenu;
    }

    public void setCodigoOpcMenu(String codigoOpcMenu) {
        this.codigoOpcMenu = codigoOpcMenu;
    }

    public String getNombreOpcion() {
        return nombreOpcion;
    }

    public void setNombreOpcion(String nombreOpcion) {
        this.nombreOpcion = nombreOpcion;
    }


    @XmlTransient
    public List<OpcionesMenuGrupo> getOpcionesMenuGrupoList() {
        return opcionesMenuGrupoList;
    }

    public void setOpcionesMenuGrupoList(List<OpcionesMenuGrupo> opcionesMenuGrupoList) {
        this.opcionesMenuGrupoList = opcionesMenuGrupoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOpcion != null ? idOpcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpcionesMenu)) {
            return false;
        }
        OpcionesMenu other = (OpcionesMenu) object;
        if ((this.idOpcion == null && other.idOpcion != null) || (this.idOpcion != null && !this.idOpcion.equals(other.idOpcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombreOpcion;
    }
    
}
