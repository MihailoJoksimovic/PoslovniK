/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.service;

import com.google.gson.JsonParser;
import com.poslovnik.gson.GsonWrapper;
import com.poslovnik.model.bean.PayoutBean;
import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.PayoutDAO;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mixa
 */
public class PayoutService {
    private static final PayoutService instance = new PayoutService();

    private PayoutService() {

    }

    public static PayoutService getInstance() {
        return instance;
    }
    
    public Payout createEntityFromRequest(HttpServletRequest request) throws IOException, ParseException {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        Payout p = null;
        
        try {
            em.getTransaction().begin();
            
            JsonParser parser = new JsonParser();
            
            BufferedReader br = new BufferedReader(request.getReader());
            
            PayoutBean payoutBean = GsonWrapper.getGson().fromJson(br.readLine(), PayoutBean.class);
            
            Integer personId = payoutBean.getPerson_id();
            
            Person person = em.find(Person.class, personId);
            
            p = new Payout();
            
            p.setPersonId(person);
            p.setAmount(payoutBean.getAmount());
            p.setDate(payoutBean.getDate());
            p.setDescription(payoutBean.getDescription());
            p.setType(payoutBean.getType());
            
            PayoutDAO.getInstance().add(em, p);
            
            person.getPayoutCollection().add(p);
            
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
        
        return p;
    }
    
    public Payout editEntityFromRequest(HttpServletRequest request) throws IOException {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        Payout p = null;
        
        try {
            em.getTransaction().begin();
            
            JsonParser parser = new JsonParser();
            
            BufferedReader br = new BufferedReader(request.getReader());
            
            PayoutBean payoutBean = GsonWrapper.getGson().fromJson(br.readLine(), PayoutBean.class);
            
            Integer payoutId = payoutBean.getId();
            
            p = em.find(Payout.class, payoutId);
            
            Person person = p.getPersonId();
            
            // not sure if this is necessary but I'll just re-add this object
            // to the person's vacation collection
            person.getPayoutCollection().remove(p);
            
            p.setAmount(payoutBean.getAmount());
            p.setDate(payoutBean.getDate());
            p.setDescription(payoutBean.getDescription());
            p.setType(payoutBean.getType());

            PayoutDAO.getInstance().edit(em, p);
            
            person.getPayoutCollection().add(p);
            
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
        
        return p;
    }
    
    public void removeEntityFromRequest(HttpServletRequest req) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Integer payoutId = Integer.parseInt(req.getParameter("id"));
            
            Payout p = em.find(Payout.class, payoutId);
            
            Person person = p.getPersonId();
            
            PayoutDAO.getInstance().delete(em, p);
            
            person.getPayoutCollection().remove(p);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Collection<Payout> getPayoutListFromRequest(HttpServletRequest request) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        Collection<Payout> payoutList = new ArrayList<>();
        
        try {
            em.getTransaction().begin();
            
            Integer personId = Integer.parseInt(request.getParameter("id"));
            
            Person person = em.find(Person.class, personId);
            
            payoutList = person.getPayoutCollection();
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        
        return payoutList;
    }
}
