/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.entidades.TipoPago;
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.Pago;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;
import sv.gob.mined.uaci.jpa.exceptions.PreexistingEntityException;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Ultima Actualizacion: 05/09/2015
 */


public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPago tipo = pago.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getId());
                pago.setTipo(tipo);
            }
            Contrato contrato = pago.getContrato();
            if (contrato != null) {
                contrato = em.getReference(contrato.getClass(), contrato.getId());
                pago.setContrato(contrato);
            }
            //REGISTRO CONTROL
            pago = JsfUtil.ultimaModificacionT(pago);
            pago.setRegistradoPor(pago.getModificadoPor());
            pago.setFechaIngreso(pago.getFechaModificado());
            em.persist(pago);
            if (tipo != null) {
                tipo.getPagoList().add(pago);
                tipo = em.merge(tipo);
            }
            if (contrato != null) {
                contrato.getPagoList().add(pago);
                contrato = em.merge(contrato);
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

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getId());
            TipoPago tipoOld = persistentPago.getTipo();
            TipoPago tipoNew = pago.getTipo();
            Contrato contratoOld = persistentPago.getContrato();
            Contrato contratoNew = pago.getContrato();
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getId());
                pago.setTipo(tipoNew);
            }
            if (contratoNew != null) {
                contratoNew = em.getReference(contratoNew.getClass(), contratoNew.getId());
                pago.setContrato(contratoNew);
            }
            //REGISTRO CONTROL
            pago = JsfUtil.ultimaModificacionT(pago);
            pago = em.merge(pago);
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getPagoList().remove(pago);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getPagoList().add(pago);
                tipoNew = em.merge(tipoNew);
            }
            if (contratoOld != null && !contratoOld.equals(contratoNew)) {
                contratoOld.getPagoList().remove(pago);
                contratoOld = em.merge(contratoOld);
            }
            if (contratoNew != null && !contratoNew.equals(contratoOld)) {
                contratoNew.getPagoList().add(pago);
                contratoNew = em.merge(contratoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getId();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("El PAGO con id " + id + " no existe.");
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
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El PAGO con id " + id + " no existe.", enfe);
            }
            TipoPago tipo = pago.getTipo();
            if (tipo != null) {
                tipo.getPagoList().remove(pago);
                tipo = em.merge(tipo);
            }
            Contrato contrato = pago.getContrato();
            if (contrato != null) {
                contrato.getPagoList().remove(pago);
                contrato = em.merge(contrato);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<Pago> findPagosByAnio(Integer anio) {
        EntityManager em = getEntityManager();
        List<Pago> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Pago.findByAnio");
            consulta.setParameter("anio", anio);
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR PAGOS DEL "+anio);
            return resultado;
        }finally {
            em.close();
        }
    }
    
}
