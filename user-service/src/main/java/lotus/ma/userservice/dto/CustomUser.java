package lotus.ma.userservice.dto;

import lombok.*;
import lotus.ma.userservice.dao.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomUser implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert your enum role to Spring Security authority
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        // Your entity stores the password under "passwordHash"
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        // Spring Security uses this to authenticate
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Use the "isActive" field from your entity
        return user.isActive();
    }

    public UUID getUserId() {
        return user.getUserId();
    }
}
