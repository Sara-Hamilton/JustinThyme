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
    private List<SeedInPacket> seeds;


    public Packet(Integer user_id, List<SeedInPacket> seeds) {
    }


    public Packet() {
    }


    public int getPacketId() {
        return id;
    }


    public List<SeedInPacket> getSeeds() {
        return seeds;
    }


    public void setSeeds(List<SeedInPacket> seeds) {
        this.seeds = seeds;
    }

    public void addSeed(SeedInPacket newSeed) {
        seeds.add(newSeed);
    }

    public void removeSeed(SeedInPacket oldSeed) {
        seeds.remove(oldSeed);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
