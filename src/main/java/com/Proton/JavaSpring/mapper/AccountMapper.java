package com.Proton.JavaSpring.mapper;

import com.Proton.JavaSpring.dto.request.accountDTO.AccountDTO;
import com.Proton.JavaSpring.dto.request.cardDTO.CardDTO;
import com.Proton.JavaSpring.entity.Account;
import com.Proton.JavaSpring.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    @Mapping(source = "cards", target = "cards")
    AccountDTO toDto(Account account);

    CardDTO toDto(Card card);
}
