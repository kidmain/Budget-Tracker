package com.kidmain.kopilochka.controllers;

import com.kidmain.kopilochka.dao.Hibernate.ProductServiceHibernate;
import com.kidmain.kopilochka.dao.JDBC.ProductServiceJDBC;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.services.ProductService;
import com.kidmain.kopilochka.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final AppUserService appUserService;
    private final ProductServiceHibernate productHibernate;
    private final ProductServiceJDBC productJDBC;

    @Autowired
    public ProductController(ProductService productService, AppUserService appUserService, ProductServiceHibernate productHibernate, ProductServiceJDBC productJDBC) {
        this.productService = productService;
        this.appUserService = appUserService;
        this.productHibernate = productHibernate;
        this.productJDBC = productJDBC;
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/product";
    }

    @PostMapping
    public String addProduct(@Valid Product product, BindingResult productBindingResult,
                             @Valid AppUser user, BindingResult userBindingResult,
                             Model model) {
        model.addAttribute("user", user);
        model.addAttribute("users", appUserService.getAllUsersByOrder());

        if (productBindingResult.hasErrors() || userBindingResult.hasErrors()) {
            return "index";
        }

        product.setUser(user);

        productService.addProduct(product);
        return "redirect:/";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/";
    }

    @DeleteMapping
    public String deleteAllProducts() {
        productService.deleteAllProducts();
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String getEditProductById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("user", productService.getProductById(id).getUser());
        model.addAttribute("users", appUserService.getAllUsersByOrder());
        return "products/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editProduct(@PathVariable("id") Long id, Product product, AppUser user) {
        productService.updateProduct(id, product, user);
        return "redirect:/";
    }
}