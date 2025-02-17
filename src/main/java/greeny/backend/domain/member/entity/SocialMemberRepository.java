package greeny.backend.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
    Optional<SocialMember> findByMemberId(Long memberId);
}