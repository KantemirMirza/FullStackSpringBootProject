package com.kani.fullstackspringbootproject.utils;

import com.kani.fullstackspringbootproject.entity.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String confirmation;

    public RegistrationCompleteEvent(User user, String confirmation) {
        super(user);
        this.user = user;
        this.confirmation = confirmation;
    }
}
