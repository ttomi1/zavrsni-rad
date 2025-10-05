package hr.tvz.zirdum.zavrsnibackend.repository;

import hr.tvz.zirdum.zavrsnibackend.model.Comment;
import hr.tvz.zirdum.zavrsnibackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostOrderByCreatedAtAsc(Post post);
}