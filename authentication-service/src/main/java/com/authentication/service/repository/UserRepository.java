package com.authentication.service.repository;

import com.authentication.service.entity.UserEntity;
import com.authentication.service.entity.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserProjection> findByUsername(String username);
}

