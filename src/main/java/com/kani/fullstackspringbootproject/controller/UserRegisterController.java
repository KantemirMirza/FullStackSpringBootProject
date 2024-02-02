package com.kani.fullstackspringbootproject.controller;

import com.kani.fullstackspringbootproject.dto.UserRegisterForm;
import com.kani.fullstackspringbootproject.entity.User;
import com.kani.fullstackspringbootproject.resetpassword.IPasswordResetTokenService;
import com.kani.fullstackspringbootproject.service.IUserService;
import com.kani.fullstackspringbootproject.token.IVerificationTokenService;
import com.kani.fullstackspringbootproject.token.VerificationToken;
import com.kani.fullstackspringbootproject.utils.ApplicationURL;
import com.kani.fullstackspringbootproject.utils.RegistrationCompleteEvent;
import com.kani.fullstackspringbootproject.utils.RegistrationCompleteEventListener;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class UserRegisterController {
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final IVerificationTokenService verificationTokenService;
    private final IPasswordResetTokenService passwordResetTokenService;
    private final RegistrationCompleteEventListener registrationCompleteEventListener;

    @GetMapping("/create")
    public String getUserForm(Model model){
        model.addAttribute("registerUser", new UserRegisterForm());
        return "registerUser";
    }

    @PostMapping("/create")
    public String registerUser(@ModelAttribute("registerUser") @Valid UserRegisterForm form,
                               BindingResult result,
                               Model model,
                               HttpServletRequest request) {
        if (result.hasErrors()) {
            return "registerUser";
        }

        if (!userService.isEmailUnique(form.getEmail())) {
            model.addAttribute("emailExistedError", "This E-Mail:  " + form.getEmail() + " Existed");
            return "registerUser";
        }

        User user = userService.saveUserToDB(form);
        // Register Completion email publisher here
        publisher.publishEvent(new RegistrationCompleteEvent(user, ApplicationURL.getApplicationUrl(request)));

        return "redirect:/register/create?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        Optional<VerificationToken> verifyToken = verificationTokenService.findByVerificationToken(token);
        if(verifyToken.isPresent() && verifyToken.get().getUser().isEnabled()){
            return "redirect:/login?verified";
        }

        String resultVerificationToken = verificationTokenService.validateVerificationToken(token);

        switch (resultVerificationToken.toLowerCase()){
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(){
        return "forgot_password_form";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        User user = userService.findUserByEmailFromDB(email);
        if(user == null){
            return "redirect:/register/forgot-password?not_found";
        }

        String passwordResetToken = UUID.randomUUID().toString();

        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
        String url = ApplicationURL.getApplicationUrl(request) + "/register/forgot-password?token=" + passwordResetToken;
        try {
            registrationCompleteEventListener.sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/register/forgot-password?success";
    }

    @GetMapping("/password-reset")
    public String passwordResetForm(@RequestParam("token")  String token, Model model){
        model.addAttribute("token", token);
        return "password-reset-form";
    }

    @PostMapping("/password-reset")
    public String resetPassword(HttpServletRequest request){

        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);
        if(!tokenVerificationResult.equalsIgnoreCase("VALID")){
            return "redirect:/error?invalid_token";
        }

        Optional<User> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
        if(theUser.isPresent()){
            passwordResetTokenService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";
    }
}
