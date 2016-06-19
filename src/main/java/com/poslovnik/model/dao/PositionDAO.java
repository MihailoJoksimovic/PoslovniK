/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

import com.poslovnik.model.data.Position;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author mixa
 */
public class PositionDAO {
    private static final PositionDAO instance = new PositionDAO();

    private PositionDAO() {
    }

    public static PositionDAO getInstance() {
        return instance;
    }
    
    public List<Position> findAll(EntityManager em) {
        Query query = em.createQuery("SELECT p FROM Position p");
        return query.getResultList();
    }
    
    public Position findById(EntityManager em, Short id) {
        Query query = em.createQuery("SELECT p FROM Position p WHERE p.id = :id");
                
        query.setParameter("id", id);
        
        return (Position) query.getSingleResult();
    }
        
}
