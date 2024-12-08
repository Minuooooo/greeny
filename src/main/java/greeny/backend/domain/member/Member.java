package greeny.backend.domain.member;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.comment.Comment;
import greeny.backend.domain.post.Post;
import greeny.backend.domain.post.PostLike;
import greeny.backend.domain.product.ProductBookmark;
import greeny.backend.domain.store.StoreBookmark;
import greeny.backend.domain.product.Product;
import greeny.backend.domain.review.ProductReview;
import greeny.backend.domain.review.StoreReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends AuditEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @OneToMany(mappedBy = "reviewer", cascade = ALL, orphanRemoval = true)
    private List<StoreReview> storeReviews = new ArrayList<>();
    @OneToMany(mappedBy = "liker", cascade = ALL, orphanRemoval = true)
    private List<StoreBookmark> storeBookmarks = new ArrayList<>();
    @OneToMany(mappedBy = "reviewer", cascade = ALL, orphanRemoval = true)
    private List<ProductReview> productReviews = new ArrayList<>();
    @OneToMany(mappedBy = "liker", cascade = ALL, orphanRemoval = true)
    private List<ProductBookmark> productBookmarks = new ArrayList<>();
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "liker", cascade = ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)  // DB에 이름 (문자열)으로 저장
    @Column(nullable = false)
    private Role role;
}
