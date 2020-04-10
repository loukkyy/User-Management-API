package com.demo.web.mvc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.persistence.model.Privileges;
import com.demo.persistence.model.User;
import com.demo.service.IUserService;
import com.demo.validation.EmailExistsException;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@RequestMapping("/users")
public class UserController {

	 @Autowired
    private IUserService userService;

    @RequestMapping
    public ModelAndView list() {
        Iterable<User> users = this.userService.findAll();
        return new ModelAndView("tl/list", "users", users);
    }

    @RequestMapping("{id}")
    public ModelAndView view(@PathVariable("id") User user) {
        return new ModelAndView("tl/view", "user", user);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid User user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("tl/form", "formErrors", result.getAllErrors());
        }
        try {
            final User registered = userService.registerNewUser(user);

        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("tl/form", "formErrors", result.getAllErrors());
        }
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/users/{user.id}", "user.id", user.getId());
    }

    @RequestMapping(value = "delete/{id}")
    @Secured(Privileges.CAN_USER_DELETE)
    //@PreAuthorize("hasRole('ROLE_" + Privileges.CAN_USER_DELETE + "')")
    //@PreAuthorize("hasPermission(#contact, 'USER_DELETE')")
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
        return new ModelAndView("redirect:/users/");
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView modifyForm(@PathVariable("id") User user) {
        return new ModelAndView("tl/form", "user", user);
    }

    // the form

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute User user) {
        return "tl/form";
    }

}