package com.demo.registration.controller;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.registration.dto.UserSignupRequest;
import com.demo.registration.model.VerificationToken;
import com.demo.registration.web.validation.EmailExistsException;
import com.demo.userManagement.model.User;
import com.demo.userManagement.service.IUserService;

@Controller
@RequestMapping("/")
public class RegistrationController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "userSignupRequest", new UserSignupRequest());
    }

    @RequestMapping(value = "users/register")
    public ModelAndView registerUser(@Valid final UserSignupRequest userSignupRequest, final BindingResult result, final HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "userSignupRequest", userSignupRequest);
        }
        try {
        	User user = modelMapper.map(userSignupRequest, User.class);
            final User registered = userService.registerNewUser(user);

            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            //eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
        } catch (EmailExistsException e) {
            result.addError(new FieldError("userSignupRequest", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "userSignupRequest", userSignupRequest);
        }
        return new ModelAndView("loginPage");
    }
    
    @RequestMapping(value = "/registrationConfirm")
    public ModelAndView confirmRegistration(final Model model, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid account confirmation token.");
            return new ModelAndView("redirect:/login");
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
            .getTime()
            - cal.getTime()
                .getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your registration token has expired. Please register again.");
            return new ModelAndView("redirect:/login");
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
        return new ModelAndView("redirect:/login");
    }
}
