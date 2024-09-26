package iocode.web.app.filters;

import iocode.web.app.entity.User;
import iocode.web.app.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that authenticates incoming requests using JWT tokens.
 * This filter is responsible for extracting the JWT token from the request header,
 * validating it, and setting up the user's authentication context.
 *
 * @author YourName
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Performs the actual filtering of the request.
     *
     * @param request The incoming request.
     * @param response The response to be sent.
     * @param filterChain The filter chain to be executed after this filter.
     * @throws ServletException If an error occurs during the filtering process.
     * @throws IOException If an error occurs during the filtering process.
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        if(jwtToken == null || !jwtService.isTokenValid(jwtToken.substring(7))) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = jwtToken.startsWith("Bearer ") ? jwtToken.substring(7) : jwtToken;
        String subject = jwtService.extractSubject(jwtToken);
        User user = (User) userDetailsService.loadUserByUsername(subject);
        var context = SecurityContextHolder.getContext();
        if(user != null && context.getAuthentication() == null) {
            var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(request);
            context.setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
