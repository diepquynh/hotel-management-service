package vn.utc.hotelmanager.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.utc.hotelmanager.auth.user.service.UserDetailsServiceImpl;
import vn.utc.hotelmanager.jwt.config.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenFilter(JwtConfig jwtConfig, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try {

            Jws<Claims> claimsJws = jwtUtils.parseToken(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(request, response);
    }
}
