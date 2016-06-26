/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.service;

import com.poslovnik.model.dao.EntityManagerWrapper;
import com.poslovnik.model.dao.PayoutDAO;
import com.poslovnik.model.dao.VacationDAO;
import com.poslovnik.model.data.Payout;
import com.poslovnik.model.data.Person;
import com.poslovnik.model.data.Vacation;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author mixa
 */
public class VacationService implements CrudServiceInterface<Vacation> {
    private static final VacationService instance = new VacationService();

    private EntityManager em = EntityManagerWrapper.getEntityManager();

    private VacationService() {

    }

    public static VacationService getInstance() {
        return instance;
    }

    @Override
    public Vacation findById(Integer id) {
        return em.find(Vacation.class, id);
    }
   

    @Override
    public List<Vacation> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(Vacation p) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        em.persist(p);
        em.getTransaction().commit();
    }

    @Override
    public void edit(Vacation v) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        
        VacationDAO.getInstance().edit(em, v);
        
        em.getTransaction().commit();
    }

    @Override
    public void delete(Vacation v) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        
        Person person = v.getPersonId();
        
        // Remove it from the Person's collection
        Vacation managedVacation = person.getVacationById(v.getId());
        
        person.getVacationCollection().remove(managedVacation);
        
        // And remove it from DB
        VacationDAO.getInstance().delete(em, v);
        
        em.getTransaction().commit();
    }
}
