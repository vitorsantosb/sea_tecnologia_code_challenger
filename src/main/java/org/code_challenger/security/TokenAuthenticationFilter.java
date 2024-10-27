package org.code_challenger.security;

import lombok.var;
import org.code_challenger.repository.TokenRepository;
import org.code_challenger.repository.UserRepository;
import org.code_challenger.repository.dto.CustomUserDetails;
import org.code_challenger.repository.dto.TokenCredentials;
import org.code_challenger.repository.dto.User;
import org.code_challenger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserService userService;

    @Autowired
    public TokenAuthenticationFilter(UserRepository userRepository, TokenRepository tokenRepository, UserService userService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        // Log all headers for debugging
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // extrai o token
            System.out.println("Token found: " + token);

            // Aqui você deve verificar se o token é válido, dependendo de sua implementação
            Optional<TokenCredentials> optionalToken = tokenRepository.findByToken(UUID.fromString(token)); // Ajuste se necessário
            if (optionalToken.isPresent()) {
                TokenCredentials tokenCredentials = optionalToken.get();
                UUID userId = tokenCredentials.getUserId();
                System.out.println("Token is valid. User ID: " + userId);

                Optional<User> optionalUser = userRepository.findById(userId);
                if (!optionalUser.isPresent()) {
                    System.out.println("User not found for ID: " + userId);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                    return;
                }

                CustomUserDetails _userDetails = userService.LogUserDetails(userId);
                System.out.println("User details: " + _userDetails);

                var authentication = new UsernamePasswordAuthenticationToken(
                        _userDetails,
                        null,
                        _userDetails.getAuthorities() // Passa as autoridades aqui
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("User authenticated successfully.");
            } else {
                System.out.println("Invalid token: " + token);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token.");
                return;
            }
        } else {
            System.out.println("Authorization header is missing or invalid.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

