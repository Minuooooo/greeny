package greeny.backend.domain.post.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.comment.entity.Comment;
import greeny.backend.domain.member.entity.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostFile> postFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private Set<PostLike> postLikes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Integer hits;

    @Column(nullable = false)
    private Boolean hasPostFile;

    @Formula("(select count(1) from post_like pl where pl.post_id = post_id)")
    private Integer likes;

    public List<String> getFileUrls(){
        List<String> fileUrls = new ArrayList<>();
        for(PostFile postFile : postFiles){
            fileUrls.add(postFile.getFileUrl());
        }
        return fileUrls;
    }

    public void updateHits(){
        this.hits += 1;
    }

    public void update(String title, String content, Boolean hasPostFile){
        this.title = title;
        this.content = content;
        this.hasPostFile = hasPostFile;
    }
}