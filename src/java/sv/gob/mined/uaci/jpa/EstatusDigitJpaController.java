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
import sv.gob.mined.uaci.entidades.EstatusDigit;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;
import sv.gob.mined.uaci.jpa.exceptions.PreexistingEntityException;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Ultima Actualizacion: 05/09/2015
 */

public class EstatusDigitJpaController implements Serializable {

    public EstatusDigitJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstatusDigit estatusDigit) throws PreexistingEntityException, Exception {
        if (estatusDigit.getArchivoList() == null) {
            estatusDigit.setArchivoList(new ArrayList<Archivo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Archivo> attachedArchivoList = new ArrayList<Archivo>();
            for (Archivo archivoListArchivoToAttach : estatusDigit.getArchivoList()) {
                archivoListArchivoToAttach = em.getReference(archivoListArchivoToAttach.getClass(), archivoListArchivoToAttach.getId());
                attachedArchivoList.add(archivoListArchivoToAttach);
            }
            estatusDigit.setArchivoList(attachedArchivoList);
            em.persist(estatusDigit);
            for (Archivo archivoListArchivo : estatusDigit.getArchivoList()) {
                EstatusDigit oldEstatusDigitOfArchivoListArchivo = archivoListArchivo.getEstatusDigit();
                archivoListArchivo.setEstatusDigit(estatusDigit);
                archivoListArchivo = em.merge(archivoListArchivo);
                if (oldEstatusDigitOfArchivoListArchivo != null) {
                    oldEstatusDigitOfArchivoListArchivo.getArchivoList().remove(archivoListArchivo);
                    oldEstatusDigitOfArchivoListArchivo = em.merge(oldEstatusDigitOfArchivoListArchivo);
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

    public void edit(EstatusDigit estatusDigit) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstatusDigit persistentEstatusDigit = em.find(EstatusDigit.class, estatusDigit.getId());
            List<Archivo> archivoListOld = persistentEstatusDigit.getArchivoList();
            List<Archivo> archivoListNew = estatusDigit.getArchivoList();
            List<Archivo> attachedArchivoListNew = new ArrayList<Archivo>();
            for (Archivo archivoListNewArchivoToAttach : archivoListNew) {
                archivoListNewArchivoToAttach = em.getReference(archivoListNewArchivoToAttach.getClass(), archivoListNewArchivoToAttach.getId());
                attachedArchivoListNew.add(archivoListNewArchivoToAttach);
            }
            archivoListNew = attachedArchivoListNew;
            estatusDigit.setArchivoList(archivoListNew);
            estatusDigit = em.merge(estatusDigit);
            for (Archivo archivoListOldArchivo : archivoListOld) {
                if (!archivoListNew.contains(archivoListOldArchivo)) {
                    archivoListOldArchivo.setEstatusDigit(null);
                    archivoListOldArchivo = em.merge(archivoListOldArchivo);
                }
            }
            for (Archivo archivoListNewArchivo : archivoListNew) {
                if (!archivoListOld.contains(archivoListNewArchivo)) {
                    EstatusDigit oldEstatusDigitOfArchivoListNewArchivo = archivoListNewArchivo.getEstatusDigit();
                    archivoListNewArchivo.setEstatusDigit(estatusDigit);
                    archivoListNewArchivo = em.merge(archivoListNewArchivo);
                    if (oldEstatusDigitOfArchivoListNewArchivo != null && !oldEstatusDigitOfArchivoListNewArchivo.equals(estatusDigit)) {
                        oldEstatusDigitOfArchivoListNewArchivo.getArchivoList().remove(archivoListNewArchivo);
                        oldEstatusDigitOfArchivoListNewArchivo = em.merge(oldEstatusDigitOfArchivoListNewArchivo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estatusDigit.getId();
                if (findEstatusDigit(id) == null) {
                    throw new NonexistentEntityException("El ESTADO DE DIGITALIZACION con id " + id + " no existe.");
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
            EstatusDigit estatusDigit;
            try {
                estatusDigit = em.getReference(EstatusDigit.class, id);
                estatusDigit.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El ESTADO DE DIGITALIZACION con id " + id + " no existe.", enfe);
            }
            List<Archivo> archivoList = estatusDigit.getArchivoList();
            for (Archivo archivoListArchivo : archivoList) {
                archivoListArchivo.setEstatusDigit(null);
                archivoListArchivo = em.merge(archivoListArchivo);
            }
            em.remove(estatusDigit);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstatusDigit> findEstatusDigitEntities() {
        return findEstatusDigitEntities(true, -1, -1);
    }

    public List<EstatusDigit> findEstatusDigitEntities(int maxResults, int firstResult) {
        return findEstatusDigitEntities(false, maxResults, firstResult);
    }

    private List<EstatusDigit> findEstatusDigitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstatusDigit.class));
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

    public EstatusDigit findEstatusDigit(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstatusDigit.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstatusDigitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstatusDigit> rt = cq.from(EstatusDigit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
