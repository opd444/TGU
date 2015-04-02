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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author seven
 */
@Entity
@Table(name = "PRODUCTODETRABAJO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productodetrabajo.findAll", query = "SELECT p FROM Productodetrabajo p"),
    @NamedQuery(name = "Productodetrabajo.findByProductoid", query = "SELECT p FROM Productodetrabajo p WHERE p.productoid = :productoid"),
    @NamedQuery(name = "Productodetrabajo.findByProductoaprobado", query = "SELECT p FROM Productodetrabajo p WHERE p.productoaprobado = :productoaprobado")})
public class Productodetrabajo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTOID")
    private BigDecimal productoid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTOAPROBADO")
    private BigInteger productoaprobado;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "PRODUCTOCONTENIDO")
    private String productocontenido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoid")
    private List<ProductoRestriccionfrecuencia> productoRestriccionfrecuenciaList;
    @JoinColumn(name = "TRABAJOID", referencedColumnName = "TRABAJOID")
    @ManyToOne(optional = false)
    private Trabajodegrado trabajoid;
    @JoinColumn(name = "FORMATOID", referencedColumnName = "FORMATOID")
    @ManyToOne(optional = false)
    private Formatoproducto formatoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoid")
    private List<ProductoRestricciontiempo> productoRestricciontiempoList;

    public Productodetrabajo() {
    }

    public Productodetrabajo(BigDecimal productoid) {
        this.productoid = productoid;
    }

    public Productodetrabajo(BigDecimal productoid, BigInteger productoaprobado, String productocontenido) {
        this.productoid = productoid;
        this.productoaprobado = productoaprobado;
        this.productocontenido = productocontenido;
    }

    public BigDecimal getProductoid() {
        return productoid;
    }

    public void setProductoid(BigDecimal productoid) {
        this.productoid = productoid;
    }

    public BigInteger getProductoaprobado() {
        return productoaprobado;
    }

    public void setProductoaprobado(BigInteger productoaprobado) {
        this.productoaprobado = productoaprobado;
    }

    public String getProductocontenido() {
        return productocontenido;
    }

    public void setProductocontenido(String productocontenido) {
        this.productocontenido = productocontenido;
    }

    @XmlTransient
    public List<ProductoRestriccionfrecuencia> getProductoRestriccionfrecuenciaList() {
        return productoRestriccionfrecuenciaList;
    }

    public void setProductoRestriccionfrecuenciaList(List<ProductoRestriccionfrecuencia> productoRestriccionfrecuenciaList) {
        this.productoRestriccionfrecuenciaList = productoRestriccionfrecuenciaList;
    }

    public Trabajodegrado getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(Trabajodegrado trabajoid) {
        this.trabajoid = trabajoid;
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
        hash += (productoid != null ? productoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productodetrabajo)) {
            return false;
        }
        Productodetrabajo other = (Productodetrabajo) object;
        if ((this.productoid == null && other.productoid != null) || (this.productoid != null && !this.productoid.equals(other.productoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Productodetrabajo[ productoid=" + productoid + " ]";
    }
    
}
