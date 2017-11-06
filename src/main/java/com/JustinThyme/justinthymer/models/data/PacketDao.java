package com.JustinThyme.justinthymer.models.data;

import com.JustinThyme.justinthymer.models.forms.Packet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface PacketDao extends CrudRepository<Packet, Integer> {

    Packet findByUserId(int user_id);
}
