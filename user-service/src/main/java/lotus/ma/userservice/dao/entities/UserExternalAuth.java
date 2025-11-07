package lotus.ma.userservice.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import lotus.ma.userservice.dao.enums.AuthProvider;


import java.util.UUID;

@Entity
@Table(name = "user_external_auths")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExternalAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(name = "provider_user_id", unique = true, nullable = false)
    private String providerUserId;
}
