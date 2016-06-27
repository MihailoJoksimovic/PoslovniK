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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
/**
 *
 * @author mixa
 */
public class PersonService {
    private static final PersonService instance = new PersonService();
        
    private PersonService() {
        
    }
    
    public static PersonService getInstance() {
        return instance;
    }
    
    public Person findById(Integer id) {
        Person p = null;
        
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            p = em.find(Person.class, id);
        } finally {
            em.close();
        }

        return p;
    }
    
    public List<Person> findAll() {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        List<Person> persons = new ArrayList<>();
        
        try {
            em.getTransaction().begin();
            
            persons = PersonDAO.getInstance().findAll(em);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        
        return persons;
        
    }
    
    public void add(Person p) throws ValidationException {
        validate(p);
        
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();

            PersonDAO.getInstance().add(em, p);

            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }
    
    public void edit(Person p) throws ValidationException {
        validate(p);
        
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();

            PersonDAO.getInstance().edit(em, p);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public void deleteById(Integer id) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Person p = em.find(Person.class, id);
            
            em.remove(p);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    private void validate(Person p) throws ValidationException {
        
    }
    
    private EntityManager getEntityManager() {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        return em;
    }
}
