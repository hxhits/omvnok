package vn.com.omart.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import vn.com.omart.auth.port.smsgw.SmsGatewayService;
import vn.com.omart.sharedkernel.application.PrefixedRedisCachePrefix;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                SerializationFeature.FAIL_ON_EMPTY_BEANS);
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
//            builder.featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN,
//                JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
            builder.dateFormat(new ISO8601DateFormat());
        };
    }

    @Bean
    RedisTemplate<String, Object> redisCacheTemplate(RedisConnectionFactory connectionFactory) {
        // Create redis template to interact with all kind of cache
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);

        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        // redisTemplate.setKeySerializer(stringRedisSerializer);
        // redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        return redisTemplate;
    }

    @Bean
    CacheManager cacheManager(RedisTemplate<String, Object> redisCacheTemplate) {
        // Redis cache manager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheTemplate);
        cacheManager.setUsePrefix(true);
        cacheManager.setCachePrefix(new PrefixedRedisCachePrefix("", ":"));

        // Cache expire times
        Map<String, Long> expires = new HashMap<>();
        expires.put(CacheName.ACTIVATION_CODE, CacheName.ACTIVATION_CODE_TTL);

        cacheManager.setExpires(expires);

        return cacheManager;
    }

    @Bean
    SmsGatewayService smsGatewayService(
        @Value("${sms.gateway.url}") String url,
        @Value("${sms.gateway.username}") String user,
        @Value("${sms.gateway.password}") String pass,
        @Value("${sms.gateway.from}") String from
    ) {
        return new SmsGatewayService(url, user, pass, from);
    }
}
