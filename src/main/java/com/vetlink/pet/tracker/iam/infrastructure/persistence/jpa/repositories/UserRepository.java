package com.vetlink.pet.tracker.iam.infrastructure.persistence.jpa.repositories;

import com.vetlink.pet.tracker.iam.domain.model.aggregates.User;
import com.vetlink.pet.tracker.iam.domain.model.valueobjects.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(EmailAddress email);
}
