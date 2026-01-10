package booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsWithRole {
    private String username;
    private Long UserID;
    private String role;
}
