package it.fanciullini.data.entity;

import it.fanciullini.utility.Roles;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name="users")
public class User implements Serializable
{

    public User() {

    }

    public User (User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.telegramId = user.getTelegramId();
        this.mail = user.getMail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.password = user.getPassword();
        this.registerDate = user.getRegisterDate();
        this.deleteDate = user.getDeleteDate();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.hidden = hidden;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "telegram_id")
    private Long telegramId;

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

    @Column(name="role")
    private Roles role;

    @Column(name="hidden")
    private Boolean hidden;

}
