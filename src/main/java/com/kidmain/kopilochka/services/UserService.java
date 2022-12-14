package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.exceptions.UserNotFoundException;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.repositories.UserRepository;
import com.kidmain.kopilochka.utils.AppUserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public UserService(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public List<AppUser> getAllUsersByOrder(boolean isJDBC) {
        if (isJDBC) {
            return jdbcTemplate.query("SELECT * FROM kopilochka.app_user ORDER BY id",
                    new AppUserRowMapper()
            );
        } else {
            return userRepository.findByOrderById();
        }
    }

    public AppUser getUserById(Long id, boolean isJDBC) {
        if (isJDBC) {
            List<AppUser> query = jdbcTemplate.query("SELECT * FROM kopilochka.app_user WHERE id=?",
                    new AppUserRowMapper(),
                    id
            );

            return query
                    .stream()
                    .findAny()
                    .orElse(null);
        } else {
            return userRepository
                    .findById(id)
                    .orElseThrow(() -> {
                                throw new UserNotFoundException("User with this id not found");
                            }
                    );
        }
    }

    public void updateUser(AppUser user, boolean isJDBC) {
        if (isJDBC) {
            jdbcTemplate.update("UPDATE kopilochka.app_user SET income=?, expenses=?, debt=? WHERE id=?",
                    user.getIncome(), user.getExpenses(), user.getDebt(), user.getId()
            );
        } else {
            user = userRepository
                    .findById(user.getId())
                    .orElseThrow(() -> {
                                throw new UserNotFoundException("User with this id not found");
                            }
                    );
            userRepository.save(user);
        }
    }
}