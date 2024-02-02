package com.kani.fullstackspringbootproject.utils;

import com.kani.fullstackspringbootproject.entity.User;
import com.kani.fullstackspringbootproject.token.IVerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final IVerificationTokenService verificationTokenService;
    private final JavaMailSender javaMailSender;
    private User user;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        // 1. get the user
        user = event.getUser();
        // 2. generate a token for user
        String token = UUID.randomUUID().toString();
        // 3. save the token for user
        verificationTokenService.saveVerificationTokenForUser(token, user);
        // 4. build the verification url
        String url = event.getConfirmation() + "/register/verifyEmail?token=" + token;
        // 5. send the email to the user
        try {
            sendVerificationTokenToEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationTokenToEmail(String url)throws MessagingException, UnsupportedEncodingException {
        String subject = "Verification Email";
        String senderName = "User Verification Service";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" + "<p> Thank you for Registration with us, please follow the link bellow to complete your Registration. </p>" + "<a href=\"" + url + "\"> Verify your email to activate your account </a> " + "<p> thank you <br> User Registration Portal Service </p>";
    messageSender(subject, senderName, mailContent, javaMailSender, user);
    }

    public void sendPasswordResetVerificationEmail(String url)throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Verification";
        String senderName = "User Verification Service";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" + "<p> Thank you for Registration with us, please follow the link bellow to complete your Registration. </p>" + "<a href=\"" + url + "\"> Verify your email to activate your account </a> " + "<p> thank you <br> User Registration Portal Service </p>";
        messageSender(subject, senderName, mailContent, javaMailSender, user);
    }


    private static void messageSender(String subject,
                               String senderName,
                               String mailContent,
                               JavaMailSender javaMailSender,
                               User user) throws MessagingException, UnsupportedEncodingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("kantemirmirza99@gmail.com", senderName);
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(mailContent, true);
        javaMailSender.send(mimeMessage);
    }
}
