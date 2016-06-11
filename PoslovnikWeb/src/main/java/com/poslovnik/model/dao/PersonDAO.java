/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.dao;

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
}
