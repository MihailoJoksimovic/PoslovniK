/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

import com.poslovnik.model.data.Person;
import com.sun.xml.internal.ws.util.Constants;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
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
            Query query = em.createQuery("SELECT id FROM Person WHERE email = :email AND password = :password");
            
            query.setParameter("email", person.getEmail());
            query.setParameter("password", person.getPassword());
            
            Integer id = (Integer) query.getSingleResult();
            
            person.setId(id);
            
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }
        
}
