package hr.tvz.zirdum.zavrsnibackend.controller;

import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import hr.tvz.zirdum.zavrsnibackend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final UserRepository userRepository;

    @PostMapping("/{username}/toggle")
    public ResponseEntity<Void> toggleFollow(@PathVariable String username, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String myEmail = principal.getName();
        User me = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User target = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        followService.toggleFollow(me.getId(), target.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/is-following")
    public ResponseEntity<Boolean> isFollowing(@PathVariable String username, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String myEmail = principal.getName();
        User me = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"));
        User target = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"));

        boolean following = followService.isFollowing(me.getId(), target.getId());
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{username}/stats")
    public ResponseEntity<?> getStats(@PathVariable String username) {
        User target = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"));

        long followers = followService.countFollowers(target.getId());
        long following = followService.countFollowing(target.getId());

        return ResponseEntity.ok(Map.of(
                "followers", followers,
                "following", following
        ));
    }
}

