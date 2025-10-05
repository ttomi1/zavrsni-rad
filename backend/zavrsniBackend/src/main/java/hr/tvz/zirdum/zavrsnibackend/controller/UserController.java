package hr.tvz.zirdum.zavrsnibackend.controller;

import hr.tvz.zirdum.zavrsnibackend.dto.PostDto;
import hr.tvz.zirdum.zavrsnibackend.dto.UserDto;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.PostRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/id")
    public ResponseEntity<Long> getUserId(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Korisnik nije pronađen")
        );

        return ResponseEntity.ok(user.getId());
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserDto> getProfile(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"));

        return ResponseEntity.ok(UserDto.from(user));
    }

    @GetMapping("/my-posts")
    public List<PostDto> getMyPosts(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return postRepository.findAllByAuthorOrderByCreatedAtDesc(user)
                .stream()
                .map(PostDto::from)
                .toList();
    }

    @GetMapping("/{username}/posts")
    public List<PostDto> getPostsByUsername(@PathVariable String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"));

        return postRepository.findAllByAuthorOrderByCreatedAtDesc(user)
                .stream()
                .map(PostDto::from)
                .toList();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return userRepository.findByUserName(username)
                .map(user -> ResponseEntity.ok(Map.of(
                        "userName", user.getUserName(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "email", user.getEmail(),
                        "imageUrl", user.getImageUrl()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<UserDto> searchUsers(@RequestParam String query) {
        return userRepository.findByUserNameContainingIgnoreCase(query)
                .stream()
                .map(UserDto::from)
                .toList();
    }

}
