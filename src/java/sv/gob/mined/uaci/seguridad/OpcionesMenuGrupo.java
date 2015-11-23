/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.seguridad;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "OPCIONES_MENU_GRUPO",schema = "SEGURIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpcionesMenuGrupo.findAll", query = "SELECT o FROM OpcionesMenuGrupo o WHERE o.idOpcion.idAplicacion = :APP "),
    @NamedQuery(name = "OpcionesMenuGrupo.findByGrupo", query = "SELECT o FROM OpcionesMenuGrupo o WHERE o.idOpcion.idAplicacion = :APP AND o.idGrupo = :grupo") })
public class OpcionesMenuGrupo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_OPC_MNU_GRUPO")
    private BigDecimal idOpcMnuGrupo;
    @Column(name = "AUT_INS")
    private Character autIns;
    @Column(name = "AUT_UPD")
    private Character autUpd;
    @Column(name = "AUT_DEL")
    private Character autDel;
    @Column(name = "CODIGO_GRUPO")
    private String codigoGrupo;
    @Column(name = "CODIGO_OPC_MENU")
    private String codigoOpcMenu;
    @JoinColumn(name = "ID_OPCION", referencedColumnName = "ID_OPCION")
    @ManyToOne
    private OpcionesMenu idOpcion;
    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID_GRUPO")
    @ManyToOne
    private Grupos idGrupo;

    public OpcionesMenuGrupo() {
    }

    public OpcionesMenuGrupo(BigDecimal idOpcMnuGrupo) {
        this.idOpcMnuGrupo = idOpcMnuGrupo;
    }

    public BigDecimal getIdOpcMnuGrupo() {
        return idOpcMnuGrupo;
    }

    public void setIdOpcMnuGrupo(BigDecimal idOpcMnuGrupo) {
        this.idOpcMnuGrupo = idOpcMnuGrupo;
    }

    public Character getAutIns() {
        return autIns;
    }
    
    public String getLbAutIns(){
        if(autIns.equals('Y')){
            return "SI";
        }else{
            return "NO";
        }
    }
    
    public String getStyleClassAutIns(){
        if(autIns.equals('Y')){
            return "color:green";
        }else{
            return "color:red";
        }
    }

    public void setAutIns(Character autIns) {
        this.autIns = autIns;
    }

    public Character getAutUpd() {
        return autUpd;
    }
    
    public String getLbAutUpd(){
        if(autUpd.equals('Y')){
            return "SI";
        }else{
            return "NO";
        }
    }
    
    public String getStyleClassAutUpd(){
        if(autUpd.equals('Y')){
            return "color:green";
        }else{
            return "color:red";
        }
    }

    public void setAutUpd(Character autUpd) {
        this.autUpd = autUpd;
    }

    public Character getAutDel() {
        return autDel;
    }
    
    public String getLbAutDel(){
        if(autDel.equals('Y')){
            return "SI";
        }else{
            return "NO";
        }
    }
    
    public String getStyleClassAutDel(){
        if(autDel.equals('Y')){
            return "color:green";
        }else{
            return "color:red";
        }
    }

    public void setAutDel(Character autDel) {
        this.autDel = autDel;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getCodigoOpcMenu() {
        return codigoOpcMenu;
    }

    public void setCodigoOpcMenu(String codigoOpcMenu) {
        this.codigoOpcMenu = codigoOpcMenu;
    }

    public OpcionesMenu getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(OpcionesMenu idOpcion) {
        this.idOpcion = idOpcion;
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
        hash += (idOpcMnuGrupo != null ? idOpcMnuGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpcionesMenuGrupo)) {
            return false;
        }
        OpcionesMenuGrupo other = (OpcionesMenuGrupo) object;
        if ((this.idOpcMnuGrupo == null && other.idOpcMnuGrupo != null) || (this.idOpcMnuGrupo != null && !this.idOpcMnuGrupo.equals(other.idOpcMnuGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.idOpcion.getNombreOpcion();
    }
    
}
