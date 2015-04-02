/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "USUARIO_ROL_TRABAJOGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioRolTrabajogrado.findAll", query = "SELECT u FROM UsuarioRolTrabajogrado u"),
    @NamedQuery(name = "UsuarioRolTrabajogrado.findByUsuarioroltrabajoid", query = "SELECT u FROM UsuarioRolTrabajogrado u WHERE u.usuarioroltrabajoid = :usuarioroltrabajoid"),
    @NamedQuery(name = "UsuarioRolTrabajogrado.findByFechaasignacion", query = "SELECT u FROM UsuarioRolTrabajogrado u WHERE u.fechaasignacion = :fechaasignacion")})
public class UsuarioRolTrabajogrado implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUARIOROLTRABAJOID")
    private BigDecimal usuarioroltrabajoid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaasignacion;
    @JoinColumn(name = "PERSONACEDULA", referencedColumnName = "PERSONACEDULA")
    @ManyToOne(optional = false)
    private Usuario personacedula;
    @JoinColumn(name = "TRABAJOID", referencedColumnName = "TRABAJOID")
    @ManyToOne(optional = false)
    private Trabajodegrado trabajoid;
    @JoinColumn(name = "ROLID", referencedColumnName = "ROLID")
    @ManyToOne(optional = false)
    private Rol rolid;

    public UsuarioRolTrabajogrado() {
    }

    public UsuarioRolTrabajogrado(BigDecimal usuarioroltrabajoid) {
        this.usuarioroltrabajoid = usuarioroltrabajoid;
    }

    public UsuarioRolTrabajogrado(BigDecimal usuarioroltrabajoid, Date fechaasignacion) {
        this.usuarioroltrabajoid = usuarioroltrabajoid;
        this.fechaasignacion = fechaasignacion;
    }

    public BigDecimal getUsuarioroltrabajoid() {
        return usuarioroltrabajoid;
    }

    public void setUsuarioroltrabajoid(BigDecimal usuarioroltrabajoid) {
        this.usuarioroltrabajoid = usuarioroltrabajoid;
    }

    public Date getFechaasignacion() {
        return fechaasignacion;
    }

    public void setFechaasignacion(Date fechaasignacion) {
        this.fechaasignacion = fechaasignacion;
    }

    public Usuario getPersonacedula() {
        return personacedula;
    }

    public void setPersonacedula(Usuario personacedula) {
        this.personacedula = personacedula;
    }

    public Trabajodegrado getTrabajoid() {
        return trabajoid;
    }

    public void setTrabajoid(Trabajodegrado trabajoid) {
        this.trabajoid = trabajoid;
    }

    public Rol getRolid() {
        return rolid;
    }

    public void setRolid(Rol rolid) {
        this.rolid = rolid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioroltrabajoid != null ? usuarioroltrabajoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioRolTrabajogrado)) {
            return false;
        }
        UsuarioRolTrabajogrado other = (UsuarioRolTrabajogrado) object;
        if ((this.usuarioroltrabajoid == null && other.usuarioroltrabajoid != null) || (this.usuarioroltrabajoid != null && !this.usuarioroltrabajoid.equals(other.usuarioroltrabajoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.UsuarioRolTrabajogrado[ usuarioroltrabajoid=" + usuarioroltrabajoid + " ]";
    }
    
}
