package com.kani.fullstackspringbootproject.resetpassword;

import com.kani.fullstackspringbootproject.entity.User;
import com.kani.fullstackspringbootproject.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService implements IPasswordResetTokenService{
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        PasswordResetToken createToken = new PasswordResetToken(passwordResetToken, user);
        passwordResetTokenRepository.save(createToken);
    }

    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(theToken);
        if(passwordResetToken.isEmpty()){
            return "INVALID";
        }
        Calendar calendar = Calendar.getInstance();
        if(passwordResetToken.get().getExpirationTime().getTime() - calendar.getTime().getTime() <= 0){
            return "EXPIRED";
        }
        return "VALID";
    }

    @Override
    public Optional<User> findUserByPasswordResetToken(String theToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(theToken).get().getUser());
    }

    @Override
    public void resetPassword(User theUser, String password) {
        theUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(theUser);
    }
}
