package com.irctn.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration extends CachingConfigurerSupport {

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(9999);
		poolConfig.setMinIdle(5);
		poolConfig.setMaxIdle(10);

		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
		jedisConnectionFactory.setHostName("localhost");
		jedisConnectionFactory.setPort(6379);


		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {

		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setExposeConnection(true);
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager() {

		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
		redisCacheManager.setTransactionAware(true);
		redisCacheManager.setLoadRemoteCachesOnStartup(true);
		redisCacheManager.setUsePrefix(true);
		return redisCacheManager;
	}

}