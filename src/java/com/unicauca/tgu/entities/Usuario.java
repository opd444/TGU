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
import javax.persistence.JoinColumn;
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
@Table(name = "USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByPersonacedula", query = "SELECT u FROM Usuario u WHERE u.personacedula = :personacedula"),
    @NamedQuery(name = "Usuario.findByPersonanombres", query = "SELECT u FROM Usuario u WHERE u.personanombres = :personanombres"),
    @NamedQuery(name = "Usuario.findByPersonaapellidos", query = "SELECT u FROM Usuario u WHERE u.personaapellidos = :personaapellidos"),
    @NamedQuery(name = "Usuario.findByPersonacorreo", query = "SELECT u FROM Usuario u WHERE u.personacorreo = :personacorreo"),
    @NamedQuery(name = "Usuario.findByUsuarionombre", query = "SELECT u FROM Usuario u WHERE u.usuarionombre = :usuarionombre"),
    @NamedQuery(name = "Usuario.findByUsuariocontrasena", query = "SELECT u FROM Usuario u WHERE u.usuariocontrasena = :usuariocontrasena")})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERSONACEDULA")
    private BigDecimal personacedula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "PERSONANOMBRES")
    private String personanombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "PERSONAAPELLIDOS")
    private String personaapellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "PERSONACORREO")
    private String personacorreo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "USUARIONOMBRE")
    private String usuarionombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "USUARIOCONTRASENA")
    private String usuariocontrasena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personacedula")
    private List<NotificacionUsuario> notificacionUsuarioList;
    @JoinColumn(name = "FACULTADID", referencedColumnName = "FACULTADID")
    @ManyToOne
    private Facultad facultadid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personacedula")
    private List<UsuarioRolTrabajogrado> usuarioRolTrabajogradoList;

    public Usuario() {
    }

    public Usuario(BigDecimal personacedula) {
        this.personacedula = personacedula;
    }

    public Usuario(BigDecimal personacedula, String personanombres, String personaapellidos, String personacorreo, String usuarionombre, String usuariocontrasena) {
        this.personacedula = personacedula;
        this.personanombres = personanombres;
        this.personaapellidos = personaapellidos;
        this.personacorreo = personacorreo;
        this.usuarionombre = usuarionombre;
        this.usuariocontrasena = usuariocontrasena;
    }

    public BigDecimal getPersonacedula() {
        return personacedula;
    }

    public void setPersonacedula(BigDecimal personacedula) {
        this.personacedula = personacedula;
    }

    public String getPersonanombres() {
        return personanombres;
    }

    public void setPersonanombres(String personanombres) {
        this.personanombres = personanombres;
    }

    public String getPersonaapellidos() {
        return personaapellidos;
    }

    public void setPersonaapellidos(String personaapellidos) {
        this.personaapellidos = personaapellidos;
    }

    public String getPersonacorreo() {
        return personacorreo;
    }

    public void setPersonacorreo(String personacorreo) {
        this.personacorreo = personacorreo;
    }

    public String getUsuarionombre() {
        return usuarionombre;
    }

    public void setUsuarionombre(String usuarionombre) {
        this.usuarionombre = usuarionombre;
    }

    public String getUsuariocontrasena() {
        return usuariocontrasena;
    }

    public void setUsuariocontrasena(String usuariocontrasena) {
        this.usuariocontrasena = usuariocontrasena;
    }

    @XmlTransient
    public List<NotificacionUsuario> getNotificacionUsuarioList() {
        return notificacionUsuarioList;
    }

    public void setNotificacionUsuarioList(List<NotificacionUsuario> notificacionUsuarioList) {
        this.notificacionUsuarioList = notificacionUsuarioList;
    }

    public Facultad getFacultadid() {
        return facultadid;
    }

    public void setFacultadid(Facultad facultadid) {
        this.facultadid = facultadid;
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
        hash += (personacedula != null ? personacedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.personacedula == null && other.personacedula != null) || (this.personacedula != null && !this.personacedula.equals(other.personacedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entities.Usuario[ personacedula=" + personacedula + " ]";
    }
    
}
