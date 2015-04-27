/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

/**
 *
 * @author seven
 */
public class ControllerUsuario {

    public ControllerUsuario() {
    }

    public Usuario crearUsuario(String consultaUsuario) {        
        
        String [] lst = consultaUsuario.split("\n");
        
//        System.out.println("*** *** ***");
        
        String [] lstNU = lst[1].split("<usuario><nombreUsuario>");
        String [] nomUsuario = lstNU[1].split("</nombreUsuario>");
//        System.out.println("nombreUsuario *** "+nomUsuario[0]);
        String nombreUsuario = nomUsuario[0];
        
        String [] lstN = lst[2].split("<nombres>");
        String [] nom = lstN[1].split("</nombres>");
//        System.out.println("nombres *** "+nom[0]);
        String nombres = nom[0];
        
        String [] lstA = lst[3].split("<apellidos>");
        String [] ape = lstA[1].split("</apellidos>");
//        System.out.println("apellidos *** "+ape[0]);
        String apellidos = ape[0];
        
        String [] lstE = lst[4].split("<email>");
        String [] emai1 = lstE[1].split("</email>");
//        System.out.println("email *** "+emai1[0]);
        String email = emai1[0];
        
        String [] lstR = lst[5].split("<rol>");
        String [] ro1 = lstR[1].split("</rol>");
//        System.out.println("rol *** "+ro1[0]);
        String rol = ro1[0];
        
//        System.out.println("*"+lst[0]);
//        System.out.println(lst[1]);
//        System.out.println(lst[2]);
//        System.out.println(lst[3]);
//        System.out.println(lst[4]);
//        System.out.println(lst[5]);        
        
        return new Usuario(nombreUsuario, nombres, apellidos, email, rol);        
    }
}
