package com.kidmain.kopilochka.repositories;

import com.kidmain.kopilochka.models.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
//@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<AppUser, Long> {
    List<AppUser> findByOrderById();
}