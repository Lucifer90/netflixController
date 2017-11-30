package it.fanciullini.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue
   @Column(name = "id")
   private Long id;

   @Column(name = "username")
   private String username;

   @Column(name = "mail")
   private String mail;

   @Column(name = "name")
   private String name;

   @Column(name = "surname")
   private String surname;

   @Column(name = "password")
   private String password;

   @Column(name = "register_date")
   private Date registerDate;

   @Column(name = "delete_date")
   private Date deleteDate;

   @Column(name = "phone")
   private String phone;

}