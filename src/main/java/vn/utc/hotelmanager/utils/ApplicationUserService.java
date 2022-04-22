package vn.utc.hotelmanager.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserService {

    public static UserDetails getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }

    public static boolean currentUserHasAdminRole() {
        UserDetails thisUser = getCurrentUser();
        return thisUser.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals(
                        ApplicationUserRoleConstants.ROLE_ADMIN.getValue())
        );
    }
}
