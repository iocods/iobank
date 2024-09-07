package iocode.web.app.service;

import iocode.web.app.dto.UserDto;
import iocode.web.app.entity.User;
import iocode.web.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository; // Inject UserRepository
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder
    private final UserDetailsService userDetailsService; // Inject UserDetailsService
    private final AuthenticationManager authenticationManager; // Inject AuthenticationManager
    private final JwtService jwtService; // Inject JwtService

    public User registerUser(UserDto userDto) {
        User user = mapToUser(userDto);
        return userRepository.save(user);
    }
    public Map<String, Object> authenticateUser(UserDto userDto) {
        Map<String, Object> authObject = new HashMap<String, Object>();
        User user = (User) userDetailsService.loadUserByUsername(userDto.getUsername());
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        authObject.put("token", "Bearer ".concat(jwtService.generateToken(userDto.getUsername())));
        authObject.put("user", user);
        return authObject;
    }
    private User mapToUser(UserDto dto){
        return User.builder()
               .lastname(dto.getLastname())
               .firstname(dto.getFirstname())
               .username(dto.getUsername())
               .password(passwordEncoder.encode(dto.getPassword()))
               .dob(dto.getDob())
               .roles(List.of("USER"))
               .tag("io_" + dto.getUsername())
               .build();
    }

}
