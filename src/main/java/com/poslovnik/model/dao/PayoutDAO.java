/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import javax.persistence.EntityManager;

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
    
    public void edit(EntityManager em, Payout p) {
        Payout merged = em.merge(p);

        em.persist(merged);
    }
    
    public void delete(EntityManager em, Payout p) {
        Payout merged = em.merge(p);
        
        em.remove(merged);
    }
}
