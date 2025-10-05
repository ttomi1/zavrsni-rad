package hr.tvz.zirdum.zavrsnibackend.controller;

import hr.tvz.zirdum.zavrsnibackend.dto.CommentDto;
import hr.tvz.zirdum.zavrsnibackend.model.Comment;
import hr.tvz.zirdum.zavrsnibackend.model.Post;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.CommentRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.PostRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{postId}")
    public CommentDto addComment(@PathVariable Long postId, @RequestBody Map<String, String> body, Principal principal) {
        Post post = postRepository.findById(postId).orElseThrow();
        User author = userRepository.findByEmail(principal.getName()).orElseThrow();

        Comment comment = new Comment();
        comment.setText(body.get("text"));
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        return CommentDto.from(commentRepository.save(comment));
    }

    @GetMapping("/{postId}")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findAllByPostOrderByCreatedAtAsc(post)
                .stream().map(CommentDto::from).toList();
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Principal principal) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Komentar nije pronađen"));

        // provjera korisnika autora komentara
        String email = principal.getName();
        if (!comment.getAuthor().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentRepository.delete(comment);
        return ResponseEntity.ok().build();
    }
}