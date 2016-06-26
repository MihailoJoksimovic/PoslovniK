/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
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
import java.util.Date;
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
        
        Payout p = new Payout();
        
        if (tip != tip.DELETE) {
            p = getObjectFromRequest(request);
        }

        switch (tip) {
            case ADD:
                addAction(request, p);
                break;
            case EDIT:
                editAction(request, p);
                break;
            case DELETE:
                deleteAction(request);
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
        Payout p = new Payout();
        
        Gson gson = GsonWrapper.getGson();
        
        PayoutBean bean = new PayoutBean();
        
        BufferedReader br = new BufferedReader(request.getReader());
        
        bean = gson.fromJson(br.readLine(), PayoutBean.class);
        
        Person person = PersonService.getInstance().findById(bean.getPerson_id());
        
        if (bean.getId() != null) {
            p = person.getPayoutById(bean.getId());
        }
        
        p.setAmount(bean.getAmount());
        p.setDate(bean.getDate());
        p.setType(bean.getType());
        p.setDescription(bean.getDescription());
        
        p.setPersonId(person);
        
        return p;
    }

    private void editAction(HttpServletRequest request, Payout p) {
        PayoutService.getInstance().edit(p);
        
        json.put("success", true);
    }
    
    private void deleteAction(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        Payout p = PayoutService.getInstance().findById(id);
        
        PayoutService.getInstance().delete(p);
        
        json.put("success", true);
    }
    
    private class PayoutBean {
        @Expose
        private double amount;
        
        @Expose
        private Integer id;
        
        @Expose
        private Date date;
        
        @Expose
        private String description;
        
        @Expose
        private String type;
        
        @Expose
        private Integer person_id;

        public double getAmount() {
            return amount;
        }

        public Integer getId() {
            return id;
        }

        public Date getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public Integer getPerson_id() {
            return person_id;
        }

        public String getType() {
            return type;
        }
        
        
    }
}
