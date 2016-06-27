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
import java.util.ArrayList;
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
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        Payout p = null;
        
        try {
            p = em.find(Payout.class, id);
        } finally {
            em.close();
        }
        
        return p;
    }
   

    @Override
    public List<Payout> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Payout> findAllForPerson(Person person) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        List<Payout> payouts = new ArrayList<>();
        
        try {
            payouts = PayoutDAO.getInstance().findAllForPerson(em, person);
        } finally {
            em.close();
        }
        
        return payouts;
    }
    
    
    public void addForPersonById(Payout payout, Integer personId)
    {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Person person = PersonService.getInstance().findById(personId);
            
            payout.setPersonId(person);
            
            PayoutDAO.getInstance().add(em, payout);
            
            em.getTransaction().commit();
            
//            person.getPayoutCollection().add(payout);
        } finally {
            em.close();
        }
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
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            PayoutDAO.getInstance().edit(em, p);
        
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        
        
    }

    @Override
    public void delete(Payout p) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Payout managedPayout = em.find(Payout.class, p.getId());

            PayoutDAO.getInstance().delete(em, managedPayout);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
        
    } 
    
    public void deleteById(Integer id) {
        EntityManager em = EntityManagerWrapper.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Payout p = em.find(Payout.class, id);
            
            p.getPersonId().getPayoutCollection().remove(p);
            
            em.remove(p);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    
}
