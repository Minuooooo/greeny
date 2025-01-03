package greeny.backend.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {

    Optional<Agreement> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}