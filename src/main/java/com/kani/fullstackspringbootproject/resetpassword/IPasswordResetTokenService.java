package com.kani.fullstackspringbootproject.resetpassword;

import com.kani.fullstackspringbootproject.entity.User;

import java.util.Optional;

public interface IPasswordResetTokenService {
    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    String validatePasswordResetToken(String theToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User theUser, String password);

}
