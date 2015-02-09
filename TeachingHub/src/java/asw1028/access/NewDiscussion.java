/*
 * Questa servlet riceve un xml con informazioni sulla nuova discussione,
 * controlla che i dati siano corretti e successivamente li salva sul db.
 */
package asw1028.access;

import asw.interfaces.IUser;
import asw1028.db.MessagesXml;
import asw1028.db.ThreadsXml;
import asw1028.db.structs.Datetime;
import asw1028.db.structs.Lastupdate;
import asw1028.db.structs.Msg;
import asw1028.db.structs.Msgs;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import asw1028.db.structs.Thread;
import asw1028.db.structs.Threads;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Questa servlet riceve un xml con informazioni sulla nuova discussione,
 * controlla che i dati siano corretti e successivamente li salva sul db.
 * @author Pievis
 */
public class NewDiscussion extends HttpServlet {

    ManageXML mxml;
    
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
        
        try {
            mxml = new ManageXML();
            doc = mxml.parse(in);
            userid = WebUtils.getContentFromNode(doc, new String[] { "userid"});
            sectionid = WebUtils.getContentFromNode(doc, new String[] { "sectionid"});
        } catch (Exception e){
            WebUtils.sendErrorMessage("Impossibile ottenere le informazioni necessarie dalla richiesta", out);
            e.printStackTrace();
            return;
        }
        
//        System.out.println("ui " + userid + "\nSessione: " + session.getAttribute("userid"));
        
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
        String xmlDbThreadsPath = getServletContext().getRealPath(SysKb.getThreadsPathForSection(sectionid));
        try{
            //nuovo thread id
            String threadId = ThreadsXml.getNewThreadId(xmlDbThreadsPath);
            //Passa alla creazione del thread con nuovo messaggio
            createNewThread(threadId, title, description, userid, sectionid);
            //Aggiungi un nuovo messaggio alla discussione
            addNewMsgToThread("0", threadId, sectionid, userid, msg);
            //Rispondi al client con success e le info sulla nuova discussione
            sendSuccessMsg(threadId, out);
        }catch(Exception e){
            e.printStackTrace();
            WebUtils.sendErrorMessage("Errore nella creazione del messaggio.", out);
        }
        
    }
    
    private void sendSuccessMsg(String threadId, OutputStream out){
        List<Pair<String, String>> elements;
        elements = new ArrayList<Pair<String,String>>();
        elements.add(new Pair<String,String>("threadid", threadId));
        WebUtils.sendElementsMessage("success", elements, out);
    }
    
    private void addNewMsgToThread(String msgId, String threadId, String sectionid, String userId, String content) throws JAXBException{
        String xmlDbMsgsPath = getServletContext().getRealPath(SysKb.getMsgsPath(sectionid, threadId));
        Msg msg = new Msg();
        msg.setAutor(userId);
        msg.setContent(content);
        //TODO msg.setDatafiles(null);
        msg.setId(msgId);
        Lastupdate lu = new Lastupdate();
        lu.setDatetime(new Datetime(new Date()));
        lu.setAutor(userId);
        msg.setLastupdate(lu);
        Msgs msgs = MessagesXml.getMessages(xmlDbMsgsPath);
        msgs.getMsg().add(msg);
        MessagesXml.setMsgs(msgs, xmlDbMsgsPath); //commit
    }
    
    private void createNewThread(String threadId, String title, String description, String userid, String sectionid){
        String xmlDbThreadsPath = getServletContext().getRealPath(SysKb.getThreadsPathForSection(sectionid));
        String msgsFilePath = getServletContext().getRealPath(SysKb.getMsgsPath(sectionid, threadId));
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
            Threads ts = ThreadsXml.getThreads(xmlDbThreadsPath);
            ts.getThread().add(newThread);
            ThreadsXml.setThreads(ts, xmlDbThreadsPath);
        } catch (Exception ex) {
            Logger.getLogger(NewDiscussion.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //Create a new msgID.xml file
        Msgs msgs = new Msgs();
        msgs.setThreadid(threadId);
        File msgXml = new File(msgsFilePath);
        try {
            marshallMsgs(msgs, new FileOutputStream(msgXml));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewDiscussion.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * Codifica Msgs in xml e lo scrive sull'outputstream 
     **/
    private void marshallMsgs(Msgs msgs, OutputStream out){
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(Msgs.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            jaxbMarshaller.marshal(msgs, System.out);
            jaxbMarshaller.marshal(msgs, out);
        }catch(Exception e){
            e.printStackTrace();
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
