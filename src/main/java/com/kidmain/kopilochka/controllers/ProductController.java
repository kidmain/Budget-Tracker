package com.kidmain.kopilochka.controllers;

import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.services.ProductService;
import com.kidmain.kopilochka.services.ProductServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductServiceTemplate productService;

    public ProductController(ProductServiceTemplate productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProductsByOrder());
        model.addAttribute("product", new Product());
        return "products/products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/product";
    }

    @PostMapping
    public String addProduct(
            @ModelAttribute("product")
            @Valid Product product, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "products/products";
        }

        productService.addProduct(product);
        return "redirect:/products";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String getEditProductById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("products", productService.getAllProducts());
        return "products/products-edit";
    }

    @PatchMapping("/{id}/edit")
    public String editProduct(
            @PathVariable("id") Long id,
            @Valid Product product, BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "products/products-edit";
        }

        Product updatedProduct = productService.getProductById(id);

        if (product.getName() != null) updatedProduct.setName(product.getName());
        if (product.getPrice() != null) updatedProduct.setPrice(product.getPrice());
        if (product.getDate() != null) updatedProduct.setDate(product.getDate());
        if (product.getUsername() != null) updatedProduct.setUsername(product.getUsername());

        productService.updateProduct(updatedProduct);
        return "redirect:/products";
    }
}