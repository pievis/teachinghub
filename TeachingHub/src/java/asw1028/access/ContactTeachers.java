/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw.interfaces.ITeacher;
import asw1028.db.UsersManager;
import asw1028.db.structs.Teacher;
import asw1028.utils.SysKb;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Lorenzo
 */
public class ContactTeachers extends HttpServlet {

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
        //response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher rd = null;
        try {
            //Business logic: take all the teachers
            String teachersPath = getServletContext().getRealPath(SysKb.xmlDbTeachers);
            String studentsPath = getServletContext().getRealPath(SysKb.xmlDbStudents);
            System.out.println("STUDENTSPATH: "+studentsPath);
            System.out.println("TEACHERSPATH: "+teachersPath);
            UsersManager userMng = new UsersManager(teachersPath, studentsPath);
            List<Teacher> teachers = userMng.getAllTeacher();
            //and forward to the JSP
            request.setAttribute("teachers", teachers);
            rd = request.getRequestDispatcher("/jsp/teachersList.jsp");            
        } catch (JAXBException ex) {
            System.out.println("Error while reading teachers from DB");
            ex.printStackTrace();
            // or forward to an error page
            request.setAttribute("message", "Errore durante la lettura dei file xml");
            request.setAttribute("exception", ex);
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
