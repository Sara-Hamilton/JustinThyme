package com.JustinThyme.justinthymer.models.forms;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;


    @OneToOne(mappedBy = "user")
    private Packet packet;

    @NotNull
    //regex pattern prevents empty string but allows spaces within the string
    @Pattern(regexp="(.|\\s)*\\S(.|\\s)*", message="Name must not be empty.")
    public String username;

    @NotNull
    @Size(min=6, message="Passwords must be at least six characters.")
    private String password;

   // classic phone number pattern, North American numbers
    @Pattern(regexp = "[(][2-9][0-8][0-9][)][2-9][0-9]{2}-[0-9]{4}", message="Not a valid number, use (XXX)XXX-XXXX format")
    private String phoneNumber;

    @NotNull
    private Seed.Area area;

    private long sessionId;

    @NotNull
    private boolean loggedIn;


    public User(String username, String password, Seed.Area area, String phoneNumber, Long sessionId, Boolean loggedIn) {
        this.username = username;
        this.password = password;
        this.area = area;
        this.phoneNumber = phoneNumber;
        this.sessionId = sessionId;
        this.loggedIn = loggedIn;

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

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;

    }

    public Packet getPacket() {
        return packet;
    }

}

