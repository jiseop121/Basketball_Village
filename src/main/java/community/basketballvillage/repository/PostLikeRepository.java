package community.basketballvillage.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import community.basketballvillage.domain.Post;
import community.basketballvillage.domain.PostLike;
import community.basketballvillage.domain.User;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {


    Optional<PostLike> findByUserAndPost(User user, Post post);
}
