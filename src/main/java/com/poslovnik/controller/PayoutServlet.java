/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poslovnik.exception.NoSuchPersonException;
import com.poslovnik.gson.GsonWrapper;
import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Person;
import com.poslovnik.service.PayoutService;
import com.poslovnik.service.PersonService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
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
public class PayoutServlet extends HttpServlet {
    
    JSONObject json = new JSONObject();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        String action = request.getParameter("action");
        ActionType tip = ActionType.getForAction(action);
        
        switch (tip) {
            case LIST:
                listAction(request, response);
                break;
            default:
                response.sendError(400, "Bad request - unknown action requested");
                
                return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ActionType tip = ActionType.getForAction(action);
        
        Payout p = getObjectFromRequest(request);
        
        switch (tip) {
            case ADD:
                addAction(request, p);
                break;
            default:
                throw new ServletException("Unknown action requested!");
        }
        
        response.getWriter().print(json.toString());
        
//        try {
//            EntityManagerWrapper.getEntityManager().flush();
//        } catch (TransactionRequiredException ex) {
//            // ..
//        }
        
    }
    
    private void listAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Person p;
        
        try {
            Integer personId = Integer.parseInt(request.getParameter("id"));
            
            p = PersonService.getInstance().findById(personId);
            
            if (p == null) {
                throw new NoSuchPersonException();
            }
        } catch (NumberFormatException | NoSuchPersonException ex) {
            response.sendError(400, "Bad request - missing/invalid person ID");
            
            return;
        }
        
        Collection<Payout> payouts = p.getPayoutCollection();
        
        Gson gson = GsonWrapper.getGson();
        
        String payuotsJsonArray = gson.toJson(payouts);
        
        response.getOutputStream().print(payuotsJsonArray);
    }
    
    private void addAction(HttpServletRequest request, Payout p) throws IOException {
        Integer personId = Integer.parseInt(request.getParameter("person_id"));
        
        Person person = PersonService.getInstance().findById(personId);
        
        PersonService.getInstance().addPayout(person, p);
        
        json.put("success", true);
    }

    private Payout getObjectFromRequest(HttpServletRequest request) throws IOException {
        Payout p;
        
        Gson gson = GsonWrapper.getGson();
        
        BufferedReader br = new BufferedReader(request.getReader());
        
        p = gson.fromJson(br.readLine(), Payout.class);
        
        if (p.getId() == null) {
            Integer personId = Integer.parseInt(request.getParameter("person_id"));
            
            Person person = PersonService.getInstance().findById(personId);
            
            p.setPersonId(person);
        }
        
        return p;
    }
}
