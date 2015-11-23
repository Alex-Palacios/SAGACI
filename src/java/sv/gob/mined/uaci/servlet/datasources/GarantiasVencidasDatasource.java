/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.servlet.datasources;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sv.gob.mined.uaci.servlet.reportes.GarantiaVencida;

/**
 *
 * @author Alex
 */
public class GarantiasVencidasDatasource implements JRDataSource {

    private List<GarantiaVencida> lista = new ArrayList<GarantiaVencida>();
    private int index = -1;
        
    
    @Override
    public boolean next() throws JRException {
        return ++index < lista.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null; 
        if("clave".equals(jrf.getName())){ 
            valor = lista.get(index).getClave();
        }else if("tipo".equals(jrf.getName())){ 
            valor = lista.get(index).getTipo();
        }else if("fRecepcion".equals(jrf.getName())){ 
            valor = lista.get(index).getfRecepcion();
        }else if("proceso".equals(jrf.getName())){ 
            valor = lista.get(index).getProceso();
        }else if("descripcion".equals(jrf.getName())){ 
            valor = lista.get(index).getDescripcion();
        }else if("contrato".equals(jrf.getName())){ 
            valor = lista.get(index).getContrato();
        }else if("proveedor".equals(jrf.getName())){ 
            valor = lista.get(index).getProveedor();
        }else if("afianzadora".equals(jrf.getName())){ 
            valor = lista.get(index).getAfianzadora();
        }else if("tecnico".equals(jrf.getName())){ 
            valor = lista.get(index).getTecnico();
        }else if("numGarantia".equals(jrf.getName())){ 
            valor = lista.get(index).getNumGarantia();
        }else if("fVenc".equals(jrf.getName())){ 
            valor = lista.get(index).getfVenc();
        }else if("monto".equals(jrf.getName())){ 
            valor = lista.get(index).getMonto();
        }else if("plazo".equals(jrf.getName())){ 
            valor = lista.get(index).getPlazo();
        }

        return valor; 
    }
    
    

    public List<GarantiaVencida> getLista() {
        return lista;
    }
    
    //AGREGA UN REGISTRO A LA LISTA
    public void addLista(GarantiaVencida cuenta){
        this.lista.add(cuenta);
    }
    
    
}
