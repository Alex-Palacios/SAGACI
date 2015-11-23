/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.seguridad;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author ALEX
 */
public class SeguridadJpaController implements Serializable {

    
    public SeguridadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void registrarEmpleado(Usuarios usuarios) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios u = finUser(usuarios);
            if(u == null){
                em.persist(usuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if(em.getTransaction() != null){
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    public void createUser(UsuariosAplicacion usuariosAplicacion) throws Exception {
        EntityManager em = null;
        if(usuariosAplicacion.getUserId().getNombres() != null){
            usuariosAplicacion.getUserId().setNombres(usuariosAplicacion.getUserId().getNombres().toUpperCase());
        }
        if(usuariosAplicacion.getUserId().getApellidos()!= null){
            usuariosAplicacion.getUserId().setApellidos(usuariosAplicacion.getUserId().getApellidos().toUpperCase());
        }
        if(usuariosAplicacion.getLogin() != null){
            usuariosAplicacion.setLogin(usuariosAplicacion.getLogin());
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuariosAplicacion.setPassword(usuariosAplicacion.getLogin().toLowerCase());
            usuariosAplicacion.setPassword(JsfUtil.getMD5(usuariosAplicacion.getPassword()));
            usuariosAplicacion.getUserId().setActivo('Y');
            if(usuariosAplicacion.getIdGrupo().getCodigoGrupo().equals("ADM")){
                usuariosAplicacion.getUserId().setEsAdminSys('Y');
            }else{
                usuariosAplicacion.getUserId().setEsAdminSys('N');
            }
            usuariosAplicacion.setIdAplicacion(ResourceBundle.getBundle("/Bundle").getString("ID_APP"));
            usuariosAplicacion.setCambiarPasswd('Y');
            usuariosAplicacion.setCodigoGrupo(usuariosAplicacion.getIdGrupo().getCodigoGrupo());
            registrarEmpleado(usuariosAplicacion.getUserId());
            usuariosAplicacion.setUserId(finUser(usuariosAplicacion.getUserId()));
            em.persist(usuariosAplicacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if(em.getTransaction() != null){
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void editUser(UsuariosAplicacion usuariosAplicacion) throws Exception {
        EntityManager em = null;
        if(usuariosAplicacion.getUserId().getNombres() != null){
            usuariosAplicacion.getUserId().setNombres(usuariosAplicacion.getUserId().getNombres().toUpperCase());
        }
        if(usuariosAplicacion.getUserId().getApellidos()!= null){
            usuariosAplicacion.getUserId().setApellidos(usuariosAplicacion.getUserId().getApellidos().toUpperCase());
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            if(usuariosAplicacion.getIdGrupo().getCodigoGrupo().equals("ADM")){
                usuariosAplicacion.getUserId().setEsAdminSys('Y');
            }else{
                usuariosAplicacion.getUserId().setEsAdminSys('N');
            }
            em.merge(usuariosAplicacion.getUserId());
            em.merge(usuariosAplicacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if(em.getTransaction() != null){
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    public void editUsername(UsuariosAplicacion usuariosAplicacion) throws Exception {
        EntityManager em = null;
        UsuariosAplicacion userIdOld = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            userIdOld = em.find(UsuariosAplicacion.class, usuariosAplicacion.getIdUsrAplicacion());
            usuariosAplicacion = em.merge(usuariosAplicacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if(userIdOld != null){
                usuariosAplicacion = userIdOld;
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void editPassword(UsuariosAplicacion usuariosAplicacion) throws Exception {
        EntityManager em = null;
        UsuariosAplicacion userIdOld = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            userIdOld = em.find(UsuariosAplicacion.class, usuariosAplicacion.getIdUsrAplicacion());
            usuariosAplicacion.setPassword(JsfUtil.getMD5(usuariosAplicacion.getPassword()));
            usuariosAplicacion = em.merge(usuariosAplicacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if(userIdOld != null){
                usuariosAplicacion = userIdOld;
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    public void destroyUser(UsuariosAplicacion usuariosAplicacion) throws Exception {
       EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuariosAplicacion userA;
            Usuarios userE;
            try {
                userA = em.getReference(UsuariosAplicacion.class, usuariosAplicacion.getIdUsrAplicacion());
                userE = em.getReference(Usuarios.class, usuariosAplicacion.getUserId().getUserId());
                userA.getIdUsrAplicacion();
                userE.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + usuariosAplicacion.getIdUsrAplicacion() + " no longer exists.", enfe);
            }
            em.remove(userA);
            em.remove(userE);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public UsuariosAplicacion obtenerUsuario(String username , String password){
        UsuariosAplicacion user = null;
        EntityManager em = getEntityManager();
        try {
            Query consulta = em.createNamedQuery("UsuariosAplicacion.findByLogin");
            consulta.setParameter("login", username);
            consulta.setParameter("password", JsfUtil.getMD5(password));
            consulta.setParameter("APP", ResourceBundle.getBundle("/Bundle").getString("ID_APP"));
            user = (UsuariosAplicacion)  consulta.getResultList().get(0);
            return user;
        } catch(Exception ex){
            return user;
        }finally {
            em.close();
        }
    }
    
    public List<UsuariosAplicacion> usuariosSAG() {
        EntityManager em = getEntityManager();
        List<UsuariosAplicacion> resultado = null;
        try {
            Query consulta = em.createNamedQuery("UsuariosAplicacion.findAll");
            consulta.setParameter("APP", ResourceBundle.getBundle("/Bundle").getString("ID_APP"));
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR USUARIOS DEL SISTEMA");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public List<Grupos> findTiposUsuarios() {
        EntityManager em = getEntityManager();
        List<Grupos> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Grupos.findAll");
            consulta.setParameter("APP", ResourceBundle.getBundle("/Bundle").getString("ID_APP"));
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR TIPOS DE USUARIOS DEL SISTEMA");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public boolean usernameDisponible(String username){
        EntityManager em = getEntityManager();
        boolean disponible = false;
        try {
            Query consulta = em.createNamedQuery("UsuariosAplicacion.countUsername");
            consulta.setParameter("login", username.toUpperCase());
            consulta.setParameter("APP", ResourceBundle.getBundle("/Bundle").getString("ID_APP"));
            Object r = consulta.getSingleResult();
            int resultado = 0;
            if (r != null) {
                resultado = Integer.parseInt(r.toString());
            }
            if(resultado > 0){
                disponible = false;
            }else{
                disponible = true;
            }
        } catch(Exception ex){
            return false;
        }finally {
            em.close();
            return disponible;
        }
    }
    
    public Grupos findGrupo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupos.class, id);
        } finally {
            em.close();
        }
    }

    
    public Usuarios finUser(Usuarios newUser){
        Usuarios user = null;
        EntityManager em = getEntityManager();
        try {
            Query consulta = em.createNamedQuery("Usuarios.findByUser");
            consulta.setParameter("nombre", newUser.getNombres());
            consulta.setParameter("apellido", newUser.getApellidos());
            user = (Usuarios)  consulta.getResultList().get(0);
            return user;
        } catch(Exception ex){
            return user;
        }finally {
            em.close();
        }
    }

    private Exception Exception() {
        throw new UnsupportedOperationException("Error Desconocido"); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public List<OpcionesMenuGrupo> permisosSAG(Grupos grupo) {
        EntityManager em = getEntityManager();
        List<OpcionesMenuGrupo> resultado = null;
        try {
            Query consulta = em.createNamedQuery("OpcionesMenuGrupo.findByGrupo");
            consulta.setParameter("grupo", grupo);
            consulta.setParameter("APP", ResourceBundle.getBundle("/Bundle").getString("ID_APP"));
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR PERMISOS");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    
    public void editPermiso(OpcionesMenuGrupo permiso) throws Exception {
        EntityManager em = null;
        OpcionesMenuGrupo permisoOld = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            permisoOld = em.find(OpcionesMenuGrupo.class, permiso.getIdOpcMnuGrupo());
            permiso = em.merge(permiso);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if(permisoOld != null){
                permiso = permisoOld;
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
