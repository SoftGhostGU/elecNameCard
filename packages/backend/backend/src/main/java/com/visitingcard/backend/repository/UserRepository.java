package com.visitingcard.backend.repository;

import com.visitingcard.backend.dto.User.UserDeleteRequest;
import com.visitingcard.backend.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public interface UserRepository extends JpaRepository<User, String> {

    final Logger logger = Logger.getLogger(UserRepository.class.getName());

    Optional<User> findByUserId(long userId);
    default Optional<User> findByUserIdWithLog(long userId) {
        logger.info("[UserRepository] Finding user by userId: " + userId);
        return findByUserId(userId);
    };

    Optional<User> findUserByOpenid(String openid);
    default Optional<User> findUserByOpenidWithLog(String openid) {
        logger.info("[UserRepository] Finding user by openid: " + openid);
        return findUserByOpenid(openid);
    }

    @Transactional
    void deleteByUserId(Long userId);
    default void deleteByUserIdWithLog(Long userId) {
        logger.info("[UserRepository] Deleting user: " + userId);
        deleteByUserId(userId);
    }

    List<User> findAll();
    default List<User> findAllWithLog() {
        logger.info("[UserRepository] Finding all users");
        return findAll();
    }
}
