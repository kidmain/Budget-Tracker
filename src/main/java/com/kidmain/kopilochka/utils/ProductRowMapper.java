//package com.kidmain.kopilochka.utils;
//
//import com.kidmain.kopilochka.models.AppUser;
//import com.kidmain.kopilochka.models.Product;
//import com.kidmain.kopilochka.repositories.UserRepository;
//import com.kidmain.kopilochka.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class ProductRowMapper implements RowMapper<Product> {
//    private final UserService userService;
//
//    @Autowired
//    public ProductRowMapper(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Product product = new Product();
//
//        product.setId(rs.getLong("id"));
//        product.setName(rs.getString("name"));
//        product.setPrice(rs.getDouble("price"));
//        product.setUser((AppUser) rs.getObject(1));
//        product.setDate(rs.getDate("date").toLocalDate());
//
//        return product;
//    }
//}
