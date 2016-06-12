/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.PersonDAO;
import com.poslovnik.model.data.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;


/**
 *
 * @author mixa
 */
@WebServlet(name = "LoginServerlet", urlPatterns = {"/login"})
public class LoginServerlet extends HttpServlet {
    JSONObject json = new JSONObject();
    
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
        
        response.setContentType("application/json");
        response.sendError(400, "Bad request");
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
        
        response.setContentType("application/json");
        
        Person p = (Person) request.getSession().getAttribute("person");
        
        if (p == null) {
            json.put("success", false);
            json.put("message", "You are not logged in");
            
            
            response.getWriter().print(json.toString());
            
            return;
        }
        
        json.put("success", true);
        json.put("userData", convertUserDataToHashmap(p));
        
        response.getWriter().println(json.toString());
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
        
        response.setContentType("application/json");
        
        Person p = createPerson(request);
        
        Boolean success = false;
        try {
            success = PersonDAO.getInstance().checkExistence(EntityManagerWrapper.getEntityManager(), p);
            
            response.setStatus(200);
            
            json.put("userData", convertUserDataToHashmap(p));

            request.getSession().setAttribute("person", p);
        } catch (Exception ex) {
            Logger.getLogger(LoginServerlet.class.getName()).log(Level.SEVERE, null, ex);
            
            success = false;
            
            response.setStatus(400);
        }
        
        json.put("success", success);
        
        response.getWriter().println(json.toString());
    }
    
    private Person createPerson(HttpServletRequest request) {
        Person p = new Person();
        p.setEmail(request.getParameter("email"));
        p.setPassword(request.getParameter("password"));
        
        return p;
    }
    
    private HashMap<String,String> convertUserDataToHashmap(Person p)
    {
        HashMap<String,String> userData = new HashMap();
            
        userData.put("id", p.getId().toString());
        userData.put("email", p.getEmail());
        userData.put("first_name", p.getFirstName());
        userData.put("permission_level", Short.toString(p.getPermissionLevel()));
        
        return userData;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Processes log in requests";
    }// </editor-fold>

}
