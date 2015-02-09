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
import java.io.IOException;
import java.io.InputStream;
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
@WebServlet(name = "NewMsgAsync", urlPatterns = {"/NewMsgAsync"})
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
        switch(operation) {
            case "newMsg":
                // creo un nuovo messaggio nel db
                CreateMessage(data);
                break;
            case "waitMsg":
                
                break;
        }
    }
    
    private void CreateMessage(Document data) {
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
