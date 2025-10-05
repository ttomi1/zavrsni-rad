package hr.tvz.zirdum.zavrsnibackend.repository;

import hr.tvz.zirdum.zavrsnibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUserName(String username);
    List<User> findByUserNameContainingIgnoreCase(String userName);
}


