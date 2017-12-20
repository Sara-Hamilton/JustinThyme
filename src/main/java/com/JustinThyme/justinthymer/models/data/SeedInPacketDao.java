package com.JustinThyme.justinthymer.models.data;


import com.JustinThyme.justinthymer.models.forms.Packet;
import com.JustinThyme.justinthymer.models.forms.SeedInPacket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SeedInPacketDao extends CrudRepository<SeedInPacket, Integer> {

    List<SeedInPacket> getByPacket(Packet aPacket);

    List<SeedInPacket> findAll();

    SeedInPacket findById(int id);

    SeedInPacket findByName(String name);


}
