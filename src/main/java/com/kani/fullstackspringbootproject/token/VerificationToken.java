package com.kani.fullstackspringbootproject.token;

import com.kani.fullstackspringbootproject.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date tokenExpirationTime;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.tokenExpirationTime = VerificationTokenExpirationTime.getExpirationTime();
    }
}
