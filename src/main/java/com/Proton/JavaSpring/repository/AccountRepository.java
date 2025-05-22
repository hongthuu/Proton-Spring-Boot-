package com.Proton.JavaSpring.repository;

import com.Proton.JavaSpring.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    Optional<Account> findById(@NonNull Long id);
}
