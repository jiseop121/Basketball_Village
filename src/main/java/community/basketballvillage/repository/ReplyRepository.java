package community.basketballvillage.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import community.basketballvillage.domain.Post;
import community.basketballvillage.domain.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {

    List<Reply> findAllByPost(Post post);
}
