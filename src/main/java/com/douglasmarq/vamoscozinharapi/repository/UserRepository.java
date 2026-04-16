package com.douglasmarq.vamoscozinharapi.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.douglasmarq.vamoscozinharapi.repository.entities.UserEntity;

@Repository
public class UserRepository {

    private final IUserRepository repository;

    public UserRepository(IUserRepository repository) {
        this.repository = repository;
    }

    public Optional<UserEntity> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email);
    }

    public Optional<UserEntity> findById(Long id) {
        return repository.findById(id);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    public UserEntity save(UserEntity user) {
        return repository.save(user);
    }
}
