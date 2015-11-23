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
import sv.gob.mined.uaci.entidades.FuenteFinanc;
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

public class FuenteFinancJpaController implements Serializable {

    public FuenteFinancJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FuenteFinanc fuenteFinanc) throws PreexistingEntityException, Exception {
        if (fuenteFinanc.getContratoList() == null) {
            fuenteFinanc.setContratoList(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Contrato> attachedContratoList = new ArrayList<Contrato>();
            for (Contrato contratoListContratoToAttach : fuenteFinanc.getContratoList()) {
                contratoListContratoToAttach = em.getReference(contratoListContratoToAttach.getClass(), contratoListContratoToAttach.getId());
                attachedContratoList.add(contratoListContratoToAttach);
            }
            fuenteFinanc.setContratoList(attachedContratoList);
            em.persist(fuenteFinanc);
            for (Contrato contratoListContrato : fuenteFinanc.getContratoList()) {
                FuenteFinanc oldFuenteOfContratoListContrato = contratoListContrato.getFuente();
                contratoListContrato.setFuente(fuenteFinanc);
                contratoListContrato = em.merge(contratoListContrato);
                if (oldFuenteOfContratoListContrato != null) {
                    oldFuenteOfContratoListContrato.getContratoList().remove(contratoListContrato);
                    oldFuenteOfContratoListContrato = em.merge(oldFuenteOfContratoListContrato);
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

    public void edit(FuenteFinanc fuenteFinanc) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FuenteFinanc persistentFuenteFinanc = em.find(FuenteFinanc.class, fuenteFinanc.getId());
            List<Contrato> contratoListOld = persistentFuenteFinanc.getContratoList();
            List<Contrato> contratoListNew = fuenteFinanc.getContratoList();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoListOldContrato : contratoListOld) {
                if (!contratoListNew.contains(contratoListOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Alunos contratos " + contratoListOldContrato + " no se encuentran vincuados.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Contrato> attachedContratoListNew = new ArrayList<Contrato>();
            for (Contrato contratoListNewContratoToAttach : contratoListNew) {
                contratoListNewContratoToAttach = em.getReference(contratoListNewContratoToAttach.getClass(), contratoListNewContratoToAttach.getId());
                attachedContratoListNew.add(contratoListNewContratoToAttach);
            }
            contratoListNew = attachedContratoListNew;
            fuenteFinanc.setContratoList(contratoListNew);
            fuenteFinanc = em.merge(fuenteFinanc);
            for (Contrato contratoListNewContrato : contratoListNew) {
                if (!contratoListOld.contains(contratoListNewContrato)) {
                    FuenteFinanc oldFuenteOfContratoListNewContrato = contratoListNewContrato.getFuente();
                    contratoListNewContrato.setFuente(fuenteFinanc);
                    contratoListNewContrato = em.merge(contratoListNewContrato);
                    if (oldFuenteOfContratoListNewContrato != null && !oldFuenteOfContratoListNewContrato.equals(fuenteFinanc)) {
                        oldFuenteOfContratoListNewContrato.getContratoList().remove(contratoListNewContrato);
                        oldFuenteOfContratoListNewContrato = em.merge(oldFuenteOfContratoListNewContrato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fuenteFinanc.getId();
                if (findFuenteFinanc(id) == null) {
                    throw new NonexistentEntityException("La FUENTE DE FINANCIAMIENTO con id " + id + " no existe.");
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
            FuenteFinanc fuenteFinanc;
            try {
                fuenteFinanc = em.getReference(FuenteFinanc.class, id);
                fuenteFinanc.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La FUENTE DE FINANCIAMIENTO con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Contrato> contratoListOrphanCheck = fuenteFinanc.getContratoList();
            for (Contrato contratoListOrphanCheckContrato : contratoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("La Fuente de Financimiento (" + fuenteFinanc + ") no se puede eliminar porque tiene contratos vinculados.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(fuenteFinanc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FuenteFinanc> findFuenteFinancEntities() {
        return findFuenteFinancEntities(true, -1, -1);
    }

    public List<FuenteFinanc> findFuenteFinancEntities(int maxResults, int firstResult) {
        return findFuenteFinancEntities(false, maxResults, firstResult);
    }

    private List<FuenteFinanc> findFuenteFinancEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FuenteFinanc.class));
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

    public FuenteFinanc findFuenteFinanc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FuenteFinanc.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuenteFinancCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FuenteFinanc> rt = cq.from(FuenteFinanc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
