package vn.utc.userservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.utc.userservice.data.ProfileRepository;
import vn.utc.userservice.data.UserRepository;
import vn.utc.userservice.data.dto.UserDetailsDTO;
import vn.utc.userservice.data.dto.UserRegistrationDTO;
import vn.utc.userservice.data.dto.request.UserUpdateRequestDTO;
import vn.utc.userservice.exception.InvalidRequestException;
import vn.utc.userservice.exception.RepositoryAccessException;
import vn.utc.userservice.exception.UserAlreadyExistException;
import vn.utc.userservice.model.Profile;
import vn.utc.userservice.model.User;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final KeycloakUserService keycloakUserService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public UserDetailsDTO registerUser(UserRegistrationDTO registrationRequest) {
        UserDetailsDTO userDetailsDTO = null;
        List<UserRepresentation> userRepresentations = keycloakUserService.readUserByUsername(registrationRequest.getUsername());
        if (!CollectionUtils.isEmpty(userRepresentations)) {
            throw new UserAlreadyExistException("This username was registered. Please check and retry.");
        }

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(registrationRequest.getFirstName());
        userRepresentation.setLastName(registrationRequest.getLastName());
        userRepresentation.setEmail(registrationRequest.getEmail());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(registrationRequest.getUsername());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(registrationRequest.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        Integer createUserResponse = keycloakUserService.createUser(userRepresentation);
        if (createUserResponse.equals(HttpStatus.SC_CREATED)) {
            List<UserRepresentation> newUserRepresentation =
                    keycloakUserService.readUserByUsername(registrationRequest.getUsername());

            try {
                Profile newProfile = Profile.builder()
                        .firstName(registrationRequest.getFirstName())
                        .lastName(registrationRequest.getLastName())
                        .address(registrationRequest.getAddress())
                        .phone(registrationRequest.getPhone())
                        .age(registrationRequest.getAge())
                        .image(registrationRequest.getImage())
                        .gender(registrationRequest.getGender())
                        .build();
                profileRepository.save(newProfile);

                User newUser = User.builder()
                        .authId(newUserRepresentation.get(0).getId())
                        .status("APPROVED")
                        .email(registrationRequest.getEmail())
                        .username(registrationRequest.getUsername())
                        .profile(newProfile)
                        .build();
                userRepository.save(newUser);

                userDetailsDTO = new UserDetailsDTO(newUser);
            } catch (Exception e) {
                throw new RepositoryAccessException(
                        String.format("Unable to create user: %s", e.getMessage())
                );
            }
        }

        return userDetailsDTO;
    }

    public UserDetailsDTO getUser(Integer userId) {
        return new UserDetailsDTO(userRepository.findById(userId)
                .orElseThrow(
                        () -> new InvalidRequestException(
                                String.format("User with id %d not found", userId)
                        )
                ));
    }

    @Transactional
    public UserDetailsDTO updateUser(Integer userId, UserUpdateRequestDTO updateRequest) {
        verifyUpdateRequest(updateRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new InvalidRequestException(
                                String.format("User with id %d not found", userId)
                        )
                );

        updateUserDetails(user, updateRequest);
        return new UserDetailsDTO(user);
    }

    private void updateUserDetails(User user, UserUpdateRequestDTO updateRequest) {
        UserRepresentation userRepresentation = keycloakUserService.readUser(user.getAuthId());
//        CredentialRepresentation credentialRepresentation = userRepresentation.getCredentials().get(0);
        Profile profile = user.getProfile();

        if (updateRequest.getUsername() != null) {
            userRepresentation.setUsername(updateRequest.getUsername());
            user.setUsername(user.getUsername());
        }

        if (updateRequest.getEmail() != null) {
            userRepresentation.setEmail(updateRequest.getEmail());
            user.setEmail(updateRequest.getEmail());
        }

//        if (updateRequest.getOldPassword() != null) {
//            String currentUserCredential = credentialRepresentation.getCredentialData();
//            if (!passwordEncoder.matches(updateRequest.getOldPassword(), currentUserCredential))
//                throw new InvalidRequestException("Old password does not match");
//
//            if (!updateRequest.getOldPassword().matches(updateRequest.getNewPassword())) {
//                credentialRepresentation.setCredentialData(updateRequest.getNewPassword());
//                userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
//            }
//        }

        updateUserProfile(profile, updateRequest);

        try {
            profileRepository.save(profile);
            userRepository.save(user);
            keycloakUserService.updateUser(userRepresentation);
        } catch (DataAccessException e) {
            throw new RepositoryAccessException(
                    String.format("Unable to update user: %s", e.getMessage())
            );
        }
    }

    private void updateUserProfile(Profile userProfile, UserUpdateRequestDTO updateRequest) {
        if (updateRequest.getFirstName() != null)
            userProfile.setFirstName(updateRequest.getFirstName());

        if (updateRequest.getLastName() != null)
            userProfile.setLastName(updateRequest.getLastName());

        if (updateRequest.getAddress() != null)
            userProfile.setAddress(updateRequest.getAddress());

        if (updateRequest.getPhone() != null)
            userProfile.setPhone(updateRequest.getPhone());

        if (updateRequest.getAge() != null)
            userProfile.setAge(updateRequest.getAge());

        if (updateRequest.getImage() != null)
            userProfile.setImage(updateRequest.getImage());

        if (updateRequest.getGender() != null)
            userProfile.setGender(updateRequest.getGender());
    }

    private void verifyUpdateRequest(UserUpdateRequestDTO updateRequest) {
        // no-op for now
    }
}
