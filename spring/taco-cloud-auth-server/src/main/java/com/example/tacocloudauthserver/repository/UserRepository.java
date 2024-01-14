package com.example.tacocloudauthserver.repository;

import com.example.tacocloudauthserver.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
