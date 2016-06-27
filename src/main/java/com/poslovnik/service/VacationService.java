/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poslovnik.gson.GsonWrapper;
import com.poslovnik.model.bean.VacationBean;
import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.VacationDAO;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Vacation;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mixa
 */
public class VacationService {
    private static final VacationService instance = new VacationService();

    private VacationService() {

    }

    public static VacationService getInstance() {
        return instance;
    }
    
    public Vacation createEntityFromRequest(HttpServletRequest request) throws IOException, ParseException {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        Vacation v = null;
        
        try {
            em.getTransaction().begin();
            
            JsonParser parser = new JsonParser();
            
            BufferedReader br = new BufferedReader(request.getReader());
            
            VacationBean vacationBean = GsonWrapper.getGson().fromJson(br.readLine(), VacationBean.class);
            
            Integer personId = vacationBean.getPerson_id();
            
            Person person = em.find(Person.class, personId);
            
            v = new Vacation();
            v.setPersonId(person);
            v.setDateFrom(vacationBean.getDate_from());
            v.setDateTo(vacationBean.getDate_to());
            v.setStatus(vacationBean.getStatus());
            
            
            VacationDAO.getInstance().add(em, v);
            
            person.getVacationCollection().add(v);
            
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
        
        return v;
    }
    
    public Vacation editEntityFromRequest(HttpServletRequest request) throws IOException {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        Vacation v = null;
        
        try {
            em.getTransaction().begin();
            
            JsonParser parser = new JsonParser();
            
            BufferedReader br = new BufferedReader(request.getReader());
            
            VacationBean vacationBean = GsonWrapper.getGson().fromJson(br.readLine(), VacationBean.class);
            
            Integer vacationId = vacationBean.getId();
            
            v = em.find(Vacation.class, vacationId);
            
            Person person = v.getPersonId();
            
            // not sure if this is necessary but I'll just re-add this object
            // to the person's vacation collection
            person.getVacationCollection().remove(v);

            v.setDateFrom(vacationBean.getDate_from());
            v.setDateTo(vacationBean.getDate_to());
            v.setStatus(vacationBean.getStatus());
            
            VacationDAO.getInstance().edit(em, v);
            
            person.getVacationCollection().add(v);
            
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
        
        return v;
    }
    
    public Collection<Vacation> getVacationListFromRequest(HttpServletRequest request) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        Collection<Vacation> vacationList = new ArrayList<>();
        
        try {
            em.getTransaction().begin();
            
            Integer personId = Integer.parseInt(request.getParameter("id"));
            
            Person person = em.find(Person.class, personId);
            
            vacationList = person.getVacationCollection();
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        
        return vacationList;
    }

    public void removeEntityFromRequest(HttpServletRequest req) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Integer vacationId = Integer.parseInt(req.getParameter("id"));
            
            Vacation v = em.find(Vacation.class, vacationId);
            
            Person person = v.getPersonId();
            
            VacationDAO.getInstance().delete(em, v);
            
            person.getVacationCollection().remove(v);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
