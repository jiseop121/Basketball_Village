package community.basketballvillage.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import community.basketballvillage.domain.Post;
import community.basketballvillage.domain.User;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findByIdAndUser(Long postId, User user);

    List<Post> findAllByUser(User user);
}
