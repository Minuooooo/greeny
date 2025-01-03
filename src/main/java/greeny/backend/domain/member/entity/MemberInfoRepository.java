package greeny.backend.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    Optional<MemberInfo> findByMemberId(Long memberId);
}