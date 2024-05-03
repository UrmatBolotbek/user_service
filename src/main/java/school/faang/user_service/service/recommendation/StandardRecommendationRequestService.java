package school.faang.user_service.service.recommendation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.recommendation.RecommendationRequestDto;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.entity.recommendation.SkillRequest;
import school.faang.user_service.exceptions.DataValidationException;
import school.faang.user_service.mapper.recommendation.RecommendationRequestMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRequestRepository;
import school.faang.user_service.service.recommendation.filters.RecommendationRequestFilter;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StandardRecommendationRequestService implements RecommendationRequestService {
    private final RecommendationRequestRepository recommendationRequestRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final RecommendationRequestMapper recommendationRequestMapper;
    private final List<RecommendationRequestFilter> recommendationRequestFilters;

    @Override
    public RecommendationRequestDto requestRecommendation(RecommendationRequest recommendationRequest) {
        validateRecommendationRequest(recommendationRequest);
        RecommendationRequest savedRecommendationRequest = recommendationRequestRepository.save(recommendationRequest);
        return recommendationRequestMapper.fromEntityToDto(savedRecommendationRequest);
    }

    private void validateRecommendationRequest(RecommendationRequest recommendationRequest) {
        long requesterId = recommendationRequest.getRequester().getId();
        long receiverId = recommendationRequest.getReceiver().getId();
        List<SkillRequest> skillRequests = recommendationRequest.getSkills();
        checkUsersExisting(requesterId);
        checkUsersExisting(receiverId);
        checkSkillsExisting(skillRequests);
        checkRequestDate(recommendationRequest);
    }

    private void checkUsersExisting(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new DataValidationException(String.format("user with id: %d is not exists", userId));
        }
    }

    private void checkSkillsExisting(List<SkillRequest> skillRequests) {
        skillRequests.forEach(skillRequest -> {
            long skillId = skillRequest.getSkill().getId();
            if (!skillRepository.existsById(skillId)) {
                throw new DataValidationException(String.format("skill with id: %d is not exists", skillId));
            }
        });
    }

    private void checkRequestDate(RecommendationRequest recommendationRequest) {
        LocalDateTime createdDateTime = recommendationRequest.getCreatedAt();
        LocalDateTime createdTimePlusSixMonth = createdDateTime.plusMonths(6);
        if (LocalDateTime.now().isBefore(createdTimePlusSixMonth)) {
            throw new DataValidationException(String.format("you can request this recommendation from %s", createdTimePlusSixMonth));
        }
    }
}