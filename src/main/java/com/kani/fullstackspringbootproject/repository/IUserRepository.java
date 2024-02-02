package com.kani.fullstackspringbootproject.repository;

import com.kani.fullstackspringbootproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);

    @Modifying
    @Query(value = "UPDATE User u SET u.firstName =:firstName, u.lastName =:lastName, u.email =:email WHERE u.id =:id")
    void update(String firstName, String lastName, String email, Long id);
}
