/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.poslovnik.exception.DuplicateEntryException;
import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.PersonDAO;
import com.poslovnik.model.dao.PositionDAO;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Position;
import java.io.BufferedReader;
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
import javax.validation.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mixa
 */
public class PersonServlet extends HttpServlet {
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
        ActionType tip = ActionType.getForAction(action);
        
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
        ActionType tip = ActionType.getForAction(action);
        
        Person p;
        
        switch (tip) {
            case DELETE: {
                p = createPerson(request, tip);
                deleteAction(request, response, p);
                break;
            }
            case ADD:
                p = createPerson(request, tip);
                addAction(request, response, p);
                break;
            case EDIT:
                p = createPerson(request, tip);
                editAction(request, response, p);
                break;
            default:
                throw new ServletException("Unknown action requested!");
        }
        
        response.getWriter().print(json.toString());
    }
    
    private void addAction(HttpServletRequest request, HttpServletResponse response, Person p) throws IOException {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        // Check for duplicates
        Person duplicate = PersonDAO.getInstance().findByEmail(em, p.getEmail());
        
        if (duplicate != null) {
            response.sendError(409, "Duplicate entry");
            
            return;
        } 
        
        try {
            em.getTransaction().begin();

            PersonDAO.getInstance().add(em, p);

            em.getTransaction().commit();

            json.put("success", true);
        } catch (ConstraintViolationException ex) {
            response.sendError(400, "Invalid data provided!");
            
            return;
        }
        
    }
            
    private void listAction(HttpServletRequest request, HttpServletResponse response) {
        EntityManager em = EntityManagerWrapper.getEntityManager();

        List<Person> personList = PersonDAO.getInstance().findAll(em);
        
        JSONArray jsonArr = new JSONArray();
        
        for (Person p : personList) {
            JSONObject jsonObj = new JSONObject();
            
            jsonObj.put("id", p.getId());
            jsonObj.put("email", p.getEmail());
            jsonObj.put("password", p.getPassword());
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
    
    private Person createPerson(HttpServletRequest request, ActionType tipAkcije) throws IOException {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        Person p = new Person();
        
        String personIdString = request.getParameter("id");
            
        if (personIdString != null) {
            Integer personId = Integer.parseInt(request.getParameter("id"));
            
            p = PersonDAO.getInstance().findById(em, personId);
        }
        
        if (tipAkcije == tipAkcije.DELETE) {
            return p;
        }

        Gson g = new Gson();

        BufferedReader br = new BufferedReader(request.getReader());

        PersonBean pb = g.fromJson(br.readLine(), PersonBean.class);

        p.setEmail(pb.getEmail());
        p.setFirstName(pb.getFirst_name());
        p.setLastName(pb.getLast_name());
        p.setTitle(pb.getTitle());
        p.setPassword(pb.getPassword());
        p.setSalt("salt");
        p.setPermissionLevel(new Short(pb.getAccount_type()));

        Position pos = PositionDAO.getInstance().findById(em, new Short(pb.getPosition()));
        p.setPosition(pos);
        
            
       return p;
        
    }

    private void editAction(HttpServletRequest request, HttpServletResponse response, Person p) throws IOException {
        
        try {
            EntityManager em = EntityManagerWrapper.getEntityManager();
        
            em.getTransaction().begin();

            PersonDAO.getInstance().edit(em, p);

            em.getTransaction().commit();

            json.put("success", true);
        } catch (ConstraintViolationException ex) {
            response.sendError(400, "Invalid data provided!");
            
            return;
        } catch (Exception ex) {
            response.sendError(500, "Unknown error");
            
            return;
        }
    }
    
    private class PersonBean {
        private String email;
        private String password;
        private String position;
        private String first_name;
        private String last_name;
        private String title;
        private String account_type;
                
                

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAccount_type() {
            return account_type;
        }

        public void setAccount_type(String account_type) {
            this.account_type = account_type;
        }
        
        
        
    }
            

}
