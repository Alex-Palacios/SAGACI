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
import sv.gob.mined.uaci.entidades.Archivo;
import sv.gob.mined.uaci.entidades.EstatusFolio;
import sv.gob.mined.uaci.entidades.EstatusDigit;
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.FuenteFinanc;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;
import sv.gob.mined.uaci.jpa.exceptions.PreexistingEntityException;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Ultima Actualizacion: 05/09/2015
 */


public class ArchivoJpaController implements Serializable {

    public ArchivoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Archivo archivo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstatusFolio estatusFolio = archivo.getEstatusFolio();
            if (estatusFolio != null) {
                estatusFolio = em.getReference(estatusFolio.getClass(), estatusFolio.getId());
                archivo.setEstatusFolio(estatusFolio);
            }
            EstatusDigit estatusDigit = archivo.getEstatusDigit();
            if (estatusDigit != null) {
                estatusDigit = em.getReference(estatusDigit.getClass(), estatusDigit.getId());
                archivo.setEstatusDigit(estatusDigit);
            }
            Contrato contrato = archivo.getContrato();
            if (contrato != null) {
                contrato = em.getReference(contrato.getClass(), contrato.getId());
                archivo.setContrato(contrato);
            }
            //REGISTRO CONTROL
            archivo = JsfUtil.ultimaModificacionT(archivo);
            archivo.setRegistradoPor(archivo.getModificadoPor());
            archivo.setFechaIngreso(archivo.getFechaModificado());
            em.persist(archivo);
            if (estatusFolio != null) {
                estatusFolio.getArchivoList().add(archivo);
                estatusFolio = em.merge(estatusFolio);
            }
            if (estatusDigit != null) {
                estatusDigit.getArchivoList().add(archivo);
                estatusDigit = em.merge(estatusDigit);
            }
            if (contrato != null) {
                contrato.getArchivoList().add(archivo);
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

    public void edit(Archivo archivo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivo persistentArchivo = em.find(Archivo.class, archivo.getId());
            EstatusFolio estatusFolioOld = persistentArchivo.getEstatusFolio();
            EstatusFolio estatusFolioNew = archivo.getEstatusFolio();
            EstatusDigit estatusDigitOld = persistentArchivo.getEstatusDigit();
            EstatusDigit estatusDigitNew = archivo.getEstatusDigit();
            Contrato contratoOld = persistentArchivo.getContrato();
            Contrato contratoNew = archivo.getContrato();
            if (estatusFolioNew != null) {
                estatusFolioNew = em.getReference(estatusFolioNew.getClass(), estatusFolioNew.getId());
                archivo.setEstatusFolio(estatusFolioNew);
            }
            if (estatusDigitNew != null) {
                estatusDigitNew = em.getReference(estatusDigitNew.getClass(), estatusDigitNew.getId());
                archivo.setEstatusDigit(estatusDigitNew);
            }
            if (contratoNew != null) {
                contratoNew = em.getReference(contratoNew.getClass(), contratoNew.getId());
                archivo.setContrato(contratoNew);
            }
            //MODIFICACION CONTROL
            archivo = JsfUtil.ultimaModificacionT(archivo);
            archivo = em.merge(archivo);
            if (estatusFolioOld != null && !estatusFolioOld.equals(estatusFolioNew)) {
                estatusFolioOld.getArchivoList().remove(archivo);
                estatusFolioOld = em.merge(estatusFolioOld);
            }
            if (estatusFolioNew != null && !estatusFolioNew.equals(estatusFolioOld)) {
                estatusFolioNew.getArchivoList().add(archivo);
                estatusFolioNew = em.merge(estatusFolioNew);
            }
            if (estatusDigitOld != null && !estatusDigitOld.equals(estatusDigitNew)) {
                estatusDigitOld.getArchivoList().remove(archivo);
                estatusDigitOld = em.merge(estatusDigitOld);
            }
            if (estatusDigitNew != null && !estatusDigitNew.equals(estatusDigitOld)) {
                estatusDigitNew.getArchivoList().add(archivo);
                estatusDigitNew = em.merge(estatusDigitNew);
            }
            if (contratoOld != null && !contratoOld.equals(contratoNew)) {
                contratoOld.getArchivoList().remove(archivo);
                contratoOld = em.merge(contratoOld);
            }
            if (contratoNew != null && !contratoNew.equals(contratoOld)) {
                contratoNew.getArchivoList().add(archivo);
                contratoNew = em.merge(contratoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = archivo.getId();
                if (findArchivo(id) == null) {
                    throw new NonexistentEntityException("El ARCHIVO con id " + id + " no existe.");
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
            Archivo archivo;
            try {
                archivo = em.getReference(Archivo.class, id);
                archivo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El ARCHIVO con id " + id + " no existe.", enfe);
            }
            EstatusFolio estatusFolio = archivo.getEstatusFolio();
            if (estatusFolio != null) {
                estatusFolio.getArchivoList().remove(archivo);
                estatusFolio = em.merge(estatusFolio);
            }
            EstatusDigit estatusDigit = archivo.getEstatusDigit();
            if (estatusDigit != null) {
                estatusDigit.getArchivoList().remove(archivo);
                estatusDigit = em.merge(estatusDigit);
            }
            Contrato contrato = archivo.getContrato();
            if (contrato != null) {
                contrato.getArchivoList().remove(archivo);
                contrato = em.merge(contrato);
            }
            em.remove(archivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Archivo> findArchivoEntities() {
        return findArchivoEntities(true, -1, -1);
    }

    public List<Archivo> findArchivoEntities(int maxResults, int firstResult) {
        return findArchivoEntities(false, maxResults, firstResult);
    }

    private List<Archivo> findArchivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Archivo.class));
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

    public Archivo findArchivo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Archivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArchivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Archivo> rt = cq.from(Archivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<Archivo> findArchivoByAnio(Integer anio) {
        EntityManager em = getEntityManager();
        List<Archivo> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Archivo.findByAnioFecha");
            consulta.setParameter("anio", anio);
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR ARCHIVOS DEL AÑO:" +anio);
            return resultado;
        }finally {
            em.close();
        }
    }
    
    public List<Archivo> findArchivoByAnio(Integer anio,FuenteFinanc fuente) {
        EntityManager em = getEntityManager();
        List<Archivo> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Archivo.findByAnioFechaAndFuente");
            consulta.setParameter("anio", anio);
            consulta.setParameter("fuente", fuente);
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR ARCHIVOS DEL AÑO:" +anio);
            return resultado;
        }finally {
            em.close();
        }
    }
    
}
