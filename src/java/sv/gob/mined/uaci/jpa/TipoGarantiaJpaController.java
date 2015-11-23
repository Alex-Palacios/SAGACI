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
import sv.gob.mined.uaci.entidades.Garantia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.gob.mined.uaci.entidades.TipoGarantia;
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

public class TipoGarantiaJpaController implements Serializable {

    public TipoGarantiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoGarantia tipoGarantia) throws PreexistingEntityException, Exception {
        if (tipoGarantia.getGarantiaList() == null) {
            tipoGarantia.setGarantiaList(new ArrayList<Garantia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Garantia> attachedGarantiaList = new ArrayList<Garantia>();
            for (Garantia garantiaListGarantiaToAttach : tipoGarantia.getGarantiaList()) {
                garantiaListGarantiaToAttach = em.getReference(garantiaListGarantiaToAttach.getClass(), garantiaListGarantiaToAttach.getId());
                attachedGarantiaList.add(garantiaListGarantiaToAttach);
            }
            tipoGarantia.setGarantiaList(attachedGarantiaList);
            em.persist(tipoGarantia);
            for (Garantia garantiaListGarantia : tipoGarantia.getGarantiaList()) {
                TipoGarantia oldTipoOfGarantiaListGarantia = garantiaListGarantia.getTipo();
                garantiaListGarantia.setTipo(tipoGarantia);
                garantiaListGarantia = em.merge(garantiaListGarantia);
                if (oldTipoOfGarantiaListGarantia != null) {
                    oldTipoOfGarantiaListGarantia.getGarantiaList().remove(garantiaListGarantia);
                    oldTipoOfGarantiaListGarantia = em.merge(oldTipoOfGarantiaListGarantia);
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

    public void edit(TipoGarantia tipoGarantia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoGarantia persistentTipoGarantia = em.find(TipoGarantia.class, tipoGarantia.getId());
            List<Garantia> garantiaListOld = persistentTipoGarantia.getGarantiaList();
            List<Garantia> garantiaListNew = tipoGarantia.getGarantiaList();
            List<String> illegalOrphanMessages = null;
            for (Garantia garantiaListOldGarantia : garantiaListOld) {
                if (!garantiaListNew.contains(garantiaListOldGarantia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunas garantias " + garantiaListOldGarantia + " no se encuentran vinculadas.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Garantia> attachedGarantiaListNew = new ArrayList<Garantia>();
            for (Garantia garantiaListNewGarantiaToAttach : garantiaListNew) {
                garantiaListNewGarantiaToAttach = em.getReference(garantiaListNewGarantiaToAttach.getClass(), garantiaListNewGarantiaToAttach.getId());
                attachedGarantiaListNew.add(garantiaListNewGarantiaToAttach);
            }
            garantiaListNew = attachedGarantiaListNew;
            tipoGarantia.setGarantiaList(garantiaListNew);
            tipoGarantia = em.merge(tipoGarantia);
            for (Garantia garantiaListNewGarantia : garantiaListNew) {
                if (!garantiaListOld.contains(garantiaListNewGarantia)) {
                    TipoGarantia oldTipoOfGarantiaListNewGarantia = garantiaListNewGarantia.getTipo();
                    garantiaListNewGarantia.setTipo(tipoGarantia);
                    garantiaListNewGarantia = em.merge(garantiaListNewGarantia);
                    if (oldTipoOfGarantiaListNewGarantia != null && !oldTipoOfGarantiaListNewGarantia.equals(tipoGarantia)) {
                        oldTipoOfGarantiaListNewGarantia.getGarantiaList().remove(garantiaListNewGarantia);
                        oldTipoOfGarantiaListNewGarantia = em.merge(oldTipoOfGarantiaListNewGarantia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoGarantia.getId();
                if (findTipoGarantia(id) == null) {
                    throw new NonexistentEntityException("El TIPO DE GARANTIA con id " + id + " no existe.");
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
            TipoGarantia tipoGarantia;
            try {
                tipoGarantia = em.getReference(TipoGarantia.class, id);
                tipoGarantia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El TIPO DE GARANTIA con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Garantia> garantiaListOrphanCheck = tipoGarantia.getGarantiaList();
            for (Garantia garantiaListOrphanCheckGarantia : garantiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Tipo de Garantia: " + tipoGarantia + " no se puede eliminar porque tiene garantias vinculadas.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoGarantia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoGarantia> findTipoGarantiaEntities() {
        return findTipoGarantiaEntities(true, -1, -1);
    }

    public List<TipoGarantia> findTipoGarantiaEntities(int maxResults, int firstResult) {
        return findTipoGarantiaEntities(false, maxResults, firstResult);
    }

    private List<TipoGarantia> findTipoGarantiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoGarantia.class));
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

    public TipoGarantia findTipoGarantia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoGarantia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoGarantiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoGarantia> rt = cq.from(TipoGarantia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public TipoGarantia findByNombre(String tipo) {
        EntityManager em = getEntityManager();
        TipoGarantia resultado = null;
        try {
            Query consulta = em.createNamedQuery("TipoGarantia.findByNombreTipoGarantia");
            consulta.setParameter("nombreTipo", tipo);
            resultado = (TipoGarantia) consulta.getResultList().get(0);
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public TipoGarantia obtenerCrearTipo(String nombre) {
        EntityManager em = getEntityManager();
        TipoGarantia tipo = null;
        try {
            tipo = findByNombre(nombre.toUpperCase());
            if(tipo == null){
                tipo = new TipoGarantia();
                tipo.setNombreTipo(nombre.toUpperCase());
                tipo.setLigadoAContrato(true);
                create(tipo);
            }
            return tipo;
        } catch(Exception ex){
            return tipo;
        }finally {
            em.close();
        }
    }
    
    
}
