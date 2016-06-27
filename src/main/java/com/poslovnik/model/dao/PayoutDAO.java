/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Vacation;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author mixa
 */
public class PayoutDAO {
    private static final PayoutDAO instance = new PayoutDAO();

    private PayoutDAO() {
    }

    public static PayoutDAO getInstance() {
        return instance;
    }
    
    public void add(EntityManager em, Payout p) {
        em.persist(p);
    }
    
    public List<Payout> findAllForPerson (EntityManager em, Person p) {
        Query query = em.createQuery("SELECT p FROM Payout p WHERE p.personId = :person_id");
        query.setParameter("person_id", p);
        return query.getResultList();
    }
    
    public void edit(EntityManager em, Payout p) {
        Payout merged = em.merge(p);

        em.persist(merged);
    }
    
    public void delete(EntityManager em, Payout p) {
        em.remove(p);
    }
}
