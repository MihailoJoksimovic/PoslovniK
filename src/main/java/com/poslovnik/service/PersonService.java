/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.service;

import com.poslovnik.exception.ValidationException;
import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.PersonDAO;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Vacation;
import java.util.List;
import javax.persistence.EntityManager;
/**
 *
 * @author mixa
 */
public class PersonService {
    private static final PersonService instance = new PersonService();
    
    private EntityManager em;
    
    private PersonService() {
        
    }
    
    public static PersonService getInstance() {
        return instance;
    }
    
    public Person findById(Integer id) {
        return PersonDAO.getInstance().findById(getEntityManager(), id);
    }
    
    public List<Person> findAll() {
        return PersonDAO.getInstance().findAll(getEntityManager());
    }
    
    public void add(Person p) throws ValidationException {
        validate(p);
        
        if (!getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().begin();
        }
        
        PersonDAO.getInstance().add(em, p);
        
        getEntityManager().getTransaction().commit();
    }
    
    public void addPayout(Person p, Payout payout) {
        
        if (!getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().begin();
        }
        
        p.addPayout(payout);
        
        getEntityManager().persist(payout);
        
        getEntityManager().getTransaction().commit();
    }
    
    public void edit(Person p) throws ValidationException {
        validate(p);
        
        if (!getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().begin();
        }
        
        PersonDAO.getInstance().edit(em, p);
        
        getEntityManager().getTransaction().commit();
    }
    
    public void delete(Person p) {
        if (!getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().begin();
        }
        
        PersonDAO.getInstance().delete(em, p);
        
        getEntityManager().getTransaction().commit();
    }
    
    private void validate(Person p) throws ValidationException {
        
    }
    
    private EntityManager getEntityManager() {
        if (em instanceof EntityManager) {
            return em;
        }
        
        em = EntityManagerWrapper.getEntityManager();
        
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void addVacation(Person person, Vacation v) {
        if (!getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().begin();
        }
        
        person.addVacation(v);
        
        getEntityManager().persist(v);
        
        getEntityManager().getTransaction().commit();
    }
}
