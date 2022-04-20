package vn.utc.hotelmanager.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.jwt.config.JwtConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtConfig jwtConfig, JwtUtils jwtUtils, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.setFilterProcessesUrl(jwtConfig.getFilterUrl());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            AuthenticationRequest credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword()
            );

            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = jwtUtils.generateToken(authResult.getName(), authResult.getAuthorities());

        User user = userRepository.findByUsername(authResult.getName());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(user));

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
    }
}
