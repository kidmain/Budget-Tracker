package com.kidmain.kopilochka.controllers;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import com.kidmain.kopilochka.services.ProductService;
import com.kidmain.kopilochka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService ) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/user";
    }

    @GetMapping("/{id}/edit")
    public String getEditUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/edit";
    }
    
    @PatchMapping("/{id}/edit")
    public String editUser(@PathVariable("id") Long id, AppUser user, Product product) {
        AppUser updatedUser = userService.getUserById(id);

        if(user.getIncome() != null) updatedUser.setIncome(user.getIncome());
        if(user.getExpenses() != null) updatedUser.setExpenses(user.getExpenses());
        if(user.getDebt() != null) updatedUser.setDebt(user.getDebt());

        userService.updateUser(updatedUser);
        return "redirect:/";
    }
}