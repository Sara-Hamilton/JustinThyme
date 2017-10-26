package com.JustinThyme.justinthymer.models.forms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    //regex pattern prevents empty string but allows spaces within the string
    @Pattern(regexp="(.|\\s)*\\S(.|\\s)*", message="Name must not be empty.")
    public String username;

    @NotNull
    @Size(min=6, message="Passwords must be at least six characters.")
    private String password;


    @OneToOne(mappedBy = "user")
    private Packet packet;

    // standard phone number format for North America
    @Pattern(regexp = "[(][2-9][0-8][0-9][)][2-9][0-9]{2}-[0-9]{4}", message="Not a valid number, use (XXX)XXX-XXXX format")
    private String phoneNumber;

    @NotNull
    private Seed.Area area;

    private String sessionId;

    public User(String username, String password, Seed.Area area, String phoneNumber, String sessionId) {
        this.username = username;
        this.password = password;
        this.area = area;
        this.phoneNumber = phoneNumber;
        this.sessionId = sessionId;
    }

    public User() { }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Seed.Area getArea() {
        return area;
    }

    public void setArea(Seed.Area area) {
        this.area = area;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    }

