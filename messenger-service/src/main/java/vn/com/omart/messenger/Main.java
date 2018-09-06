package vn.com.omart.messenger;

import com.authy.AuthyApiClient;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.constants.Constants;
import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import vn.com.omart.messenger.port.adapter.rest.MessengerResource;
import vn.com.omart.sharedkernel.application.PrefixedRedisCachePrefix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class Main {
	
	@Value("${messenger.twilio.authy.production.api.key}")
	private String Product_Api_Key;

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
//        expires.put(CacheName.CATEGORIES, CacheName.CATEGORIES_TTL);
//        expires.put(CacheName.POI, CacheName.POI_TTL);
//
//        expires.put(CacheName.PROVINCE, CacheName.PROVINCE_TTL);
//        expires.put(CacheName.DISTRICT, CacheName.DISTRICT_TTL);
//        expires.put(CacheName.WARD, CacheName.WARD_TTL);

        cacheManager.setExpires(expires);

        return cacheManager;
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

    @Bean(name = "restTemplate")
    @Primary
    RestTemplate restTemplate() {
        RestTemplate rest = new RestTemplate();
        rest.setMessageConverters(rest.getMessageConverters().stream().peek(httpMessageConverter -> {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                List<MediaType> supportedMediaTypes = new ArrayList<>();
                final List<MediaType> unmodifiableListMediaType = httpMessageConverter.getSupportedMediaTypes();
                // convert from unmodifiableList to modifiableList
                supportedMediaTypes.addAll(unmodifiableListMediaType);

                // add more
                supportedMediaTypes.add(MediaType.valueOf("text/json"));
                supportedMediaTypes.add(MediaType.valueOf("text/json; charset=UTF-8"));

                ((MappingJackson2HttpMessageConverter) httpMessageConverter).setSupportedMediaTypes(supportedMediaTypes);
            }
        }).collect(Collectors.toList()));

        // Monitor
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new ClientHttpRequestInterceptor() {
            private final Logger logger = LoggerFactory.getLogger("rest_template");


            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                StopWatch stopwatch = new StopWatch();

                stopwatch.start();
                ClientHttpResponse response = execution.execute(request, body);
                stopwatch.stop();

                logger.info("ExecTime={}ms Method=http.{}.{}",
                    stopwatch.getTotalTimeMillis(),
                    request.getMethod(), request.getURI());

                return response;
            }

        });
        rest.setInterceptors(interceptors);

        return rest;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
        MessengerResource messengerResource,
        @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer mlc = new RedisMessageListenerContainer();
        mlc.setConnectionFactory(redisConnectionFactory);
        mlc.addMessageListener(messengerResource, new PatternTopic("chat"));
        return mlc;
    }

//    @Bean
//    ThumborService thumborService(RestTemplate restTemplate, Environment environment,
//                                  @Value("${thumbor.host}") String thumborHost,
//                                  @Value("${thumbor.proxy}") String thumborProxy,
//                                  @Value("${thumbor.secretKey}") String thumborSecretKey) {
//        return new ThumborService(restTemplate, environment,
//            thumborHost, thumborProxy, thumborSecretKey);
//    }

    @Bean
    public FcmClient fcmClient(@Value("${push.notification.fcm.secretkey}") String fcmSecretKey) {
        return new FcmClient(new IFcmClientSettings() {
            @Override
            public String getFcmUrl() {
                return Constants.FCM_URL;
            }

            @Override
            public String getApiKey() {
                return fcmSecretKey;
            }
        });
    }
    
    @Bean
    public AuthyApiClient authyApiClient() {
        return new AuthyApiClient(Product_Api_Key);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
