package school.faang.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.faang.user_service.entity.ProjectSubscription;

import java.util.Optional;

@Repository
public interface ProjectSubscriptionRepository extends JpaRepository<ProjectSubscription, Long> {
    @Query(nativeQuery = true,
            value = "select exists (select 1 from project_subscription where follower_id = :followerId and project_id = :projectId)")
    boolean existsByFollowerIdAndProjectId(long followerId, long projectId);

    @Query(nativeQuery = true,
            value = "insert into project_subscription  (follower_id, project_id) values (:followerId, :projectId)")
    void followProject(long followerId, long projectId);
    Optional<Long> findFollowerIdByProjectId(@Param("projectId") Long projectId);

}