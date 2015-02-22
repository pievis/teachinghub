/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw1028.db.ThreadsXml;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This servlet returns a list of discussions of the specified section.
 * @author Pierluigi
 */
public class ManageDiscussions extends HttpServlet {

    /**
     * Returns the threads xml for the specified section
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
    
    private void operations(Document data, HttpServletRequest request, HttpServletResponse response, ManageXML mngXML) throws IOException {
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        OutputStream out = response.getOutputStream();
        
        switch(operation) {
            case "getDiscussion" : //this replace the service provided by the old servlet GetThreads
                System.out.println("Getting all of the discussions");
                getAllDiscussions(data, out);
                break;
            case "getDiscussionInfo" :
                System.out.println("Getting the discussion info");
                getDiscussionInfo(data,out,mngXML);
                break;
        }
    }
    
    /***
     * Send into the output stream the XML containing all the discussions for a given section
     * @param data Data received form the client, containing the section id
     * @param out Output stream, where redirect the xml file
     */
    private void getAllDiscussions(Document data, OutputStream out) {
        //Take the section id in the received document
        String section = getSectionId(data);
        
        if(section.isEmpty()) {
            System.out.println("Error in data received form the client");
            return;
        }

        String xmlThredsFilePath = getServletContext().getRealPath(
                            SysKb.getThreadsPathForSection(section));
        
        if(!(new File(xmlThredsFilePath).exists())){
            //Se non esiste è un altro errore
            sendError("Impossibile trovare i threads della sezione specificata.", out);
            return;
        }
        //Recupero il file e lo ritorno in output come response
        WebUtils.fileToOutputStream(xmlThredsFilePath, out);
    }
    
    private void getDiscussionInfo(Document data, OutputStream out, ManageXML mngXML) {
        //Take the section id and the discussion id in the received document
        String section = getSectionId(data);
        String discussion = getDiscussionId(data);
        
        if(section.isEmpty() || discussion.isEmpty()) {
            System.out.println("Error in data received form the client");
            return;
        }
        String xmlThredsFilePath = getServletContext().getRealPath(
                            SysKb.getThreadsPathForSection(section));
        if(!(new File(xmlThredsFilePath).exists())){
            //Se non esiste è un altro errore
            sendError("Impossibile trovare i threads della sezione specificata.", out);
            return;
        }
        
        try {
            Document discXml = ThreadsXml.getThreadById(xmlThredsFilePath, discussion);
            //send to the client
            mngXML.transform(out, discXml);
        } catch (JAXBException | TransformerConfigurationException | ParserConfigurationException ex) {
            System.out.println("Error while getting data from database");
            ex.printStackTrace();
            Logger.getLogger(ManageDiscussions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException | IOException ex) {
            System.out.println("Error while sending data to the client");
            ex.printStackTrace();
            Logger.getLogger(ManageDiscussions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
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
    
    void sendError(String text, OutputStream out){
        WebUtils.sendErrorMessage(text, out);
        System.out.println(text);
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
