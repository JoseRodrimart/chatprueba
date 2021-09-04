package com.jose.chatprueba.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;

@Configuration
@EnableCaching
public class globalConfig {

    /**
     * <b>Bean para configurar la caché de spring.</b>
     *
     * @return manager de caché
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("usuarios");
    }

    /**
     * <b>Bean que gestiona la configuración básica del almacenamiento de la caché en el servidor Redis</b>
     *
     * @return Configuración de caché redis
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(3)) //la caché general persiste x mins en la base redis
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * <b>Customización del amacenamiento de cachés spring en redis</b>
     *
     * @return Customizador del gestor de constructores de caché de Redis
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration("usuarios",
                        RedisCacheConfiguration
                                .defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10)) //Especificamos la duración de caché de esa caché específica
                );
    }

    /**
     * <b>Bean que gestion el codificador de contraseñas</b>
     *
     * @return Encoder de contraseñas
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}
