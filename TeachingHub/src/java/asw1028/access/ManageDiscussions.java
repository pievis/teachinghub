/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        response.setContentType("text/xml;charset=UTF-8");
        String section = request.getParameter("section"); //the requested section
        OutputStream out = response.getOutputStream();
        if(section == null){
            //Errore se non conosco la sezione
            sendError("Il parametro section non è specificato.", out);
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
