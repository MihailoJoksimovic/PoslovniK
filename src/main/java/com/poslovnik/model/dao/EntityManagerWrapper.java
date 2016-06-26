package com.poslovnik.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerWrapper {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PoslovnikPU");
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
