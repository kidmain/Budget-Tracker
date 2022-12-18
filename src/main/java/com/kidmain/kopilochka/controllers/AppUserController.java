package com.kidmain.kopilochka.controllers;

import com.kidmain.kopilochka.dao.Hibernate.AppUserServiceHibernate;
import com.kidmain.kopilochka.dao.JDBC.AppUserServiceJDBC;
import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.services.ProductService;
import com.kidmain.kopilochka.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class AppUserController {
    private final AppUserService appUserService;
    private final ProductService productService;
    private final AppUserServiceHibernate userHibernate;
    private final AppUserServiceJDBC userJDBC;

    @Autowired
    public AppUserController(AppUserService appUserService, ProductService productService, AppUserServiceHibernate userHibernate, AppUserServiceJDBC userJDBC) {
        this.appUserService = appUserService;
        this.productService = productService;
        this.userHibernate = userHibernate;
        this.userJDBC = userJDBC;
    }

    @GetMapping("/{id}")
    public String getUserById(
            @PathVariable("id") Long id, Model model

    ) {
        model.addAttribute("user", appUserService.getUserById(id));
        model.addAttribute("products", productService.getAllProductsByUserId(id));
        return "users/user";
    }

    @GetMapping("/{id}/edit")
    public String getEditUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", appUserService.getUserById(id));
        return "users/edit";
    }
    
    @PatchMapping("/{id}/edit")
    public String editUser(@PathVariable("id") Long id, AppUser user, Product product) {
        AppUser updatedUser = appUserService.getUserById(id);

        if(user.getIncome() != null) updatedUser.setIncome(user.getIncome());
        if(user.getExpenses() != null) updatedUser.setExpenses(user.getExpenses());
        if(user.getDebt() != null) updatedUser.setDebt(user.getDebt());

        appUserService.updateUser(updatedUser);
        return "redirect:/";
    }
}