package com.s2.technostore;

import dao.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

public class TechnoStore {

    public static void main(String[] args) {                                                                       
            Conexion conexion = new Conexion();                                                              
                                                                                                                       
            try(Connection c = conexion.conexion()) {
                if (c != null){
                    System.out.println("Conexion exitosa!");
                }else {
                    System.out.println("yaper");
                }
            }catch(SQLException e) {
                e.getMessage();
            }                                                          
        }
}
