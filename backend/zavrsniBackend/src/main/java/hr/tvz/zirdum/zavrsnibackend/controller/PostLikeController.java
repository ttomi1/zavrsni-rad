package hr.tvz.zirdum.zavrsnibackend.controller;

import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import hr.tvz.zirdum.zavrsnibackend.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final UserRepository userRepository;

    @PostMapping("/{postId}/toggle")
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String email = principal.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"))
                .getId();

        postLikeService.toggleLike(userId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(postLikeService.countLikes(postId));
    }

    @GetMapping("/{postId}/liked-by-me")
    public ResponseEntity<Boolean> isLikedByMe(@PathVariable Long postId, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String email = principal.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"))
                .getId();

        boolean liked = postLikeService.isLikedByUser(userId, postId);
        return ResponseEntity.ok(liked);
    }
}