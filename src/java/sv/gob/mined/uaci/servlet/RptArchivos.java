/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
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
import sv.gob.mined.uaci.entidades.Archivo;
import sv.gob.mined.uaci.servlet.datasources.ArchivosDatasource;
import sv.gob.mined.uaci.servlet.reportes.ArchivoExp;

/**
 *
 * @author ALEX
 */
public class RptArchivos extends HttpServlet {

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
        try {
            
            ServletOutputStream out = response.getOutputStream();
            //PARAMETROS ENVIADOS AL SERVLET
            List<Archivo> archivos = ((List<Archivo>) (request.getSession().getAttribute("archivos")));
            
            if(archivos != null){
                response.setContentType("application/pdf");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                //Se crea una instacia de matriz para guardar y enviar los parametros que ocupa el reporte
                Map parametros = new HashMap();
                parametros.put("urlLogo", getServletContext().getRealPath("resources/reportes/escudoES.jpg"));
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                JasperReport reporte = null;
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/rptArchivo.jasper"));
                
                
                ArchivosDatasource datasource = new ArchivosDatasource();
                for(Archivo a : archivos){
                    ArchivoExp exp = new ArchivoExp();
                    exp.setRegistradoPor(a.getRegistradoPor());
                    exp.setCaja(a.getCaja());
                    exp.setfRecepcion(a.getFechaIngresoLabel());
                    exp.setProceso(a.getContrato().getProceso().getCodProceso());
                    exp.setDescripcion(a.getContrato().getProceso().getDescripcion());
                    exp.setContrato(a.getContrato().getNumero());
                    exp.setProveedor(a.getContrato().getContratista().getNombre());
                    if(a.getEstatusFolio() != null){
                        exp.setFolio(a.getEstatusFolio().getEstado());
                    }
                    exp.setTecnico(a.getContrato().getProceso().getTecnico().getNombreCompleto());
                    if(a.getEstatusDigit() != null){
                        exp.setDigit(a.getEstatusDigit().getEstado());
                    }
                    if(a.getFechaEntrega()!= null){
                        exp.setfEntrega(a.getFechaEntregaLabel());
                    }
                    exp.setMonto(a.getContrato().getMonto());
                    exp.setPlazo(a.getContrato().getPlazo());
                    exp.setNumPag(a.getNumpag());
                    exp.setAmpo(a.getAmpo());
                    datasource.addLista(exp);  
                }
                //GENERAR REPORTE
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,parametros,datasource);
                JRExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                exporter.exportReport();
            }else{
                throw new Exception("Reporte Vacio");
            }
        }catch (JRException ex) {
            Logger.getLogger(RptGarantiasVencidas.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception ex){
            Logger.getLogger(RptGarantiasVencidas.class.getName()).log(Level.SEVERE, null, ex);
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
