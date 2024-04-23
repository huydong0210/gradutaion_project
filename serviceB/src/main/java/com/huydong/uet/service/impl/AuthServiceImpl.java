package com.huydong.uet.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huydong.uet.security.KeycloakProperties;
import com.huydong.uet.service.AuthService;
import com.huydong.uet.service.dto.JWTToken;
import com.huydong.uet.service.dto.UserInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceImpl implements AuthService {

    private final KeycloakProperties keycloakProperties;

    public AuthServiceImpl(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public Optional<JWTToken> getAccessTokenFromCode(String code) {
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());
        formData.add("redirect_uri", keycloakProperties.getRedirectUri());
        formData.add("code", code);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8090/realms/graduation_project/protocol/openid-connect/token",
                HttpMethod.POST,
                request,
                String.class
            );
            if (response.hasBody()) {
                JSONObject jsonObject = new JSONObject(response.getBody());
                JWTToken token = new JWTToken();
                token.setAccessToken(jsonObject.get("access_token").toString());
                token.setRefreshToken(jsonObject.get("refresh_token").toString());
                token.setIdToken(jsonObject.get("id_token").toString());
                return Optional.of(token);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<UserInfo> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());
        formData.add("token", accessToken);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(formData, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8090/realms/graduation_project/protocol/openid-connect/token/introspect",
                HttpMethod.POST,
                entity,
                String.class
            );
            if (response.hasBody()) {
                ObjectMapper objectMapper = new ObjectMapper();
                TypeReference<List<String>> roles = new TypeReference<List<String>>() {};
                JSONObject jsonObject = new JSONObject(response.getBody());
                UserInfo result = new UserInfo();
                result.setEmail(jsonObject.get("email").toString());
                result.setName(jsonObject.get("name").toString());
                result.setUsername(jsonObject.get("preferred_username").toString());
                String scope = jsonObject.getString("scope");
                result.setRoles(Arrays.asList(scope.split(" ")));
                return Optional.ofNullable(result);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public String getLogOutUri(String idToken) {
        StringBuilder result = new StringBuilder();
        result
            .append("http://localhost:8090/realms/graduation_project/protocol/openid-connect/logout?")
            .append("id_token_hint=")
            .append(idToken)
            .append("&")
            .append("post_logout_redirect_uri=")
            .append("http://localhost:9000/");

        return result.toString();
    }

    private static void setTimeOut(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        int time = 120000;
        requestFactory.setReadTimeout(time);
        requestFactory.setConnectTimeout(time);
        restTemplate.setRequestFactory(requestFactory);
    }
}
