package com.laptevn.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptevn.account.repository.InMemoryAccountRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AccountControllerIT {
    private final static String AUTHORIZATION_HEADER_NAME = "authorization";

    private MockMvc restClient;
    private ObjectMapper objectMapper;
    private String accessToken;
    private String tokenType;

    @Autowired
    public void setRestClient(MockMvc restClient) {
        this.restClient = restClient;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
    public void getAllWithEmptyRepository() throws Exception {
        Collection<Account> expectedResult = new ArrayList<>();
        restClient.perform(get("/accounts/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    public void notFoundAccount() throws Exception {
        restClient.perform(
                get("/account/10")
                        .header(AUTHORIZATION_HEADER_NAME, createAuthenticationHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAccountWithoutAuthorization() throws Exception {
        restClient.perform(
                get("/account/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createAccountWithInvalidId() throws Exception {
        restClient.perform(
                post("/account/2")
                        .header(AUTHORIZATION_HEADER_NAME, createAuthenticationHeader())
                        .content(objectMapper.writeValueAsString(InMemoryAccountRepositoryTest.account))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountWithoutAuthorization() throws Exception {
        restClient.perform(
                post("/account/1")
                        .content(objectMapper.writeValueAsString(InMemoryAccountRepositoryTest.account))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createAccount() throws Exception {
        restClient.perform(
                post("/account/1")
                        .header(AUTHORIZATION_HEADER_NAME, createAuthenticationHeader())
                        .content(objectMapper.writeValueAsString(InMemoryAccountRepositoryTest.account))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        restClient.perform(get("/accounts/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        String.format(
                                "$.[?(@.id == \'%d\' && @.firstName == \'%s\' && @.lastName == \'%s\')]",
                                InMemoryAccountRepositoryTest.account.getId(),
                                InMemoryAccountRepositoryTest.account.getFirstName(),
                                InMemoryAccountRepositoryTest.account.getLastName()))
                        .exists());

        restClient.perform(
                get("/account/1")
                        .header(AUTHORIZATION_HEADER_NAME, createAuthenticationHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(InMemoryAccountRepositoryTest.account)));
    }

    @Test
    public void createDuplicateAccount() throws Exception {
        Account account = new Account()
                .setId(3)
                .setFirstName("Nikolai")
                .setLastName("Laptev");

        restClient.perform(
                post("/account/3")
                        .header(AUTHORIZATION_HEADER_NAME, createAuthenticationHeader())
                        .content(objectMapper.writeValueAsString(account))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        restClient.perform(
                post("/account/3")
                        .header(AUTHORIZATION_HEADER_NAME, createAuthenticationHeader())
                        .content(objectMapper.writeValueAsString(account))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    private String createAuthenticationHeader() {
        return String.format("%s %s", tokenType, accessToken);
    }
}