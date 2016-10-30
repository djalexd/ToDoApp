package com.example;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA Entity
 */
@Data
@Entity
//@NamedQuery(name = "User.findByUserName", query="select u from User u where userName = :usrn")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String userName;
    @Column
    private String password;

    public User(){}

    // zicea ca genereaza mai multe tipuri de constructori? cu @Data? cel cu parametri sa pare ca nu poate fi folosit din UserEndpoint
    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
