package com.kidmain.kopilochka.controllers;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.services.ProductService;
import com.kidmain.kopilochka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/product";
    }

    @PostMapping
    public String addProduct(Product product, AppUser user) {
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
        model.addAttribute("users", userService.getAllUsersByOrder());
        return "products/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editProduct(@PathVariable("id") Long id, Product product, AppUser user) {
        Product updatedProduct = productService.getProductById(id);

        if (product.getName() != null) updatedProduct.setName(product.getName());
        if (product.getPrice() != null) updatedProduct.setPrice(product.getPrice());
        if (product.getDate() != null) updatedProduct.setDate(product.getDate());
        updatedProduct.setUser(user);

        productService.updateProduct(updatedProduct);
        return "redirect:/";
    }
}