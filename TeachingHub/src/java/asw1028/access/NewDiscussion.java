/*
 * Questa servlet riceve un xml con informazioni sulla nuova discussione,
 * controlla che i dati siano corretti e successivamente li salva sul db.
 */
package asw1028.access;

import asw1028.db.ThreadsXml;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import asw1028.db.structs.Thread;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;

/**
 * Questa servlet riceve un xml con informazioni sulla nuova discussione,
 * controlla che i dati siano corretti e successivamente li salva sul db.
 * @author Pievis
 */
public class NewDiscussion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
        HttpSession session = request.getSession();
        
        String userid = null, sectionid = null;
        Document doc = null;
        ManageXML mxml;
        try {
            mxml = new ManageXML();
            doc = mxml.parse(in);
            userid = WebUtils.getContentFromNode(doc, new String[] { "userid"});
            sectionid = WebUtils.getContentFromNode(doc, new String[] { "sectionid"});
        } catch (Exception e){
            e.printStackTrace();
        }
        
        if(!userid.equals(session.getAttribute("userid"))){
            //L'utente non è quello in sessione, è un errore
            WebUtils.sendErrorMessage("Errore con la sessione utente", out);
            return;
        }
        //procedo prendendo i dati dal content
        String title = null, description = null, msg = null;
        try{
            title = WebUtils.getContentFromNode(doc, new String[] { "content", "title"});
            description = WebUtils.getContentFromNode(doc, new String[] { "content", "description"});
            msg = WebUtils.getContentFromNode(doc, new String[] { "content", "msg"});
        }catch(Exception e){
            e.printStackTrace();
            WebUtils.sendErrorMessage("Errore nel formato dati ricevuto", out);
            return;
        }
        //Controlli lato server
        if(title.length() < 4){
            WebUtils.sendErrorMessage("Titolo troppo corto", out);
            return;
        }
        if(msg.length() < 4){
            WebUtils.sendErrorMessage("Messaggio troppo corto", out);
            return;
        }
        //Passa alla creazione del thread con nuovo messaggio
    }
    
    private void createNewThread(String title, String description, String userid, String sectionid){
        String xmlDbThreadsPath = SysKb.getThreadsPathForSection(sectionid);
        String threadId = ThreadsXml.getNewThreadId(xmlDbThreadsPath);
        if(threadId == null){
            //la sezione è vuota
            threadId = "0";
        }
        Thread newThread = new Thread();
        newThread.setAutor(userid);
        newThread.setDescription(description);
        newThread.setTitle(title);
        newThread.setId(threadId);
        Date currentDate = new Date();
        newThread.setCreationdate(currentDate);
        newThread.setLastupdate(currentDate, userid);
        
        try {
            List<Thread> threads = ThreadsXml.getThreads(xmlDbThreadsPath).getThread();
            
        } catch (Exception ex) {
            Logger.getLogger(NewDiscussion.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
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
