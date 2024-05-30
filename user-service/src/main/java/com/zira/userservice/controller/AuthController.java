package com.zira.userservice.controller;


import com.zira.userservice.config.JwtTokenProvider;
import com.zira.userservice.entity.User;
import com.zira.userservice.exception.UserException;
import com.zira.userservice.repository.UserRepository;
import com.zira.userservice.request.LoginRequest;
import com.zira.userservice.response.AuthResponse;
import com.zira.userservice.service.impl.AuthServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthServiceImpl userService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody User user) throws UserException {

        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getUsername();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null){
            throw new UserException("Email is already connected with an other account");
        }

        User newUser = new User();
        newUser.setUsername(fullName) ;
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);

        userRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("registration Success");
        authResponse.setStatus(true);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signInUser(@RequestBody LoginRequest loginRequest){
        String userName = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(userName, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login successfully");
        authResponse.setStatus(true);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = userService.loadUserByUsername(userName);

        if(userDetails == null)
            throw new BadCredentialsException("Invalid username or password");

        if(!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException(("Invalid Username or password"));

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

}
