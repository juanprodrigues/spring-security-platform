package com.security.starter.autoconfigure;

import com.security.core.config.JwtConfiguration;
import com.security.core.jwt.JwtProvider;
import com.security.core.jwt.JwtProviderFactory;
import com.security.starter.filter.JwtAuthenticationFilter;
import com.security.starter.handler.JwtAccessDeniedHandler;
import com.security.starter.handler.JwtAuthenticationEntryPoint;
import com.security.starter.properties.JwtProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityAutoConfiguration {

    // =========================
    // JWT CORE
    // =========================

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

    // =========================
    // FILTER
    // =========================

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    // =========================
    // HANDLERS
    // =========================

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

    // =========================
    // 🔥 SECURITY CHAIN (CLAVE)
    // =========================

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint entryPoint,
            JwtAccessDeniedHandler deniedHandler
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(deniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/public").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}