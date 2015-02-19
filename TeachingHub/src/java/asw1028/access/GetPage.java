/*
 * Questa classe ritorna una pagina scritta da un docente basandosi su 
 * sezione e id pagina.
 */
package asw1028.access;

import asw1028.db.PagesXml;
import asw1028.db.structs.Page;
import asw1028.db.structs.Pages;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import org.w3c.dom.Document;

/**
 *
 * @author Pievis
 */
public class GetPage extends HttpServlet {

    /**
     * Ritorna una o più Pages in xml, basandosi su idpagina e idsezione
     * (Passati tramite un xml strutturato)
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        InputStream in = request.getInputStream();
        OutputStream out = response.getOutputStream();
        
        ManageXML mxml;
        Document doc = null;
        try {
            mxml = new ManageXML();
            doc = mxml.parse(in);
        } catch (Exception e){
            e.printStackTrace();
            sendError("Errore nel processare la richiesta", out);
            return;
        }
        
        //Servlet action is defined by the root tag name
        String action = doc.getDocumentElement().getTagName();
        switch(action){
            case "getpage":
                getPage(doc, out);
                break;
            case "getpages":
                getPages(doc, out);
                break;
            default:
                sendError("Impossibile eseguire l'azione richiesta", out);
        }
    }
    
    /**
     * Ritorna al client un file xml con le informazioni sulla pagina richiesta.
     * La pagina è identificata da una sezione e un id pagina.
     * <getpage>
     *  </id>
     *  </section>
     * </getpage>
     **/ 
    private void getPage(Document doc, OutputStream out){
        String pageid = WebUtils.getContentFromNode(doc, new String[] { "id"});
        String sectionid = WebUtils.getContentFromNode(doc, new String[] { "section"});
        
        String xmlPagesPath = getServletContext().getRealPath(SysKb.getPagesPathForSection(sectionid));
        //Errori
        if(!(new File(xmlPagesPath)).exists())
            sendError("Impossibile trovare la pagina richiesta", out);
        //Processo
        try{
            Pages pgs = PagesXml.getPages(xmlPagesPath);
            for(Page p : pgs.getPage()){
                if(p.getId().equals(pageid))
                    returnPageToClient(p, out);
            }
        }catch (Exception e){
            e.printStackTrace();
            sendError("Errore nel reperire la pagina richiesta.", out);
        }
    }
    
    /**
     * manda in output tutte le pagine per la sezione specificata in doc
     **/
    private void getPages(Document doc, OutputStream out){
        String sectionid = WebUtils.getContentFromNode(doc, new String[] { "section"});
        String xmlPagesPath = getServletContext().getRealPath(SysKb.getPagesPathForSection(sectionid));
        WebUtils.fileToOutputStream(xmlPagesPath, out);
    }
    
    /**
     * Codifica la pagina in xml e lo scrive sull'outputstream 
     **/
    private void returnPageToClient(Page p, OutputStream out) throws PropertyException, JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(Page.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(p, out);
    }
    
    private void sendError(String text, OutputStream out){
        System.out.println(text);
        WebUtils.sendErrorMessage(text, out);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
