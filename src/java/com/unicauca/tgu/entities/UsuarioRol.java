/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author seven
 */
@Entity
@Table(name = "USUARIO_ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioRol.findAll", query = "SELECT u FROM UsuarioRol u"),
    @NamedQuery(name = "UsuarioRol.findByPersonacedula", query = "SELECT u FROM UsuarioRol u WHERE u.usuarioRolPK.personacedula = :personacedula"),
    @NamedQuery(name = "UsuarioRol.findByRolid", query = "SELECT u FROM UsuarioRol u WHERE u.usuarioRolPK.rolid = :rolid"),
    @NamedQuery(name = "UsuarioRol.findByUsuariorolid", query = "SELECT u FROM UsuarioRol u WHERE u.usuariorolid = :usuariorolid"),
    @NamedQuery(name = "UsuarioRol.findByUsurolfecha", query = "SELECT u FROM UsuarioRol u WHERE u.usurolfecha = :usurolfecha")})
public class UsuarioRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioRolPK usuarioRolPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUARIOROLID")
    private BigInteger usuariorolid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUROLFECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usurolfecha;

    public UsuarioRol() {
    }

    public UsuarioRol(UsuarioRolPK usuarioRolPK) {
        this.usuarioRolPK = usuarioRolPK;
    }

    public UsuarioRol(UsuarioRolPK usuarioRolPK, BigInteger usuariorolid, Date usurolfecha) {
        this.usuarioRolPK = usuarioRolPK;
        this.usuariorolid = usuariorolid;
        this.usurolfecha = usurolfecha;
    }

    public UsuarioRol(BigInteger personacedula, BigInteger rolid) {
        this.usuarioRolPK = new UsuarioRolPK(personacedula, rolid);
    }

    public UsuarioRolPK getUsuarioRolPK() {
        return usuarioRolPK;
    }

    public void setUsuarioRolPK(UsuarioRolPK usuarioRolPK) {
        this.usuarioRolPK = usuarioRolPK;
    }

    public BigInteger getUsuariorolid() {
        return usuariorolid;
    }

    public void setUsuariorolid(BigInteger usuariorolid) {
        this.usuariorolid = usuariorolid;
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
        hash += (usuarioRolPK != null ? usuarioRolPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioRol)) {
            return false;
        }
        UsuarioRol other = (UsuarioRol) object;
        if ((this.usuarioRolPK == null && other.usuarioRolPK != null) || (this.usuarioRolPK != null && !this.usuarioRolPK.equals(other.usuarioRolPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.tgu.entities.UsuarioRol[ usuarioRolPK=" + usuarioRolPK + " ]";
    }
    
}
