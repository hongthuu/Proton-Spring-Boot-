package com.Proton.JavaSpring.service;


import com.Proton.JavaSpring.entity.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService<T>{

//    private static final String ACCOUNT_KEY_PREFIX = "account:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    public void save(String keyPrefix, Long id, T data, Duration duration) {
        String key = keyPrefix + id;
        redisTemplate.opsForValue().set(key, data, duration);
    }

    public T get(String keyPrefix, Long id, Class<T> clazz) {
        String key = keyPrefix + id;
        Object value = redisTemplate.opsForValue().get(key);

        if (value != null) {
            return clazz.cast(value);
        }
        return null;
    }

    public void delete(String keyPrefix, Long id) {
        String key = keyPrefix + id;
        redisTemplate.delete(key);
    }

//    public void saveAccount(Account account, Duration duration) {
//        String key = ACCOUNT_KEY_PREFIX + account.getAccountId();
//        log.info("Saving account to Redis with key: {}", key);
//        redisTemplate.opsForValue().set(key, account, duration);
//    }
//
//    public Account getAccount(Long id) {
//        String key = ACCOUNT_KEY_PREFIX + id;
//        log.info("Fetching account from Redis with key: {}", key);
//        Object value = redisTemplate.opsForValue().get(key);
//
//        if (value != null) {
//            log.info("Account found in Redis cache");
//            return (Account) value;
//        }
//
//        log.info("Account not found in Redis cache");
//        return null;
//    }
//
//    public void deleteAccount(Long id) {
//        String key = ACCOUNT_KEY_PREFIX + id;
//        log.info("Deleting account from Redis with key: {}", key);
//        redisTemplate.delete(key);
//    }






    private String getKeyFrom(String keyword,
                              Long categoryId,
                              PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id")
                              .getDirection() == Sort.Direction.ASC ? "asc" : "desc";
        String key = String.format("all_products:%d:%d:%s", pageNumber, pageSize, sortDirection);
        return key;
    }

    public List<Account> getAllProducts(String keyword,
                                                Long categoryId,
                                                PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);

        List<Account> productResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<List<Account>>() {}) :
                        null;

        return productResponses;
    }

    public void clear() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushAll();
    }

    public void saveAllProducts(List<Account> productResponses,
                                String keyword,
                                Long categoryId,
                                PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}