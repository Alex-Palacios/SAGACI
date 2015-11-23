/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.seguridad;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;

/**
 *
 * @author Alex
 */
@ManagedBean(name = "seguridadController")
@SessionScoped
public class SeguridadController implements Serializable {
    private SeguridadJpaController jpaController;
    private String username;
    private String password;
    private boolean login;
    private UsuariosAplicacion usuario;
    
    private String nuevoUsername;
    private String passwordAnterior;
    private String nuevoPassword;

    
    public SeguridadController() {
        
    }

    public SeguridadJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new SeguridadJpaController(Persistence.createEntityManagerFactory("SAGACIv1.0PU"));
        }
        return jpaController;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public UsuariosAplicacion getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuariosAplicacion usuario) {
        this.usuario = usuario;
    }

    public String getNuevoUsername() {
        return nuevoUsername;
    }

    public void setNuevoUsername(String nuevoUsername) {
        this.nuevoUsername = nuevoUsername;
    }

    public String getPasswordAnterior() {
        return passwordAnterior;
    }

    public void setPasswordAnterior(String passwordAnterior) {
        this.passwordAnterior = passwordAnterior;
    }

    public String getNuevoPassword() {
        return nuevoPassword;
    }

    public void setNuevoPassword(String nuevoPassword) {
        this.nuevoPassword = nuevoPassword;
    }
    
    
    public void login(){
        usuario = getJpaController().obtenerUsuario(username, password);
        if(usuario != null){
            //SI LAS CREDENCIALES SON VALIDAS
            if(usuario.getUserId().getActivo().compareTo('Y') == 0){
                establecerPermisosUsuario();
                login = true;
                username = null;
                password = null;
                irHome();
            }else{
                //SI EL USUARIO ESTA INACTIVO
                password = "";
                usuario = null;
                login = false;
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("warning_login_usuario_inactivo"));
            }
        }else{
            //SI LAS CREDENCIALES NO SON VALIDAS
            usuario = null;
            password = null;
            login = false;
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("error_login"));
        }
    }

    
    //FUNCION DE CERRAR SESION
    public void logout(){
        username = null;
        password = null;
        usuario = null;
        login = false;
        JsfUtil.cerrarSessionUsuario();
        JsfUtil.irA("");
    }
    
    
    public void irHome(){
        JsfUtil.irA(ResourceBundle.getBundle("/Bundle").getString("url_home"));
    }
    
    
    public void prepareUpdateCredenciales(){
        passwordAnterior = null;
        nuevoPassword = null;
        nuevoUsername = null;
    }
    
    
    public void cambiarUsername(){
        if(validarUsername(nuevoUsername)){
            nuevoUsername = nuevoUsername;
            usuario.setLogin(nuevoUsername);
            try {
                getJpaController().editUsername(usuario);
                JsfUtil.addSuccessMessage("USERNAME ACTUALIZADO");
                JsfUtil.cerrarVentana("updateUser");
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                JsfUtil.ventanaErrorEffect("updateUser");
            }
        }else{
            JsfUtil.addWarnMessage("Ingrese Username nuevamente");
        }
    }
    
    
    public void cambiarPassword(){
        String passMD5 = null;
        try {
            passMD5 = JsfUtil.getMD5(passwordAnterior);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SeguridadController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SeguridadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(passMD5 != null && usuario.getPassword().equals(passMD5)){
            if(validarPassword(nuevoPassword)){
                usuario.setPassword(nuevoPassword);
                try {
                    getJpaController().editPassword(usuario);
                    JsfUtil.addSuccessMessage("PASSWORD ACTUALIZADO");
                    JsfUtil.cerrarVentana("updatePassword");
                } catch (Exception e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                    JsfUtil.ventanaErrorEffect("updatePassword");
                }
            }else{
                JsfUtil.addWarnMessage("Ingrese Password nuevamente");
            }
        }else{
            JsfUtil.addErrorMessage("Password Actual incorrecto");
        }
        
    }
    
    
    public boolean validarUsername(String username){
        boolean OK = true;
        if(username == null){
            JsfUtil.addErrorMessage("Username requerido");
            return false;
        }else if(username.isEmpty()){
            JsfUtil.addErrorMessage("Username requerido");
            return false;
        }else if(username.length() < 8 || username.length() > 20){
            JsfUtil.addErrorMessage("Username debe tener una longitud entre 8 - 20");
            return false;
        }else if(!getJpaController().usernameDisponible(username)){
            JsfUtil.addErrorMessage("Username no esta disponible es posible que ya exista");
            return false;
        }
        return OK;
    }
    
    public boolean validarPassword(String password){
        boolean OK = true;
        if(password == null){
            JsfUtil.addErrorMessage("PAssword requerido");
            return false;
        }else if(password.isEmpty()){
            JsfUtil.addErrorMessage("Password requerido");
            return false;
        }else if(password.length() < 7 || password.length() > 12){
            JsfUtil.addErrorMessage("Password debe tener una longitud entre 7 - 12");
            return false;
        }else if(password.toUpperCase().equals(usuario.getLogin())){
            JsfUtil.addErrorMessage("Password debe ser distinto al nombre de usuario");
            return false;
        }else if(!validarCadenaPassword(password)){
            JsfUtil.addErrorMessage("Password contiene caracteres no permitidos para una contraseña");
            return false;
        }
        return OK;
    }
    
    
    public boolean validarCadenaPassword(String pass){
        boolean OK = true;
        for(int i = 0; i < pass.length(); i++){
            char c = pass.charAt(i);
            if(c >= 65 && c <= 90){ 
                // A-Z
            }else if(c >= 97 && c <= 122 ){
                // a-z
            }else if(c >= 48 && c <= 57){
                // 0-9
            }else if((c >= 35 && c <= 38) || c == 42 || c == 43 || (c >= 45 && c <= 47) || c == 61 || c == 64 || c == 92 || c== 95){
                // - + * / @ # $ \ % _ . = &
            }else{
                return false;
            }
        } 
        return OK;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    // USUARIOS ////////////////////////////////////////////////////////////////////////
    private List<UsuariosAplicacion> usuariosSAG;
    private UsuariosAplicacion newItemUser;
    private UsuariosAplicacion selectedUser;
    private boolean establecerFechaExp;
    private boolean userActivo;
    private boolean cambiarTipoUser;
    private Grupos nuevoTipo;
    

    public List<UsuariosAplicacion> getUsuariosSAG() {
        if(usuariosSAG == null){
            usuariosSAG = getJpaController().usuariosSAG();
        }
        return usuariosSAG;
    }

    public void setUsuariosSAG(List<UsuariosAplicacion> usuariosSAG) {
        this.usuariosSAG = usuariosSAG;
    }

    public UsuariosAplicacion getNewItemUser() {
        if(newItemUser == null){
            newItemUser = new UsuariosAplicacion();
        }
        return newItemUser;
    }

    public boolean isEstablecerFechaExp() {
        return establecerFechaExp;
    }

    public void setEstablecerFechaExp(boolean establecerFechaExp) {
        this.establecerFechaExp = establecerFechaExp;
    }

    public boolean isUserActivo() {
        return userActivo;
    }

    public void setUserActivo(boolean userActivo) {
        this.userActivo = userActivo;
    }

    public boolean isCambiarTipoUser() {
        return cambiarTipoUser;
    }

    public void setCambiarTipoUser(boolean cambiarTipoUser) {
        this.cambiarTipoUser = cambiarTipoUser;
    }

    public Grupos getNuevoTipo() {
        return nuevoTipo;
    }

    public void setNuevoTipo(Grupos nuevoTipo) {
        this.nuevoTipo = nuevoTipo;
    }

    
    public void setNewItemUser(UsuariosAplicacion newItemUser) {
        this.newItemUser = newItemUser;
    }

    public UsuariosAplicacion getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UsuariosAplicacion selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    public void recreateUsuariosList(){
        usuariosSAG = null;
    }
    
    public void prepareCrearUsuario(){
        newItemUser = new UsuariosAplicacion();
        newItemUser.setUserId(new Usuarios());
        establecerFechaExp = false;
    }
    
    public List<Grupos> getTiposUsuarios() {
        return getJpaController().findTiposUsuarios();
    }
    
    public SelectItem[] getTiposUsuariosSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTiposUsuarios(), true);
    }
    
    
    
    public void crearUsuario(){
        if(validarDatosUsuario()){
            try {
                if(!establecerFechaExp){
                    newItemUser.getUserId().setFechaExpiracion(null);
                }
                getJpaController().createUser(newItemUser);
                JsfUtil.addSuccessMessage("USUARIO CREADO");
                JsfUtil.cerrarVentana("crearUsuario");
                recreateUsuariosList();
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                JsfUtil.ventanaErrorEffect("crearUsuario");
            }
        }
    }
    
    public void prepararEditar(){
        if(selectedUser != null){
            if(selectedUser.getUserId().getActivo().equals('Y')){
                userActivo = true;
            }else{
                userActivo = false;
            }
            if(selectedUser.getUserId().getFechaExpiracion() != null){
                establecerFechaExp = true;
            }else{
                establecerFechaExp = false;
            }
        }
    }
    
    public void editarUsuario(){
        if(establecerFechaExp && JsfUtil.hoy().compareTo(selectedUser.getUserId().getFechaExpiracion()) >= 0){
            JsfUtil.addErrorMessage("Fecha de Expiracion inválida");
        }else{
            try {
                if(!establecerFechaExp){
                    selectedUser.getUserId().setFechaExpiracion(null);
                }
                if(userActivo){
                    selectedUser.getUserId().setActivo('Y');
                }else{
                    selectedUser.getUserId().setActivo('N');
                }
                if(cambiarTipoUser){
                    selectedUser.setIdGrupo(nuevoTipo);
                }
                getJpaController().editUser(selectedUser);
                JsfUtil.addSuccessMessage("USUARIO ACTUALIZADO");
                JsfUtil.cerrarVentana("editarUsuario");
                recreateUsuariosList();
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                JsfUtil.ventanaErrorEffect("editarUsuario");
            }
        }
    }
    
    
    
    public void eliminarUsuario() {
        try {
            getJpaController().destroyUser(selectedUser);
            recreateUsuariosList();
            JsfUtil.addSuccessMessage("USUARIO ELIMINADO");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    
    public boolean validarDatosUsuario(){
        boolean OK = true;
        if(!validarUsername(newItemUser.getLogin())){
            OK = false;
        }
        if(establecerFechaExp && JsfUtil.hoy().compareTo(newItemUser.getUserId().getFechaExpiracion()) >= 0){
            JsfUtil.addErrorMessage("Fecha de Expiracion inválida");
            OK = false;
        }
        
        return OK;
    }
    
    
     ///////////////////////////////////////////////////////////////////////////////////
    // PERMISO ////////////////////////////////////////////////////////////////////////
    
    private Grupos grupo;
    private List<OpcionesMenuGrupo> permisosSAG;
    private OpcionesMenuGrupo selectedPermiso;

    public Grupos getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupos grupo) {
        this.grupo = grupo;
    }

    public List<OpcionesMenuGrupo> getPermisosSAG() {
        if(permisosSAG == null){
            permisosSAG = getJpaController().permisosSAG(grupo);
        }
        return permisosSAG;
    }

    public void setPermisosSAG(List<OpcionesMenuGrupo> permisosSAG) {
        this.permisosSAG = permisosSAG;
    }

    public OpcionesMenuGrupo getSelectedPermiso() {
        return selectedPermiso;
    }

    public void setSelectedPermiso(OpcionesMenuGrupo selectedPermiso) {
        this.selectedPermiso = selectedPermiso;
    }
    
    public void recreatePermisosList(){
        permisosSAG = null;
    }
    
    public void editarPermiso(){
        try {
            getJpaController().editPermiso(selectedPermiso);
            JsfUtil.addSuccessMessage("PERMISO ACTUALIZADO");
            JsfUtil.cerrarVentana("editarPermiso");
            recreateUsuariosList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.ventanaErrorEffect("editarPermiso");
        }
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////
    // PERMISOS ////////////////////////////////////////////////////////////////////////
    private Permiso P1; //TECNICOS
    private Permiso P2; //METODOS DE ADQUISICION
    private Permiso P3; //PROCESOS
    private Permiso P4; //PROVEEDORES
    private Permiso P5; //ESTADOS CONTRATOS
    private Permiso P6; //UNIDAD TECNICA
    private Permiso P7; //CONTRATOS
    private Permiso P8; //FOLIO Y DIGITALIZACION
    private Permiso P9; //ARCHIVO
    private Permiso P10;//PAGOS
    private Permiso P11;//AFIANZADORAS
    private Permiso P12;//TIPO GARANTIAS
    private Permiso P13;//GARANTIAS
    private Permiso P14;//USUARIOS
    private Permiso P15;//PERMISOS

    public Permiso getP1() {
        return P1;
    }

    public void setP1(Permiso P1) {
        this.P1 = P1;
    }

    public Permiso getP2() {
        return P2;
    }

    public void setP2(Permiso P2) {
        this.P2 = P2;
    }

    public Permiso getP3() {
        return P3;
    }

    public void setP3(Permiso P3) {
        this.P3 = P3;
    }

    public Permiso getP4() {
        return P4;
    }

    public void setP4(Permiso P4) {
        this.P4 = P4;
    }

    public Permiso getP5() {
        return P5;
    }

    public void setP5(Permiso P5) {
        this.P5 = P5;
    }

    public Permiso getP6() {
        return P6;
    }

    public void setP6(Permiso P6) {
        this.P6 = P6;
    }

    public Permiso getP7() {
        return P7;
    }

    public void setP7(Permiso P7) {
        this.P7 = P7;
    }

    public Permiso getP8() {
        return P8;
    }

    public void setP8(Permiso P8) {
        this.P8 = P8;
    }

    public Permiso getP9() {
        return P9;
    }

    public void setP9(Permiso P9) {
        this.P9 = P9;
    }

    public Permiso getP10() {
        return P10;
    }

    public void setP10(Permiso P10) {
        this.P10 = P10;
    }

    public Permiso getP11() {
        return P11;
    }

    public void setP11(Permiso P11) {
        this.P11 = P11;
    }

    public Permiso getP12() {
        return P12;
    }

    public void setP12(Permiso P12) {
        this.P12 = P12;
    }

    public Permiso getP13() {
        return P13;
    }

    public void setP13(Permiso P13) {
        this.P13 = P13;
    }

    public Permiso getP14() {
        return P14;
    }

    public void setP14(Permiso P14) {
        this.P14 = P14;
    }

    public Permiso getP15() {
        return P15;
    }

    public void setP15(Permiso P15) {
        this.P15 = P15;
    }
    
    private void establecerPermisosUsuario(){
        if(usuario != null){
            for(OpcionesMenuGrupo opcion: usuario.getIdGrupo().getOpcionesMenuGrupoList()){
                Permiso p = new Permiso();
                if(opcion.getAutIns().equals('Y')){
                    p.setInsert(true);
                }
                if(opcion.getAutUpd().equals('Y')){
                    p.setUpdate(true);
                }
                if(opcion.getAutDel().equals('Y')){
                    p.setDelete(true);
                }
                if(opcion.getCodigoOpcMenu().equals("P1")){
                    P1 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P2")){
                    P2 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P3")){
                    P3 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P4")){
                    P4 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P5")){
                    P5 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P6")){
                    P6 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P7")){
                    P7 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P8")){
                    P8 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P9")){
                    P9 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P10")){
                    P10 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P11")){
                    P11 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P12")){
                    P12 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P13")){
                    P13 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P14")){
                    P14 = p;
                }else if(opcion.getCodigoOpcMenu().equals("P15")){
                    P15 = p;
                }
            }
        }
    }
    
     ///////////////////////////////////////////////////////////////////////////////////
    // CONVERTER ////////////////////////////////////////////////////////////////////////
    @FacesConverter(forClass = Grupos.class)
    public static class GruposConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SeguridadController controller = (SeguridadController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "seguridadController");
            return controller.getJpaController().findGrupo(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Grupos) {
                Grupos o = (Grupos) object;
                return getStringKey(o.getIdGrupo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Grupos.class.getName());
            }
        }
    }
}
