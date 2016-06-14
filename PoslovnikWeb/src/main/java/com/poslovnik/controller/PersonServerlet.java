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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mixa
 */
public class PersonServerlet extends HttpServlet {
    JSONObject json = new JSONObject();
  
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
        String action = request.getParameter("action");
        TipAkcije tip = TipAkcije.getForAction(action);
        
        switch (tip) {
            case LIST: {
                listAction(request, response);
                break;
            }
            default:
                throw new ServletException("Unknown action requested!");
        }
        
        response.getWriter().print(json.toString());
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
        
        
        String action = request.getParameter("action");
        TipAkcije tip = TipAkcije.getForAction(action);
        
        switch (tip) {
            case DELETE: {
                Person p = createPerson(request, tip);
                deleteAction(request, response, p);
                break;
            }
            default:
                throw new ServletException("Unknown action requested!");
        }
        
        response.getWriter().print(json.toString());
    }
    
    private void listAction(HttpServletRequest request, HttpServletResponse response) {
        EntityManager em = EntityManagerWrapper.getEntityManager();

        List<Person> personList = PersonDAO.getInstance().findAll(em);
        
        JSONArray jsonArr = new JSONArray();
        
        for (Person p : personList) {
            JSONObject jsonObj = new JSONObject();
            
            jsonObj.put("id", p.getId());
            jsonObj.put("email", p.getEmail());
            jsonObj.put("first_name", p.getFirstName());
            jsonObj.put("last_name", p.getLastName());
            jsonObj.put("title", p.getTitle());
            jsonObj.put("permission_level", p.getPermissionLevel());
            jsonObj.put("position_name", p.getPosition().getName());
            
            jsonArr.put(jsonObj);
        }
        
        json.put("data", jsonArr);
    }

    private void deleteAction(HttpServletRequest request, HttpServletResponse response, Person p) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        em.getTransaction().begin();
        
        Person managedPerson = em.find(Person.class, p.getId());
        
        PersonDAO.getInstance().delete(em, managedPerson);

        em.getTransaction().commit();
        
        json.put("success", true);
    }
    
    private Person createPerson(HttpServletRequest request, TipAkcije tipAkcije) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        Person p = new Person();
        
        Integer personId = Integer.parseInt(request.getParameter("id"));
        
        if (tipAkcije == TipAkcije.DELETE) {
            p = PersonDAO.getInstance().findById(em, personId);
        }
        
        return p;
        
    }
            

}
