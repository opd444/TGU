/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author seven
 */
@Entity
@Table(name = "PROGRAMA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programa.findAll", query = "SELECT p FROM Programa p"),
    @NamedQuery(name = "Programa.findByProgramaid", query = "SELECT p FROM Programa p WHERE p.programaid = :programaid"),
    @NamedQuery(name = "Programa.findByProgramanombre", query = "SELECT p FROM Programa p WHERE p.programanombre = :programanombre")})
public class Programa implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROGRAMAID")
    private BigDecimal programaid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "PROGRAMANOMBRE")
    private String programanombre;
    @JoinColumn(name = "FACULTADID", referencedColumnName = "FACULTADID")
    @ManyToOne(optional = false)
    private Facultad facultadid;

    public Programa() {
    }

    public Programa(BigDecimal programaid) {
        this.programaid = programaid;
    }

    public Programa(BigDecimal programaid, String programanombre) {
        this.programaid = programaid;
        this.programanombre = programanombre;
    }

    public BigDecimal getProgramaid() {
        return programaid;
    }

    public void setProgramaid(BigDecimal programaid) {
        this.programaid = programaid;
    }

    public String getProgramanombre() {
        return programanombre;
    }

    public void setProgramanombre(String programanombre) {
        this.programanombre = programanombre;
    }

    public Facultad getFacultadid() {
        return facultadid;
    }

    public void setFacultadid(Facultad facultadid) {
        this.facultadid = facultadid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (programaid != null ? programaid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programa)) {
            return false;
        }
        Programa other = (Programa) object;
        if ((this.programaid == null && other.programaid != null) || (this.programaid != null && !this.programaid.equals(other.programaid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Programa[ programaid=" + programaid + " ]";
    }
    
}
