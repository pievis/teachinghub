/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Mario
 */
@MultipartConfig(
fileSizeThreshold=32,
maxFileSize=1024*1024,
maxRequestSize=1024*1024*5)
public class UploadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Part part=request.getPart("file1");
        String dest=request.getParameter("dest1");
        
        out.print("<HTML><HEAD><TITLE>");
        out.print("</TITLE></HEAD><BODY>");
        out.print("<h1>Your file data</h1>");
        out.print("<h1>file header</h1>");
        out.print("<p>");
        out.print("File Name : "+part.getName());
        out.print("<br>");
        out.print("Content Type : "+part.getContentType());
        out.print("<br>");
        out.print("File Size : "+part.getSize());
        out.print("<br>");
        for(String header : part.getHeaderNames()) {
            out.print(header + " : "+part.getHeader(header));
            out.print("<br>");
        }
        out.print("</p>");
        if (part.getContentType().equals("text/plain")) {
            out.print("<h1>file content</h1>");        
            out.print("<pre>");
            BufferedReader inPart = new BufferedReader(new InputStreamReader(part.getInputStream()));
            String s;
            while ((s = inPart.readLine()) != null) {
                out.println(s);
            }
            inPart.close();
            out.print("</pre>");
        }
        out.print("</BODY></HTML>");  
        out.close();    
        if (!dest.equals("")) part.write(dest);  
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
