package com.example.crypto_platform.user;

import com.example.crypto_platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Long> {
    Optional<User> findByTelegramUserId(Long userId);
    boolean existsByTelegramUserId(Long telegramUserId);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}
