package com.security.starter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.security.core.jwt.JwtProvider;
import com.security.starter.filter.JwtAuthenticationFilter;
import com.security.starter.properties.JwtProperties;

@SpringBootTest(
        properties = {
                "security.jwt.secret=12345678901234567890123456789012",
                "security.jwt.issuer=empresa",
                "security.jwt.expiration=3600000",
                "security.jwt.refresh-expiration=604800000"
        }
)
@AutoConfiguration
class SecurityAutoConfigurationTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    private JwtProperties properties;

    @Test
    void shouldLoadAllBeans() {

        assertThat(jwtProvider).isNotNull();

        assertThat(jwtFilter).isNotNull();

        assertThat(properties).isNotNull();
    }
}