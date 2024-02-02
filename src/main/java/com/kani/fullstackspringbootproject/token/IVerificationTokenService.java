package com.kani.fullstackspringbootproject.token;

import com.kani.fullstackspringbootproject.entity.User;

import java.util.Optional;

public interface IVerificationTokenService {
    String validateVerificationToken(String token);
    void saveVerificationTokenForUser(String token, User user);
    Optional<VerificationToken> findByVerificationToken(String token);

    void deleteUserToken(Long id);

}
