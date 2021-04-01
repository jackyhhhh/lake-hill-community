package com.hjg.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(){return new RedisTemplate<>();}
}
