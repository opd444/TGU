/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "FASE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fase.findAll", query = "SELECT f FROM Fase f"),
    @NamedQuery(name = "Fase.findByFaseid", query = "SELECT f FROM Fase f WHERE f.faseid = :faseid"),
    @NamedQuery(name = "Fase.findByFasenombre", query = "SELECT f FROM Fase f WHERE f.fasenombre = :fasenombre"),
    @NamedQuery(name = "Fase.findByFaseorden", query = "SELECT f FROM Fase f WHERE f.faseorden = :faseorden")})
public class Fase implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FASEID")
    private BigDecimal faseid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "FASENOMBRE")
    private String fasenombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FASEORDEN")
    private BigInteger faseorden;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faseid")
    private List<TrabajogradoFase> trabajogradoFaseList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faseid")
    private List<Formatoproducto> formatoproductoList;

    public Fase() {
    }

    public Fase(BigDecimal faseid) {
        this.faseid = faseid;
    }

    public Fase(BigDecimal faseid, String fasenombre, BigInteger faseorden) {
        this.faseid = faseid;
        this.fasenombre = fasenombre;
        this.faseorden = faseorden;
    }

    public BigDecimal getFaseid() {
        return faseid;
    }

    public void setFaseid(BigDecimal faseid) {
        this.faseid = faseid;
    }

    public String getFasenombre() {
        return fasenombre;
    }

    public void setFasenombre(String fasenombre) {
        this.fasenombre = fasenombre;
    }

    public BigInteger getFaseorden() {
        return faseorden;
    }

    public void setFaseorden(BigInteger faseorden) {
        this.faseorden = faseorden;
    }

    @XmlTransient
    public List<TrabajogradoFase> getTrabajogradoFaseList() {
        return trabajogradoFaseList;
    }

    public void setTrabajogradoFaseList(List<TrabajogradoFase> trabajogradoFaseList) {
        this.trabajogradoFaseList = trabajogradoFaseList;
    }

    @XmlTransient
    public List<Formatoproducto> getFormatoproductoList() {
        return formatoproductoList;
    }

    public void setFormatoproductoList(List<Formatoproducto> formatoproductoList) {
        this.formatoproductoList = formatoproductoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faseid != null ? faseid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fase)) {
            return false;
        }
        Fase other = (Fase) object;
        if ((this.faseid == null && other.faseid != null) || (this.faseid != null && !this.faseid.equals(other.faseid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Fase[ faseid=" + faseid + " ]";
    }
    
}
