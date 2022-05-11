package vn.utc.clients.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String email;
    private String username;
    private UserProfile profileDetails;
}
