package backend.teamble.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
}
