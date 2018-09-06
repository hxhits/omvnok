package vn.com.omart.sharedkernel.application;

import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class PrefixedRedisCachePrefix implements RedisCachePrefix {

    @SuppressWarnings("rawtypes")
    private final RedisSerializer serializer = new StringRedisSerializer();
    private String prefix;
    private final String delimiter;

    public PrefixedRedisCachePrefix() {
        this("", ":");
    }

    public PrefixedRedisCachePrefix(String prefix, String delimiter) {
        this.prefix = prefix;
        this.delimiter = delimiter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte[] prefix(String cacheName) {
        return serializer.serialize(getPrefix() + cacheName + getDelimeter());
    }

    private String getPrefix() {
        if (this.prefix != null) {
            return this.prefix;
        }

        return "";
    }

    private String getDelimeter() {
        if (this.delimiter != null) {
            return this.delimiter;
        }

        return ":";
    }
}
