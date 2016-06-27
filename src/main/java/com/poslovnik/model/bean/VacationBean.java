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
public class VacationBean {
    @Expose
    private Integer id;

    @Expose
    private Date date_from;

    @Expose
    private Date date_to;

    @Expose
    private String status;

    @Expose
    private Integer person_id;

    public Integer getId() {
        return id;
    }

    public Date getDate_from() {
        return date_from;
    }

    public Date getDate_to() {
        return date_to;
    }

    public String getStatus() {
        return status;
    }

    public Integer getPerson_id() {
        return person_id;
    }
}
