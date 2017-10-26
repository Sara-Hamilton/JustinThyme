package com.JustinThyme.justinthymer.models.converters;

import com.JustinThyme.justinthymer.models.forms.Packet;
import com.JustinThyme.justinthymer.models.forms.Seed;
import com.JustinThyme.justinthymer.models.forms.SeedInPacket;

import java.util.Date;

public class SeedToPacketSeed {
    public static SeedInPacket fromSeedToPacket(Seed aSeed, Packet aPacket) {
        SeedInPacket someSeedInPacket = new SeedInPacket(aPacket);
        String name = aSeed.getName();
        Date date = aSeed.getPlantDate();


        someSeedInPacket.setName(name);
        someSeedInPacket.setPlantDate(date);
        someSeedInPacket.setPacket(aPacket);

        return someSeedInPacket;
    }

}
