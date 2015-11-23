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
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.Proveedor;
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


public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, Exception {
        if (proveedor.getGarantiaList() == null) {
            proveedor.setGarantiaList(new ArrayList<Garantia>());
        }
        if (proveedor.getContratoList() == null) {
            proveedor.setContratoList(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Garantia> attachedGarantiaList = new ArrayList<Garantia>();
            for (Garantia garantiaListGarantiaToAttach : proveedor.getGarantiaList()) {
                garantiaListGarantiaToAttach = em.getReference(garantiaListGarantiaToAttach.getClass(), garantiaListGarantiaToAttach.getId());
                attachedGarantiaList.add(garantiaListGarantiaToAttach);
            }
            proveedor.setGarantiaList(attachedGarantiaList);
            List<Contrato> attachedContratoList = new ArrayList<Contrato>();
            for (Contrato contratoListContratoToAttach : proveedor.getContratoList()) {
                contratoListContratoToAttach = em.getReference(contratoListContratoToAttach.getClass(), contratoListContratoToAttach.getId());
                attachedContratoList.add(contratoListContratoToAttach);
            }
            proveedor.setContratoList(attachedContratoList);
            em.persist(proveedor);
            for (Garantia garantiaListGarantia : proveedor.getGarantiaList()) {
                Proveedor oldProveedorOfGarantiaListGarantia = garantiaListGarantia.getProveedor();
                garantiaListGarantia.setProveedor(proveedor);
                garantiaListGarantia = em.merge(garantiaListGarantia);
                if (oldProveedorOfGarantiaListGarantia != null) {
                    oldProveedorOfGarantiaListGarantia.getGarantiaList().remove(garantiaListGarantia);
                    oldProveedorOfGarantiaListGarantia = em.merge(oldProveedorOfGarantiaListGarantia);
                }
            }
            for (Contrato contratoListContrato : proveedor.getContratoList()) {
                Proveedor oldContratistaOfContratoListContrato = contratoListContrato.getContratista();
                contratoListContrato.setContratista(proveedor);
                contratoListContrato = em.merge(contratoListContrato);
                if (oldContratistaOfContratoListContrato != null) {
                    oldContratistaOfContratoListContrato.getContratoList().remove(contratoListContrato);
                    oldContratistaOfContratoListContrato = em.merge(oldContratistaOfContratoListContrato);
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

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getId());
            List<Garantia> garantiaListOld = persistentProveedor.getGarantiaList();
            List<Garantia> garantiaListNew = proveedor.getGarantiaList();
            List<Contrato> contratoListOld = persistentProveedor.getContratoList();
            List<Contrato> contratoListNew = proveedor.getContratoList();
            List<String> illegalOrphanMessages = null;
            for (Garantia garantiaListOldGarantia : garantiaListOld) {
                if (!garantiaListNew.contains(garantiaListOldGarantia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunas garantias " + garantiaListOldGarantia + " no se encuentran vinculadas.");
                }
            }
            for (Contrato contratoListOldContrato : contratoListOld) {
                if (!contratoListNew.contains(contratoListOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunos contratos " + contratoListOldContrato + " no se encuentran vinculados.");
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
            proveedor.setGarantiaList(garantiaListNew);
            List<Contrato> attachedContratoListNew = new ArrayList<Contrato>();
            for (Contrato contratoListNewContratoToAttach : contratoListNew) {
                contratoListNewContratoToAttach = em.getReference(contratoListNewContratoToAttach.getClass(), contratoListNewContratoToAttach.getId());
                attachedContratoListNew.add(contratoListNewContratoToAttach);
            }
            contratoListNew = attachedContratoListNew;
            proveedor.setContratoList(contratoListNew);
            proveedor = em.merge(proveedor);
            for (Garantia garantiaListNewGarantia : garantiaListNew) {
                if (!garantiaListOld.contains(garantiaListNewGarantia)) {
                    Proveedor oldProveedorOfGarantiaListNewGarantia = garantiaListNewGarantia.getProveedor();
                    garantiaListNewGarantia.setProveedor(proveedor);
                    garantiaListNewGarantia = em.merge(garantiaListNewGarantia);
                    if (oldProveedorOfGarantiaListNewGarantia != null && !oldProveedorOfGarantiaListNewGarantia.equals(proveedor)) {
                        oldProveedorOfGarantiaListNewGarantia.getGarantiaList().remove(garantiaListNewGarantia);
                        oldProveedorOfGarantiaListNewGarantia = em.merge(oldProveedorOfGarantiaListNewGarantia);
                    }
                }
            }
            for (Contrato contratoListNewContrato : contratoListNew) {
                if (!contratoListOld.contains(contratoListNewContrato)) {
                    Proveedor oldContratistaOfContratoListNewContrato = contratoListNewContrato.getContratista();
                    contratoListNewContrato.setContratista(proveedor);
                    contratoListNewContrato = em.merge(contratoListNewContrato);
                    if (oldContratistaOfContratoListNewContrato != null && !oldContratistaOfContratoListNewContrato.equals(proveedor)) {
                        oldContratistaOfContratoListNewContrato.getContratoList().remove(contratoListNewContrato);
                        oldContratistaOfContratoListNewContrato = em.merge(oldContratistaOfContratoListNewContrato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedor.getId();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("El PROVEEDOR con id " + id + " no existe.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El PROVEEDOR con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Garantia> garantiaListOrphanCheck = proveedor.getGarantiaList();
            for (Garantia garantiaListOrphanCheckGarantia : garantiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Proveedor: " + proveedor + ") no se puede eliminar tiene garantias vinculados.");
            }
            List<Contrato> contratoListOrphanCheck = proveedor.getContratoList();
            for (Contrato contratoListOrphanCheckContrato : contratoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Proveedor: " + proveedor + " no se puede eliminar porque tiene contratos vinculados");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public Proveedor findByNombre(String proveedor) {
        EntityManager em = getEntityManager();
        Proveedor resultado = null;
        try {
            Query consulta = em.createNamedQuery("Proveedor.findByNombreProveedor");
            consulta.setParameter("nombreProveedor", proveedor);
            resultado = (Proveedor) consulta.getResultList().get(0);
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public Proveedor obtenerCrearProveedor(String nombre) {
        EntityManager em = getEntityManager();
        Proveedor proveedor = null;
        try {
            proveedor = findByNombre(nombre.toUpperCase());
            if(proveedor == null){
                proveedor = new Proveedor();
                proveedor.setNombre(nombre.toUpperCase());
                proveedor.setNaturaleza(2);
                create(proveedor);
            }
            return proveedor;
        } catch(Exception ex){
            return proveedor;
        }finally {
            em.close();
        }
    }
    
}
