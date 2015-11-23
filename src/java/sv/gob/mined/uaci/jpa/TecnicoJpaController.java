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
import sv.gob.mined.uaci.entidades.Tecnico;
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

public class TecnicoJpaController implements Serializable {

    public TecnicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tecnico tecnico) throws PreexistingEntityException, Exception {
        if (tecnico.getProcesoList() == null) {
            tecnico.setProcesoList(new ArrayList<Proceso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Proceso> attachedProcesoList = new ArrayList<Proceso>();
            for (Proceso procesoListProcesoToAttach : tecnico.getProcesoList()) {
                procesoListProcesoToAttach = em.getReference(procesoListProcesoToAttach.getClass(), procesoListProcesoToAttach.getId());
                attachedProcesoList.add(procesoListProcesoToAttach);
            }
            tecnico.setProcesoList(attachedProcesoList);
            em.persist(tecnico);
            for (Proceso procesoListProceso : tecnico.getProcesoList()) {
                Tecnico oldTecnicoOfProcesoListProceso = procesoListProceso.getTecnico();
                procesoListProceso.setTecnico(tecnico);
                procesoListProceso = em.merge(procesoListProceso);
                if (oldTecnicoOfProcesoListProceso != null) {
                    oldTecnicoOfProcesoListProceso.getProcesoList().remove(procesoListProceso);
                    oldTecnicoOfProcesoListProceso = em.merge(oldTecnicoOfProcesoListProceso);
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
    

    public void edit(Tecnico tecnico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tecnico persistentTecnico = em.find(Tecnico.class, tecnico.getId());
            List<Proceso> procesoListOld = persistentTecnico.getProcesoList();
            List<Proceso> procesoListNew = tecnico.getProcesoList();
            List<String> illegalOrphanMessages = null;
            for (Proceso procesoListOldProceso : procesoListOld) {
                if (!procesoListNew.contains(procesoListOldProceso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunos procesos " + procesoListOldProceso + " no se encuentran vinculados");
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
            tecnico.setProcesoList(procesoListNew);
            tecnico = em.merge(tecnico);
            for (Proceso procesoListNewProceso : procesoListNew) {
                if (!procesoListOld.contains(procesoListNewProceso)) {
                    Tecnico oldTecnicoOfProcesoListNewProceso = procesoListNewProceso.getTecnico();
                    procesoListNewProceso.setTecnico(tecnico);
                    procesoListNewProceso = em.merge(procesoListNewProceso);
                    if (oldTecnicoOfProcesoListNewProceso != null && !oldTecnicoOfProcesoListNewProceso.equals(tecnico)) {
                        oldTecnicoOfProcesoListNewProceso.getProcesoList().remove(procesoListNewProceso);
                        oldTecnicoOfProcesoListNewProceso = em.merge(oldTecnicoOfProcesoListNewProceso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tecnico.getId();
                if (findTecnico(id) == null) {
                    throw new NonexistentEntityException("El TECNICO con id " + id + " no existe.");
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
            Tecnico tecnico;
            try {
                tecnico = em.getReference(Tecnico.class, id);
                tecnico.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El TECNICO con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Proceso> procesoListOrphanCheck = tecnico.getProcesoList();
            for (Proceso procesoListOrphanCheckProceso : procesoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Tecnico:" + tecnico + " no puede eliminarse porque tiene algunos procesos asignados ");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tecnico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tecnico> findTecnicoEntities() {
        return findTecnicoEntities(true, -1, -1);
    }

    public List<Tecnico> findTecnicoEntities(int maxResults, int firstResult) {
        return findTecnicoEntities(false, maxResults, firstResult);
    }

    private List<Tecnico> findTecnicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tecnico.class));
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

    public Tecnico findTecnico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tecnico.class, id);
        } finally {
            em.close();
        }
    }

    public int getTecnicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tecnico> rt = cq.from(Tecnico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Tecnico findByNombre(String nombre) {
        EntityManager em = getEntityManager();
        Tecnico resultado = null;
        try {
            Query consulta = em.createNamedQuery("Tecnico.findByNombre");
            consulta.setParameter("nombreTecnico", nombre);
            resultado = (Tecnico)  consulta.getResultList().get(0);
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public Tecnico obtenerCrearTecnico(String nombre) {
        EntityManager em = getEntityManager();
        Tecnico tecnico = null;
        try {
            tecnico = findByNombre(nombre.toUpperCase());
            if(tecnico == null){
                tecnico = new Tecnico();
                tecnico.setNombre(nombre.toUpperCase());
                create(tecnico);
                em.refresh(tecnico);
            }
            return tecnico;
        } catch(Exception ex){
            return tecnico;
        }finally {
            em.close();
        }
    }
    
}
