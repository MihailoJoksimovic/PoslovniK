/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.service;

import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.PayoutDAO;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author mixa
 */
public class PayoutService implements CrudServiceInterface<Payout>{
    private static final PayoutService instance = new PayoutService();

    private EntityManager em = EntityManagerWrapper.getEntityManager();

    private PayoutService() {

    }

    public static PayoutService getInstance() {
        return instance;
    }

    @Override
    public Payout findById(Integer id) {
        return em.find(Payout.class, id);
    }
   

    @Override
    public List<Payout> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(Payout p) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        em.persist(p);
        em.getTransaction().commit();
    }

    @Override
    public void edit(Payout p) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        
        PayoutDAO.getInstance().edit(em, p);
        
        em.getTransaction().commit();
    }

    @Override
    public void delete(Payout p) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        
        PayoutDAO.getInstance().delete(em, p);
        
        em.getTransaction().commit();
    }
    
    
}
