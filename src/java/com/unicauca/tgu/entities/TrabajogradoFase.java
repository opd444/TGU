/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author seven
 */
@Entity
@Table(name = "TRABAJOGRADO_FASE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TrabajogradoFase.findAll", query = "SELECT t FROM TrabajogradoFase t"),
    @NamedQuery(name = "TrabajogradoFase.findByTrabajofaseid", query = "SELECT t FROM TrabajogradoFase t WHERE t.trabajofaseid = :trabajofaseid"),
    @NamedQuery(name = "TrabajogradoFase.findByEstado", query = "SELECT t FROM TrabajogradoFase t WHERE t.estado = :estado")})
public class TrabajogradoFase implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRABAJOFASEID")
    private BigDecimal trabajofaseid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO")
    private BigInteger estado;
    @JoinColumn(name = "TRABAJOID", referencedColumnName = "TRABAJOID")
    @ManyToOne(optional = false)
    private Trabajodegrado trabajoid;
    @JoinColumn(name = "FASEID", referencedColumnName = "FASEID")
    @ManyToOne(optional = false)
    private Fase faseid;

    public TrabajogradoFase() {
    }

    public TrabajogradoFase(BigDecimal trabajofaseid) {
        this.trabajofaseid = trabajofaseid;
    }

    public TrabajogradoFase(BigDecimal trabajofaseid, BigInteger estado) {
        this.trabajofaseid = trabajofaseid;
        this.estado = estado;
    }

    public BigDecimal getTrabajofaseid() {
        return trabajofaseid;
    }

    public void setTrabajofaseid(BigDecimal trabajofaseid) {
        this.trabajofaseid = trabajofaseid;
    }

    public BigInteger getEstado() {
        return estado;
    }

    public void setEstado(BigInteger estado) {
        this.estado = estado;
    }

    public Trabajodegrado getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(Trabajodegrado trabajoid) {
        this.trabajoid = trabajoid;
    }

    public Fase getFaseid() {
        return faseid;
    }

    public void setFaseid(Fase faseid) {
        this.faseid = faseid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trabajofaseid != null ? trabajofaseid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TrabajogradoFase)) {
            return false;
        }
        TrabajogradoFase other = (TrabajogradoFase) object;
        if ((this.trabajofaseid == null && other.trabajofaseid != null) || (this.trabajofaseid != null && !this.trabajofaseid.equals(other.trabajofaseid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.TrabajogradoFase[ trabajofaseid=" + trabajofaseid + " ]";
    }
    
}
