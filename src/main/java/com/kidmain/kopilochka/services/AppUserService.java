package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.exceptions.AppUserNotFoundException;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.repositories.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppUserService {
    private final AppUserRepository userRepository;

    public AppUserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getAllUsersByOrder() {
        return userRepository.findByOrderById();
    }

    public AppUser getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                            throw new AppUserNotFoundException("User with this id not found");
                        }
                );
    }

    @Transactional
    public void updateUser(AppUser user) {
        user = userRepository
                .findById(user.getId())
                .orElseThrow(() -> {
                            throw new AppUserNotFoundException("User with this id not found");
                        }
                );
        userRepository.save(user);
    }
}