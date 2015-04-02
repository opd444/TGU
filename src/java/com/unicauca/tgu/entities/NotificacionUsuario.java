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
@Table(name = "NOTIFICACION_USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionUsuario.findAll", query = "SELECT n FROM NotificacionUsuario n"),
    @NamedQuery(name = "NotificacionUsuario.findByNotificacionusuarioid", query = "SELECT n FROM NotificacionUsuario n WHERE n.notificacionusuarioid = :notificacionusuarioid"),
    @NamedQuery(name = "NotificacionUsuario.findByFechanotificacion", query = "SELECT n FROM NotificacionUsuario n WHERE n.fechanotificacion = :fechanotificacion")})
public class NotificacionUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NOTIFICACIONUSUARIOID")
    private BigDecimal notificacionusuarioid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHANOTIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechanotificacion;
    @JoinColumn(name = "PERSONACEDULA", referencedColumnName = "PERSONACEDULA")
    @ManyToOne(optional = false)
    private Usuario personacedula;
    @JoinColumn(name = "NOTIFICACIONID", referencedColumnName = "NOTIFICACIONID")
    @ManyToOne(optional = false)
    private Notificacion notificacionid;

    public NotificacionUsuario() {
    }

    public NotificacionUsuario(BigDecimal notificacionusuarioid) {
        this.notificacionusuarioid = notificacionusuarioid;
    }

    public NotificacionUsuario(BigDecimal notificacionusuarioid, Date fechanotificacion) {
        this.notificacionusuarioid = notificacionusuarioid;
        this.fechanotificacion = fechanotificacion;
    }

    public BigDecimal getNotificacionusuarioid() {
        return notificacionusuarioid;
    }

    public void setNotificacionusuarioid(BigDecimal notificacionusuarioid) {
        this.notificacionusuarioid = notificacionusuarioid;
    }

    public Date getFechanotificacion() {
        return fechanotificacion;
    }

    public void setFechanotificacion(Date fechanotificacion) {
        this.fechanotificacion = fechanotificacion;
    }

    public Usuario getPersonacedula() {
        return personacedula;
    }

    public void setPersonacedula(Usuario personacedula) {
        this.personacedula = personacedula;
    }

    public Notificacion getNotificacionid() {
        return notificacionid;
    }

    public void setNotificacionid(Notificacion notificacionid) {
        this.notificacionid = notificacionid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificacionusuarioid != null ? notificacionusuarioid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionUsuario)) {
            return false;
        }
        NotificacionUsuario other = (NotificacionUsuario) object;
        if ((this.notificacionusuarioid == null && other.notificacionusuarioid != null) || (this.notificacionusuarioid != null && !this.notificacionusuarioid.equals(other.notificacionusuarioid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.NotificacionUsuario[ notificacionusuarioid=" + notificacionusuarioid + " ]";
    }
    
}
