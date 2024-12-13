package com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence;

import com.vladyslavpalamarchuk.trackmycoin.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByChatId(Long chatId);

  User findUserByChatId(Long chatId);
}
