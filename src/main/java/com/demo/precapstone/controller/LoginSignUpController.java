package com.demo.precapstone.controller;

import com.demo.precapstone.dao.User;
import com.demo.precapstone.dto.LoginRequestDTO;
import com.demo.precapstone.repository.UserRepository;
import com.demo.precapstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginSignUpController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        String username = request.getUsername();
        String password = request.getPassword();

        User user = userRepository.findByUserName(username);

        if (user != null) {
            // DB에 저장된 암호화된 비밀번호
            String storedPassword = user.getPassword();

            // 전달된 비밀번호를 암호화하여 DB의 비밀번호와 비교
            boolean matches = passwordEncoder.matches(password, storedPassword);

            // 콘솔에 암호화된 비밀번호 비교 결과 출력
            System.out.println("Entered Password: " + password);
            System.out.println("Stored Password (Encrypted): " + storedPassword);
            System.out.println("Passwords match: " + matches);

            if (matches) {
                // 비밀번호가 일치하는 경우
                return ResponseEntity.ok("Login successful");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody LoginRequestDTO request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (userRepository.findByUserName(username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        userService.registerUser(username, password);

        return ResponseEntity.ok("User registered successfully");
    }
}