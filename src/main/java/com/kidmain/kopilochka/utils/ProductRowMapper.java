package com.kidmain.kopilochka.utils;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        AppUser user = new AppUser();

        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Double price = rs.getDouble("price");
        Long user_id = rs.getLong("user_id");
        LocalDate date = rs.getDate("date").toLocalDate();

        user.setId(user_id);

        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setUser(user);
        product.setDate(date);

        return product;
    }
}
