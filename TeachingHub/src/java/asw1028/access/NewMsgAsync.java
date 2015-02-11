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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import org.w3c.dom.*;

/**
 *
 * @author Lorenzo
 * 
 */
//male che va: NewMsgAsync
@WebServlet(name = "NewMsgAsync", urlPatterns = {"/NewMessage"})
public class NewMsgAsync extends HttpServlet {

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
            System.out.println("Parsing xml file");
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
        HttpSession session = request.getSession();
        //name of operation is message root
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        OutputStream out = response.getOutputStream();
        String success;
        switch(operation) {
            case "firstTimeAcces":
                takeAllMessages(data, out);
                break;
            case "newMsg":
                // creo un nuovo messaggio nel db
                success = CreateMessage(data);
                if(success.isEmpty())
                    WebUtils.sendSimpleMessage("success", "Messaggio creato con successo.", out);
                else
                    WebUtils.sendErrorMessage(success, out);
                break;
            case "waitMsg":
                
                break;
        }
    }
    
    private String CreateMessage(Document data) {
        String section = null; // eg. Matematica
        String idDisc = null; //discussion eg. polinomi
        String userid = null; // eg. Nyaz
        String content = null;
        
        try {
            section = WebUtils.getContentFromNode(data, new String[] { "section"});
            idDisc = WebUtils.getContentFromNode(data, new String[] { "iddisc"});
            userid = WebUtils.getContentFromNode(data, new String[] { "userid"});
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
    
    private void takeAllMessages(Document data, OutputStream out) {
        String section = null; // eg. Matematica
        String idDisc = null; //discussion eg. polinomi
        
        try {
            section = WebUtils.getContentFromNode(data, new String[] { "section"});
            idDisc = WebUtils.getContentFromNode(data, new String[] { "iddisc"});
        }
        catch (Exception e) {
            System.out.println("DANGER! Error in the request for message data fields");
            WebUtils.sendErrorMessage( "Errore nei dati inviati", out);
            return;
        }
        if(section == null || idDisc == null) {
            WebUtils.sendErrorMessage( "Impossibile trovare la discussione specificata", out);
            return;
        }
        String xmlMsgsFilePath = getServletContext().getRealPath(
                            SysKb.getMsgsPath(section, idDisc));
        
        if(!(new File(xmlMsgsFilePath).exists())){
            //handle not existing file case
            WebUtils.sendErrorMessage("Impossibile trovare i messaggi della discussione specificata.", out);
            return;
        }
        WebUtils.fileToOutputStream(xmlMsgsFilePath, out);
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