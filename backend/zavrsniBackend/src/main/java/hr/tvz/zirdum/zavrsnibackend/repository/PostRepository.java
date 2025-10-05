package hr.tvz.zirdum.zavrsnibackend.repository;

import hr.tvz.zirdum.zavrsnibackend.model.Post;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByAuthorOrderByCreatedAtDesc(User author);

    @Query("SELECT p FROM Post p WHERE p.author.id IN :followingIds ORDER BY p.createdAt DESC")
    List<Post> findPostsFromFollowing(@Param("followingIds") List<Long> followingIds);

    @Query("""
    SELECT p FROM Post p JOIN FETCH p.author WHERE p.author.id IN :followingIds ORDER BY p.createdAt DESC""")
    List<Post> findFeedPosts(@Param("followingIds") List<Long> followingIds);

}