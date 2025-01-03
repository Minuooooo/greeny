package greeny.backend.domain.post;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.comment.Comment;
import greeny.backend.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.Formula;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Post extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Integer hits;

    @Column(nullable = false)
    private Boolean hasPostFile;

    @Formula("(SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = post_id)")
    private Integer likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    public void updateHits(){
        this.hits += 1;
    }

    public void update(String title, String content, Boolean hasPostFile){
        this.title = title;
        this.content = content;
        this.hasPostFile = hasPostFile;
    }
}