package com.kani.fullstackspringbootproject.token;

import com.kani.fullstackspringbootproject.entity.User;
import com.kani.fullstackspringbootproject.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenService implements IVerificationTokenService{
    private final IVerificationTokenRepository verificationTokenRepository;
    private final IUserRepository userRepository;

    @Override
    public String validateVerificationToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken.isEmpty()){
           return "INVALID";
        }
        User user = verificationToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.get().getTokenExpirationTime().getTime()
                - calendar.getTime().getTime()) <= 0 ){
            return "EXPIRED";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "VALID";
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void deleteUserToken(Long id) {
        verificationTokenRepository.deleteByUserId(id);
    }
}
