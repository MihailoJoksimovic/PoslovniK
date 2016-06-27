/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.bean;

import com.google.gson.annotations.Expose;
import java.util.Date;

/**
 *
 * @author mixa
 */
public class PayoutBean {
    @Expose
    private double amount;

    @Expose
    private Integer id;

    @Expose
    private Date date;

    @Expose
    private String description;

    @Expose
    private String type;

    @Expose
    private Integer person_id;

    public double getAmount() {
        return amount;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public String getType() {
        return type;
    }
        
}
