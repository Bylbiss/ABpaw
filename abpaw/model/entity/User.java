/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;
/**
 *
 * @author LOQ
 */

// OOP 
//1. encapsulation
//2. abstracttion
//3. inheritance = superclass bakal di extends
//4. polymorphism = getRole dan getId di override subclass


//abstraction = ga iso langsung intansiasi
public abstract class User {
    //atribut encapsulation
    private String username;
    private String password;
    private String email;

    public User() {  //default constructor
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //GETTER SETTER encap
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    //abstrak methods -> wajib imple subclass
    public abstract String getRole(); //dapet id min/dok/pemilik
    public abstract int getId(); //subclass yg implem polymorphism
}