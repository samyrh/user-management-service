package lotus.ma.userservice.web;

import lombok.RequiredArgsConstructor;
import lotus.ma.userservice.dao.entities.Subscription;
import lotus.ma.userservice.dao.entities.User;
import lotus.ma.userservice.dao.enums.AuthProvider;
import lotus.ma.userservice.dao.enums.PlanName;
import lotus.ma.userservice.dao.enums.UserRole;
import lotus.ma.userservice.dao.repositories.SubscriptionRepository;
import lotus.ma.userservice.dao.repositories.UserRepository;
import lotus.ma.userservice.dto.CustomUser;
import lotus.ma.userservice.dto.LoginUserDto;
import lotus.ma.userservice.dto.RegisterUserDto;
import lotus.ma.userservice.dto.UserResponseDto;
import lotus.ma.userservice.services.AuthenticationService;
import lotus.ma.userservice.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    // ✅ Register endpoint
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterUserDto request) {
        // 🔹 Find the FREE subscription plan in DB
        Subscription freePlan = subscriptionRepository.findByPlanName(PlanName.FREE)
                .orElseThrow(() -> new RuntimeException("Default FREE subscription not found in DB"));

        // 🔹 Create and save new user linked with FREE plan
        User newUser = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.USER)
                .authProvider(AuthProvider.LOCAL)
                .isActive(true)
                .subscription(freePlan)
                .build();

        newUser = userRepository.save(newUser);

        // 🔹 Generate JWT token
        var jwtToken = jwtService.generateToken(CustomUser.builder().user(newUser).build());

        // 🔹 Build response
        return ResponseEntity.ok(
                UserResponseDto.builder()
                        .userId(newUser.getUserId())
                        .email(newUser.getEmail())
                        .role(newUser.getRole().name())
                        .planName(freePlan.getPlanName().name())
                        .subscriptionStatus(freePlan.getStatus())
                        .token(jwtToken)
                        .message("Registration successful ✅ Linked to FREE plan.")
                        .build()
        );
    }

    // ✅ Login endpoint
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginUserDto request) {
        // 🔹 Authenticate credentials
        var authenticatedUser = authenticationService.authenticate(request);
        var user = authenticatedUser.getUser();

        // 🔹 Get subscription info (if exists)
        var subscription = user.getSubscription();

        String planName = subscription != null ? subscription.getPlanName().name() : "NONE";
        String subscriptionStatus = subscription != null ? subscription.getStatus() : "INACTIVE";

        // 🔹 Generate JWT token
        var jwtToken = jwtService.generateToken(authenticatedUser);

        // 🔹 Return full response
        return ResponseEntity.ok(
                UserResponseDto.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .planName(planName)
                        .subscriptionStatus(subscriptionStatus)
                        .token(jwtToken)
                        .message("Login successful ✅")
                        .build()
        );
    }
}
