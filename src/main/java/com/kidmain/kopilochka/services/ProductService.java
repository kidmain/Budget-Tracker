package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository, UserService service) {
        this.productRepository = productRepository;
        this.userService = service;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository
                .findAll()
                .forEach(products::add);
        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProductsByOrder() {
        return productRepository.findByOrderById();
    }

    public void addProduct(Product product) {
        userService.updateUser(updateUserBudget(product, true));
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        Product product = getProductById(id);
        userService.updateUser(updateUserBudget(product, false));
        productRepository.deleteById(id);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    private AppUser updateUserBudget(Product product, boolean isExpense) {
        AppUser user = userService.getUserById(product.getUser().getId());
        if (isExpense) {
            user.setExpenses(user.getExpenses() + product.getPrice());
        } else {
            user.setExpenses(user.getExpenses() - product.getPrice());
        }
        return user;
    }
}