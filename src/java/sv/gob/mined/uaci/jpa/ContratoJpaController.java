/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.gob.mined.uaci.entidades.UnidadTecnica;
import sv.gob.mined.uaci.entidades.Proveedor;
import sv.gob.mined.uaci.entidades.Proceso;
import sv.gob.mined.uaci.entidades.FuenteFinanc;
import sv.gob.mined.uaci.entidades.EstatusContrato;
import sv.gob.mined.uaci.entidades.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.entidades.Garantia;
import sv.gob.mined.uaci.entidades.Archivo;
import sv.gob.mined.uaci.entidades.Contrato;
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


public class ContratoJpaController implements Serializable {

    public ContratoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contrato contrato) throws PreexistingEntityException, Exception {
        
        if (contrato.getPagoList() == null) {
            contrato.setPagoList(new ArrayList<Pago>());
        }
        if (contrato.getGarantiaList() == null) {
            contrato.setGarantiaList(new ArrayList<Garantia>());
        }
        if (contrato.getArchivoList() == null) {
            contrato.setArchivoList(new ArrayList<Archivo>());
        }
        EntityManager em = null;
        try {
            if(!validarContrato(contrato)){
                throw new Exception();
            }
            em = getEntityManager();
            em.getTransaction().begin();
            UnidadTecnica unidad = contrato.getUnidad();
            if (unidad != null) {
                unidad = em.getReference(unidad.getClass(), unidad.getId());
                contrato.setUnidad(unidad);
            }
            Proveedor contratista = contrato.getContratista();
            if (contratista != null) {
                contratista = em.getReference(contratista.getClass(), contratista.getId());
                contrato.setContratista(contratista);
            }
            Proceso proceso = contrato.getProceso();
            if (proceso != null) {
                proceso = em.getReference(proceso.getClass(), proceso.getId());
                contrato.setProceso(proceso);
            }
            FuenteFinanc fuente = contrato.getFuente();
            if (fuente != null) {
                fuente = em.getReference(fuente.getClass(), fuente.getId());
                contrato.setFuente(fuente);
            }
            EstatusContrato estado = contrato.getEstatusContrato();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getId());
                contrato.setEstatusContrato(estado);
            }
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : contrato.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            contrato.setPagoList(attachedPagoList);
            List<Garantia> attachedGarantiaList = new ArrayList<Garantia>();
            for (Garantia garantiaListGarantiaToAttach : contrato.getGarantiaList()) {
                garantiaListGarantiaToAttach = em.getReference(garantiaListGarantiaToAttach.getClass(), garantiaListGarantiaToAttach.getId());
                attachedGarantiaList.add(garantiaListGarantiaToAttach);
            }
            contrato.setGarantiaList(attachedGarantiaList);
            List<Archivo> attachedArchivoList = new ArrayList<Archivo>();
            for (Archivo archivoListArchivoToAttach : contrato.getArchivoList()) {
                archivoListArchivoToAttach = em.getReference(archivoListArchivoToAttach.getClass(), archivoListArchivoToAttach.getId());
                attachedArchivoList.add(archivoListArchivoToAttach);
            }
            contrato.setArchivoList(attachedArchivoList);
            //REGISTRO CONTROL
            contrato = JsfUtil.ultimaModificacionT(contrato);
            contrato.setRegistradoPor(contrato.getModificadoPor());
            contrato.setFechaIngreso(contrato.getFechaModificado());
            em.persist(contrato);
            if (unidad != null) {
                unidad.getContratoList().add(contrato);
                unidad = em.merge(unidad);
            }
            if (contratista != null) {
                contratista.getContratoList().add(contrato);
                contratista = em.merge(contratista);
            }
            if (proceso != null) {
                proceso.getContratoList().add(contrato);
                proceso = em.merge(proceso);
            }
            if (fuente != null) {
                fuente.getContratoList().add(contrato);
                fuente = em.merge(fuente);
            }
            if (estado != null) {
                estado.getContratoList().add(contrato);
                estado = em.merge(estado);
            }
            for (Pago pagoListPago : contrato.getPagoList()) {
                Contrato oldContratoOfPagoListPago = pagoListPago.getContrato();
                pagoListPago.setContrato(contrato);
                pagoListPago = em.merge(pagoListPago);
                if (oldContratoOfPagoListPago != null) {
                    oldContratoOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldContratoOfPagoListPago = em.merge(oldContratoOfPagoListPago);
                }
            }
            for (Garantia garantiaListGarantia : contrato.getGarantiaList()) {
                Contrato oldContratoOfGarantiaListGarantia = garantiaListGarantia.getContrato();
                garantiaListGarantia.setContrato(contrato);
                garantiaListGarantia = em.merge(garantiaListGarantia);
                if (oldContratoOfGarantiaListGarantia != null) {
                    oldContratoOfGarantiaListGarantia.getGarantiaList().remove(garantiaListGarantia);
                    oldContratoOfGarantiaListGarantia = em.merge(oldContratoOfGarantiaListGarantia);
                }
            }
            for (Archivo archivoListArchivo : contrato.getArchivoList()) {
                Contrato oldContratoOfArchivoListArchivo = archivoListArchivo.getContrato();
                archivoListArchivo.setContrato(contrato);
                archivoListArchivo = em.merge(archivoListArchivo);
                if (oldContratoOfArchivoListArchivo != null) {
                    oldContratoOfArchivoListArchivo.getArchivoList().remove(archivoListArchivo);
                    oldContratoOfArchivoListArchivo = em.merge(oldContratoOfArchivoListArchivo);
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

    public void edit(Contrato contrato) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            if(!validarContrato(contrato)){
                throw new Exception();
            }
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato persistentContrato = em.find(Contrato.class, contrato.getId());
            UnidadTecnica unidadOld = persistentContrato.getUnidad();
            UnidadTecnica unidadNew = contrato.getUnidad();
            Proveedor contratistaOld = persistentContrato.getContratista();
            Proveedor contratistaNew = contrato.getContratista();
            Proceso procesoOld = persistentContrato.getProceso();
            Proceso procesoNew = contrato.getProceso();
            FuenteFinanc fuenteOld = persistentContrato.getFuente();
            FuenteFinanc fuenteNew = contrato.getFuente();
            EstatusContrato estadoOld = persistentContrato.getEstatusContrato();
            EstatusContrato estadoNew = contrato.getEstatusContrato();
            List<Pago> pagoListOld = persistentContrato.getPagoList();
            List<Pago> pagoListNew = contrato.getPagoList();
            List<Garantia> garantiaListOld = persistentContrato.getGarantiaList();
            List<Garantia> garantiaListNew = contrato.getGarantiaList();
            List<Archivo> archivoListOld = persistentContrato.getArchivoList();
            List<Archivo> archivoListNew = contrato.getArchivoList();
            List<String> illegalOrphanMessages = null;
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunos pagos " + pagoListOldPago + " no se encuentran vinculados.");
                }
            }
            for (Archivo archivoListOldArchivo : archivoListOld) {
                if (!archivoListNew.contains(archivoListOldArchivo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("Algunos archivos " + archivoListOldArchivo + " no se encuentran vinculados.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (unidadNew != null) {
                unidadNew = em.getReference(unidadNew.getClass(), unidadNew.getId());
                contrato.setUnidad(unidadNew);
            }
            if (contratistaNew != null) {
                contratistaNew = em.getReference(contratistaNew.getClass(), contratistaNew.getId());
                contrato.setContratista(contratistaNew);
            }
            if (procesoNew != null) {
                procesoNew = em.getReference(procesoNew.getClass(), procesoNew.getId());
                contrato.setProceso(procesoNew);
            }
            if (fuenteNew != null) {
                fuenteNew = em.getReference(fuenteNew.getClass(), fuenteNew.getId());
                contrato.setFuente(fuenteNew);
            }
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getId());
                contrato.setEstatusContrato(estadoNew);
            }
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            contrato.setPagoList(pagoListNew);
            List<Garantia> attachedGarantiaListNew = new ArrayList<Garantia>();
            for (Garantia garantiaListNewGarantiaToAttach : garantiaListNew) {
                garantiaListNewGarantiaToAttach = em.getReference(garantiaListNewGarantiaToAttach.getClass(), garantiaListNewGarantiaToAttach.getId());
                attachedGarantiaListNew.add(garantiaListNewGarantiaToAttach);
            }
            garantiaListNew = attachedGarantiaListNew;
            contrato.setGarantiaList(garantiaListNew);
            List<Archivo> attachedArchivoListNew = new ArrayList<Archivo>();
            for (Archivo archivoListNewArchivoToAttach : archivoListNew) {
                archivoListNewArchivoToAttach = em.getReference(archivoListNewArchivoToAttach.getClass(), archivoListNewArchivoToAttach.getId());
                attachedArchivoListNew.add(archivoListNewArchivoToAttach);
            }
            archivoListNew = attachedArchivoListNew;
            contrato.setArchivoList(archivoListNew);
            
            //MODIFICACION CONTROL
            contrato = JsfUtil.ultimaModificacionT(contrato);
            contrato = em.merge(contrato);
            if (unidadOld != null && !unidadOld.equals(unidadNew)) {
                unidadOld.getContratoList().remove(contrato);
                unidadOld = em.merge(unidadOld);
            }
            if (unidadNew != null && !unidadNew.equals(unidadOld)) {
                unidadNew.getContratoList().add(contrato);
                unidadNew = em.merge(unidadNew);
            }
            if (contratistaOld != null && !contratistaOld.equals(contratistaNew)) {
                contratistaOld.getContratoList().remove(contrato);
                contratistaOld = em.merge(contratistaOld);
            }
            if (contratistaNew != null && !contratistaNew.equals(contratistaOld)) {
                contratistaNew.getContratoList().add(contrato);
                contratistaNew = em.merge(contratistaNew);
            }
            if (procesoOld != null && !procesoOld.equals(procesoNew)) {
                procesoOld.getContratoList().remove(contrato);
                procesoOld = em.merge(procesoOld);
            }
            if (procesoNew != null && !procesoNew.equals(procesoOld)) {
                procesoNew.getContratoList().add(contrato);
                procesoNew = em.merge(procesoNew);
            }
            if (fuenteOld != null && !fuenteOld.equals(fuenteNew)) {
                fuenteOld.getContratoList().remove(contrato);
                fuenteOld = em.merge(fuenteOld);
            }
            if (fuenteNew != null && !fuenteNew.equals(fuenteOld)) {
                fuenteNew.getContratoList().add(contrato);
                fuenteNew = em.merge(fuenteNew);
            }
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getContratoList().remove(contrato);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getContratoList().add(contrato);
                estadoNew = em.merge(estadoNew);
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Contrato oldContratoOfPagoListNewPago = pagoListNewPago.getContrato();
                    pagoListNewPago.setContrato(contrato);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldContratoOfPagoListNewPago != null && !oldContratoOfPagoListNewPago.equals(contrato)) {
                        oldContratoOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldContratoOfPagoListNewPago = em.merge(oldContratoOfPagoListNewPago);
                    }
                }
            }
            for (Garantia garantiaListOldGarantia : garantiaListOld) {
                if (!garantiaListNew.contains(garantiaListOldGarantia)) {
                    garantiaListOldGarantia.setContrato(null);
                    garantiaListOldGarantia = em.merge(garantiaListOldGarantia);
                }
            }
            for (Garantia garantiaListNewGarantia : garantiaListNew) {
                if (!garantiaListOld.contains(garantiaListNewGarantia)) {
                    Contrato oldContratoOfGarantiaListNewGarantia = garantiaListNewGarantia.getContrato();
                    garantiaListNewGarantia.setContrato(contrato);
                    garantiaListNewGarantia = em.merge(garantiaListNewGarantia);
                    if (oldContratoOfGarantiaListNewGarantia != null && !oldContratoOfGarantiaListNewGarantia.equals(contrato)) {
                        oldContratoOfGarantiaListNewGarantia.getGarantiaList().remove(garantiaListNewGarantia);
                        oldContratoOfGarantiaListNewGarantia = em.merge(oldContratoOfGarantiaListNewGarantia);
                    }
                }
            }
            for (Archivo archivoListNewArchivo : archivoListNew) {
                if (!archivoListOld.contains(archivoListNewArchivo)) {
                    Contrato oldContratoOfArchivoListNewArchivo = archivoListNewArchivo.getContrato();
                    archivoListNewArchivo.setContrato(contrato);
                    archivoListNewArchivo = em.merge(archivoListNewArchivo);
                    if (oldContratoOfArchivoListNewArchivo != null && !oldContratoOfArchivoListNewArchivo.equals(contrato)) {
                        oldContratoOfArchivoListNewArchivo.getArchivoList().remove(archivoListNewArchivo);
                        oldContratoOfArchivoListNewArchivo = em.merge(oldContratoOfArchivoListNewArchivo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contrato.getId();
                if (findContrato(id) == null) {
                    throw new NonexistentEntityException("El CONTRATO con id " + id + " no existe.");
                }
            }
            if(em.getTransaction() != null && em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.getTransaction().rollback();
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
            Contrato contrato;
            try {
                contrato = em.getReference(Contrato.class, id);
                contrato.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El CONTRATO con id " + id + " no existe.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pago> pagoListOrphanCheck = contrato.getPagoList();
            for (Pago pagoListOrphanCheckPago : pagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Contrato: " + contrato + " no se puede eliminar porque hay pagos vinculados");
            }
            List<Archivo> archivoListOrphanCheck = contrato.getArchivoList();
            for (Archivo archivoListOrphanCheckArchivo : archivoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Contrato: " + contrato + " no se puede eliminar porque ya se encuentra archivado.");
            }
            List<Garantia> garantiaListOrphanCheck = contrato.getGarantiaList();
            for (Garantia garantiaListOrphanCheckArchivo : garantiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("El Contrato: " + contrato + " no se puede eliminar porque hay garantias vinculadas.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UnidadTecnica unidad = contrato.getUnidad();
            if (unidad != null) {
                unidad.getContratoList().remove(contrato);
                unidad = em.merge(unidad);
            }
            Proveedor contratista = contrato.getContratista();
            if (contratista != null) {
                contratista.getContratoList().remove(contrato);
                contratista = em.merge(contratista);
            }
            Proceso proceso = contrato.getProceso();
            if (proceso != null) {
                proceso.getContratoList().remove(contrato);
                proceso = em.merge(proceso);
            }
            FuenteFinanc fuente = contrato.getFuente();
            if (fuente != null) {
                fuente.getContratoList().remove(contrato);
                fuente = em.merge(fuente);
            }
            EstatusContrato estado = contrato.getEstatusContrato();
            if (estado != null) {
                estado.getContratoList().remove(contrato);
                estado = em.merge(estado);
            }
            List<Garantia> garantiaList = contrato.getGarantiaList();
            for (Garantia garantiaListGarantia : garantiaList) {
                garantiaListGarantia.setContrato(null);
                garantiaListGarantia = em.merge(garantiaListGarantia);
            }
            em.remove(contrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    public boolean validarContrato(Contrato contrato){
        boolean valido = true;
        if(contrato.getMonto() != null){
            if(contrato.getMonto().compareTo(BigDecimal.ZERO) == -1){
                valido = false;
                JsfUtil.addWarnMessage("MONTO INVALIDO");
            }
        }else{
            contrato.setMonto(BigDecimal.ZERO);
        }
        if(contrato.getFechaFin() != null && contrato.getFechaInicio() != null){
            if(contrato.getFechaFin().before(contrato.getFechaInicio())){
                valido = false;
                JsfUtil.addWarnMessage("FECHA FIN DEBE SER MAYOR A FECHA INICIO");
            }
        }
        return valido;
    }

    public List<Contrato> findContratoEntities() {
        return findContratoEntities(true, -1, -1);
    }

    public List<Contrato> findContratoEntities(int maxResults, int firstResult) {
        return findContratoEntities(false, maxResults, firstResult);
    }

    private List<Contrato> findContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contrato.class));
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

    public Contrato findContrato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contrato> rt = cq.from(Contrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<Contrato> findContratoByAnioFechaDoc(Integer anio) {
        EntityManager em = getEntityManager();
        List<Contrato> resultado = null;
        try {
            if(anio == 0){
                Query consulta = em.createNamedQuery("Contrato.findByAnioFechaNULL");
                resultado = consulta.getResultList();
            }else{
                Query consulta = em.createNamedQuery("Contrato.findByAnioFecha");
                consulta.setParameter("anio", anio);
                resultado = consulta.getResultList();
            }
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR CONTRATOS DEL AÑO:" +anio);
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    
    public List<Contrato> findContratoByProceso(Proceso proceso) {
        EntityManager em = getEntityManager();
        List<Contrato> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Contrato.findByProceso");
            consulta.setParameter("proceso", proceso);
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR CONTRATOS DEL PROCESO:" +proceso.toString());
            return resultado;
        }finally {
            em.close();
        }
    }
    
    public Contrato findByNumContrato(String numero) {
        EntityManager em = getEntityManager();
        Contrato resultado = null;
        try {
            Query consulta = em.createNamedQuery("Contrato.findByNumContrato");
            consulta.setParameter("numContrato", numero);
            resultado = (Contrato) consulta.getResultList().get(0);
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public List<Contrato> findContratoParaArchivar() {
        EntityManager em = getEntityManager();
        List<Contrato> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Contrato.findByArchivar");
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR CONTRATOS POR ARCHIVAR ");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public List<Contrato> findContratosArchivados(Integer anio) {
        EntityManager em = getEntityManager();
        List<Contrato> resultado = null;
        try {
            if(anio == 0){
                Query consulta = em.createNamedQuery("Contrato.findByArchivadosByAnioNULL");
                resultado = consulta.getResultList();
            }else{
                Query consulta = em.createNamedQuery("Contrato.findByArchivadosByAnio");
                consulta.setParameter("anio", anio);
                resultado = consulta.getResultList();
            }
            
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR CONTRATOS ARCHIVADOS");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    public List<Contrato> findContratoPagos() {
        EntityManager em = getEntityManager();
        List<Contrato> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Contrato.findByArchivar");
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR CONTRATOS PARA REGISTRAR PAGOS ");
            return resultado;
        }finally {
            em.close();
        }
    }
    
}
