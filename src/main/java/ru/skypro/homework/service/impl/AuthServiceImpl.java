package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        if (encoder.matches(password, user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        UserDetails user = User.builder()
                .username(register.getUsername())
                .password(encoder.encode(register.getPassword()))
                .roles(register.getRole().name())
                .build();
        manager.createUser(user);
        userRepository.save(new ru.skypro.homework.entity.UserEntity(
                null,
                register.getUsername(),
                register.getFirstName(),
                register.getLastName(),
                register.getPhone(),
                encoder.encode(register.getPassword()),
                register.getRole()
        ));
        return true;
    }

}
