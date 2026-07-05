package com.security.starter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

import com.security.core.jwt.JwtProvider;
import com.security.core.model.JwtUser;

@SpringBootTest(
        properties = {
                "security.jwt.secret=12345678901234567890123456789012",
                "security.jwt.issuer=empresa",
                "security.jwt.expiration=3600000",
                "security.jwt.refresh-expiration=604800000"
        }
)
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest {

	@Autowired
	private JwtProvider jwtProvider;
	
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAuthenticateUsingJwt()
            throws Exception {
//
//        String token =
//                TokenFactory.generateToken();
    	String token = jwtProvider.generateAccessToken(
    	        new JwtUser(1L, "admin", Set.of("ADMIN"))
    	).token();

        mockMvc.perform(
                        get("/private")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().string("admin"));
    }
}