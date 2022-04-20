package vn.utc.hotelmanager.auth.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    public static UserDetails getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }
}
