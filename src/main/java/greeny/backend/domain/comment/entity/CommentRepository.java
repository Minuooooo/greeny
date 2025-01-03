package greeny.backend.domain.comment.entity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"writer"})
    List<Comment> findAllByPostId(Long postId);

    @Query("select c from Comment c join fetch c.writer WHERE c.id = :id")
    Optional<Comment> findByIdWithWriter(@Param("id") Long id);
}