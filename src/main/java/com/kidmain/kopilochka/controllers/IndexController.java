package com.kidmain.kopilochka.controllers;

import com.kidmain.kopilochka.dao.Hibernate.ProductServiceHibernate;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.services.AppUserService;
import com.kidmain.kopilochka.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final ProductService productService;
    private final AppUserService appUserService;
    private final ProductServiceHibernate productHibernate;

    @Autowired
    public IndexController(ProductService productService, AppUserService appUserService, ProductServiceHibernate productHibernate) {
        this.productService = productService;
        this.appUserService = appUserService;
        this.productHibernate = productHibernate;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("products", productHibernate.getAllProductsByOrder());
        model.addAttribute("user", new AppUser());
        model.addAttribute("users", appUserService.getAllUsersByOrder());
        return "index";
    }
}
