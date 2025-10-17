package com.tekton.challenge.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, "percentage", Duration.ofMinutes(30), 100);
        };
    }

    private void createCache(CacheManager cm, String name, Duration ttl, long heapEntries) {
        Cache<Object, Object> existing = cm.getCache(name);
        if (existing != null) {
            existing.clear();
            return;
        }
        javax.cache.configuration.Configuration<Object, Object> cfg =
                Eh107Configuration.fromEhcacheCacheConfiguration(
                        CacheConfigurationBuilder
                                .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(heapEntries))
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
                                .build()
                );
        cm.createCache(name, cfg);
    }
}
