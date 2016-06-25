/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.service;

import java.util.List;

/**
 *
 * @author mixa
 */
public interface CrudServiceInterface<T> {
    public T findById(Integer id);
    
    public List<T> findAll();
    
    public void add(T p);

    public void edit(T p);
    
    public void delete(T p);
}
