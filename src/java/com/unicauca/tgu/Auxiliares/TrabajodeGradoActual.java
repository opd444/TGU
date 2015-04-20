/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.tgu.Auxiliares;

import com.unicauca.tgu.entities.Usuario;
import java.math.BigDecimal;

/**
 *
 * @author pcblanco
 */
public class TrabajodeGradoActual {
    
         public static int id = 24;
         public static String nombreTg = "TRAB2";
         public static Usuario est1 = new Usuario(BigDecimal.valueOf(3),"Carlos","Paz","@","opd","123");
         public static Usuario est2 = new Usuario(BigDecimal.valueOf(2),"Juan","Paz","@","opd","123");
         public static Usuario director = new Usuario(BigDecimal.ZERO,"Orlando","Paz","@","opd","123");
}
