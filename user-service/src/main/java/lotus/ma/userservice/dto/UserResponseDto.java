package lotus.ma.userservice.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private UUID userId;
    private String email;
    private String role;
    private String planName;
    private String subscriptionStatus;
    private String token;
    private String message;
}
