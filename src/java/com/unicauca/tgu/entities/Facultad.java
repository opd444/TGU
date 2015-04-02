/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author seven
 */
@Entity
@Table(name = "FACULTAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facultad.findAll", query = "SELECT f FROM Facultad f"),
    @NamedQuery(name = "Facultad.findByFacultadid", query = "SELECT f FROM Facultad f WHERE f.facultadid = :facultadid"),
    @NamedQuery(name = "Facultad.findByFacultadnombre", query = "SELECT f FROM Facultad f WHERE f.facultadnombre = :facultadnombre")})
public class Facultad implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FACULTADID")
    private BigDecimal facultadid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "FACULTADNOMBRE")
    private String facultadnombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facultadid")
    private List<Programa> programaList;
    @OneToMany(mappedBy = "facultadid")
    private List<Usuario> usuarioList;

    public Facultad() {
    }

    public Facultad(BigDecimal facultadid) {
        this.facultadid = facultadid;
    }

    public Facultad(BigDecimal facultadid, String facultadnombre) {
        this.facultadid = facultadid;
        this.facultadnombre = facultadnombre;
    }

    public BigDecimal getFacultadid() {
        return facultadid;
    }

    public void setFacultadid(BigDecimal facultadid) {
        this.facultadid = facultadid;
    }

    public String getFacultadnombre() {
        return facultadnombre;
    }

    public void setFacultadnombre(String facultadnombre) {
        this.facultadnombre = facultadnombre;
    }

    @XmlTransient
    public List<Programa> getProgramaList() {
        return programaList;
    }

    public void setProgramaList(List<Programa> programaList) {
        this.programaList = programaList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facultadid != null ? facultadid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facultad)) {
            return false;
        }
        Facultad other = (Facultad) object;
        if ((this.facultadid == null && other.facultadid != null) || (this.facultadid != null && !this.facultadid.equals(other.facultadid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Facultad[ facultadid=" + facultadid + " ]";
    }
    
}
