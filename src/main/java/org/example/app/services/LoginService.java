package org.example.app.services;

import org.example.web.controllers.LoginController;
import org.example.web.dto.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticate(LoginForm loginForm) {
        logger.info("try auth with user-from: {}", loginForm);
        logger.info("login: {}, pass: {}, encode: {}",
                loginForm.getUsername(), loginForm.getPassword(), passwordEncoder.encode("1234")
                );

        return loginForm.getUsername().equals("root")
                && passwordEncoder.matches(loginForm.getPassword(), "1234");
    }
}