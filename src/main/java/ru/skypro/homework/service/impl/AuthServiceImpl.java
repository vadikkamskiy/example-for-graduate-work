package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.entity.UserEntity;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserDetailsManager manager,
                           UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder encoder) {
        this.manager = manager;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    @Override
    public boolean login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication.isAuthenticated();
        } catch (AuthenticationException e) {
            return false;
        }
    }

    @Override
    public boolean register(Register register){
        try {
            if (userRepository.findByUsername(register.getUsername()).isPresent()) {
                return false;
            }
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(register.getUsername());
            userEntity.setFirstName(register.getFirstName());
            userEntity.setLastName(register.getLastName());
            userEntity.setPhone(register.getPhone());
            userEntity.setPassword(encoder.encode(register.getPassword()));
            userEntity.setRole(register.getRole());
            userRepository.save(userEntity);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }
    @Override
    public boolean logout() {
        SecurityContextHolder.clearContext();
        return true;
    }

}
