package com.security.starter.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.security.core.config.JwtConfiguration;
import com.security.core.jwt.JwtProvider;
import com.security.core.jwt.JwtProviderFactory;
import com.security.starter.config.SecurityConfig;
import com.security.starter.filter.JwtAuthenticationFilter;
import com.security.starter.handler.JwtAccessDeniedHandler;
import com.security.starter.handler.JwtAuthenticationEntryPoint;
import com.security.starter.properties.JwtProperties;

import jakarta.annotation.PostConstruct;

@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@Import(SecurityConfig.class)
public class SecurityAutoConfiguration {

	@PostConstruct
	public void init() {
	    System.out.println("SECURITY STARTER CARGADO");
	}
    @Bean
    @ConditionalOnMissingBean
    public JwtProvider jwtProvider(JwtProperties properties) {

        JwtConfiguration configuration = new JwtConfiguration(
                properties.getSecret(),
                properties.getIssuer(),
                properties.getExpiration(),
                properties.getRefreshExpiration()
        );

        return JwtProviderFactory.create(configuration);
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationFilter.class)
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityConfig securityConfig() {
        return new SecurityConfig();
    }
}