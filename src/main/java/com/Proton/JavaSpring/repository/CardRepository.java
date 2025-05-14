package com.Proton.JavaSpring.repository;

import com.Proton.JavaSpring.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccount_AccountId(Long accountId);
}
