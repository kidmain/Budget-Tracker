package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.exceptions.UserNotFoundException;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getAllUsersByOrder() {
        return userRepository.findByOrderById();
    }

    public AppUser getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                            throw new UserNotFoundException("User with this id not found");
                        }
                );
    }

    @Transactional
    public void updateUser(AppUser user) {
        user = userRepository
                .findById(user.getId())
                .orElseThrow(() -> {
                            throw new UserNotFoundException("User with this id not found");
                        }
                );
        userRepository.save(user);
    }
}