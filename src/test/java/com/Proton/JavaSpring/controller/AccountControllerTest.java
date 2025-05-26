package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.dto.request.accountDTO.AccountDTO;
import com.Proton.JavaSpring.dto.request.accountDTO.CreateAccountDTO;
import com.Proton.JavaSpring.dto.request.accountDTO.UpdateAccountDTO;
import com.Proton.JavaSpring.entity.Account;
import com.Proton.JavaSpring.entity.User;
import com.Proton.JavaSpring.repository.UserRepository;
import com.Proton.JavaSpring.service.JwtService;
import com.Proton.JavaSpring.service.serviceImpl.AccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountServiceImpl accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Account testAccount;
    private CreateAccountDTO createAccountDTO;

    @BeforeEach
    void initData() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        objectMapper = new ObjectMapper();

        createAccountDTO = CreateAccountDTO.builder()
                .customerName("Thu")
                .email("thu@test.com")
                .phoneNumber("0987654321")
                .build();

        testAccount = Account.builder()
                .customerName("Thu")
                .email("thu@test.com")
                .phoneNumber("0987654321")
                .build();
    }

    @Test
    void createAccount_Success() throws Exception {
        String json = objectMapper.writeValueAsString(createAccountDTO);

        Mockito.when(accountService.createAccount(any(CreateAccountDTO.class)))
                .thenReturn(testAccount);

        mockMvc.perform(post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(accountService, times(1)).createAccount(any(CreateAccountDTO.class));
    }


//    @Test
//    void createAccount_Fail() throws Exception {
//        createAccountDTO.setEmail("thu@test");
//        String json = objectMapper.writeValueAsString(createAccountDTO);
//
//        Mockito.when(accountService.createAccount(any(CreateAccountDTO.class)))
//                .thenReturn(testAccount);
//
//        mockMvc.perform(post("/createAccount")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//
//        verify(accountService, times(1)).createAccount(any(CreateAccountDTO.class));
//    }


}