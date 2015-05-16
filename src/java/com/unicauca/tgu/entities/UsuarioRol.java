/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author pcblanco
 */
@Entity
@Table(name = "USUARIO_ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioRol.findAll", query = "SELECT u FROM UsuarioRol u"),
    @NamedQuery(name = "UsuarioRol.findByUsuariorolid", query = "SELECT u FROM UsuarioRol u WHERE u.usuariorolid = :usuariorolid"),
    @NamedQuery(name = "UsuarioRol.findByPersonacedula", query = "SELECT u FROM UsuarioRol u WHERE u.personacedula = :personacedula"),
    @NamedQuery(name = "UsuarioRol.findByRolid", query = "SELECT u FROM UsuarioRol u WHERE u.rolid = :rolid"),
    @NamedQuery(name = "UsuarioRol.findByUsurolfecha", query = "SELECT u FROM UsuarioRol u WHERE u.usurolfecha = :usurolfecha")})
public class UsuarioRol implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUARIOROLID")
    private BigDecimal usuariorolid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERSONACEDULA")
    private BigInteger personacedula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLID")
    private BigInteger rolid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUROLFECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usurolfecha;

    public UsuarioRol() {
    }

    public UsuarioRol(BigDecimal usuariorolid) {
        this.usuariorolid = usuariorolid;
    }

    public UsuarioRol(BigDecimal usuariorolid, BigInteger personacedula, BigInteger rolid, Date usurolfecha) {
        this.usuariorolid = usuariorolid;
        this.personacedula = personacedula;
        this.rolid = rolid;
        this.usurolfecha = usurolfecha;
    }

    public BigDecimal getUsuariorolid() {
        return usuariorolid;
    }

    public void setUsuariorolid(BigDecimal usuariorolid) {
        this.usuariorolid = usuariorolid;
    }

    public BigInteger getPersonacedula() {
        return personacedula;
    }

    public void setPersonacedula(BigInteger personacedula) {
        this.personacedula = personacedula;
    }

    public BigInteger getRolid() {
        return rolid;
    }

    public void setRolid(BigInteger rolid) {
        this.rolid = rolid;
    }

    public Date getUsurolfecha() {
        return usurolfecha;
    }

    public void setUsurolfecha(Date usurolfecha) {
        this.usurolfecha = usurolfecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuariorolid != null ? usuariorolid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioRol)) {
            return false;
        }
        UsuarioRol other = (UsuarioRol) object;
        if ((this.usuariorolid == null && other.usuariorolid != null) || (this.usuariorolid != null && !this.usuariorolid.equals(other.usuariorolid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.tgu.entities.UsuarioRol[ usuariorolid=" + usuariorolid + " ]";
    }
    
}
