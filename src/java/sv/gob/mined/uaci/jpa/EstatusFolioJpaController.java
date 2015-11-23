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
import sv.gob.mined.uaci.entidades.Archivo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.gob.mined.uaci.entidades.EstatusFolio;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;
import sv.gob.mined.uaci.jpa.exceptions.PreexistingEntityException;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Ultima Actualizacion: 05/09/2015
 */


public class EstatusFolioJpaController implements Serializable {

    public EstatusFolioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstatusFolio estatusFolio) throws PreexistingEntityException, Exception {
        if (estatusFolio.getArchivoList() == null) {
            estatusFolio.setArchivoList(new ArrayList<Archivo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Archivo> attachedArchivoList = new ArrayList<Archivo>();
            for (Archivo archivoListArchivoToAttach : estatusFolio.getArchivoList()) {
                archivoListArchivoToAttach = em.getReference(archivoListArchivoToAttach.getClass(), archivoListArchivoToAttach.getId());
                attachedArchivoList.add(archivoListArchivoToAttach);
            }
            estatusFolio.setArchivoList(attachedArchivoList);
            em.persist(estatusFolio);
            for (Archivo archivoListArchivo : estatusFolio.getArchivoList()) {
                EstatusFolio oldEstatusFolioOfArchivoListArchivo = archivoListArchivo.getEstatusFolio();
                archivoListArchivo.setEstatusFolio(estatusFolio);
                archivoListArchivo = em.merge(archivoListArchivo);
                if (oldEstatusFolioOfArchivoListArchivo != null) {
                    oldEstatusFolioOfArchivoListArchivo.getArchivoList().remove(archivoListArchivo);
                    oldEstatusFolioOfArchivoListArchivo = em.merge(oldEstatusFolioOfArchivoListArchivo);
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

    public void edit(EstatusFolio estatusFolio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstatusFolio persistentEstatusFolio = em.find(EstatusFolio.class, estatusFolio.getId());
            List<Archivo> archivoListOld = persistentEstatusFolio.getArchivoList();
            List<Archivo> archivoListNew = estatusFolio.getArchivoList();
            List<Archivo> attachedArchivoListNew = new ArrayList<Archivo>();
            for (Archivo archivoListNewArchivoToAttach : archivoListNew) {
                archivoListNewArchivoToAttach = em.getReference(archivoListNewArchivoToAttach.getClass(), archivoListNewArchivoToAttach.getId());
                attachedArchivoListNew.add(archivoListNewArchivoToAttach);
            }
            archivoListNew = attachedArchivoListNew;
            estatusFolio.setArchivoList(archivoListNew);
            estatusFolio = em.merge(estatusFolio);
            for (Archivo archivoListOldArchivo : archivoListOld) {
                if (!archivoListNew.contains(archivoListOldArchivo)) {
                    archivoListOldArchivo.setEstatusFolio(null);
                    archivoListOldArchivo = em.merge(archivoListOldArchivo);
                }
            }
            for (Archivo archivoListNewArchivo : archivoListNew) {
                if (!archivoListOld.contains(archivoListNewArchivo)) {
                    EstatusFolio oldEstatusFolioOfArchivoListNewArchivo = archivoListNewArchivo.getEstatusFolio();
                    archivoListNewArchivo.setEstatusFolio(estatusFolio);
                    archivoListNewArchivo = em.merge(archivoListNewArchivo);
                    if (oldEstatusFolioOfArchivoListNewArchivo != null && !oldEstatusFolioOfArchivoListNewArchivo.equals(estatusFolio)) {
                        oldEstatusFolioOfArchivoListNewArchivo.getArchivoList().remove(archivoListNewArchivo);
                        oldEstatusFolioOfArchivoListNewArchivo = em.merge(oldEstatusFolioOfArchivoListNewArchivo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estatusFolio.getId();
                if (findEstatusFolio(id) == null) {
                    throw new NonexistentEntityException("El ESTADO DE FOLIO con id " + id + " no existe.");
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstatusFolio estatusFolio;
            try {
                estatusFolio = em.getReference(EstatusFolio.class, id);
                estatusFolio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El ESTADO DE FOLIO con id " + id + " no existe.", enfe);
            }
            List<Archivo> archivoList = estatusFolio.getArchivoList();
            for (Archivo archivoListArchivo : archivoList) {
                archivoListArchivo.setEstatusFolio(null);
                archivoListArchivo = em.merge(archivoListArchivo);
            }
            em.remove(estatusFolio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstatusFolio> findEstatusFolioEntities() {
        return findEstatusFolioEntities(true, -1, -1);
    }

    public List<EstatusFolio> findEstatusFolioEntities(int maxResults, int firstResult) {
        return findEstatusFolioEntities(false, maxResults, firstResult);
    }

    private List<EstatusFolio> findEstatusFolioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstatusFolio.class));
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

    public EstatusFolio findEstatusFolio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstatusFolio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstatusFolioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstatusFolio> rt = cq.from(EstatusFolio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
