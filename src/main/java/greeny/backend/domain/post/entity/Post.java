package greeny.backend.domain.post.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
public class Post extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "writer_id", nullable = false)
//    private Member writer;
    @Column(nullable = false)
    private String title;
    @Column(length = 500, nullable = false)
    private String content;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer hits;
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    List<PostFile> postFiles = new ArrayList<>();

}