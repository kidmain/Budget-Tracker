package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getAllUsers() {
        List<AppUser> users = new ArrayList<>();
        userRepository
                .findAll()
                .forEach(users::add);
        return users;
    }

    public AppUser getAppUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}