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
import sv.gob.mined.uaci.servlet.reportes.ArchivoExp;

/**
 *
 * @author Alex
 */
public class ArchivosDatasource implements JRDataSource {

    private List<ArchivoExp> lista = new ArrayList<ArchivoExp>();
    private int index = -1;
        
    
    @Override
    public boolean next() throws JRException {
        return ++index < lista.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null; 
        if("registradoPor".equals(jrf.getName())){ 
            valor = lista.get(index).getRegistradoPor();
        }else if("caja".equals(jrf.getName())){ 
            valor = lista.get(index).getCaja();
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
        }else if("ampo".equals(jrf.getName())){ 
            valor = lista.get(index).getAmpo();
        }else if("tecnico".equals(jrf.getName())){ 
            valor = lista.get(index).getTecnico();
        }else if("folio".equals(jrf.getName())){ 
            valor = lista.get(index).getFolio();
        }else if("fEntrega".equals(jrf.getName())){ 
            valor = lista.get(index).getfEntrega();
        }else if("monto".equals(jrf.getName())){ 
            valor = lista.get(index).getMonto();
        }else if("plazo".equals(jrf.getName())){ 
            valor = lista.get(index).getPlazo();
        }else if("digit".equals(jrf.getName())){ 
            valor = lista.get(index).getDigit();
        }else if("numPag".equals(jrf.getName())){ 
            valor = lista.get(index).getNumPag();
        }

        return valor; 
    }
    
    

    public List<ArchivoExp> getLista() {
        return lista;
    }
    
    //AGREGA UN REGISTRO A LA LISTA
    public void addLista(ArchivoExp archivo){
        this.lista.add(archivo);
    }
    
    
}
