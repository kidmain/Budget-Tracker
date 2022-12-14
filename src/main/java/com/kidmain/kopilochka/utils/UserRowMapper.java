package com.kidmain.kopilochka.utils;

import com.kidmain.kopilochka.models.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<AppUser> {
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        AppUser user = new AppUser();

        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Double income = rs.getDouble("income");
        Double expenses = rs.getDouble("expenses");
        Double debt = rs.getDouble("debt");


        user.setId(id);
        user.setName(name);
        user.setIncome(income);
        user.setExpenses(expenses);
        user.setDebt(debt);

        return user;
    }
}