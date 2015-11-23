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
import sv.gob.mined.uaci.entidades.Contrato;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.gob.mined.uaci.entidades.UnidadTecnica;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;
import sv.gob.mined.uaci.jpa.exceptions.PreexistingEntityException;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Ultima Actualizacion: 05/09/2015
 */


public class UnidadTecnicaJpaController implements Serializable {

    public UnidadTecnicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UnidadTecnica unidadTecnica) throws PreexistingEntityException, Exception {
        if (unidadTecnica.getContratoList() == null) {
            unidadTecnica.setContratoList(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Contrato> attachedContratoList = new ArrayList<Contrato>();
            for (Contrato contratoListContratoToAttach : unidadTecnica.getContratoList()) {
                contratoListContratoToAttach = em.getReference(contratoListContratoToAttach.getClass(), contratoListContratoToAttach.getId());
                attachedContratoList.add(contratoListContratoToAttach);
            }
            unidadTecnica.setContratoList(attachedContratoList);
            em.persist(unidadTecnica);
            for (Contrato contratoListContrato : unidadTecnica.getContratoList()) {
                UnidadTecnica oldUnidadOfContratoListContrato = contratoListContrato.getUnidad();
                contratoListContrato.setUnidad(unidadTecnica);
                contratoListContrato = em.merge(contratoListContrato);
                if (oldUnidadOfContratoListContrato != null) {
                    oldUnidadOfContratoListContrato.getContratoList().remove(contratoListContrato);
                    oldUnidadOfContratoListContrato = em.merge(oldUnidadOfContratoListContrato);
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

    public void edit(UnidadTecnica unidadTecnica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UnidadTecnica persistentUnidadTecnica = em.find(UnidadTecnica.class, unidadTecnica.getId());
            List<Contrato> contratoListOld = persistentUnidadTecnica.getContratoList();
            List<Contrato> contratoListNew = unidadTecnica.getContratoList();
            List<Contrato> attachedContratoListNew = new ArrayList<Contrato>();
            for (Contrato contratoListNewContratoToAttach : contratoListNew) {
                contratoListNewContratoToAttach = em.getReference(contratoListNewContratoToAttach.getClass(), contratoListNewContratoToAttach.getId());
                attachedContratoListNew.add(contratoListNewContratoToAttach);
            }
            contratoListNew = attachedContratoListNew;
            unidadTecnica.setContratoList(contratoListNew);
            unidadTecnica = em.merge(unidadTecnica);
            for (Contrato contratoListOldContrato : contratoListOld) {
                if (!contratoListNew.contains(contratoListOldContrato)) {
                    contratoListOldContrato.setUnidad(null);
                    contratoListOldContrato = em.merge(contratoListOldContrato);
                }
            }
            for (Contrato contratoListNewContrato : contratoListNew) {
                if (!contratoListOld.contains(contratoListNewContrato)) {
                    UnidadTecnica oldUnidadOfContratoListNewContrato = contratoListNewContrato.getUnidad();
                    contratoListNewContrato.setUnidad(unidadTecnica);
                    contratoListNewContrato = em.merge(contratoListNewContrato);
                    if (oldUnidadOfContratoListNewContrato != null && !oldUnidadOfContratoListNewContrato.equals(unidadTecnica)) {
                        oldUnidadOfContratoListNewContrato.getContratoList().remove(contratoListNewContrato);
                        oldUnidadOfContratoListNewContrato = em.merge(oldUnidadOfContratoListNewContrato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = unidadTecnica.getId();
                if (findUnidadTecnica(id) == null) {
                    throw new NonexistentEntityException("La UNIDAD TECNICA con id " + id + " no existe.");
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
            UnidadTecnica unidadTecnica;
            try {
                unidadTecnica = em.getReference(UnidadTecnica.class, id);
                unidadTecnica.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La UNIDAD TECNICA con id " + id + " no existe.", enfe);
            }
            List<Contrato> contratoList = unidadTecnica.getContratoList();
            for (Contrato contratoListContrato : contratoList) {
                contratoListContrato.setUnidad(null);
                contratoListContrato = em.merge(contratoListContrato);
            }
            em.remove(unidadTecnica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UnidadTecnica> findUnidadTecnicaEntities() {
        return findUnidadTecnicaEntities(true, -1, -1);
    }

    public List<UnidadTecnica> findUnidadTecnicaEntities(int maxResults, int firstResult) {
        return findUnidadTecnicaEntities(false, maxResults, firstResult);
    }

    private List<UnidadTecnica> findUnidadTecnicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UnidadTecnica.class));
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

    public UnidadTecnica findUnidadTecnica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UnidadTecnica.class, id);
        } finally {
            em.close();
        }
    }

    public int getUnidadTecnicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UnidadTecnica> rt = cq.from(UnidadTecnica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
