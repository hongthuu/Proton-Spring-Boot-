package com.Proton.JavaSpring.repository;

import com.Proton.JavaSpring.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findByAccount_AccountId(Long accountId);

    @Query("select b from Balance b where b.account.accountId = :accountId")
    List<Balance> getBalancesByAccountId(@Param("accountId") Long accountId);
}
