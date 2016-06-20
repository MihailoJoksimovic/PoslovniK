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
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.poslovnik.exception.DuplicateEntryException;
import com.poslovnik.exception.ValidationException;
import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.PersonDAO;
import com.poslovnik.model.dao.PositionDAO;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Position;
import com.poslovnik.service.PersonService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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
        
        Person p = createPersonFromRequest(request, tip);
        
        switch (tip) {
            case DELETE: {
                deleteAction(request, response, p);
                break;
            }
            case ADD:
                addAction(request, response, p);
                break;
            case EDIT:
                editAction(request, response, p);
                break;
            default:
                throw new ServletException("Unknown action requested!");
        }
        
        response.getWriter().print(json.toString());
    }
    
    private void addAction(HttpServletRequest request, HttpServletResponse response, Person p) throws IOException {
        PersonService ps = PersonService.getInstance();
        
        try {
            ps.add(p);
            
            json.put("success", true);
        } catch (ValidationException | ConstraintViolationException ex) {
            response.sendError(400, "Validation failed");
        }
    }
            
    private void listAction(HttpServletRequest request, HttpServletResponse response) {
        PersonService ps = PersonService.getInstance();
        
        JSONArray jsonArr = new JSONArray();
        
        for (Person p : ps.findAll()) {
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

    private void editAction(HttpServletRequest request, HttpServletResponse response, Person p) throws IOException {
        PersonService ps = PersonService.getInstance();
        
        try {
            ps.edit(p);
            
            json.put("success", true);
        } catch (ValidationException | ConstraintViolationException ex) {
            response.sendError(400, "Validation failed");
        }
    }
    
    private void deleteAction(HttpServletRequest request, HttpServletResponse response, Person p) throws IOException {
        PersonService ps = PersonService.getInstance();
        
        ps.delete(p);
            
        json.put("success", true);
    }

    private Person createPersonFromRequest(HttpServletRequest request, ActionType tipAkcije) throws IOException {
        PersonService ps = PersonService.getInstance();
        
        Person p = new Person();
        
        String personIdString = request.getParameter("id");
            
        if (personIdString != null) {
            Integer personId = Integer.parseInt(request.getParameter("id"));
            
            p = ps.findById(personId);
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

        Position pos = PositionDAO.getInstance().findById(EntityManagerWrapper.getEntityManager(), new Short(pb.getPosition()));
        p.setPosition(pos);
            
       return p;
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
