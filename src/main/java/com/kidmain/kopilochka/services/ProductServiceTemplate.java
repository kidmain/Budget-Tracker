//package com.kidmain.kopilochka.services;
//
//import com.kidmain.kopilochka.models.Product;
//import com.kidmain.kopilochka.utils.ProductRowMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ProductServiceTemplate {
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public ProductServiceTemplate(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Product> getAllProducts() {
//        return jdbcTemplate.query("SELECT * FROM kopilochka.products", new BeanPropertyRowMapper<>(Product.class));
//    }
//
//    public List<Product> getAllProductsByOrder() {
//        return jdbcTemplate.query("SELECT * FROM kopilochka.products ORDER BY id", new BeanPropertyRowMapper<>(Product.class));
//    }
//
//    public Product getProductById(Long id) {
//        List<Product> query = jdbcTemplate.query(
//                "SELECT * FROM kopilochka.products WHERE id=?",
//                new BeanPropertyRowMapper<>(Product.class),
//                id
//        );
//
//        return query
//                .stream()
//                .findAny()
//                .orElse(null);
//    }
//
//    public void addProduct(Product product) {
//        jdbcTemplate.update("INSERT INTO kopilochka.products (name, price, user_id, date) VALUES(?, ?, ?, ?)",
//                product.getName(), product.getPrice(), product.getUser(), product.getDate());
//    }
//
//    public void updateProduct(Product product) {
//        jdbcTemplate.update("UPDATE kopilochka.products SET name=?, price=?, user_id=?, date=? WHERE id=?",
//                product.getName(), product.getPrice(), product.getUser(), product.getDate(), product.getId());
//    }
//
//    public void deleteProductById(Long id) {
//        jdbcTemplate.update("DELETE FROM kopilochka.products WHERE id=?", id);
//    }
//
//    public void deleteAllProducts() {
//        jdbcTemplate.update("DELETE FROM kopilochka.products");
//    }
//}
