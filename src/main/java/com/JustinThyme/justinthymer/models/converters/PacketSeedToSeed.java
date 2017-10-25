package com.JustinThyme.justinthymer.models.converters;

import com.JustinThyme.justinthymer.models.forms.Packet;
import com.JustinThyme.justinthymer.models.forms.Seed;
import com.JustinThyme.justinthymer.models.forms.SeedInPacket;

import java.util.Date;

public class PacketSeedToSeed {
    public static Seed fromPackToSeed(SeedInPacket aSeedInPacket) {

        Seed someSeed = new Seed();
        String name = aSeedInPacket.getName();
        Date date = aSeedInPacket.getPlantDate();


        //note not sure if reminder needs to be set to null

        someSeed.setName(name);
        someSeed.setPlantDate(date);


        return someSeed;
    }
}
