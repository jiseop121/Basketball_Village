package community.basketballvillage.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import community.basketballvillage.domain.Bookmark;
import community.basketballvillage.domain.Post;
import community.basketballvillage.domain.User;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    Optional<Bookmark> findByUserAndPost(User user, Post post);
}
