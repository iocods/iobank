package iocode.web.app.config;

import iocode.web.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This class is responsible for configuring various components of the application, including authentication,
 * password encoding, REST template, and a scheduled executor service.
 *
 * @author YourName
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;

    /**
     * Creates a custom UserDetailsService that retrieves user details from the database using the provided
     * UserRepository. The username is case-insensitive.
     *
     * @return a UserDetailsService instance
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userRepository::findByUsernameIgnoreCase;
    }

    /**
     * Creates a BCryptPasswordEncoder instance for encoding passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a DaoAuthenticationProvider instance that uses the custom UserDetailsService and the
     * BCryptPasswordEncoder for authentication.
     *
     * @return a DaoAuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider () {
        var daoProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoProvider.setUserDetailsService(userDetailsService());
        return daoProvider;
    }

    /**
     * Creates an AuthenticationManager instance using the provided AuthenticationConfiguration.
     *
     * @param config the AuthenticationConfiguration instance
     * @return an AuthenticationManager instance
     * @throws Exception if an error occurs during the creation of the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates a RestTemplate instance for making HTTP requests.
     *
     * @return a RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates a ScheduledExecutorService instance with a single thread for scheduling tasks.
     *
     * @return a ScheduledExecutorService instance
     */
    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }
}
