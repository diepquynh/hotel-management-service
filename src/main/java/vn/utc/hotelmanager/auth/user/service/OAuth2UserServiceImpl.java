package vn.utc.hotelmanager.auth.user.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import vn.utc.hotelmanager.auth.authorities.data.RoleRepository;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.model.OAuth2UserImpl;
import vn.utc.hotelmanager.auth.user.model.Provider;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.auth.user.model.UserDetailsImpl;
import vn.utc.hotelmanager.jwt.JwtUtils;
import vn.utc.hotelmanager.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    @Autowired
    private Environment env;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userDAO;
    private final JwtUtils jwtUtils;

    @Autowired
    public OAuth2UserServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDAO = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new OAuth2UserImpl(user);
    }

    public User postLogin(String email) {
        User user = userDAO.findByEmail(email);

        if (user == null) {
            User newUser = new User();

            newUser.setEmail(email);
            newUser.setUsername(RandomStringGenerator.getString(45));
            newUser.setPassword(
                    passwordEncoder.encode(RandomStringGenerator.getString(64)));
            newUser.setEnabled(true);
            newUser.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));
            newUser.setProvider(Provider.GOOGLE);

            userDAO.save(newUser);
            return newUser;
        } else {
            if (user.getProvider() == null)
                user.setProvider(Provider.GOOGLE);

            userDAO.save(user);
            return user;
        }
    }

    public String processToken(String idTokenString) throws GeneralSecurityException, IOException {
        String clientId = env.getProperty("spring.security.oauth2.client.registration.google.clientId");
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            UserDetails userDetails = new UserDetailsImpl(postLogin(email));

            return jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
        }

        return null;
    }
}
