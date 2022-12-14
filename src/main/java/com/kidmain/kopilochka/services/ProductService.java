package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.exceptions.ProductNotFoundException;
import com.kidmain.kopilochka.exceptions.UserNotFoundException;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository, UserService service) {
        this.productRepository = productRepository;
        this.userService = service;
    }

    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> {
                            throw new ProductNotFoundException("Product with this id not found");
                        }
                );
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository
                .findAll()
                .forEach(products::add);
        return products;
    }

    public List<Product> getAllProductsByOrder() {
        return productRepository.findByOrderById();
    }

    public List<Product> getAllProductsByUserId(Long id) {
        return productRepository
                .findByUserId(id)
                .orElseThrow(() -> {
                            throw new UserNotFoundException("User with this id not found");
                        }
                );
    }

    @Transactional
    public void addProduct(Product product) {
        product.setPrice(product.getPrice());
        userService.updateUser(updateUserBudget(product, true));
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(
            Product oldProduct, Product updatedProduct,
            AppUser user, AppUser updatedUser
            
    ) {
        double updatedUserExpenses = editUserExpenses(oldProduct, updatedProduct, updatedUser);
        updatedUser.setExpenses(updatedUserExpenses);

        if (oldProduct.getName() != null) updatedProduct.setName(oldProduct.getName());
        if (oldProduct.getPrice() != null) updatedProduct.setPrice(oldProduct.getPrice());
        if (oldProduct.getDate() != null) updatedProduct.setDate(oldProduct.getDate());
        updatedProduct.setUser(user);

        productRepository.save(updatedProduct);
        userService.updateUser(updatedUser);
    }

    @Transactional
    public void deleteProductById(Long id) {
        Product product = getProductById(id);
        userService.updateUser(updateUserBudget(product, false));
        productRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllProducts() {
        userService
                .getAllUsersByOrder()
                .forEach(user -> user.setExpenses(0.));
        productRepository.deleteAll();
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