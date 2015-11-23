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
import sv.gob.mined.uaci.entidades.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.gob.mined.uaci.entidades.TipoPago;
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

public class TipoPagoJpaController implements Serializable {

    public TipoPagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPago tipoPago) throws PreexistingEntityException, Exception {
        if (tipoPago.getPagoList() == null) {
            tipoPago.setPagoList(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : tipoPago.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            tipoPago.setPagoList(attachedPagoList);
            em.persist(tipoPago);
            for (Pago pagoListPago : tipoPago.getPagoList()) {
                TipoPago oldTipoOfPagoListPago = pagoListPago.getTipo();
                pagoListPago.setTipo(tipoPago);
                pagoListPago = em.merge(pagoListPago);
                if (oldTipoOfPagoListPago != null) {
                    oldTipoOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldTipoOfPagoListPago = em.merge(oldTipoOfPagoListPago);
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

    public void edit(TipoPago tipoPago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPago persistentTipoPago = em.find(TipoPago.class, tipoPago.getId());
            List<Pago> pagoListOld = persistentTipoPago.getPagoList();
            List<Pago> pagoListNew = tipoPago.getPagoList();
            List<String> illegalOrphanMessages = null;
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunos pagos " + pagoListOldPago + " no se encuentran vinculados.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            tipoPago.setPagoList(pagoListNew);
            tipoPago = em.merge(tipoPago);
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    TipoPago oldTipoOfPagoListNewPago = pagoListNewPago.getTipo();
                    pagoListNewPago.setTipo(tipoPago);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldTipoOfPagoListNewPago != null && !oldTipoOfPagoListNewPago.equals(tipoPago)) {
                        oldTipoOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldTipoOfPagoListNewPago = em.merge(oldTipoOfPagoListNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoPago.getId();
                if (findTipoPago(id) == null) {
                    throw new NonexistentEntityException("El TIPO DE PAGO con id " + id + " no existe.");
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
            TipoPago tipoPago;
            try {
                tipoPago = em.getReference(TipoPago.class, id);
                tipoPago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El TIPO DE PAGO con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pago> pagoListOrphanCheck = tipoPago.getPagoList();
            for (Pago pagoListOrphanCheckPago : pagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El tipo de pago (" + tipoPago + ") no se puede eliminar porque tiene pagos vinculados ");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPago> findTipoPagoEntities() {
        return findTipoPagoEntities(true, -1, -1);
    }

    public List<TipoPago> findTipoPagoEntities(int maxResults, int firstResult) {
        return findTipoPagoEntities(false, maxResults, firstResult);
    }

    private List<TipoPago> findTipoPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPago.class));
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

    public TipoPago findTipoPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPago.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPago> rt = cq.from(TipoPago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
