package com.kani.fullstackspringbootproject.service;

import com.kani.fullstackspringbootproject.dto.UserRegisterForm;
import com.kani.fullstackspringbootproject.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsersFromDB();
    User saveUserToDB(UserRegisterForm userRegisterForm);
    User findUserByEmailFromDB(String email);
    boolean isEmailUnique(String email);
    Optional<User> findUserById(Long id);
    void updateUser(Long id, String firstName, String lastName, String email);
    void deleteUser(Long id);
}
