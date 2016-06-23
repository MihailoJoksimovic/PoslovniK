/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

import com.google.gson.Gson;
import com.poslovnik.exception.NoSuchPersonException;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import com.poslovnik.service.PersonService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        
        for (Payout payout : payouts) {
            // Remove reference to Person entity
            // so that we can serialize the object
            // easily :)
            
            payout.setPersonId(null);
        }
        
        Gson gson = new Gson();
        
        String payuotsJsonArray = gson.toJson(payouts);
        
        json.put("success", true);
        json.put("data", payuotsJsonArray);
        
        response.getOutputStream().print(json.toString());
    }

    
}
