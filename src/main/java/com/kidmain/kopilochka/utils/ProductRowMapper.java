package com.kidmain.kopilochka.utils;

import com.kidmain.kopilochka.models.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();

        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setUsername(rs.getString("username"));
        product.setDate(rs.getDate("date").toLocalDate());

        return product;
    }
}
