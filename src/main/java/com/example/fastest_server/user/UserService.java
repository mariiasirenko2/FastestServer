package com.example.fastest_server.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.lang.Integer.valueOf;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserDetailService userDetailService;


    public User  signUpUser(User user) {

        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();
        if (userExists) {
            throw new IllegalStateException("Invalid email");
        }

       //encode password and push new user to DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);
        userRepository.save(user);


        return user;
    }

    public User getUserIdByUsername(String username){

        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                "User not found: check username"));

    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(
                "User not found: check username"));
    }

}
