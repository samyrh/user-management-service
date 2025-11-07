package lotus.ma.userservice.dao.repositories;

import lotus.ma.userservice.dao.entities.Subscription;
import lotus.ma.userservice.dao.enums.PlanName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByPlanName(PlanName planName);
}
