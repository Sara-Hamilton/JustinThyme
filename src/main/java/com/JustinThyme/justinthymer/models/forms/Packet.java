package com.JustinThyme.justinthymer.models.forms;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Packet {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;



    public int getId() {
        return id;
    }

    @OneToMany(mappedBy="packet")
    //@ManyToOne
    private List<SeedInPacket> seeds;

//    @ManyToOne
//    private List<Seed> seeds;
//    @ManyToMany
//    List<SeedInPacket> seeds = new ArrayList<>();



    public Packet(Integer user_id, List<SeedInPacket> seeds) {
    }


    public Packet() {
    }


    public int getPacketId() {
        return id;
    }
//    public int getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(Integer user_id) {
//        this.user_id = user_id;
//    }



    public List<SeedInPacket> getSeeds() {
        return seeds;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public void setSeeds(List<SeedInPacket> seeds) {
        this.seeds = seeds;
    }

    public void addSeed(SeedInPacket newSeed) {
        seeds.add(newSeed);
    }

    public void removeSeed(SeedInPacket oldSeed) {
        seeds.remove(oldSeed);
    }


    public void setReminder(Seed aSeed) {
        aSeed.reminder = true;
    }
    public void removeReminder(Seed aSeed) {
        aSeed.reminder = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
