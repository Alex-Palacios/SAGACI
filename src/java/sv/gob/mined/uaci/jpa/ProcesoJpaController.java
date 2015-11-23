/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.Garantia;
import sv.gob.mined.uaci.entidades.Tecnico;
import sv.gob.mined.uaci.entidades.MetodoAdq;
import sv.gob.mined.uaci.entidades.Proceso;
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

public class ProcesoJpaController implements Serializable {

    public ProcesoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proceso proceso) throws PreexistingEntityException, Exception {
        if (proceso.getGarantiaList() == null) {
            proceso.setGarantiaList(new ArrayList<Garantia>());
        }
        if (proceso.getContratoList() == null) {
            proceso.setContratoList(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tecnico idTecnico = proceso.getTecnico();
            if (idTecnico != null) {
                idTecnico = em.getReference(idTecnico.getClass(), idTecnico.getId());
                proceso.setTecnico(idTecnico);
            }
            MetodoAdq idMetodo = proceso.getMetodo();
            if (idMetodo != null) {
                idMetodo = em.getReference(idMetodo.getClass(), idMetodo.getId());
                proceso.setMetodo(idMetodo);
            }
            List<Garantia> attachedGarantiaList = new ArrayList<Garantia>();
            for (Garantia garantiaListGarantiaToAttach : proceso.getGarantiaList()) {
                garantiaListGarantiaToAttach = em.getReference(garantiaListGarantiaToAttach.getClass(), garantiaListGarantiaToAttach.getId());
                attachedGarantiaList.add(garantiaListGarantiaToAttach);
            }
            proceso.setGarantiaList(attachedGarantiaList);
            List<Contrato> attachedContratoList = new ArrayList<Contrato>();
            for (Contrato contratoListContratoToAttach : proceso.getContratoList()) {
                contratoListContratoToAttach = em.getReference(contratoListContratoToAttach.getClass(), contratoListContratoToAttach.getId());
                attachedContratoList.add(contratoListContratoToAttach);
            }
            proceso.setContratoList(attachedContratoList);
            //REGISTRO CONTROL
            proceso = JsfUtil.ultimaModificacionT(proceso);
            proceso.setRegistradoPor(proceso.getModificadoPor());
            proceso.setFechaIngreso(proceso.getFechaModificado());
            em.persist(proceso);
            if (idTecnico != null) {
                idTecnico.getProcesoList().add(proceso);
                idTecnico = em.merge(idTecnico);
            }
            if (idMetodo != null) {
                idMetodo.getProcesoList().add(proceso);
                idMetodo = em.merge(idMetodo);
            }
            for (Garantia garantiaListGarantia : proceso.getGarantiaList()) {
                Proceso oldIdProcesoOfGarantiaListGarantia = garantiaListGarantia.getProceso();
                garantiaListGarantia.setProceso(proceso);
                garantiaListGarantia = em.merge(garantiaListGarantia);
                if (oldIdProcesoOfGarantiaListGarantia != null) {
                    oldIdProcesoOfGarantiaListGarantia.getGarantiaList().remove(garantiaListGarantia);
                    oldIdProcesoOfGarantiaListGarantia = em.merge(oldIdProcesoOfGarantiaListGarantia);
                }
            }
            for (Contrato contratoListContrato : proceso.getContratoList()) {
                Proceso oldIdProcesoOfContratoListContrato = contratoListContrato.getProceso();
                contratoListContrato.setProceso(proceso);
                contratoListContrato = em.merge(contratoListContrato);
                if (oldIdProcesoOfContratoListContrato != null) {
                    oldIdProcesoOfContratoListContrato.getContratoList().remove(contratoListContrato);
                    oldIdProcesoOfContratoListContrato = em.merge(oldIdProcesoOfContratoListContrato);
                }
            }
            em.getTransaction().commit();
        } 
        catch (Exception ex) 
        {
            if(em.getTransaction() != null && em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw ex;
        }
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proceso proceso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proceso persistentProceso = em.find(Proceso.class, proceso.getId());
            Tecnico idTecnicoOld = persistentProceso.getTecnico();
            Tecnico idTecnicoNew = proceso.getTecnico();
            MetodoAdq idMetodoOld = persistentProceso.getMetodo();
            MetodoAdq idMetodoNew = proceso.getMetodo();
            List<Garantia> garantiaListOld = persistentProceso.getGarantiaList();
            List<Garantia> garantiaListNew = proceso.getGarantiaList();
            List<Contrato> contratoListOld = persistentProceso.getContratoList();
            List<Contrato> contratoListNew = proceso.getContratoList();
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
            if (idTecnicoNew != null) {
                idTecnicoNew = em.getReference(idTecnicoNew.getClass(), idTecnicoNew.getId());
                proceso.setTecnico(idTecnicoNew);
            }
            if (idMetodoNew != null) {
                idMetodoNew = em.getReference(idMetodoNew.getClass(), idMetodoNew.getId());
                proceso.setMetodo(idMetodoNew);
            }
            List<Garantia> attachedGarantiaListNew = new ArrayList<Garantia>();
            for (Garantia garantiaListNewGarantiaToAttach : garantiaListNew) {
                garantiaListNewGarantiaToAttach = em.getReference(garantiaListNewGarantiaToAttach.getClass(), garantiaListNewGarantiaToAttach.getId());
                attachedGarantiaListNew.add(garantiaListNewGarantiaToAttach);
            }
            garantiaListNew = attachedGarantiaListNew;
            proceso.setGarantiaList(garantiaListNew);
            List<Contrato> attachedContratoListNew = new ArrayList<Contrato>();
            for (Contrato contratoListNewContratoToAttach : contratoListNew) {
                contratoListNewContratoToAttach = em.getReference(contratoListNewContratoToAttach.getClass(), contratoListNewContratoToAttach.getId());
                attachedContratoListNew.add(contratoListNewContratoToAttach);
            }
            contratoListNew = attachedContratoListNew;
            proceso.setContratoList(contratoListNew);
            //REGISTRAR MODIFICACION
            proceso = JsfUtil.ultimaModificacionT(proceso);
            proceso = em.merge(proceso);
            if (idTecnicoOld != null && !idTecnicoOld.equals(idTecnicoNew)) {
                idTecnicoOld.getProcesoList().remove(proceso);
                idTecnicoOld = em.merge(idTecnicoOld);
            }
            if (idTecnicoNew != null && !idTecnicoNew.equals(idTecnicoOld)) {
                idTecnicoNew.getProcesoList().add(proceso);
                idTecnicoNew = em.merge(idTecnicoNew);
            }
            if (idMetodoOld != null && !idMetodoOld.equals(idMetodoNew)) {
                idMetodoOld.getProcesoList().remove(proceso);
                idMetodoOld = em.merge(idMetodoOld);
            }
            if (idMetodoNew != null && !idMetodoNew.equals(idMetodoOld)) {
                idMetodoNew.getProcesoList().add(proceso);
                idMetodoNew = em.merge(idMetodoNew);
            }
            for (Garantia garantiaListNewGarantia : garantiaListNew) {
                if (!garantiaListOld.contains(garantiaListNewGarantia)) {
                    Proceso oldIdProcesoOfGarantiaListNewGarantia = garantiaListNewGarantia.getProceso();
                    garantiaListNewGarantia.setProceso(proceso);
                    garantiaListNewGarantia = em.merge(garantiaListNewGarantia);
                    if (oldIdProcesoOfGarantiaListNewGarantia != null && !oldIdProcesoOfGarantiaListNewGarantia.equals(proceso)) {
                        oldIdProcesoOfGarantiaListNewGarantia.getGarantiaList().remove(garantiaListNewGarantia);
                        oldIdProcesoOfGarantiaListNewGarantia = em.merge(oldIdProcesoOfGarantiaListNewGarantia);
                    }
                }
            }
            for (Contrato contratoListNewContrato : contratoListNew) {
                if (!contratoListOld.contains(contratoListNewContrato)) {
                    Proceso oldIdProcesoOfContratoListNewContrato = contratoListNewContrato.getProceso();
                    contratoListNewContrato.setProceso(proceso);
                    contratoListNewContrato = em.merge(contratoListNewContrato);
                    if (oldIdProcesoOfContratoListNewContrato != null && !oldIdProcesoOfContratoListNewContrato.equals(proceso)) {
                        oldIdProcesoOfContratoListNewContrato.getContratoList().remove(contratoListNewContrato);
                        oldIdProcesoOfContratoListNewContrato = em.merge(oldIdProcesoOfContratoListNewContrato);
                    }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proceso proceso;
            try {
                proceso = em.getReference(Proceso.class, id);
                proceso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El PROCESO con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Garantia> garantiaListOrphanCheck = proceso.getGarantiaList();
            for (Garantia garantiaListOrphanCheckGarantia : garantiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Proceso: " + proceso + " No se puede eliminar potque tiene garantias vinculadas.");
            }
            List<Contrato> contratoListOrphanCheck = proceso.getContratoList();
            for (Contrato contratoListOrphanCheckContrato : contratoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Proceso: " + proceso + " no se puede eliminar porque tiene contratos vinculados.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tecnico idTecnico = proceso.getTecnico();
            if (idTecnico != null) {
                idTecnico.getProcesoList().remove(proceso);
                idTecnico = em.merge(idTecnico);
            }
            MetodoAdq idMetodo = proceso.getMetodo();
            if (idMetodo != null) {
                idMetodo.getProcesoList().remove(proceso);
                idMetodo = em.merge(idMetodo);
            }
            em.remove(proceso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proceso> findProcesoEntities() {
        return findProcesoEntities(true, -1, -1);
    }

    public List<Proceso> findProcesoEntities(int maxResults, int firstResult) {
        return findProcesoEntities(false, maxResults, firstResult);
    }

    private List<Proceso> findProcesoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proceso.class));
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

    public Proceso findProceso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proceso.class, id);
        } finally {
            em.close();
        }
    }

    public int getProcesoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proceso> rt = cq.from(Proceso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<Proceso> findProcesoByAnioRegistro(Integer anio) {
        EntityManager em = getEntityManager();
        List<Proceso> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Proceso.findByAnioRegistro");
            consulta.setParameter("anio", anio);
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR PROCESOS DEL AÑO:" +anio);
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public Proceso findByNumProceso(String numproceso) {
        EntityManager em = getEntityManager();
        Proceso resultado = null;
        try {
            Query consulta = em.createNamedQuery("Proceso.findByNumProceso");
            consulta.setParameter("numProceso", numproceso);
            resultado = (Proceso)  consulta.getResultList().get(0);
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
}
