package vn.utc.hotelmanager.auth.user.service;

import vn.utc.hotelmanager.auth.authorities.data.RoleRepository;
import vn.utc.hotelmanager.auth.exception.UserAlreadyExistException;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.data.dto.UserRegistrationDTO;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.auth.user.model.UserDetailsImpl;
import vn.utc.hotelmanager.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userDAO;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                                  UserRepository userDAO, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDAO = userDAO;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user;

        if (username.contains("@"))
            user = userDAO.findByEmail(username);
        else
            user = userDAO.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Could not find user");

        return new UserDetailsImpl(user);
    }

    public void registerUser(UserRegistrationDTO userRequest) {
        boolean usernameExist = userDAO.findByUsername(userRequest.getUsername()) != null;
        if (usernameExist)
            throw new UserAlreadyExistException("Username exist!");

        boolean emailExist = userDAO.findByEmail(userRequest.getEmail()) != null;
        if (emailExist)
            throw new UserAlreadyExistException("Email exist!");

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEnabled(true);

        userDAO.save(user);
    }

    public String updateUser(User newUser) {
        // This is never null
        User oldUser = userDAO.findById(newUser.getId()).get();

        if (newUser.getUsername() != null) {
            boolean usernameExist = userDAO.findByUsername(newUser.getUsername()) != null;
            if (usernameExist)
                throw new UserAlreadyExistException("Username exist!");
            oldUser.setUsername(newUser.getUsername());
        }

        if (newUser.getEmail() != null) {
            boolean emailExist = userDAO.findByEmail(newUser.getEmail()) != null;
            if (emailExist)
                throw new UserAlreadyExistException("Email exist!");
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getPassword() != null)
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userDAO.save(oldUser);
        UserDetailsImpl userDetails = new UserDetailsImpl(oldUser);
        return jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
    }
}
