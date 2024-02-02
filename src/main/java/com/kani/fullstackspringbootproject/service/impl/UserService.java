package com.kani.fullstackspringbootproject.service.impl;

import com.kani.fullstackspringbootproject.dto.UserRegisterForm;
import com.kani.fullstackspringbootproject.entity.Role;
import com.kani.fullstackspringbootproject.entity.User;
import com.kani.fullstackspringbootproject.repository.IUserRepository;
import com.kani.fullstackspringbootproject.service.IUserService;
import com.kani.fullstackspringbootproject.token.IVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IVerificationTokenService verificationTokenService;

    @Override
    public List<User> getAllUsersFromDB() {
        return userRepository.findAll();
    }

    @Override
    public User saveUserToDB(UserRegisterForm userRegisterForm) {
        User user = new User(userRegisterForm.getFirstName(),
                userRegisterForm.getLastName(),
                userRegisterForm.getEmail(),
                passwordEncoder.encode(userRegisterForm.getPassword()),
                Arrays.asList(new Role("USER")));
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmailFromDB(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new RuntimeException("User Not Found !!!"));
    }

    @Override
    public boolean isEmailUnique(String email) {
        try {
            findUserByEmailFromDB(email);
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, String firstName, String lastName, String email) {
        userRepository.update(firstName,lastName, email, id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<User> theUser = userRepository.findById(id);
        theUser.ifPresent(user -> verificationTokenService.deleteUserToken(user.getId()));
        userRepository.deleteById(id);
    }
}
