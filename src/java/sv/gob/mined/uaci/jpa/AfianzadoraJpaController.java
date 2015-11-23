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
import sv.gob.mined.uaci.entidades.Afianzadora;
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
public class AfianzadoraJpaController implements Serializable {

    public AfianzadoraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Afianzadora afianzadora) throws PreexistingEntityException, Exception {
        if (afianzadora.getGarantiaList() == null) {
            afianzadora.setGarantiaList(new ArrayList<Garantia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Garantia> attachedGarantiaList = new ArrayList<Garantia>();
            for (Garantia garantiaListGarantiaToAttach : afianzadora.getGarantiaList()) {
                garantiaListGarantiaToAttach = em.getReference(garantiaListGarantiaToAttach.getClass(), garantiaListGarantiaToAttach.getId());
                attachedGarantiaList.add(garantiaListGarantiaToAttach);
            }
            afianzadora.setGarantiaList(attachedGarantiaList);
            em.persist(afianzadora);
            for (Garantia garantiaListGarantia : afianzadora.getGarantiaList()) {
                Afianzadora oldAfianzadoraOfGarantiaListGarantia = garantiaListGarantia.getAfianzadora();
                garantiaListGarantia.setAfianzadora(afianzadora);
                garantiaListGarantia = em.merge(garantiaListGarantia);
                if (oldAfianzadoraOfGarantiaListGarantia != null) {
                    oldAfianzadoraOfGarantiaListGarantia.getGarantiaList().remove(garantiaListGarantia);
                    oldAfianzadoraOfGarantiaListGarantia = em.merge(oldAfianzadoraOfGarantiaListGarantia);
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

    public void edit(Afianzadora afianzadora) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Afianzadora persistentAfianzadora = em.find(Afianzadora.class, afianzadora.getId());
            List<Garantia> garantiaListOld = persistentAfianzadora.getGarantiaList();
            List<Garantia> garantiaListNew = afianzadora.getGarantiaList();
            List<String> illegalOrphanMessages = null;
            for (Garantia garantiaListOldGarantia : garantiaListOld) {
                if (!garantiaListNew.contains(garantiaListOldGarantia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunas garantias " + garantiaListOldGarantia + " no se encuantran vinculadas.");
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
            afianzadora.setGarantiaList(garantiaListNew);
            afianzadora = em.merge(afianzadora);
            for (Garantia garantiaListNewGarantia : garantiaListNew) {
                if (!garantiaListOld.contains(garantiaListNewGarantia)) {
                    Afianzadora oldAfianzadoraOfGarantiaListNewGarantia = garantiaListNewGarantia.getAfianzadora();
                    garantiaListNewGarantia.setAfianzadora(afianzadora);
                    garantiaListNewGarantia = em.merge(garantiaListNewGarantia);
                    if (oldAfianzadoraOfGarantiaListNewGarantia != null && !oldAfianzadoraOfGarantiaListNewGarantia.equals(afianzadora)) {
                        oldAfianzadoraOfGarantiaListNewGarantia.getGarantiaList().remove(garantiaListNewGarantia);
                        oldAfianzadoraOfGarantiaListNewGarantia = em.merge(oldAfianzadoraOfGarantiaListNewGarantia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = afianzadora.getId();
                if (findAfianzadora(id) == null) {
                    throw new NonexistentEntityException("La AFIANZADORA con id " + id + " no existe.");
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
            Afianzadora afianzadora;
            try {
                afianzadora = em.getReference(Afianzadora.class, id);
                afianzadora.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La AFIANZADORA con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Garantia> garantiaListOrphanCheck = afianzadora.getGarantiaList();
            for (Garantia garantiaListOrphanCheckGarantia : garantiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("La Afianzadora: " + afianzadora + " no se puede eliminar porque hay garantias vinculadas");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(afianzadora);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Afianzadora> findAfianzadoraEntities() {
        return findAfianzadoraEntities(true, -1, -1);
    }

    public List<Afianzadora> findAfianzadoraEntities(int maxResults, int firstResult) {
        return findAfianzadoraEntities(false, maxResults, firstResult);
    }

    private List<Afianzadora> findAfianzadoraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Afianzadora.class));
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

    public Afianzadora findAfianzadora(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Afianzadora.class, id);
        } finally {
            em.close();
        }
    }

    public int getAfianzadoraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Afianzadora> rt = cq.from(Afianzadora.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public Afianzadora findByNombre(String nombre) {
        EntityManager em = getEntityManager();
        Afianzadora resultado = null;
        try {
            Query consulta = em.createNamedQuery("Afianzadora.findByNombreAfianzadora");
            consulta.setParameter("nombreAfianzadora", nombre);
            resultado = (Afianzadora)  consulta.getResultList().get(0);
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public Afianzadora obtenerCrearAfianzadora(String nombre) {
        EntityManager em = getEntityManager();
        Afianzadora afianzadora = null;
        try {
            afianzadora = findByNombre(nombre.toUpperCase());
            if(afianzadora == null){
                afianzadora = new Afianzadora();
                afianzadora.setNombre(nombre.toUpperCase());
                create(afianzadora);
            }
            return afianzadora;
        } catch(Exception ex){
            return afianzadora;
        }finally {
            em.close();
        }
    }
    
    
}
