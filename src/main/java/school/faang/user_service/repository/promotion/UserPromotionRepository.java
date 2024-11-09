package school.faang.user_service.repository.promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.faang.user_service.entity.promotion.UserPromotion;

@Repository
public interface UserPromotionRepository extends JpaRepository<UserPromotion, Long> {
    boolean existsByUserId(long userId);
}
