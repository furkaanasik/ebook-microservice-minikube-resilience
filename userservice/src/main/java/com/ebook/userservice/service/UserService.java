package com.ebook.userservice.service;

import com.ebook.userservice.dto.LoginResponseDTO;
import com.ebook.userservice.dto.UserDTO;
import com.ebook.userservice.entity.User;
import com.ebook.userservice.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelService modelService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelService modelService, @Lazy AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelService = modelService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(String email, String password) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String jwt = this.jwtService.generateJwtToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponseDTO("Bearer " + jwt);
    }

    public UserDTO getUserById(Long id) {
        Optional<User> optUser = this.userRepository.findById(id);
        if(optUser.isPresent()) {
            return this.modelService.map(optUser.get(), UserDTO.class);
        }
        throw new RuntimeException("User not found!");
    }

    public UserDTO create(UserDTO userDTO) {
        Optional<User> optUser = this.findByEmail(userDTO.getEmail());
        if(!optUser.isPresent()) {
            User user = this.modelService.map(userDTO, User.class);
            user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
            user = this.userRepository.save(user);
            return this.modelService.map(user, UserDTO.class);
        }
        throw new RuntimeException("User already exist!");
    }

    private Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.findByEmail(email).orElseThrow(() -> new RuntimeException(email + " cannot find!"));
    }


}
