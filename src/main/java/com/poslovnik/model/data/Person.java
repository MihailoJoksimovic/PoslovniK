/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.NoSuchEntityException;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mixa
 */
@Entity
@Table(name = "person")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")})
public class Person implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personId", orphanRemoval=true, fetch=FetchType.EAGER)
    private Collection<Vacation> vacationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personId", orphanRemoval=true, fetch=FetchType.EAGER)
    private Collection<Payout> payoutCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "salt")
    private String salt;
    @Column(name = "dt_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAdded;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 7)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "permission_level")
    private short permissionLevel;
    @JoinColumn(name = "position", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Position position;

    public Person() {
    }

    public Person(Integer id) {
        this.id = id;
    }

    public Person(Integer id, String email, String password, String salt, String firstName, String lastName, short permissionLevel) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.permissionLevel = permissionLevel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getDtAdded() {
        return dtAdded;
    }

    public void setDtAdded(Date dtAdded) {
        this.dtAdded = dtAdded;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(short permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.poslovnik.model.data.Person[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Payout> getPayoutCollection() {
//        if (payoutCollection == null) {
//            payoutCollection = new ArrayList<Payout>();
//        }

        return payoutCollection;
    }

    public void setPayoutCollection(Collection<Payout> payoutCollection) {
        this.payoutCollection = payoutCollection;
    }
    
    public void addPayout(Payout payout) {
        payout.setPersonId(this);
        this.getPayoutCollection().add(payout);
    }
    
    public Payout getPayoutById(Integer id) {
        for (Payout p : getPayoutCollection()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        
        throw new NoSuchEntityException();
    }

    @XmlTransient
    public Collection<Vacation> getVacationCollection() {
//        if (vacationCollection == null) {
//            vacationCollection = new ArrayList<Vacation>();
//        }

        return vacationCollection;
    }

    public void setVacationCollection(Collection<Vacation> vacationCollection) {
        this.vacationCollection = vacationCollection;
    }

    public Vacation getVacationById(Integer id) {
        for (Vacation v : getVacationCollection()) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        
        throw new NoSuchEntityException();
    }

    public void addVacation(Vacation v) {
        v.setPersonId(this);
        this.getVacationCollection().add(v);
    }
    
}
