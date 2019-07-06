package com.laptevn.bank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BankControllerIT {
    private final static String AUTHORIZATION_HEADER_NAME = "authorization";

    private MockMvc restClient;
    private String accessToken;
    private String tokenType;

    @Autowired
    public void setRestClient(MockMvc restClient) {
        this.restClient = restClient;
    }

    @Value("${access.token}")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Value("${token.type}")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Test
    public void noAccountServiceAndEmptyCache() throws Exception {
        restClient.perform(
                get("/bank/10")
                        .header(AUTHORIZATION_HEADER_NAME, createAuthenticationHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void noAuthorization() throws Exception {
        restClient.perform(
                get("/bank/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String createAuthenticationHeader() {
        return String.format("%s %s", tokenType, accessToken);
    }
}