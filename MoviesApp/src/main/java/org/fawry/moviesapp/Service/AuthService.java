package org.fawry.moviesapp.Service;

import org.fawry.moviesapp.config.JwtUtil;
import org.fawry.moviesapp.dto.LoginDto;
import org.fawry.moviesapp.entity.User;
import org.fawry.moviesapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String registerUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return jwtUtil.generateToken(user.getName(), user.getRole());
    }

    public String loginUser(LoginDto loginDto) {
        User user = userRepository.findByName(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtUtil.generateToken(user.getName(), user.getRole());
    }
}
