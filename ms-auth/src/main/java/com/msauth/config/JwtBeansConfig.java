package com.msauth.config;

import com.security.core.config.JwtConfiguration;
import com.security.core.jwt.JwtGeneratorImpl;
import com.security.core.jwt.JwtParserImpl;
import com.security.core.jwt.JwtProvider;
import com.security.core.jwt.JwtProviderImpl;
import com.security.core.jwt.JwtValidatorImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtBeansConfig {

    @Bean
    public JwtConfiguration jwtConfiguration(
            JwtProperties properties
    ) {

        return new JwtConfiguration(
                properties.secret(),
                properties.issuer(),
                properties.expiration(),
                properties.refreshExpiration()
        );
    }

    @Bean
    public JwtGeneratorImpl jwtGenerator(
            JwtConfiguration configuration
    ) {

        return new JwtGeneratorImpl(configuration);
    }

    @Bean
    public JwtParserImpl jwtParser(
            JwtConfiguration configuration
    ) {

        return new JwtParserImpl(configuration);
    }

    @Bean
    public JwtValidatorImpl jwtValidator(
            JwtParserImpl parser
    ) {

        return new JwtValidatorImpl(parser);
    }

    @Bean
    public JwtProvider jwtProvider(
            JwtGeneratorImpl generator,
            JwtValidatorImpl validator,
            JwtParserImpl parser
    ) {

        return new JwtProviderImpl(
                generator,
                validator,
                parser
        );
    }
}