package com.kidmain.kopilochka.controllers;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.services.ProductService;
import com.kidmain.kopilochka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final boolean IS_JDBC = true;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public IndexController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("products", productService.getAllProductsByOrder(IS_JDBC));
        model.addAttribute("user", new AppUser());
        model.addAttribute("users", userService.getAllUsersByOrder(IS_JDBC));
        return "index";
    }
}
