package com.JustinThyme.justinthymer.models.data;


import com.JustinThyme.justinthymer.models.forms.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {


    User findById(int userId);

}
