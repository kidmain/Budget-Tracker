package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.exceptions.ProductNotFoundException;
import com.kidmain.kopilochka.exceptions.UserNotFoundException;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.repositories.ProductRepository;
import com.kidmain.kopilochka.utils.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public ProductService(JdbcTemplate jdbcTemplate, ProductRepository productRepository, UserService service) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository = productRepository;
        this.userService = service;
    }

    public List<Product> getAllProducts(boolean isJDBC) {
        if (isJDBC) {
            return jdbcTemplate.query(
                    "SELECT * FROM kopilochka.products",
                    new ProductRowMapper()
            );
        } else {
            List<Product> products = new ArrayList<>();
            productRepository
                    .findAll()
                    .forEach(products::add);
            return products;
        }
    }

    public Product getProductById(Long id, boolean isJDBC) {
        if (isJDBC) {
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
        } else {
            return productRepository
                    .findById(id)
                    .orElseThrow(() -> {
                                throw new ProductNotFoundException("Product with this id not found");
                            }
                    );
        }
    }

    public List<Product> getAllProductsByOrder(boolean isJDBC) {
        if (isJDBC) {
            return jdbcTemplate.query(
                    "SELECT * FROM kopilochka.products ORDER BY kopilochka.products.id",
                    new ProductRowMapper()
            );
        } else {
            return productRepository.findByOrderById();
        }
    }

    public void addProduct(Product product, boolean isJDBC) {
        if (isJDBC) {
            jdbcTemplate.update(
                    "INSERT INTO kopilochka.products (name, price, user_id, date) VALUES(?, ?, ?, ?)",
                product.getName(), product.getPrice(), product.getUser().getId(), product.getDate()
            );

            jdbcTemplate.update(
                    "UPDATE kopilochka.app_user SET expenses=? WHERE id=?",
                    updateUserBudget(product, true).getExpenses(), product.getUser().getId()
            );
        } else {
            product.setPrice(product.getPrice());
            userService.updateUser(updateUserBudget(product, true), false);
            productRepository.save(product);
        }
    }

    public void updateProduct(
            Product oldProduct, Product updatedProduct,
            AppUser user, AppUser updatedUser,
            boolean isJDBC
    ) {
        if (isJDBC) {
            jdbcTemplate.update(
                    "UPDATE kopilochka.products p SET name=?, price=?, date=?, user_id=? WHERE p.id=?",
                oldProduct.getName(), oldProduct.getPrice(), oldProduct.getDate(), user.getId(), updatedProduct.getId()
            );

            double updatedUserExpenses = editUserExpenses(oldProduct, updatedProduct, updatedUser);
            jdbcTemplate.update(
                    "UPDATE kopilochka.app_user SET expenses=? WHERE id=?",
                    updatedUserExpenses, updatedProduct.getUser().getId()
                );

        } else {
            double updatedUserExpenses = editUserExpenses(oldProduct, updatedProduct, updatedUser);
            updatedUser.setExpenses(updatedUserExpenses);

            if (oldProduct.getName() != null) updatedProduct.setName(oldProduct.getName());
            if (oldProduct.getPrice() != null) updatedProduct.setPrice(oldProduct.getPrice());
            if (oldProduct.getDate() != null) updatedProduct.setDate(oldProduct.getDate());
            updatedProduct.setUser(user);

            productRepository.save(updatedProduct);
            userService.updateUser(updatedUser, false);
        }
    }

    public void deleteProductById(Long id, boolean isJDBC) {
        Product product = getProductById(id, false);
        if (isJDBC) {
            jdbcTemplate.update(
                    "DELETE FROM kopilochka.products WHERE id=?", id);
            jdbcTemplate.update(
                    "UPDATE kopilochka.app_user SET expenses=? WHERE id=?",
                    updateUserBudget(product, false).getExpenses(), product.getUser().getId()
            );
        } else {
            userService.updateUser(updateUserBudget(product, false), false);
            productRepository.deleteById(id);
        }
    }

    public void deleteAllProducts(boolean isJDBC) {
        if (isJDBC) {
            jdbcTemplate.update("DELETE FROM kopilochka.products");
            jdbcTemplate.update("UPDATE kopilochka.app_user SET expenses=0.");
        } else {
            userService
                    .getAllUsersByOrder(false)
                    .forEach(user -> user.setExpenses(0.));
            productRepository.deleteAll();
        }
    }

    public List<Product> getAllProductsByUserId(Long id, boolean isJDBC) {
        if (isJDBC) {
            return jdbcTemplate.query("SELECT * FROM kopilochka.products WHERE user_id=?",
                    new ProductRowMapper(),
                    id
            );
        } else {
            return productRepository
                    .findByUserId(id)
                    .orElseThrow(() -> {
                                throw new UserNotFoundException("User with this id not found");
                            }
                    );
        }
    }

    ////////////////////////////
    ///// PRIVATE METHODS /////
    ///////////////////////////
    private AppUser updateUserBudget(Product product, boolean isExpense) {
        AppUser user = userService.getUserById(product.getUser().getId(), false);

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