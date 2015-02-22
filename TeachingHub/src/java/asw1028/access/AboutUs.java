/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw1028.db.structs.Aboutus;
import asw1028.db.structs.Msgs;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Pievis
 */
public class AboutUs extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * Implementa il MVC e passa l'oggetto Aboutus a aboutus.jsp
     * con un forward di request/response
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        try{
            //Getting aboutus info
            JAXBContext jaxbContext = JAXBContext.newInstance(Aboutus.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            String filepath = getServletContext().getRealPath("/WEB-INF/xml/aboutus.xml");
            Aboutus au = (Aboutus) jaxbUnmarshaller.unmarshal(new File(filepath));
            //Forward to...
            request.setAttribute("aboutus", au);
            rd = request.getRequestDispatcher("/jsp/aboutus.jsp");
        }catch(Exception e){
            e.printStackTrace();
            request.setAttribute("message", "Errore durante la lettura dei file xml");
            request.setAttribute("exception", e);
            rd = request.getRequestDispatcher("/jsp/error.jsp");
        }
        rd.forward(request, response);
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
