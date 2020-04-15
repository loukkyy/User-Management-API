package com.demo.userManagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.registration.dto.UserSignupRequest;
import com.demo.registration.web.validation.EmailExistsException;
import com.demo.security.ActiveUserService;
import com.demo.userManagement.dto.UserDto;
import com.demo.userManagement.model.Privileges;
import com.demo.userManagement.model.User;
import com.demo.userManagement.service.IUserService;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@RequestMapping("/users")
public class UserController {

	 @Autowired
    private IUserService userService;
	 
	 @Autowired
	 private ActiveUserService activeUserService;
	 
	 @Autowired
	 private ModelMapper modelMapper;
	 
	    @RequestMapping(method = RequestMethod.GET)
	    public ModelAndView list() {
	        List<User> users = this.userService
	        		.findAll();
			Iterable<UserDto> usersDto = users
	        		.stream()
	                .map(u -> modelMapper.map(u, UserDto.class))
	                .collect(Collectors.toList());
	        return new ModelAndView("tl/list", "users", usersDto);
	    }
		 
	    @RequestMapping(value = "json", method = RequestMethod.GET)
	    @ResponseBody
	    public Iterable<UserDto> listJson() {
	        List<User> users = this.userService
	        		.findAll();
			return users
	        		.stream()
	                .map(u -> modelMapper.map(u, UserDto.class))
	                .collect(Collectors.toList());
	    }

    @RequestMapping(params = "active", method = RequestMethod.GET)
    public ModelAndView listActiveUsers(@RequestParam("active") Boolean active) {
        List<String> allActiveUsers = this.activeUserService.getAllActiveUsers();
        Iterable<UserDto> users = allActiveUsers
        		.stream()
        		.map(s -> modelMapper.map(s, UserDto.class))
        		.collect(Collectors.toList());
        return new ModelAndView("tl/list", "users", users);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("id") UserDto userDto) {
        return new ModelAndView("tl/view", "user", userDto);
    }

    @RequestMapping(value = "json/{id}", method = RequestMethod.GET)
    @ResponseBody
    public UserDto viewJson(@PathVariable("id") UserDto userDto) {
        return userDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid UserSignupRequest userSignupRequest, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("tl/form", "formErrors", result.getAllErrors());
        }
        try {

        	User user = modelMapper.map(userSignupRequest, User.class);
            final User registered = userService.registerNewUser(user);
            redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
            return new ModelAndView("redirect:/users/{user.id}", "user.id", registered.getId());
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("tl/form", "formErrors", result.getAllErrors());
        }
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
    public ModelAndView modifyForm(@PathVariable("id") UserDto userDto) {
        return new ModelAndView("tl/form", "user", userDto);
    }

    // the form

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute UserSignupRequest userSignupRequest) {
        return "tl/form";
    }
    
    @Bean
    public Converter<String, UserDto> messageConverter() {
    	return new Converter<String, UserDto>() {
    		@Override
    		public UserDto convert(String id) {
    			User user = userService.findById(Long.valueOf(id));
    			return modelMapper.map(user, UserDto.class);
    		}
    	};
    	
    }

}