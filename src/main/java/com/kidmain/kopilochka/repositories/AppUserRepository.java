package com.kidmain.kopilochka.repositories;

import com.kidmain.kopilochka.models.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    List<AppUser> findByOrderById();
}