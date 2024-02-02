package com.kani.fullstackspringbootproject.resetpassword;

import com.kani.fullstackspringbootproject.entity.User;
import com.kani.fullstackspringbootproject.token.VerificationTokenExpirationTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = VerificationTokenExpirationTime.getExpirationTime();
    }
}
