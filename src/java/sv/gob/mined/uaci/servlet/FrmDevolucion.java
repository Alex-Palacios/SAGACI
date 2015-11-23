
package sv.gob.mined.uaci.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import sv.gob.mined.uaci.contoladores.util.JsfUtil;
import sv.gob.mined.uaci.entidades.Garantia;

/**
 *
 * @author Alex
 */
public class FrmDevolucion extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        ServletOutputStream out = response.getOutputStream();
        try {
            //OBTENER DATOS
            Garantia datos = ((Garantia) (request.getSession().getAttribute("garantia")));
            if(datos != null){
                
            response.setContentType("application/pdf");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
                String numForm = request.getSession().getAttribute("numFormulario").toString();
                String gerente = request.getSession().getAttribute("gerente").toString();
                Date hoy = JsfUtil.hoy();
                //PARAMETROS
                Map parametros = new HashMap(); 
                parametros.put("urlEscudoES", getServletContext().getRealPath("resources/reportes/escudoES.jpg"));
                parametros.put("numForm", numForm);
                parametros.put("tipoGarantia", datos.getTipo().toString());
                if(datos.getContrato() != null){
                    parametros.put("contrato", datos.getContrato().toString());
                }else{
                    parametros.put("contrato", "S/N");
                }
                parametros.put("metodoAdquisicion", datos.getProceso().getMetodo().toString());
                parametros.put("horaFecha", "a las " + hoy.getHours() +" horas " + hoy.getMinutes() +" minutos del dia " + JsfUtil.dateFormat(hoy, "dd 'de' MMMM 'de' yyyy"));
                parametros.put("descripcion", datos.getProceso().getDescripcion());
                parametros.put("proveedor", datos.getProveedor().toString());
                parametros.put("afianzadora", datos.getAfianzadora().toString());
                parametros.put("numGarantia", datos.getNumGarantia());
                parametros.put("fechaVencimiento", JsfUtil.dateFormat(datos.getFechaVencimiento(),"dd 'de' MMMM 'de' yyyy"));
                parametros.put("montoGarantia", datos.getMonto());
                parametros.put("gerente",gerente);
                
                //REPORTE
                JasperReport reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/frmDevolucion.jasper"));
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros);
                JRExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                exporter.exportReport();
            }
        }catch (JRException ex) {
            Logger.getLogger(FrmDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ex){
            Logger.getLogger(FrmDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
