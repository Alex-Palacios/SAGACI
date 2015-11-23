/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.gob.mined.uaci.entidades.Proceso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.entidades.MetodoAdq;
import sv.gob.mined.uaci.jpa.exceptions.IllegalOrphanException;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;
import sv.gob.mined.uaci.jpa.exceptions.PreexistingEntityException;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Ultima Actualizacion: 05/09/2015
 */


public class MetodoAdqJpaController implements Serializable {

    public MetodoAdqJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MetodoAdq metodoAdq) throws PreexistingEntityException, Exception {
        if (metodoAdq.getProcesoList() == null) {
            metodoAdq.setProcesoList(new ArrayList<Proceso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Proceso> attachedProcesoList = new ArrayList<Proceso>();
            for (Proceso procesoListProcesoToAttach : metodoAdq.getProcesoList()) {
                procesoListProcesoToAttach = em.getReference(procesoListProcesoToAttach.getClass(), procesoListProcesoToAttach.getId());
                attachedProcesoList.add(procesoListProcesoToAttach);
            }
            metodoAdq.setProcesoList(attachedProcesoList);
            em.persist(metodoAdq);
            for (Proceso procesoListProceso : metodoAdq.getProcesoList()) {
                MetodoAdq oldMetodoOfProcesoListProceso = procesoListProceso.getMetodo();
                procesoListProceso.setMetodo(metodoAdq);
                procesoListProceso = em.merge(procesoListProceso);
                if (oldMetodoOfProcesoListProceso != null) {
                    oldMetodoOfProcesoListProceso.getProcesoList().remove(procesoListProceso);
                    oldMetodoOfProcesoListProceso = em.merge(oldMetodoOfProcesoListProceso);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if(em.getTransaction() != null && em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void crearMetodo(String metodo){
        
    }

    public void edit(MetodoAdq metodoAdq) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MetodoAdq persistentMetodoAdq = em.find(MetodoAdq.class, metodoAdq.getId());
            List<Proceso> procesoListOld = persistentMetodoAdq.getProcesoList();
            List<Proceso> procesoListNew = metodoAdq.getProcesoList();
            List<String> illegalOrphanMessages = null;
            for (Proceso procesoListOldProceso : procesoListOld) {
                if (!procesoListNew.contains(procesoListOldProceso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunos procesos " + procesoListOldProceso + " no se encuentran vinculados.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Proceso> attachedProcesoListNew = new ArrayList<Proceso>();
            for (Proceso procesoListNewProcesoToAttach : procesoListNew) {
                procesoListNewProcesoToAttach = em.getReference(procesoListNewProcesoToAttach.getClass(), procesoListNewProcesoToAttach.getId());
                attachedProcesoListNew.add(procesoListNewProcesoToAttach);
            }
            procesoListNew = attachedProcesoListNew;
            metodoAdq.setProcesoList(procesoListNew);
            metodoAdq = em.merge(metodoAdq);
            for (Proceso procesoListNewProceso : procesoListNew) {
                if (!procesoListOld.contains(procesoListNewProceso)) {
                    MetodoAdq oldMetodoOfProcesoListNewProceso = procesoListNewProceso.getMetodo();
                    procesoListNewProceso.setMetodo(metodoAdq);
                    procesoListNewProceso = em.merge(procesoListNewProceso);
                    if (oldMetodoOfProcesoListNewProceso != null && !oldMetodoOfProcesoListNewProceso.equals(metodoAdq)) {
                        oldMetodoOfProcesoListNewProceso.getProcesoList().remove(procesoListNewProceso);
                        oldMetodoOfProcesoListNewProceso = em.merge(oldMetodoOfProcesoListNewProceso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = metodoAdq.getId();
                if (findMetodoAdq(id) == null) {
                    throw new NonexistentEntityException("El METODO DE ADQUISICION con id " + id + " no existe.");
                }
            }
            if(em.getTransaction() != null && em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MetodoAdq metodoAdq;
            try {
                metodoAdq = em.getReference(MetodoAdq.class, id);
                metodoAdq.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El METODO DE ADQUISICION con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Proceso> procesoListOrphanCheck = metodoAdq.getProcesoList();
            for (Proceso procesoListOrphanCheckProceso : procesoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Metodo de Adquisicion: " + metodoAdq + " no se puede eliminar porque hay procesos vinculados.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(metodoAdq);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MetodoAdq> findMetodoAdqEntities() {
        return findMetodoAdqEntities(true, -1, -1);
    }

    public List<MetodoAdq> findMetodoAdqEntities(int maxResults, int firstResult) {
        return findMetodoAdqEntities(false, maxResults, firstResult);
    }

    private List<MetodoAdq> findMetodoAdqEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MetodoAdq.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MetodoAdq findMetodoAdq(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MetodoAdq.class, id);
        } finally {
            em.close();
        }
    }

    public int getMetodoAdqCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MetodoAdq> rt = cq.from(MetodoAdq.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public MetodoAdq findByNombre(String metodo) {
        EntityManager em = getEntityManager();
        MetodoAdq resultado = null;
        try {
            Query consulta = em.createNamedQuery("MetodoAdq.findByNombreMetodo");
            consulta.setParameter("nombreMetodo", metodo);
            List x = consulta.getResultList();
            if(x != null && x.size() > 0){
                resultado = (MetodoAdq) x.get(0);
            }
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public MetodoAdq findByCodigo(String codigo) {
        EntityManager em = getEntityManager();
        MetodoAdq resultado = null;
        try {
            Query consulta = em.createNamedQuery("MetodoAdq.findByCodMetodo");
            consulta.setParameter("codigo", codigo);
            List x = consulta.getResultList();
            if(x != null && x.size() > 0){
                resultado = (MetodoAdq) x.get(0);
            }
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    public MetodoAdq obtenerCrearMetodo(String nombre) {
        EntityManager em = getEntityManager();
        MetodoAdq metodo = null;
        MetodoAdq repite = null;
        try {
            nombre = JsfUtil.quitaEspacios(nombre);
            metodo = findByNombre(nombre.toUpperCase());
            if(metodo == null){
                metodo = new MetodoAdq();
                String separadores = "[ .,;?!¡¿\'\"\\[\\]]+";
                String[] palabrasSeparadas = nombre.split(separadores);
                if(palabrasSeparadas.length > 1){
                    String codigo = "";
                    for(int i=0; i< palabrasSeparadas.length; i++){
                       codigo = codigo + palabrasSeparadas[i].substring(0, 1);
                    }
                    metodo.setCodigo(codigo);
                }else{
                    metodo.setCodigo(nombre.substring(0, 2));
                }
                metodo.setNombre(nombre.toUpperCase());
                repite = findByCodigo(metodo.getCodigo());
                int num = 1;
                while(repite != null){
                      metodo.setCodigo(metodo.getCodigo()+num);
                      repite = findByCodigo(metodo.getCodigo());
                }
                create(metodo);
            }
            return metodo;
        } catch(Exception ex){
            return metodo;
        }finally {
            em.close();
        }
    }
    
    
}
