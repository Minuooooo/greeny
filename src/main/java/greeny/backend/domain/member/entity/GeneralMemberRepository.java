package greeny.backend.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GeneralMemberRepository extends JpaRepository<GeneralMember, Long> {

    Optional<GeneralMember> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}