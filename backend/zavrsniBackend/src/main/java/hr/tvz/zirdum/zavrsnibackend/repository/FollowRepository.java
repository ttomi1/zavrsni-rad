package hr.tvz.zirdum.zavrsnibackend.repository;

import hr.tvz.zirdum.zavrsnibackend.model.Follow;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    List<Follow> findAllByFollowerId(Long followerId);
    List<Follow> findAllByFollowingId(Long followingId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
    List<Long> findFollowingIds(@Param("userId") Long userId);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    long countByFollowing(User user);
    long countByFollower(User user);
}