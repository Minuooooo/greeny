package greeny.backend.domain.member.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.comment.entity.Comment;
import greeny.backend.domain.post.entity.Post;
import greeny.backend.domain.post.entity.PostLike;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.StoreReview;
import greeny.backend.domain.wishlist.entity.ProductWishlist;
import greeny.backend.domain.wishlist.entity.StoreWishlist;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.CascadeType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Member extends AuditEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<StoreWishlist> storeWishlists = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<ProductWishlist> productWishlists = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", cascade = ALL, orphanRemoval = true)
    private List<StoreReview> storeReviews = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", cascade = ALL, orphanRemoval = true)
    private List<ProductReview> productReviews = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "liker", cascade = ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}