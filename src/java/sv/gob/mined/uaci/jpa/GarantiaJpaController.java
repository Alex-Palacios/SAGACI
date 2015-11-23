/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.queries.StoredFunctionCall;
import org.eclipse.persistence.queries.ValueReadQuery;
import sv.gob.mined.uaci.contoladores.util.GarantiaImport;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.entidades.TipoGarantia;
import sv.gob.mined.uaci.entidades.Proveedor;
import sv.gob.mined.uaci.entidades.Proceso;
import sv.gob.mined.uaci.entidades.Contrato;
import sv.gob.mined.uaci.entidades.Afianzadora;
import sv.gob.mined.uaci.entidades.Garantia;
import sv.gob.mined.uaci.entidades.MetodoAdq;
import sv.gob.mined.uaci.entidades.Tecnico;
import sv.gob.mined.uaci.jpa.exceptions.NonexistentEntityException;
import sv.gob.mined.uaci.jpa.exceptions.PreexistingEntityException;

/**
 * @author Marlon Alexander Palacios
 * @version 10/12/2014
 * @since 1.0
 * 
 * Ultima Actualizacion: 05/09/2015
 */

public class GarantiaJpaController implements Serializable {

    public GarantiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Garantia garantia) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            if(!validarGarantia(garantia)){
                throw new Exception();
            }
            em = getEntityManager();
            em.getTransaction().begin();
            TipoGarantia tipo = garantia.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getId());
                garantia.setTipo(tipo);
            }
            Proveedor proveedor = garantia.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getId());
                garantia.setProveedor(proveedor);
            }
            Proceso proceso = garantia.getProceso();
            if (proceso != null) {
                proceso = em.getReference(proceso.getClass(), proceso.getId());
                garantia.setProceso(proceso);
            }
            Contrato contrato = garantia.getContrato();
            if (contrato != null) {
                contrato = em.getReference(contrato.getClass(), contrato.getId());
                garantia.setContrato(contrato);
            }
            Afianzadora afianzadora = garantia.getAfianzadora();
            if (afianzadora != null) {
                afianzadora = em.getReference(afianzadora.getClass(), afianzadora.getId());
                garantia.setAfianzadora(afianzadora);
            }
            //REGISTRO CONTROL
            garantia = JsfUtil.ultimaModificacionT(garantia);
            garantia.setRegistradoPor(garantia.getModificadoPor());
            garantia.setFechaIngreso(garantia.getFechaModificado());
            if(garantia.getFechaDevolucion() == null){
                garantia.setEstado(1);//REGISTRADA
            }else{
                garantia.setEstado(2);//DEVUELTA
            }
            em.persist(garantia);
            if (tipo != null) {
                tipo.getGarantiaList().add(garantia);
                tipo = em.merge(tipo);
            }
            if (proveedor != null) {
                proveedor.getGarantiaList().add(garantia);
                proveedor = em.merge(proveedor);
            }
            if (proceso != null) {
                proceso.getGarantiaList().add(garantia);
                proceso = em.merge(proceso);
            }
            if (contrato != null) {
                contrato.getGarantiaList().add(garantia);
                contrato = em.merge(contrato);
            }
            if (afianzadora != null) {
                afianzadora.getGarantiaList().add(garantia);
                afianzadora = em.merge(afianzadora);
            }
            em.getTransaction().commit();
            em.refresh(garantia);
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

    public void edit(Garantia garantia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            if(!validarGarantia(garantia)){
                throw new Exception();
            }
            em = getEntityManager();
            em.getTransaction().begin();
            Garantia persistentGarantia = em.find(Garantia.class, garantia.getId());
            TipoGarantia tipoOld = persistentGarantia.getTipo();
            TipoGarantia tipoNew = garantia.getTipo();
            Proveedor proveedorOld = persistentGarantia.getProveedor();
            Proveedor proveedorNew = garantia.getProveedor();
            Proceso procesoOld = persistentGarantia.getProceso();
            Proceso procesoNew = garantia.getProceso();
            Contrato contratoOld = persistentGarantia.getContrato();
            Contrato contratoNew = garantia.getContrato();
            Afianzadora afianzadoraOld = persistentGarantia.getAfianzadora();
            Afianzadora afianzadoraNew = garantia.getAfianzadora();
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getId());
                garantia.setTipo(tipoNew);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getId());
                garantia.setProveedor(proveedorNew);
            }
            if (procesoNew != null) {
                procesoNew = em.getReference(procesoNew.getClass(), procesoNew.getId());
                garantia.setProceso(procesoNew);
            }
            if (contratoNew != null) {
                contratoNew = em.getReference(contratoNew.getClass(), contratoNew.getId());
                garantia.setContrato(contratoNew);
            }
            if (afianzadoraNew != null) {
                afianzadoraNew = em.getReference(afianzadoraNew.getClass(), afianzadoraNew.getId());
                garantia.setAfianzadora(afianzadoraNew);
            }
            //MODIFICACION CONTROL
            garantia = JsfUtil.ultimaModificacionT(garantia);
            garantia = em.merge(garantia);
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getGarantiaList().remove(garantia);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getGarantiaList().add(garantia);
                tipoNew = em.merge(tipoNew);
            }
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getGarantiaList().remove(garantia);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getGarantiaList().add(garantia);
                proveedorNew = em.merge(proveedorNew);
            }
            if (procesoOld != null && !procesoOld.equals(procesoNew)) {
                procesoOld.getGarantiaList().remove(garantia);
                procesoOld = em.merge(procesoOld);
            }
            if (procesoNew != null && !procesoNew.equals(procesoOld)) {
                procesoNew.getGarantiaList().add(garantia);
                procesoNew = em.merge(procesoNew);
            }
            if (contratoOld != null && !contratoOld.equals(contratoNew)) {
                contratoOld.getGarantiaList().remove(garantia);
                contratoOld = em.merge(contratoOld);
            }
            if (contratoNew != null && !contratoNew.equals(contratoOld)) {
                contratoNew.getGarantiaList().add(garantia);
                contratoNew = em.merge(contratoNew);
            }
            if (afianzadoraOld != null && !afianzadoraOld.equals(afianzadoraNew)) {
                afianzadoraOld.getGarantiaList().remove(garantia);
                afianzadoraOld = em.merge(afianzadoraOld);
            }
            if (afianzadoraNew != null && !afianzadoraNew.equals(afianzadoraOld)) {
                afianzadoraNew.getGarantiaList().add(garantia);
                afianzadoraNew = em.merge(afianzadoraNew);
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Garantia garantia;
            try {
                garantia = em.getReference(Garantia.class, id);
                garantia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La GARANTIA con id " + id + " no existe.", enfe);
            }
            TipoGarantia tipo = garantia.getTipo();
            if (tipo != null) {
                tipo.getGarantiaList().remove(garantia);
                tipo = em.merge(tipo);
            }
            Proveedor proveedor = garantia.getProveedor();
            if (proveedor != null) {
                proveedor.getGarantiaList().remove(garantia);
                proveedor = em.merge(proveedor);
            }
            Proceso proceso = garantia.getProceso();
            if (proceso != null) {
                proceso.getGarantiaList().remove(garantia);
                proceso = em.merge(proceso);
            }
            Contrato contrato = garantia.getContrato();
            if (contrato != null) {
                contrato.getGarantiaList().remove(garantia);
                contrato = em.merge(contrato);
            }
            Afianzadora afianzadora = garantia.getAfianzadora();
            if (afianzadora != null) {
                afianzadora.getGarantiaList().remove(garantia);
                afianzadora = em.merge(afianzadora);
            }
            em.remove(garantia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public boolean validarGarantia(Garantia garantia){
        boolean valido = true;
        if(garantia.getMonto().compareTo(BigDecimal.ZERO) < 1){
            valido = false;
            JsfUtil.addWarnMessage("MONTO INVALIDO");
        }
        if(garantia.getFechaVencimiento() != null && garantia.getFechaInicio() != null ){
            if(garantia.getFechaVencimiento().before(garantia.getFechaInicio())){
                valido = false;
                JsfUtil.addWarnMessage("FECHA VENCIMIENTO DEBE SER MAYOR A FECHA RECEPCION");
            }
        }
        
        return valido;
    }
    
    
    
    public List<GarantiaImport> importarGarantias(List<GarantiaImport> lista){
        List<GarantiaImport> noImport = new ArrayList<GarantiaImport>();
        int p =0;
        int c =0;
        int g =0;
        int existen = 0;
        EntityManager em = null;
        em = getEntityManager();
        em.getTransaction().begin();
        try{
            for(GarantiaImport item : lista){
                if(noImport.size() == 0){
                      try{
                            Garantia garantia = findGarantiaByClave(item.getAnioClave(), item.getNumClave());
                            if(garantia == null){
                                //CREAR GARANTIA
                                Proveedor proveedor = JsfUtil.SessionProveedorController().getJpaController().obtenerCrearProveedor(item.getProveedor());
                                TipoGarantia tipo = JsfUtil.SessionTipoGarantiaController().getJpaController().obtenerCrearTipo(item.getTipoGarantia());
                                Afianzadora afianzadora = JsfUtil.SessionAfianzadoraController().getJpaController().obtenerCrearAfianzadora(item.getAfianzadora());
                                //CREAR CONTRATO
                                Proceso proceso = JsfUtil.SessionProcesoController().getJpaController().findByNumProceso(item.getNumProceso());
                                if(proceso == null){
                                    //CREAR PROCESO
                                    Tecnico tecnico = JsfUtil.SessionTecnicoController().getJpaController().obtenerCrearTecnico(item.getTecnico());
                                    MetodoAdq metodo = JsfUtil.SessionMetodoAdqController().getJpaController().obtenerCrearMetodo(item.getMetodo());
                                    proceso = new Proceso();
                                    proceso.setMetodo(metodo);
                                    proceso.setTecnico(tecnico);
                                    proceso.setNumero(item.getNumProceso());
                                    proceso.setDescripcion(item.getProceso());
                                    proceso.setSiap(item.getSiap());
                                    JsfUtil.SessionProcesoController().getJpaController().create(proceso);
                                    p++;
                                }
                                Contrato contrato = JsfUtil.SessionContratoController().getJpaController().findByNumContrato(item.getContrato());
                                if(contrato == null){
                                    if(item.getContrato() != null && !item.getContrato().equals("")){
                                        contrato = new Contrato();
                                        contrato.setProceso(proceso);
                                        contrato.setContratista(proveedor);
                                        contrato.setNumero(item.getContrato());
                                        try{
                                            contrato.setFecha(item.getFechaContrato());
                                        }catch(Exception e1){contrato.setFecha(null);}
                                        try{
                                            contrato.setPlazo(item.getPlazoContrato());
                                        }catch(Exception e1){contrato.setPlazo(null);}
                                        try{
                                            contrato.setMonto(new BigDecimal(item.getMontoContrato()));
                                        }catch(Exception e1){contrato.setMonto(null);}
                                        try{
                                            contrato.setModificativa(item.getModificativa());
                                        }catch(Exception e1){contrato.setModificativa(null);}

                                        JsfUtil.SessionContratoController().getJpaController().create(contrato);
                                        c++;
                                    }
                                }
                                garantia = new Garantia();
                                garantia.setProceso(proceso);
                                garantia.setContrato(contrato);
                                garantia.setProveedor(proveedor);
                                garantia.setAfianzadora(afianzadora);
                                garantia.setTipo(tipo);
                                garantia.setAnio(item.getAnioClave());
                                garantia.setCorrelativo(item.getNumClave());
                                garantia.setFechaDevolucion(item.getFechaDevolucion());
                                garantia.setRetiradoPor(item.getPersonaRetira());
                                garantia.setPlazo(item.getPlazo());
                                garantia.setMonto(new BigDecimal(item.getMontoGarantia()));
                                garantia.setNumGarantia(item.getNumGarantia());
                                garantia.setFechaVencimiento(item.getFechaVencimiento());
                                garantia.setFechaInicio(item.getFechaRecepcion());
                                create(garantia);
                                g++;
                            }else{
                                existen++;
                            }
                        }catch(Exception e){
                            noImport.add(item);
                        }  
                }else{
                    noImport.add(item);
                }
                
            }
            em.getTransaction().commit();
            if(existen > 0){
                JsfUtil.addWarnMessage(existen + " GARANTIAS NO SE GUARDARON PORQUE YA EXISTE EN EL SISTEMA");
            }
            if(p > 0){
                JsfUtil.addSuccessMessage("NUEVOS PROCESOS " + p);
            }
            if(c > 0){
                JsfUtil.addSuccessMessage("NUEVOS CONTRATOS " + c);
            }
            JsfUtil.addSuccessMessage(g+" GARANTIAS IMPORTADAS ");
        }catch(Exception e){
            try{em.getTransaction().rollback();}catch(Exception ex){} 
        }finally{
            em.close();
        }
        return noImport;
    }
    
    
    public List<Garantia> findGarantiaEntities() {
        return findGarantiaEntities(true, -1, -1);
    }

    public List<Garantia> findGarantiaEntities(int maxResults, int firstResult) {
        return findGarantiaEntities(false, maxResults, firstResult);
    }

    private List<Garantia> findGarantiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Garantia.class));
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

    public Garantia findGarantia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Garantia.class, id);
        } finally {
            em.close();
        }
    }

    public int getGarantiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Garantia> rt = cq.from(Garantia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    
    public List<Garantia> findGarantiaByAnio(Integer anio) {
        EntityManager em = getEntityManager();
        List<Garantia> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Garantia.findByAnio");
            consulta.setParameter("anio", anio);
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR GARANTIAS DEL AÑO:" +anio);
            return resultado;
        }finally {
            em.close();
        }
    }
    
    public Garantia findGarantiaByClave(Integer anio,Integer correlativo) {
        EntityManager em = getEntityManager();
        Garantia resultado = null;
        try {
            Query consulta = em.createNamedQuery("Garantia.findByClave");
            consulta.setParameter("anio", anio);
            consulta.setParameter("correlativo", correlativo);
            resultado =(Garantia)  consulta.getResultList().get(0);
            return resultado;
        } catch(Exception ex){
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    
    public List<Garantia> reporteGarantiasVencidas() {
        EntityManager em = getEntityManager();
        List<Garantia> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Garantia.vencidas");
            consulta.setParameter("fecha", JsfUtil.hoy());
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR GARANTIAS VENCIDAS");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    public List<Garantia> reporteGarantiasXVencer(Date fecha) {
        EntityManager em = getEntityManager();
        List<Garantia> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Garantia.Xvencer");
            consulta.setParameter("fecha", fecha);
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR GARANTIAS POR VENCER");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    
    
    public List<Garantia> reporteGarantiasVigentes() {
        EntityManager em = getEntityManager();
        List<Garantia> resultado = null;
        try {
            Query consulta = em.createNamedQuery("Garantia.vigentes");
            consulta.setParameter("fecha", JsfUtil.hoy());
            resultado = consulta.getResultList();
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR GARANTIAS VIGENTES");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    
    /*
     * FUNCION QUE CUENTA EL NUMERO DE ARTICULOS
     */
    public int countVencidas() {
        EntityManager em = getEntityManager();
        int resultado = 0;
        try {
            Query consulta = em.createNamedQuery("Garantia.countVencidas");
            consulta.setParameter("fecha", JsfUtil.hoy());
            Object query = consulta.getSingleResult();
            
            if (query != null) {
                resultado = Integer.parseInt(query.toString());
            }
            return resultado;
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL CONSULTAR GARANTIAS VENCIDAS");
            return resultado;
        }finally {
            em.close();
        }
    }
    
    
    
    
    
    //FUNCIONES NUMERO DE GARANTIA Y FORMULARIO DE DEVOLUCION
    
    public Integer getNextNumGrantia(Integer anio){
        
        EntityManager em = getEntityManager();
        BigDecimal resultado = BigDecimal.ZERO;
        try {
            resultado = (BigDecimal) em.createNativeQuery("SELECT SAGACI.GET_NEXT_GARANTIA(?1) FROM DUAL")
                      .setParameter(1, anio)
                      .getSingleResult(); 
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL OBTENER PROXIMO CORRELATIVO DE GARANTIA");
        }finally {
            em.close();
        }
        return resultado.intValue();
    }
    
    
    public Integer getNextFormDev(Integer anio){
        
        EntityManager em = getEntityManager();
        BigDecimal resultado = BigDecimal.ZERO;
        try {
            resultado = (BigDecimal) em.createNativeQuery("SELECT SAGACI.GET_NEXT_FORMDEV(?1) FROM DUAL")
                      .setParameter(1, anio)
                      .getSingleResult(); 
        } catch(Exception ex){
            JsfUtil.addErrorMessage(ex,"ERROR AL OBTENER PROXIMO CORRELATIVO DE FORMULARIO DE DEVOLUCION");
        }finally {
            em.close();
        }
        return resultado.intValue();
    }
    
    
}
