package com.Proton.JavaSpring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cards")
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false, unique = false)
    @JsonIgnore
    private Account account;

    @Column(name = "cardType")
    private String cardType;

    @Column(name = "expiryDate")
    private Date expiryDate;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}
