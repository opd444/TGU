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
@Table(name = "ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findByRolid", query = "SELECT r FROM Rol r WHERE r.rolid = :rolid"),
    @NamedQuery(name = "Rol.findByRolnombre", query = "SELECT r FROM Rol r WHERE r.rolnombre = :rolnombre")})
public class Rol implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLID")
    private BigDecimal rolid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "ROLNOMBRE")
    private String rolnombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolid")
    private List<RolFormatoproducto> rolFormatoproductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolid")
    private List<UsuarioRolTrabajogrado> usuarioRolTrabajogradoList;

    public Rol() {
    }

    public Rol(BigDecimal rolid) {
        this.rolid = rolid;
    }

    public Rol(BigDecimal rolid, String rolnombre) {
        this.rolid = rolid;
        this.rolnombre = rolnombre;
    }

    public BigDecimal getRolid() {
        return rolid;
    }

    public void setRolid(BigDecimal rolid) {
        this.rolid = rolid;
    }

    public String getRolnombre() {
        return rolnombre;
    }

    public void setRolnombre(String rolnombre) {
        this.rolnombre = rolnombre;
    }

    @XmlTransient
    public List<RolFormatoproducto> getRolFormatoproductoList() {
        return rolFormatoproductoList;
    }

    public void setRolFormatoproductoList(List<RolFormatoproducto> rolFormatoproductoList) {
        this.rolFormatoproductoList = rolFormatoproductoList;
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
        hash += (rolid != null ? rolid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.rolid == null && other.rolid != null) || (this.rolid != null && !this.rolid.equals(other.rolid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Rol[ rolid=" + rolid + " ]";
    }
    
}
