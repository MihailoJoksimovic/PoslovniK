/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.data;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mixa
 */
@Entity
@Table(name = "payout")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payout.findAll", query = "SELECT p FROM Payout p"),
    @NamedQuery(name = "Payout.findById", query = "SELECT p FROM Payout p WHERE p.id = :id"),
    @NamedQuery(name = "Payout.findByAmount", query = "SELECT p FROM Payout p WHERE p.amount = :amount"),
    @NamedQuery(name = "Payout.findByDate", query = "SELECT p FROM Payout p WHERE p.date = :date"),
    @NamedQuery(name = "Payout.findByDescription", query = "SELECT p FROM Payout p WHERE p.description = :description"),
    @NamedQuery(name = "Payout.findByType", query = "SELECT p FROM Payout p WHERE p.type = :type")})
public class Payout implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    @Expose
    private double amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "description")
    @Expose
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "type")
    @Expose
    private String type;
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Person personId;

    public Payout() {
    }

    public Payout(Integer id) {
        this.id = id;
    }

    public Payout(Integer id, double amount, Date date, String description, String type) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payout)) {
            return false;
        }
        Payout other = (Payout) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.poslovnik.model.data.Payout[ id=" + id + " ]";
    }
    
}
