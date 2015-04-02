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
@Table(name = "NOTIFICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notificacion.findAll", query = "SELECT n FROM Notificacion n"),
    @NamedQuery(name = "Notificacion.findByNotificacionid", query = "SELECT n FROM Notificacion n WHERE n.notificacionid = :notificacionid"),
    @NamedQuery(name = "Notificacion.findByNotificacionasunto", query = "SELECT n FROM Notificacion n WHERE n.notificacionasunto = :notificacionasunto"),
    @NamedQuery(name = "Notificacion.findByNotificaciondescripcion", query = "SELECT n FROM Notificacion n WHERE n.notificaciondescripcion = :notificaciondescripcion")})
public class Notificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NOTIFICACIONID")
    private BigDecimal notificacionid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "NOTIFICACIONASUNTO")
    private String notificacionasunto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "NOTIFICACIONDESCRIPCION")
    private String notificaciondescripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notificacionid")
    private List<NotificacionUsuario> notificacionUsuarioList;

    public Notificacion() {
    }

    public Notificacion(BigDecimal notificacionid) {
        this.notificacionid = notificacionid;
    }

    public Notificacion(BigDecimal notificacionid, String notificacionasunto, String notificaciondescripcion) {
        this.notificacionid = notificacionid;
        this.notificacionasunto = notificacionasunto;
        this.notificaciondescripcion = notificaciondescripcion;
    }

    public BigDecimal getNotificacionid() {
        return notificacionid;
    }

    public void setNotificacionid(BigDecimal notificacionid) {
        this.notificacionid = notificacionid;
    }

    public String getNotificacionasunto() {
        return notificacionasunto;
    }

    public void setNotificacionasunto(String notificacionasunto) {
        this.notificacionasunto = notificacionasunto;
    }

    public String getNotificaciondescripcion() {
        return notificaciondescripcion;
    }

    public void setNotificaciondescripcion(String notificaciondescripcion) {
        this.notificaciondescripcion = notificaciondescripcion;
    }

    @XmlTransient
    public List<NotificacionUsuario> getNotificacionUsuarioList() {
        return notificacionUsuarioList;
    }

    public void setNotificacionUsuarioList(List<NotificacionUsuario> notificacionUsuarioList) {
        this.notificacionUsuarioList = notificacionUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificacionid != null ? notificacionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        if ((this.notificacionid == null && other.notificacionid != null) || (this.notificacionid != null && !this.notificacionid.equals(other.notificacionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Notificacion[ notificacionid=" + notificacionid + " ]";
    }
    
}
