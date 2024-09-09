package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.PublicKey;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByStatus(String status);

    List<Account> findByAccountStatus(String accountStatus);

    Optional<Account> findByPublicKey(PublicKey publicKey);
}
