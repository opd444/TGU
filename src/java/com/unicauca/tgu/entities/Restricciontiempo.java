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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "RESTRICCIONTIEMPO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Restricciontiempo.findAll", query = "SELECT r FROM Restricciontiempo r"),
    @NamedQuery(name = "Restricciontiempo.findByRestriccionid", query = "SELECT r FROM Restricciontiempo r WHERE r.restriccionid = :restriccionid"),
    @NamedQuery(name = "Restricciontiempo.findByRestriccionnombre", query = "SELECT r FROM Restricciontiempo r WHERE r.restriccionnombre = :restriccionnombre"),
    @NamedQuery(name = "Restricciontiempo.findByRestriccionestado", query = "SELECT r FROM Restricciontiempo r WHERE r.restriccionestado = :restriccionestado"),
    @NamedQuery(name = "Restricciontiempo.findByMaxduracion", query = "SELECT r FROM Restricciontiempo r WHERE r.maxduracion = :maxduracion")})
public class Restricciontiempo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RESTRICCIONID")
    private BigDecimal restriccionid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "RESTRICCIONNOMBRE")
    private String restriccionnombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RESTRICCIONESTADO")
    private BigInteger restriccionestado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAXDURACION")
    private BigInteger maxduracion;
    @JoinColumn(name = "FORMATOID", referencedColumnName = "FORMATOID")
    @ManyToOne(optional = false)
    private Formatoproducto formatoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restriccionid")
    private List<ProductoRestricciontiempo> productoRestricciontiempoList;

    public Restricciontiempo() {
    }

    public Restricciontiempo(BigDecimal restriccionid) {
        this.restriccionid = restriccionid;
    }

    public Restricciontiempo(BigDecimal restriccionid, String restriccionnombre, BigInteger restriccionestado, BigInteger maxduracion) {
        this.restriccionid = restriccionid;
        this.restriccionnombre = restriccionnombre;
        this.restriccionestado = restriccionestado;
        this.maxduracion = maxduracion;
    }

    public BigDecimal getRestriccionid() {
        return restriccionid;
    }

    public void setRestriccionid(BigDecimal restriccionid) {
        this.restriccionid = restriccionid;
    }

    public String getRestriccionnombre() {
        return restriccionnombre;
    }

    public void setRestriccionnombre(String restriccionnombre) {
        this.restriccionnombre = restriccionnombre;
    }

    public BigInteger getRestriccionestado() {
        return restriccionestado;
    }

    public void setRestriccionestado(BigInteger restriccionestado) {
        this.restriccionestado = restriccionestado;
    }

    public BigInteger getMaxduracion() {
        return maxduracion;
    }

    public void setMaxduracion(BigInteger maxduracion) {
        this.maxduracion = maxduracion;
    }

    public Formatoproducto getFormatoid() {
        return formatoid;
    }

    public void setFormatoid(Formatoproducto formatoid) {
        this.formatoid = formatoid;
    }

    @XmlTransient
    public List<ProductoRestricciontiempo> getProductoRestricciontiempoList() {
        return productoRestricciontiempoList;
    }

    public void setProductoRestricciontiempoList(List<ProductoRestricciontiempo> productoRestricciontiempoList) {
        this.productoRestricciontiempoList = productoRestricciontiempoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (restriccionid != null ? restriccionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Restricciontiempo)) {
            return false;
        }
        Restricciontiempo other = (Restricciontiempo) object;
        if ((this.restriccionid == null && other.restriccionid != null) || (this.restriccionid != null && !this.restriccionid.equals(other.restriccionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Restricciontiempo[ restriccionid=" + restriccionid + " ]";
    }
    
}
