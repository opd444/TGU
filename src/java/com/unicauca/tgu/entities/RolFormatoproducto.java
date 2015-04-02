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
@Table(name = "ROL_FORMATOPRODUCTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RolFormatoproducto.findAll", query = "SELECT r FROM RolFormatoproducto r"),
    @NamedQuery(name = "RolFormatoproducto.findByRolformatoprodid", query = "SELECT r FROM RolFormatoproducto r WHERE r.rolformatoprodid = :rolformatoprodid"),
    @NamedQuery(name = "RolFormatoproducto.findByResponsabilidad", query = "SELECT r FROM RolFormatoproducto r WHERE r.responsabilidad = :responsabilidad")})
public class RolFormatoproducto implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLFORMATOPRODID")
    private BigDecimal rolformatoprodid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "RESPONSABILIDAD")
    private String responsabilidad;
    @JoinColumn(name = "ROLID", referencedColumnName = "ROLID")
    @ManyToOne(optional = false)
    private Rol rolid;
    @JoinColumn(name = "FORMATOID", referencedColumnName = "FORMATOID")
    @ManyToOne(optional = false)
    private Formatoproducto formatoid;

    public RolFormatoproducto() {
    }

    public RolFormatoproducto(BigDecimal rolformatoprodid) {
        this.rolformatoprodid = rolformatoprodid;
    }

    public RolFormatoproducto(BigDecimal rolformatoprodid, String responsabilidad) {
        this.rolformatoprodid = rolformatoprodid;
        this.responsabilidad = responsabilidad;
    }

    public BigDecimal getRolformatoprodid() {
        return rolformatoprodid;
    }

    public void setRolformatoprodid(BigDecimal rolformatoprodid) {
        this.rolformatoprodid = rolformatoprodid;
    }

    public String getResponsabilidad() {
        return responsabilidad;
    }

    public void setResponsabilidad(String responsabilidad) {
        this.responsabilidad = responsabilidad;
    }

    public Rol getRolid() {
        return rolid;
    }

    public void setRolid(Rol rolid) {
        this.rolid = rolid;
    }

    public Formatoproducto getFormatoid() {
        return formatoid;
    }

    public void setFormatoid(Formatoproducto formatoid) {
        this.formatoid = formatoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolformatoprodid != null ? rolformatoprodid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolFormatoproducto)) {
            return false;
        }
        RolFormatoproducto other = (RolFormatoproducto) object;
        if ((this.rolformatoprodid == null && other.rolformatoprodid != null) || (this.rolformatoprodid != null && !this.rolformatoprodid.equals(other.rolformatoprodid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.RolFormatoproducto[ rolformatoprodid=" + rolformatoprodid + " ]";
    }
    
}
