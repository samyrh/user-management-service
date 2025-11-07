package lotus.ma.userservice.services;


import lombok.AllArgsConstructor;
import lotus.ma.userservice.dao.entities.User;
import lotus.ma.userservice.dao.repositories.UserRepository;
import lotus.ma.userservice.dto.CustomUser;
import lotus.ma.userservice.dto.LoginUserDto;
import lotus.ma.userservice.dto.RegisterUserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public CustomUser signup(RegisterUserDto input) {
        User customer = User.builder()
                .email(input.getEmail())
                .passwordHash(passwordEncoder.encode(input.getPassword()))
                .fullName(input.getFullName())
                .build();
        customer = customerRepository.save(customer);
        return CustomUser.builder().user(customer).build();
    }

    public CustomUser authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        User customer = customerRepository.findByEmail(input.getEmail()).orElseThrow();
        return CustomUser.builder().user(customer).build();
    }

}