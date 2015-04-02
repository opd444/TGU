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
@Table(name = "TRABAJODEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trabajodegrado.findAll", query = "SELECT t FROM Trabajodegrado t"),
    @NamedQuery(name = "Trabajodegrado.findByTrabajoid", query = "SELECT t FROM Trabajodegrado t WHERE t.trabajoid = :trabajoid"),
    @NamedQuery(name = "Trabajodegrado.findByTrabajonombre", query = "SELECT t FROM Trabajodegrado t WHERE t.trabajonombre = :trabajonombre")})
public class Trabajodegrado implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRABAJOID")
    private BigDecimal trabajoid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "TRABAJONOMBRE")
    private String trabajonombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoid")
    private List<TrabajogradoFase> trabajogradoFaseList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoid")
    private List<Productodetrabajo> productodetrabajoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoid")
    private List<UsuarioRolTrabajogrado> usuarioRolTrabajogradoList;

    public Trabajodegrado() {
    }

    public Trabajodegrado(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public Trabajodegrado(BigDecimal trabajoid, String trabajonombre) {
        this.trabajoid = trabajoid;
        this.trabajonombre = trabajonombre;
    }

    public BigDecimal getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(BigDecimal trabajoid) {
        this.trabajoid = trabajoid;
    }

    public String getTrabajonombre() {
        return trabajonombre;
    }

    public void setTrabajonombre(String trabajonombre) {
        this.trabajonombre = trabajonombre;
    }

    @XmlTransient
    public List<TrabajogradoFase> getTrabajogradoFaseList() {
        return trabajogradoFaseList;
    }

    public void setTrabajogradoFaseList(List<TrabajogradoFase> trabajogradoFaseList) {
        this.trabajogradoFaseList = trabajogradoFaseList;
    }

    @XmlTransient
    public List<Productodetrabajo> getProductodetrabajoList() {
        return productodetrabajoList;
    }

    public void setProductodetrabajoList(List<Productodetrabajo> productodetrabajoList) {
        this.productodetrabajoList = productodetrabajoList;
    }

    @XmlTransient
    public List<UsuarioRolTrabajogrado> getUsuarioRolTrabajogradoList() {
        return usuarioRolTrabajogradoList;
    }

    public void setUsuarioRolTrabajogradoList(List<UsuarioRolTrabajogrado> usuarioRolTrabajogradoList) {
        this.usuarioRolTrabajogradoList = usuarioRolTrabajogradoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trabajoid != null ? trabajoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabajodegrado)) {
            return false;
        }
        Trabajodegrado other = (Trabajodegrado) object;
        if ((this.trabajoid == null && other.trabajoid != null) || (this.trabajoid != null && !this.trabajoid.equals(other.trabajoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Trabajodegrado[ trabajoid=" + trabajoid + " ]";
    }
    
}
