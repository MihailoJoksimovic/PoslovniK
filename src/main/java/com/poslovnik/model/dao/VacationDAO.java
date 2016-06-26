/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Vacation;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author mixa
 */
public class VacationDAO {
    private static final VacationDAO instance = new VacationDAO();

    private VacationDAO() {
    }

    public static VacationDAO getInstance() {
        return instance;
    }
    
    public List<Vacation> findAllForPerson (EntityManager em, Person p) {
        Query query = em.createQuery("SELECT v FROM Vacation v WHERE v.personId = :person_id");
        query.setParameter("person_id", p);
        return query.getResultList();
    }
    
    public void add(EntityManager em, Vacation v) {
        em.persist(v);
    }
    
    public void edit(EntityManager em, Vacation v) {
        Vacation merged = em.merge(v);

        em.persist(merged);
    }
    
    public void delete(EntityManager em, Vacation v) {
//        Vacation merged = em.merge(v);
        
        em.remove(v);
    }
}
