/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw1028.db.MessagesXml;
import asw1028.db.structs.Datetime;
import asw1028.db.structs.Lastupdate;
import asw1028.db.structs.Msg;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Lorenzo
 * 
 */
//male che va: NewMsgAsync
@WebServlet(name = "NewMsgAsync", urlPatterns = {"/NewMessage"}, asyncSupported = true)
public class NewMsgAsync extends HttpServlet {

    HashMap<String, HashMap<String, Object>> ctxHandler = new HashMap<String, HashMap<String, Object>>();
    
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
        
        InputStream is = request.getInputStream();
        response.setContentType("text/xml;charset=UTF-8");

        try {
            ManageXML mngXML = new ManageXML();
            Document data = mngXML.parse(is);
            is.close();

            operations(data, request, response, mngXML);
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    private void operations(Document data, HttpServletRequest request, HttpServletResponse response, ManageXML mngXML) 
            throws Exception {
        
        //name of operation is message root
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        OutputStream out = response.getOutputStream();
        String success;
        switch(operation) {
            case "firstTimeAcces":
                System.out.println("First Time Access received: creating greetings document");
                //create the greetings (root) element
                success = createGreetingsDoc(data, request, response, mngXML);
                if(!(success.isEmpty())) {
                    //send the data to the client
                    WebUtils.sendErrorMessage(success, out);
                }
                break;
            case "newMsg":
                System.out.println("Operation newMsg received");
                // creo un nuovo messaggio nel db
                success = CreateMessage(data);
                if(success.isEmpty())
                    WebUtils.sendSimpleMessage("success", "Messaggio creato con successo.", out);
                else
                    WebUtils.sendErrorMessage(success, out);
                forwardNewMsg(data, mngXML);
                break;
            case "waitMsg":
                System.out.println("Operation waiting for msgs received");
		// (long) polling, meaning if there are new messages, give them to me
                askNewMsg(data, request, response, mngXML);
                break;
        }
    }
    
    private String CreateMessage(Document data) {
        String section = getSectionId(data); // eg. Matematica
        String idDisc = getDiscussionId(data); //discussion eg. polinomi
        String userid = null; // Alias autor eg. Nyaz
        String content = null;
        
        try {
            userid = WebUtils.getContentFromNode(data, new String[] { "autor"});
            content = WebUtils.getContentFromNode(data, new String[] { "content"});
        }
        catch (Exception e) {
            System.out.println("DANGER! Error in message's data fields");
            return "Error in message's data fields";
        }
        String msgPath = getServletContext().getRealPath(SysKb.getMsgsPath(section, idDisc));
        Msg msg = new Msg();
        msg.setAutor(userid);
        msg.setContent(content);
        Lastupdate lastUpTime = new Lastupdate();
        lastUpTime.setAutor(userid);
        lastUpTime.setDatetime(new Datetime(new Date()));
        msg.setLastupdate(lastUpTime);
        msg.setDatafiles(null);
        try {
            MessagesXml.addMsg(msg, msgPath);
        } catch (JAXBException ex) {
            Logger.getLogger(NewMsgAsync.class.getName()).log(Level.SEVERE, null, ex);
            return "Error while writing on the db";
        }
        return ""; // "" stands for no errors
    }
    
    private String createGreetingsDoc(Document data, HttpServletRequest request, HttpServletResponse response, ManageXML mngXML) {
        String section = getSectionId(data); // eg. Matematica
        String idDisc = getDiscussionId(data); //discussion eg. polinomi

        if(section.isEmpty() || idDisc.isEmpty()) {
            return "Impossibile trovare la discussione specificata";
        }
        String xmlMsgsFilePath = getServletContext().getRealPath(SysKb.getMsgsPath(section, idDisc));
        FileInputStream msgsFile;
        try {
            msgsFile = new FileInputStream(xmlMsgsFilePath);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewMsgAsync.class.getName()).log(Level.SEVERE, null, ex);
            return "Impossibile trovare i messaggi della discussione specificata.";
        }
        String id = addDiscussionViewer(section, idDisc);
        //Add the pseudoId as an attribute of root
        Document msgs;
        try {
            //System.out.println("Trying to parse msgs at path: "+xmlMsgsFilePath);
            msgs = mngXML.parse(msgsFile);
        } catch (IOException | SAXException ex) {
            Logger.getLogger(NewMsgAsync.class.getName()).log(Level.SEVERE, null, ex);
            return "Errore nella creazione dei dati del protocollo di greetings";
        }
        
        Document responseDoc = mngXML.newDocument("greetings");
        //create the node containing the pseudo-id
        //System.out.println("Attempting to create id element");
        Element clientId = responseDoc.createElement("clientid");
        //System.out.println("Attempting to create text node");
        clientId.appendChild(responseDoc.createTextNode(id));
        //append all nodes to the root
        //System.out.println("Attempting to add id element to "+responseDoc.toString());
        responseDoc.getDocumentElement().appendChild(clientId);
        //System.out.println("...and messages like: "+msgs.getDocumentElement().getNodeName());
        responseDoc.getDocumentElement().appendChild(responseDoc.importNode(msgs.getDocumentElement(), true));
        
        System.out.println("Adding data to the hashMap");
        synchronized(this) {
            HashMap<String, Object> newClient = new HashMap<String, Object>();
            newClient.put(id, new LinkedList<Document>());
            ctxHandler.put(section+id,newClient);
        }
        
        //Bind session
        request.getSession().setAttribute("clientid", id);
        
        System.out.println("Sending greetings to "+id);
        try {
            OutputStream out = response.getOutputStream();
            mngXML.transform(out,responseDoc);
            out.close();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(NewMsgAsync.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    
    private String addDiscussionViewer(String section, String discussion) {
        // greetings message: give to clients a pseudo-random identifier
        Date day = new Date();
        Random rnd = new Random(day.getTime());
        String pseudoId = "" + Calendar.HOUR_OF_DAY + Calendar.MINUTE + Calendar.MILLISECOND + rnd.nextInt();
        synchronized(this) {
            //viewer of a discussion
            HashMap<String, Object> viewer = new HashMap<String, Object>();
            viewer.put(pseudoId, new LinkedList<Document>());
            ctxHandler.put(section+discussion, viewer);
        }
        return pseudoId;
    }
    
    private String getDiscussionId(Document data) {
        String idDisc = null; //discussion eg. polinomi
        
        try {
            idDisc = WebUtils.getContentFromNode(data, new String[] { "iddisc"});
        }
        catch (Exception e) {
            System.out.println("DANGER! Error in the request for message data fields");
            return "";
        }
        if(idDisc == null) {
            return "";
        }
        return idDisc;
    }
    
    private String getSectionId(Document data) {
        String section = null; // eg. Matematica    
        
        try {
            section = WebUtils.getContentFromNode(data, new String[] { "section"});
        }
        catch (Exception e) {
            System.out.println("DANGER! Error in the request for message data fields");
            return "";
        }
        if(section == null) {
            return "";
        }
        return section;
    }
    
    private String getClientId(Document data) {
        String clientId = null;
        try {
            clientId = WebUtils.getContentFromNode(data, new String[] { "idclient" }); 
        }
        catch (Exception e) {
            System.out.println("DANGER! Error in the request for message data fields");
            return "";
        }
        
        if(clientId == null) {
            return "";
        }
        return clientId;
    }
    
    private void askNewMsg(Document data, HttpServletRequest request, HttpServletResponse response, ManageXML mngXML) {
        //if there are new msg send them to client
        Document msgsToSend = mngXML.newDocument("cometmsgs");
        String section = getSectionId(data); // eg. Matematica
        String idDisc = getDiscussionId(data); //discussion eg. polinomi
        String clientId = getClientId(data);
        
        if(section.isEmpty() || idDisc.isEmpty() || clientId.isEmpty()) {
            System.out.println("section: "+section);
            System.out.println("disc: "+idDisc);
            System.out.println("client: "+clientId);
            System.out.print("Error while reading some fields: ");
        }
        
        boolean async;
        synchronized (this) {
            HashMap<String, Object> clients = ctxHandler.get(section+idDisc);
            //TODO: il problema è qui!!
            LinkedList<Document> list = (LinkedList<Document>) clients.get(clientId);
            async = list.isEmpty();
            if (async) { //non ci sono messaggi disponibili -> crea il contesto asincrono
                //codice della richiesta che si blocca in attesa 
                AsyncContext asyncContext = request.startAsync(); //blocca l'invio della risposta per la richiesta e restituisce un oggetto AsyncContext
                asyncContext.setTimeout(10 * 1000); //set timeout oggetto AsyncContext
                asyncContext.addListener(new AsyncAdapter() {
                    @Override
                    public void onTimeout(AsyncEvent e) {
                        try {
                            ManageXML mngXML = new ManageXML();
                            //ottiene il contesto che ha generato l'evento -->
                            AsyncContext asyncContext = e.getAsyncContext();
                            // TODO da modificare
                            HttpServletRequest reqAsync = (HttpServletRequest) asyncContext.getRequest();
                            String user = (String) reqAsync.getSession().getAttribute("clientid");
                            System.out.println("timeout event launched");
                             // --> in questo modo può mandare dati sulla response
                            Document answer = mngXML.newDocument("timeout");
                            boolean confirm;
                            synchronized (NewMsgAsync.this) {
                                if (confirm = (clients.get(user) instanceof AsyncContext)) {
                                    clients.put(user, new LinkedList<Document>());
                                }
                            }
                            if (confirm) {
                                //manda il timeout solo se il client era effettivamente in attesa
                                OutputStream tos = asyncContext.getResponse().getOutputStream();
                                mngXML.transform(tos, answer);
                                tos.close();
                                // chiude la response e dichiara completata la gestione asincrona della richiesta (diattiva timeout)
                                asyncContext.complete();
                            }
                        } catch (ParserConfigurationException | IOException | TransformerException ex) {
                            System.out.print("Error in timeout handler: ");
                            System.out.println(ex);
                        }
                    }
                });
                clients.put(clientId, asyncContext); // lo memorizzo perchè un'altra richiesta risveglierà l'asyncContext
            } else {
                // fetch all the elements for a client
                for(Document msg : list) {
                    msgsToSend.appendChild(msg);
                }
            }
        }
        if (!async) {
            try {
                OutputStream os = response.getOutputStream();
                mngXML.transform(os, msgsToSend);
                os.close();
            } catch (IOException | TransformerException ex) {
                System.out.print("Error while sending the messages enqueue to clients");
                Logger.getLogger(NewMsgAsync.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void forwardNewMsg(Document data, ManageXML mngXML) {
        String section = getSectionId(data); // eg. Matematica
        String idDisc = getDiscussionId(data); //discussion eg. polinomi
        
        synchronized (this) { // la servlet può essere contattata da più client -> race condition
            HashMap<String, Object> clients = ctxHandler.get(section+idDisc);
            
            for (String destUser : clients.keySet()) {
                Object value = clients.get(destUser); //questi object sono
                if (value instanceof AsyncContext) { //AsyncContext se i client sono in attesa su una pop
                    try {
                        System.out.println("Forwarding a new message: ");
                        mngXML.transform(System.out, data);
                        OutputStream aos = ((AsyncContext) value).getResponse().getOutputStream();
                        mngXML.transform(aos, data);
                        aos.close();
                    } catch (IOException | TransformerException ex) {
                        Logger.getLogger(NewMsgAsync.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    ((AsyncContext) value).complete();
                    clients.put(destUser, new LinkedList<Document>());
                } else { //code di messaggi associati agli username se non hanno ancora effettuato una pop
                    ((LinkedList<Document>) value).addLast(data);
                }
            }
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

    public static void main() {
        
    }
}