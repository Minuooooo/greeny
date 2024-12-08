package greeny.backend.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
    Optional<MemberProfile> findByMemberId(Long memberId);
}
