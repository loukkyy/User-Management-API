package com.demo.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.model.Privileges;
import com.demo.model.User;
import com.demo.service.IUserService;

import io.jsonwebtoken.JwtException;

@RestController
public class ApiController {

	@Autowired
	private IUserService userService;

    @GetMapping("/api/users")
    @Secured(Privileges.CAN_USER_READ)
    public List<User>  getAllUsers() {
        List<User> users = this.userService.findAll();
        return users;
    }
    
    @PostMapping("/api/users")
    @Secured(Privileges.CAN_USER_WRITE)
    public ResponseEntity<User> saveNewUser(@Valid @RequestBody User user) {
        User registered = null;
		registered = userService.registerNewUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registered.getId()).toUri();
		return ResponseEntity.created(location).body(registered);
    }

    @DeleteMapping("/api/users/{id}")
    @Secured(Privileges.CAN_USER_DELETE)
    @ResponseStatus(code = HttpStatus.OK)
    public String delete(@PathVariable("id") Long id) {
        String response = this.userService.delete(id);
		return response;
    }
    


    @GetMapping("/api/test")
    public void  test() {
        throw new JwtException("Token has expired");
    }
}
