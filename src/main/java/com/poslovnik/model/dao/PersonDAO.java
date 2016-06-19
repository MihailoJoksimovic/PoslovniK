/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

import com.poslovnik.model.data.Person;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author mixa
 */
public class PersonDAO {
    private static final PersonDAO instance = new PersonDAO();

    private PersonDAO() {
    }

    public static PersonDAO getInstance() {
        return instance;
    }
    
    public boolean checkExistence(EntityManager em, Person person) throws Exception {
        try {
            Query query = em.createQuery("SELECT p FROM Person p WHERE p.email = :email AND p.password = :password");
            
            query.setParameter("email", person.getEmail());
            query.setParameter("password", person.getPassword());
            
            Person p = (Person) query.getSingleResult();
            
            person.setId(p.getId());
            person.setEmail(p.getEmail());
            person.setFirstName(p.getFirstName());
            person.setPermissionLevel(p.getPermissionLevel());
            
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }
    
    public List<Person> findAll(EntityManager em) {
        Query query = em.createQuery("SELECT p FROM Person p");
        return query.getResultList();
    }
    
    public Person findById(EntityManager em, Integer id) {
        Query query = em.createQuery("SELECT p FROM Person p WHERE p.id = :id");
                
        query.setParameter("id", id);
        
        return (Person) query.getSingleResult();
    }
    
    public Person findByEmail(EntityManager em, String email) {
        try {
            Query query = em.createQuery("SELECT p FROM Person p WHERE p.email = :email");
                
            query.setParameter("email", email);
            
            return (Person) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public void delete(EntityManager em, Person p) {
        em.remove(p);
    }
    
    public void add(EntityManager em, Person p) {
        em.persist(p);
    }
        
}
