package com.Proton.JavaSpring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    @JsonIgnore
    private Account account;

    @Column(name = "availableBalance")
    private double  availableBalance;

    @Column(name = "holdBalance")
    private double  holdBalance;
}
