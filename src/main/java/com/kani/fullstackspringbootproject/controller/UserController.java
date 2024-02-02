package com.kani.fullstackspringbootproject.controller;

import com.kani.fullstackspringbootproject.entity.User;
import com.kani.fullstackspringbootproject.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @GetMapping("/list")
    public String getAllUsersFromDb(Model model){
        model.addAttribute("listOfUsers", userService.getAllUsersFromDB());
        return "listOfUsers";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model){
        Optional<User> update = userService.findUserById(id);
        model.addAttribute("user", update);
        return "update_user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user){
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/user/list?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/user/list?delete_success";
    }
}
