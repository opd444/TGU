/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author seven
 */
@Embeddable
public class UsuarioRolPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERSONACEDULA")
    private BigInteger personacedula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLID")
    private BigInteger rolid;

    public UsuarioRolPK() {
    }

    public UsuarioRolPK(BigInteger personacedula, BigInteger rolid) {
        this.personacedula = personacedula;
        this.rolid = rolid;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personacedula != null ? personacedula.hashCode() : 0);
        hash += (rolid != null ? rolid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioRolPK)) {
            return false;
        }
        UsuarioRolPK other = (UsuarioRolPK) object;
        if ((this.personacedula == null && other.personacedula != null) || (this.personacedula != null && !this.personacedula.equals(other.personacedula))) {
            return false;
        }
        if ((this.rolid == null && other.rolid != null) || (this.rolid != null && !this.rolid.equals(other.rolid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.tgu.entities.UsuarioRolPK[ personacedula=" + personacedula + ", rolid=" + rolid + " ]";
    }
    
}
