package cn.hybris.boot.configure;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import cn.hybris.common.redis.HessianRedisSerializer;

/**
 * @author Qu Dihuai
 *
 */
@Configuration
@ComponentScan("cn.hybris.**")
public class SpringBeansConfiguration {

	@Bean
	public RedisTemplate<String, Serializable> redisTemplate(final RedisConnectionFactory factory)
	{
		final RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);

		final RedisSerializer<String> keySerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(keySerializer);
		redisTemplate.setHashKeySerializer(keySerializer);

		final RedisSerializer<Serializable> valueSerializer = new HessianRedisSerializer<>();
		redisTemplate.setValueSerializer(valueSerializer);
		redisTemplate.setHashValueSerializer(valueSerializer);

		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}
}
