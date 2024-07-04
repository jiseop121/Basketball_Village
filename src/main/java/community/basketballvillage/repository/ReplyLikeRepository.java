package community.basketballvillage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import community.basketballvillage.domain.ReplyLike;

@Repository
public interface ReplyLikeRepository extends JpaRepository<ReplyLike,Long> {

}
