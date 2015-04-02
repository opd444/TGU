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
@Table(name = "PRODUCTO_RESTRICCIONFRECUENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductoRestriccionfrecuencia.findAll", query = "SELECT p FROM ProductoRestriccionfrecuencia p"),
    @NamedQuery(name = "ProductoRestriccionfrecuencia.findByProductorestriccionid", query = "SELECT p FROM ProductoRestriccionfrecuencia p WHERE p.productorestriccionid = :productorestriccionid"),
    @NamedQuery(name = "ProductoRestriccionfrecuencia.findByValoractual", query = "SELECT p FROM ProductoRestriccionfrecuencia p WHERE p.valoractual = :valoractual")})
public class ProductoRestriccionfrecuencia implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTORESTRICCIONID")
    private BigDecimal productorestriccionid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALORACTUAL")
    private BigInteger valoractual;
    @JoinColumn(name = "RESTRICCIONID", referencedColumnName = "RESTRICCIONID")
    @ManyToOne(optional = false)
    private Restriccionfrecuencia restriccionid;
    @JoinColumn(name = "PRODUCTOID", referencedColumnName = "PRODUCTOID")
    @ManyToOne(optional = false)
    private Productodetrabajo productoid;

    public ProductoRestriccionfrecuencia() {
    }

    public ProductoRestriccionfrecuencia(BigDecimal productorestriccionid) {
        this.productorestriccionid = productorestriccionid;
    }

    public ProductoRestriccionfrecuencia(BigDecimal productorestriccionid, BigInteger valoractual) {
        this.productorestriccionid = productorestriccionid;
        this.valoractual = valoractual;
    }

    public BigDecimal getProductorestriccionid() {
        return productorestriccionid;
    }

    public void setProductorestriccionid(BigDecimal productorestriccionid) {
        this.productorestriccionid = productorestriccionid;
    }

    public BigInteger getValoractual() {
        return valoractual;
    }

    public void setValoractual(BigInteger valoractual) {
        this.valoractual = valoractual;
    }

    public Restriccionfrecuencia getRestriccionid() {
        return restriccionid;
    }

    public void setRestriccionid(Restriccionfrecuencia restriccionid) {
        this.restriccionid = restriccionid;
    }

    public Productodetrabajo getProductoid() {
        return productoid;
    }

    public void setProductoid(Productodetrabajo productoid) {
        this.productoid = productoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productorestriccionid != null ? productorestriccionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoRestriccionfrecuencia)) {
            return false;
        }
        ProductoRestriccionfrecuencia other = (ProductoRestriccionfrecuencia) object;
        if ((this.productorestriccionid == null && other.productorestriccionid != null) || (this.productorestriccionid != null && !this.productorestriccionid.equals(other.productorestriccionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.ProductoRestriccionfrecuencia[ productorestriccionid=" + productorestriccionid + " ]";
    }
    
}
