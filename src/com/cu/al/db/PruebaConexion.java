/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cu.al.db;

/**
 *
 * @author ricar
 */
public class PruebaConexion {
      public static void main(String[] args) {
          ConexionMySQL connMySQl = new ConexionMySQL();
        try {
            connMySQl.open();
            System.out.println("Conexión establecida con MySQl!");
                        
                      
            connMySQl.close();
            System.out.println("Conexión Cerrada correctamente con MySQL!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
    
}
