package com.security.starter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "security.jwt.secret=12345678901234567890123456789012",
                "security.jwt.issuer=empresa",
                "security.jwt.expiration=3600000",
                "security.jwt.refresh-expiration=604800000"
        }
)
@AutoConfigureMockMvc
class PublicEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAccessPublicEndpoint()
            throws Exception {

        mockMvc.perform(
                        get("/auth/public")
                )
                .andExpect(status().isOk());
    }
}