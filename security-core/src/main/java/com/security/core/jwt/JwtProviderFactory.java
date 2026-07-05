package com.security.core.jwt;

import com.security.core.config.JwtConfiguration;

public final class JwtProviderFactory {

    private JwtProviderFactory() {
    }

    public static JwtProvider create(JwtConfiguration configuration) {

        JwtParser parser =
                new JwtParserImpl(configuration);

        JwtGenerator generator =
                new JwtGeneratorImpl(configuration);

        JwtValidator validator =
                new JwtValidatorImpl(parser);

        return new JwtProviderImpl(
                generator,
                validator,
                parser
        );
    }
}