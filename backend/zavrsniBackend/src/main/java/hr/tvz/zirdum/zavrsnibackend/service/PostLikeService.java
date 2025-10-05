package hr.tvz.zirdum.zavrsnibackend.service;

import hr.tvz.zirdum.zavrsnibackend.model.Post;
import hr.tvz.zirdum.zavrsnibackend.model.PostLike;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.PostLikeRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.PostRepository;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void toggleLike(Long userId, Long postId) {
        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            postLikeRepository.deleteByUserIdAndPostId(userId, postId); // unlike
        } else {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post nije pronađen"));

            PostLike like = new PostLike();
            like.setUser(user);
            like.setPost(post);
            postLikeRepository.save(like);
        }
    }

    public boolean isLikedByUser(Long userId, Long postId) {
        return postLikeRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }

    public long countLikes(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }
}