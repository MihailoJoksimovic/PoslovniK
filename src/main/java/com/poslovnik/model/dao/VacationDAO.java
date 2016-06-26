/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

import com.poslovnik.model.data.Vacation;
import javax.persistence.EntityManager;

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
    
    public void add(EntityManager em, Vacation v) {
        em.persist(v);
    }
    
    public void edit(EntityManager em, Vacation v) {
        Vacation merged = em.merge(v);

        em.persist(merged);
    }
    
    public void delete(EntityManager em, Vacation v) {
        Vacation merged = em.merge(v);
        
        em.remove(merged);
    }
}
