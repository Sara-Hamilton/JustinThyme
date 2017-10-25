package com.JustinThyme.justinthymer.models.forms;


import javax.persistence.*;
import java.util.Date;

@Entity
public class SeedInPacket extends Seed {

    @Id
    @GeneratedValue
    public int id;

    //@OneToMany(mappedBy="packet_id")
    @ManyToOne
    private Packet packet;

    public Boolean reminder;


    public SeedInPacket(Packet aPacket){
        super();
        this.packet = aPacket;
        this.reminder = false;
    }

    public SeedInPacket() {

    }


    public Boolean getReminder() {
        return reminder;
    }

    public Date getPlantDate() {
        return super.plantDate;
    }
    public String getName() {
        return name;
    }


    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public void setReminder(SeedInPacket aSeed) {
        aSeed.reminder = true;
    }
    public void removeReminder(SeedInPacket aSeed) {
        aSeed.reminder = false;
    }
}
