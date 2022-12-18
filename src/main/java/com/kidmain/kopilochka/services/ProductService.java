package com.kidmain.kopilochka.services;

import com.kidmain.kopilochka.exceptions.ProductNotFoundException;
import com.kidmain.kopilochka.exceptions.AppUserNotFoundException;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final ProductRepository productRepository;
    private final AppUserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository, AppUserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
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
                            throw new AppUserNotFoundException("User with this id not found");
                        }
                );
    }

    @Transactional
    public void addProduct(Product product) {
        product.setPrice(product.getPrice());
        product.setCreatedAt(new Date());

        productRepository.save(product);
        userService.updateUser(updateUserBudget(product, true));
    }

    @Transactional
    public void updateProduct(
            Long id,
            Product updatedProduct,
            AppUser updatedUser
    ) {
        Product oldProduct = getProductById(id);
        AppUser oldUser = userService.getUserById(oldProduct.getUser().getId());
        updatedUser = userService.getUserById(updatedUser.getId());

        if (!oldUser.equals(updatedUser)) {
            oldUser.setExpenses(oldUser.getExpenses() - oldProduct.getPrice());
            updatedUser.setExpenses(updatedUser.getExpenses() + updatedProduct.getPrice());
            userService.updateUser(oldUser);
            userService.updateUser(updatedUser);
        } else {
            double updatedUserExpenses = editUserExpenses(updatedProduct, oldProduct, oldUser);
            updatedUser.setExpenses(updatedUserExpenses);
            userService.updateUser(updatedUser);
        }

        oldProduct.setName(updatedProduct.getName());
        oldProduct.setPrice(updatedProduct.getPrice());
        oldProduct.setDate(updatedProduct.getDate());
        oldProduct.setUser(updatedUser);

        oldProduct.setModifiedAt(new Date());
        productRepository.save(oldProduct);
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