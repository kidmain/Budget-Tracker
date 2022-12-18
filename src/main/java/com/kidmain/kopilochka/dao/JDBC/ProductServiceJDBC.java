package com.kidmain.kopilochka.dao.JDBC;

import com.kidmain.kopilochka.exceptions.ProductNotFoundException;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.utils.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class ProductServiceJDBC {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private final JdbcTemplate jdbcTemplate;
    private final AppUserServiceJDBC userService;

    @Autowired
    public ProductServiceJDBC(JdbcTemplate jdbcTemplate, AppUserServiceJDBC userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    public Product getProductById(Long id) {
        List<Product> query = jdbcTemplate.query(
                "SELECT * FROM kopilochka.products products JOIN kopilochka.app_user users ON products.user_id = users.id WHERE products.id=?",
                new ProductRowMapper(),
                id
        );

        return query.stream()
                .findAny()
                .orElseThrow(() -> {
                            throw new ProductNotFoundException("Product with this id not found");
                        }
                );
    }

    public List<Product> getAllProducts() {
        return jdbcTemplate.query(
                "SELECT * FROM kopilochka.products",
                new ProductRowMapper()
        );
    }

    public List<Product> getAllProductsByOrder() {
        return jdbcTemplate.query(
                "SELECT * FROM kopilochka.products ORDER BY kopilochka.products.id",
                new ProductRowMapper()
        );
    }

    public List<Product> getAllProductsByUserId(Long id) {
        return jdbcTemplate.query("SELECT * FROM kopilochka.products WHERE user_id=?",
                new ProductRowMapper(),
                id
        );
    }

    public void addProduct(Product product) {
        jdbcTemplate.update(
                "INSERT INTO kopilochka.products (name, price, user_id, date) VALUES(?, ?, ?, ?)",
                product.getName(), product.getPrice(), product.getUser().getId(), product.getDate()
        );

        jdbcTemplate.update(
                "UPDATE kopilochka.app_user SET expenses=? WHERE id=?",
                updateUserBudget(product, true).getExpenses(), product.getUser().getId()
        );
    }

    public void updateProduct(
            Product oldProduct, Product updatedProduct,
            AppUser user, AppUser updatedUser,
            boolean isJDBC
    ) {
        jdbcTemplate.update(
                "UPDATE kopilochka.products p SET name=?, price=?, date=?, user_id=? WHERE p.id=?",
                oldProduct.getName(), oldProduct.getPrice(), oldProduct.getDate(), user.getId(), updatedProduct.getId()
        );

        double updatedUserExpenses = editUserExpenses(oldProduct, updatedProduct, updatedUser);
        jdbcTemplate.update(
                "UPDATE kopilochka.app_user SET expenses=? WHERE id=?",
                updatedUserExpenses, updatedProduct.getUser().getId()
        );
    }

    public void deleteProductById(Long id) {
        Product product = getProductById(id);
        jdbcTemplate.update(
                "DELETE FROM kopilochka.products WHERE id=?", id);
        jdbcTemplate.update(
                "UPDATE kopilochka.app_user SET expenses=? WHERE id=?",
                updateUserBudget(product, false).getExpenses(), product.getUser().getId()
        );
    }

    public void deleteAllProducts() {
        jdbcTemplate.update("DELETE FROM kopilochka.products");
        jdbcTemplate.update("UPDATE kopilochka.app_user SET expenses=0.");

        userService
                .getAllUsersByOrder()
                .forEach(user -> user.setExpenses(0.));
    }

    ////////////////////////////
    ///// PRIVATE METHODS /////
    ///////////////////////////
    private AppUser updateUserBudget(Product product, boolean isExpense) {
        AppUser user = userService.getUserById(product.getUser().getId());

        double userExpense = user.getExpenses();
        double productPrice = product.getPrice();

        if (isExpense) {
            user.setExpenses(getFormattedDouble(userExpense + productPrice));
        } else {
            user.setExpenses(getFormattedDouble(userExpense - productPrice));
        }
        return user;
    }

    private double editUserExpenses(Product oldProduct, Product updatedProduct, AppUser user) {
        boolean isExpense = oldProduct.getPrice() > updatedProduct.getPrice();

        if (isExpense) {
            return oldProduct.getPrice() - updatedProduct.getPrice() + user.getExpenses();
        } else {
            return user.getExpenses() - (updatedProduct.getPrice() - oldProduct.getPrice());
        }
    }

    private double getFormattedDouble(Double number) {
        return Double.parseDouble(DECIMAL_FORMAT.format(number));
    }
}

