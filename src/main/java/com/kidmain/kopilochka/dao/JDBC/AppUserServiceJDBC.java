package com.kidmain.kopilochka.dao.JDBC;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.utils.AppUserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceJDBC {
    private final JdbcTemplate jdbcTemplate;

    public AppUserServiceJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AppUser> getAllUsersByOrder() {
        return jdbcTemplate.query("SELECT * FROM kopilochka.app_user ORDER BY id",
                new AppUserRowMapper()
        );
    }

    public AppUser getUserById(Long id) {
        List<AppUser> query = jdbcTemplate.query("SELECT * FROM kopilochka.app_user WHERE id=?",
                new AppUserRowMapper(),
                id
        );

        return query
                .stream()
                .findAny()
                .orElse(null);
    }

    public void updateUser(AppUser user) {
        jdbcTemplate.update("UPDATE kopilochka.app_user SET income=?, expenses=?, debt=? WHERE id=?",
                user.getIncome(), user.getExpenses(), user.getDebt(), user.getId()
        );
    }
}
