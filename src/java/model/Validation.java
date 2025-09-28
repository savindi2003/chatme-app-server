/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author hp
 */
public class Validation {
    public static boolean isValidPassword(String password){
    return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=]).{8,}$");
    }
    

     public static boolean isMobileNumberValid(String mobile){
//    return mobile.matches("^07[01245678]{1}[0-9]{7}$");
     return mobile.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$");
    }
}
