package hr.tvz.zirdum.zavrsnibackend.service;

import hr.tvz.zirdum.zavrsnibackend.model.Follow;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.FollowRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void toggleFollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Ne možeš pratiti sam sebe!");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower nije pronađen"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Following nije pronađen"));

        followRepository.findByFollowerAndFollowing(follower, following).ifPresentOrElse(
                // ako već postoji, unfollow
                followRepository::delete,
                // inače kreiraj novi follow
                () -> {
                    Follow follow = new Follow();
                    follow.setFollower(follower);
                    follow.setFollowing(following);
                    followRepository.save(follow);
                }
        );
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) return false;

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower nije pronađen"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Following nije pronađen"));

        return followRepository.findByFollowerAndFollowing(follower, following).isPresent();
    }

    public long countFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen"));
        return followRepository.countByFollowing(user);
    }

    public long countFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen"));
        return followRepository.countByFollower(user);
    }
}

