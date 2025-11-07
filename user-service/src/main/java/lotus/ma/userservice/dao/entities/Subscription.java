package lotus.ma.userservice.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import lotus.ma.userservice.dao.enums.PlanName;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id")
    private UUID subscriptionId;

    // many users can share a subscription plan
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_name", nullable = false)
    private PlanName planName = PlanName.FREE;

    @Column(nullable = false)
    private String status = "active";


    @Column(name = "backtests_per_day_limit", nullable = false)
    private int backtestsPerDayLimit = 5;

    @Column(name = "deployments_per_round_limit", nullable = false)
    private int deploymentsPerRoundLimit = 2;

    @Column(name = "api_requests_per_hour_limit")
    private Integer apiRequestsPerHourLimit = 1000;


    @Column(name = "backtests_used_today", nullable = false)
    private int backtestsUsedToday = 0;

    @Column(name = "deployments_used_current_round", nullable = false)
    private int deploymentsUsedCurrentRound = 0;

    // ⏰ Reset Times
    @Column(name = "daily_quota_reset_at")
    private Instant dailyQuotaResetAt;

    @Column(name = "hourly_quota_reset_at")
    private Instant hourlyQuotaResetAt;

    // 📅 Subscription Period
    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;
}
