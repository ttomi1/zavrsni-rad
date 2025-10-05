package hr.tvz.zirdum.zavrsnibackend.controller;

import hr.tvz.zirdum.zavrsnibackend.dto.PostDto;
import hr.tvz.zirdum.zavrsnibackend.model.Post;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.FollowRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.PostRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PostController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FollowRepository followRepository;

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostDto::from)
                .toList();
    }


    @PostMapping
    public ResponseEntity<?> createPost(@RequestParam("text") String text, @RequestParam("image") MultipartFile image, Principal principal) throws IOException {

        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        // spremi sliku
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        Path imagePath = Paths.get("uploads/" + fileName);
        Files.createDirectories(imagePath.getParent());
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // spremi objavu
        Post post = new Post();
        post.setText(text);
        post.setImageUrl("/uploads/" + fileName); // putanja
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        return ResponseEntity.ok((Map.of("message", "Objava spremljena.")));
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, Principal principal) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post nije pronađen"));

        // provjeri ulogirani korisnik autor posta
        String email = principal.getName();
        if (!post.getAuthor().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        postRepository.delete(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostDto>> getFeed(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"))
                .getId();

        List<Long> followingIds = followRepository.findFollowingIds(userId);
        if (followingIds.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<PostDto> posts = postRepository.findPostsFromFollowing(followingIds)
                .stream()
                .map(PostDto::from)
                .toList();

        return ResponseEntity.ok(posts);
    }

}
