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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author seven
 */
@Entity
@Table(name = "FORMATOPRODUCTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Formatoproducto.findAll", query = "SELECT f FROM Formatoproducto f"),
    @NamedQuery(name = "Formatoproducto.findByFormatoid", query = "SELECT f FROM Formatoproducto f WHERE f.formatoid = :formatoid"),
    @NamedQuery(name = "Formatoproducto.findByFormatonombre", query = "SELECT f FROM Formatoproducto f WHERE f.formatonombre = :formatonombre"),
    @NamedQuery(name = "Formatoproducto.findByFormatoaprobable", query = "SELECT f FROM Formatoproducto f WHERE f.formatoaprobable = :formatoaprobable"),
    @NamedQuery(name = "Formatoproducto.findByFormatoobligatorio", query = "SELECT f FROM Formatoproducto f WHERE f.formatoobligatorio = :formatoobligatorio")})
public class Formatoproducto implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FORMATOID")
    private BigDecimal formatoid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "FORMATONOMBRE")
    private String formatonombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FORMATOAPROBABLE")
    private BigInteger formatoaprobable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FORMATOOBLIGATORIO")
    private BigInteger formatoobligatorio;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "FORMATOCONTENIDO")
    private String formatocontenido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formatoid")
    private List<Restricciontiempo> restricciontiempoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formatoid")
    private List<RolFormatoproducto> rolFormatoproductoList;
    @JoinColumn(name = "FASEID", referencedColumnName = "FASEID")
    @ManyToOne(optional = false)
    private Fase faseid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formatoid")
    private List<Productodetrabajo> productodetrabajoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formatoid")
    private List<Restriccionfrecuencia> restriccionfrecuenciaList;

    public Formatoproducto() {
    }

    public Formatoproducto(BigDecimal formatoid) {
        this.formatoid = formatoid;
    }

    public Formatoproducto(BigDecimal formatoid, String formatonombre, BigInteger formatoaprobable, BigInteger formatoobligatorio, String formatocontenido) {
        this.formatoid = formatoid;
        this.formatonombre = formatonombre;
        this.formatoaprobable = formatoaprobable;
        this.formatoobligatorio = formatoobligatorio;
        this.formatocontenido = formatocontenido;
    }

    public BigDecimal getFormatoid() {
        return formatoid;
    }

    public void setFormatoid(BigDecimal formatoid) {
        this.formatoid = formatoid;
    }

    public String getFormatonombre() {
        return formatonombre;
    }

    public void setFormatonombre(String formatonombre) {
        this.formatonombre = formatonombre;
    }

    public BigInteger getFormatoaprobable() {
        return formatoaprobable;
    }

    public void setFormatoaprobable(BigInteger formatoaprobable) {
        this.formatoaprobable = formatoaprobable;
    }

    public BigInteger getFormatoobligatorio() {
        return formatoobligatorio;
    }

    public void setFormatoobligatorio(BigInteger formatoobligatorio) {
        this.formatoobligatorio = formatoobligatorio;
    }

    public String getFormatocontenido() {
        return formatocontenido;
    }

    public void setFormatocontenido(String formatocontenido) {
        this.formatocontenido = formatocontenido;
    }

    @XmlTransient
    public List<Restricciontiempo> getRestricciontiempoList() {
        return restricciontiempoList;
    }

    public void setRestricciontiempoList(List<Restricciontiempo> restricciontiempoList) {
        this.restricciontiempoList = restricciontiempoList;
    }

    @XmlTransient
    public List<RolFormatoproducto> getRolFormatoproductoList() {
        return rolFormatoproductoList;
    }

    public void setRolFormatoproductoList(List<RolFormatoproducto> rolFormatoproductoList) {
        this.rolFormatoproductoList = rolFormatoproductoList;
    }

    public Fase getFaseid() {
        return faseid;
    }

    public void setFaseid(Fase faseid) {
        this.faseid = faseid;
    }

    @XmlTransient
    public List<Productodetrabajo> getProductodetrabajoList() {
        return productodetrabajoList;
    }

    public void setProductodetrabajoList(List<Productodetrabajo> productodetrabajoList) {
        this.productodetrabajoList = productodetrabajoList;
    }

    @XmlTransient
    public List<Restriccionfrecuencia> getRestriccionfrecuenciaList() {
        return restriccionfrecuenciaList;
    }

    public void setRestriccionfrecuenciaList(List<Restriccionfrecuencia> restriccionfrecuenciaList) {
        this.restriccionfrecuenciaList = restriccionfrecuenciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formatoid != null ? formatoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Formatoproducto)) {
            return false;
        }
        Formatoproducto other = (Formatoproducto) object;
        if ((this.formatoid == null && other.formatoid != null) || (this.formatoid != null && !this.formatoid.equals(other.formatoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Formatoproducto[ formatoid=" + formatoid + " ]";
    }
    
}
